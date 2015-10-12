/*
 * ===========================================
 * Java Pdf Extraction Decoding Access Library
 * ===========================================
 *
 * Project Info:  http://www.idrsolutions.com
 * Help section for developers at http://www.idrsolutions.com/support/
 *
 * (C) Copyright 1997-2015 IDRsolutions and Contributors.
 *
 * This file is part of JPedal/JPDF2HTML5
 *
 
 *
 * ---------------
 * ConvertPagesToGoogleMaps.java
 * ---------------
 */

package org.jpedal.examples.images;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

import org.jpedal.PdfDecoderInt;
import org.jpedal.PdfDecoderServer;
import org.jpedal.constants.JPedalSettings;
import org.jpedal.objects.PdfPageData;

public class ConvertPagesToGoogleMaps {
    
    public static void main (final String[] args) {
        try {
            // General input validation/checks.
            if (args.length != 2) {
                throw new Exception("Arguments incorrect. Arguments are \"/PDF_Location/pdf.pdf\" \"/Output_Directory/\"");
            }
            
            if (!args[0].endsWith(".pdf")) {
                throw new Exception(args[0] + " not a PDF.");
            }
            
            final File pdf = new File(args[0]);
            
            if (!pdf.exists()) {
                throw new Exception(pdf.getAbsolutePath() + " does not exist.");
            }
            
            File outputDir = new File(args[1]);
            
            if (!outputDir.exists()) {
                throw new Exception(outputDir.getAbsolutePath() + " does not exist.");
            }
            
            // Begin conversion.
            final PdfDecoderInt decoder = new PdfDecoderServer(true);
            decoder.openPdfFile(args[0]);
            
            final String pdfName = pdf.getName().substring(0, pdf.getName().length()-4);
            
            outputDir = new File(outputDir, pdfName);
            outputDir.mkdir();
            
            final int pageCount = decoder.getPageCount();
            
            for (int page = 1; page <= pageCount; page++) {
                
                final String pageAsString = getPageAsString(page, pageCount);
                new File(outputDir.getAbsolutePath() + File.separator + pageAsString + File.separator).mkdir();
                
                final PdfPageData pageData = decoder.getPdfPageData();
                
                final int pdfPageWidth = pageData.getCropBoxWidth(page);
                final int pdfPageHeight = pageData.getCropBoxHeight(page);
                
                final int tileSize = 256;
                final int minZoom = 1;
                final int maxZoom = 4;
                
                // Generate tiles for each zoom level.
                for (int zoomLevel = minZoom; zoomLevel <= maxZoom; zoomLevel++) {
                    final int numTiles = (int)Math.sqrt((int)Math.pow(4,zoomLevel));
                    final int pageSize = tileSize*numTiles;
                    
                    // Make the longest side our wanted length.
                    final float scaleBy;
                    if (pdfPageWidth > pdfPageHeight) {
                        scaleBy = pageSize / (float)pdfPageWidth;
                    } else {
                        scaleBy = pageSize / (float)pdfPageHeight;
                    }
                    
                    // Grab page at our wanted resolution.
                    final BufferedImage image;
                    
                    if(scaleBy>1){
                        final Map<Integer, Object> params=new HashMap<Integer, Object>();
                        
                        //alternatively secify a page size (aspect ratio preserved so will do best fit)
                        //set a page size (JPedal will put best fit to this)
                        params.put(JPedalSettings.EXTRACT_AT_PAGE_SIZE, new String[]{String.valueOf(pageSize),
                            String.valueOf(pageSize)});
                        
                        //which takes priority (default is false)
                        params.put(JPedalSettings.PAGE_SIZE_OVERRIDES_IMAGE, Boolean.TRUE);
                        
                        if(params!=null) {
                            decoder.modifyNonstaticJPedalParameters(params);
                        }
                        
                        image=decoder.getPageAsHiRes(page);
                    } else {
                        
                        decoder.setPageParameters(scaleBy, page);
                        image=decoder.getPageAsImage(page);
                        
                    }
                    
                    // Make square and center page.
                    final BufferedImage bf = new BufferedImage(pageSize, pageSize, BufferedImage.TYPE_INT_ARGB);
                    final Graphics2D g2 = bf.createGraphics();
                    
                    final int newX = (pageSize - image.getWidth())/2;
                    final int newY = (pageSize - image.getHeight())/2;
                    
                    g2.drawImage(image, newX, newY, image.getWidth(), image.getHeight(), null);
                    
                    // Generate the required tiles.
                    for (int yTile = 0; yTile < numTiles; yTile++) {
                        final int y = yTile * tileSize;
                        
                        for (int xTile = 0; xTile < numTiles; xTile++) {
                            final int x = xTile * tileSize;
                            
                            final BufferedImage chop = bf.getSubimage(x, y, tileSize, tileSize);
                            javax.imageio.ImageIO.write(chop,"png",
                                    new java.io.FileOutputStream(outputDir.getAbsolutePath() + File.separator + pageAsString + File.separator +"tile_" + zoomLevel + '_' + xTile + '-' + yTile + ".png"));
                        }
                    }
                }
                
                // Generate our HTML files.
                try {
                    final BufferedOutputStream CSSOutput = new BufferedOutputStream(new FileOutputStream(outputDir.getAbsolutePath() + File.separator + pageAsString + ".html"));
                    
                    final String prevPage = (page > 1) ? getPageAsString(page-1, pageCount) : null;
                    final String nextPage = (page < pageCount) ? getPageAsString(page+1, pageCount) : null;
                    
                    CSSOutput.write(getHTML(pdfName, pageAsString, prevPage, nextPage, String.valueOf(pageCount), minZoom, maxZoom).getBytes());
                    CSSOutput.flush();
                    CSSOutput.close();
                    
                } catch (final Exception ee) {
                    ee.printStackTrace();
                }
                
                System.out.println("Page " + page + " completed!");
            }
            
            decoder.closePdfFile();
            
            try {
                final BufferedOutputStream CSSOutput = new BufferedOutputStream(new FileOutputStream(outputDir.getAbsolutePath() + File.separator + "index.html"));
                
                final String page = "<!DOCTYPE html><html><head><meta http-equiv=\"Refresh\" content=\"0; url=" + getPageAsString(1,pageCount) + ".html\"></head><body></body></html>";
                CSSOutput.write(page.getBytes());
                CSSOutput.flush();
                CSSOutput.close();
                
            } catch (final Exception ee) {
                ee.printStackTrace();
            }
            
        } catch (final Exception e) {
            System.out.println("Failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // Returns page number with 0's prefixed if required.
    private static String getPageAsString(final int page, final int pageCount) {
        final int zeros = (String.valueOf(pageCount)).length() - (String.valueOf(page)).length();
        StringBuilder pageAsString = new StringBuilder();
        for (int i = 0; i < zeros; i++) {
            pageAsString.append('0');
        }
        pageAsString.append(page);
        return pageAsString.toString();
    }
    
    // Build the output for a page in HTML.
    private static String getHTML(final String pdfName, final String pageNumber, final String prevPage, final String nextPage, final String pageCount, final int minZoom, final int maxZoom) {
        
        final StringBuilder sb = new StringBuilder();
        
        sb.append("<!DOCTYPE html>\n");
        sb.append("<html lang=\"en\">\n");
        sb.append("<head>\n");
        sb.append("\t<meta charset=\"utf-8\" />\n");
        sb.append("\t<title>").append(pdfName).append(" Page ").append(pageNumber).append("</title>\n");
        sb.append("</head>\n");
        sb.append('\n');
        sb.append("<body>\n");
        sb.append("\t<div id=\"page").append(pageNumber).append("\" style=\"width:1050px;height:590px;margin:10px auto;border:2px solid #000;\"></div>\n");
        sb.append("\t<center>").append((prevPage != null) ? "<a href=\"" + prevPage + ".html\" >&lt;&lt;</a>" : "&lt;&lt;").append(" Page ").append(pageNumber).append(" of ").append(pageCount).append(' ').append((nextPage != null) ? "<a href=\"" + nextPage + ".html\" > &gt;&gt;</a>" : "&gt;&gt;").append("</center>\n");
        sb.append('\n');
        sb.append("\t<script type=\"text/javascript\" src=\"http://maps.google.com/maps/api/js?libraries=geometry&sensor=false\"></script>\n");
        sb.append("\t<script type=\"text/javascript\">\n");
        sb.append("\t/* <![CDATA[ */\n");
        sb.append("\t\t// Google Maps Demo\n");
        sb.append("\t\tvar Demo = Demo || {};\n");
        sb.append("\t\tDemo.ImagesBaseUrl = '';\n");
        sb.append('\n');
        sb.append("\t\t//Page").append(pageNumber).append('\n');
        sb.append("\t\tDemo.Page").append(pageNumber).append(" = function (container) {\n");
        sb.append("\t\t\t// Create map\n");
        sb.append("\t\t\tthis._map = new google.maps.Map(container, {\n");
        sb.append("\t\t\t\tzoom: ").append(minZoom).append(",\n");
        sb.append("\t\t\t\tcenter: new google.maps.LatLng(0, -20),\n");
        sb.append("\t\t\t\tmapTypeControl: false,\n");
        sb.append("\t\t\t\tstreetViewControl: false\n");
        sb.append("\t\t\t});\n");
        sb.append('\n');
        sb.append("\t\t\t// Set custom tiles\n");
        sb.append("\t\t\tthis._map.mapTypes.set('").append(pageNumber).append("', new Demo.ImgMapType('").append(pageNumber).append("', '#E5E3DF'));\n");
        sb.append("\t\t\tthis._map.setMapTypeId('").append(pageNumber).append("');\n");
        sb.append("\t\t};\n");
        sb.append('\n');
        sb.append("\t\t// ImgMapType class\n");
        sb.append("\t\tDemo.ImgMapType = function (theme, backgroundColor) {\n");
        sb.append("\t\t\tthis.name = this._theme = theme;\n");
        sb.append("\t\t\tthis._backgroundColor = backgroundColor;\n");
        sb.append("\t\t};\n");
        sb.append('\n');
        sb.append("\t\tDemo.ImgMapType.prototype.tileSize = new google.maps.Size(256, 256);\n");
        sb.append("\t\tDemo.ImgMapType.prototype.minZoom = ").append(minZoom).append(";\n");
        sb.append("\t\tDemo.ImgMapType.prototype.maxZoom = ").append(maxZoom).append(";\n");
        sb.append('\n');
        sb.append("\t\tDemo.ImgMapType.prototype.getTile = function (coord, zoom, ownerDocument) {\n");
        sb.append("\t\t\tvar tilesCount = Math.pow(2, zoom);\n");
        sb.append('\n');
        sb.append("\t\t\tif (coord.x >= tilesCount || coord.x < 0 || coord.y >= tilesCount || coord.y < 0) {\n");
        sb.append("\t\t\t\tvar div = ownerDocument.createElement('div');\n");
        sb.append("\t\t\t\tdiv.style.width = this.tileSize.width + 'px';\n");
        sb.append("\t\t\t\tdiv.style.height = this.tileSize.height + 'px';\n");
        sb.append("\t\t\t\tdiv.style.backgroundColor = this._backgroundColor;\n");
        sb.append("\t\t\t\treturn div;\n");
        sb.append("\t\t\t}\n");
        sb.append('\n');
        sb.append("\t\t\tvar img = ownerDocument.createElement('IMG');\n");
        sb.append("\t\t\timg.width = this.tileSize.width;\n");
        sb.append("\t\t\timg.height = this.tileSize.height;\n");
        sb.append("\t\t\timg.src = Demo.Utils.GetImageUrl(this._theme + '/tile_' + zoom + '_' + coord.x + '-' + coord.y + '.png');\n");
        sb.append('\n');
        sb.append("\t\t\treturn img;\n");
        sb.append("\t\t};\n");
        sb.append('\n');
        sb.append("\t\t// Other\n");
        sb.append("\t\tDemo.Utils = Demo.Utils || {};\n");
        sb.append('\n');
        sb.append("\t\tDemo.Utils.GetImageUrl = function (image) {\n");
        sb.append("\t\t\treturn Demo.ImagesBaseUrl + image;\n");
        sb.append("\t\t};\n");
        sb.append('\n');
        sb.append("\t\t// Map creation\n");
        sb.append("\t\tgoogle.maps.event.addDomListener(window, 'load', function () {\n");
        sb.append("\t\t\tvar page").append(pageNumber).append(" = new Demo.Page").append(pageNumber).append("(document.getElementById('page").append(pageNumber).append("'));\n");
        sb.append("\t\t});\n");
        sb.append("\t/* ]]> */\n");
        sb.append("\t</script>\n");
        sb.append("</body>\n");
        sb.append("</html>\n");
        
        return sb.toString();
    }
    
}
