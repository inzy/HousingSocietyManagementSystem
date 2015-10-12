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
 * ExampleImageDrawOnScreenHandler.java
 * ---------------
 */
package org.jpedal.examples.handlers;

import org.jpedal.objects.GraphicsState;
import org.jpedal.objects.raw.PdfObject;
import org.jpedal.constants.PDFImageProcessing;
import org.jpedal.io.JAIHelper;
import org.jpedal.io.ObjectStore;
import org.jpedal.utils.LogWriter;
import org.jpedal.color.ColorSpaces;

import javax.media.jai.KernelJAI;
import javax.media.jai.TiledImage;
import javax.media.jai.JAI;
import javax.media.jai.operator.CropDescriptor;
import java.awt.image.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.*;
import java.io.File;

/**
 * example code to plugin external image handler. Code to enable commented out in Viewer
 */
public class ExampleImageDrawOnScreenHandler implements org.jpedal.external.ImageHandler {

    //tell JPedal if it ignores its own Image code or not
    @Override
    public boolean alwaysIgnoreGenericHandler() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }//pass in raw data for image handling - if valid image returned it will be used.
    //if alwaysIgnoreGenericHandler() is true JPedal code always ignored. If false, JPedal code used if null

    @Override
    public BufferedImage processImageData(final GraphicsState gs, final PdfObject XObject) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean imageHasBeenScaled() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean drawImageOnscreen(BufferedImage image, final int optionsApplied, AffineTransform upside_down,
                                     final String currentImageFile, final Graphics2D g2, final boolean renderDirect,
                                     final ObjectStore objectStoreRef, final boolean isPrinting) {

        //this is the draw code from DynamicVectorRenderer as at 11th June 2007

        final double[] values=new double[6];
        upside_down.getMatrix(values);

        final boolean isSlightlyRotated=(values[0]*values[1]!=0)||(values[2]*values[3]!=0);

        //accelerate large bw images non-rotated
        //accelerate large bw images non-rotated (use for all images for moment)
				if(isSlightlyRotated || image.getWidth()<800 || renderDirect){ //image.getType()!=12 || CTM[0][0]<0 || CTM[1][1]<0 || CTM[1][0]<0 || CTM[0][1]<0)
                    g2.drawImage(image,upside_down,null);
                }else{ //speedup large straightforward images

                    double dy=0,dx=0;

                    //if already turned, tweak transform
					if(optionsApplied!=PDFImageProcessing.NOTHING){

						//int count=values.length;
						//for(int jj=0;jj<count;jj++)
						//System.out.println(jj+"=="+values[jj]);

//						System.out.println(image.getWidth());
//						System.out.println(image.getHeight());
//						System.out.println(values[4]*image.getHeight()/image.getWidth());

                        //alter array to account for rotation elsewhere
						if((optionsApplied & PDFImageProcessing.IMAGE_ROTATED)==PDFImageProcessing.IMAGE_ROTATED){

                            if(values[0]>0 && values[3]<0 &&(optionsApplied & PDFImageProcessing.IMAGE_INVERTED)==PDFImageProcessing.IMAGE_INVERTED){
                                final double newWidth= (values[0]*image.getWidth());
                                final double newHeight= -((values[3]*image.getHeight()));
                                dy=values[5]-newHeight;
                                values[5]=newHeight;

                                //allow for rounding error in scaling
                                if(newWidth-(int)newWidth>0.5){
                                    dx -= 1;
                                }

                            }else if(values[0]<0 && values[3]>0){
                                final double tmp1=values[0];
                                //double tmp3=values[2];
                                values[0]=values[3];
                                values[3]=tmp1;
                                values[4]=0;
                                values[5]=(int)(values[4]*image.getHeight()/image.getWidth());

                            }
                        } else if(values[0]>0 && values[3]>0 &&(optionsApplied & PDFImageProcessing.IMAGE_INVERTED)==PDFImageProcessing.IMAGE_INVERTED){

                            dy=values[5];

                            final double tmp1=values[0];
                            //double tmp3=values[2];
                            values[0]=values[3];
                            values[3]=tmp1;
                            values[4]=0;
                            values[5]=(int)(values[4]*image.getHeight()/image.getWidth());
                        }else{
                        }

                        upside_down=new AffineTransform(values);
                    }

					boolean imageProcessed=false;

                    if(JAIHelper.isJAIused()){

						//assume worked and set if fails
						//imageProcessed=true;

						/**
						 * try in memory first - does not seem to be a big hit if it fails
						 *
						try{

							image = (javax.media.jai.JAI.create("affine", image, upside_down, new javax.media.jai.InterpolationBicubic(1))).getAsBufferedImage();

							imageProcessed=false;
						}catch(Exception ee){
							imageProcessed=false;
							ee.printStackTrace();
						}catch(Error err){
							imageProcessed=false;

						}
                        /**/

                        if(!imageProcessed && currentImageFile!=null){  //try it via tiled image if failed (thanks to Cesssna for starting code)

							//assume worked and set if fails
							imageProcessed=true;

							try{

								// The minium tile size of an image
								final Dimension tileSize = new Dimension(512,512);

                                
                                /***/
								RenderedImage ri;

								final com.sun.media.jai.codec.SeekableStream s = new com.sun.media.jai.codec.FileSeekableStream(new File(objectStoreRef.getFileForCachedImage(currentImageFile)));

								final com.sun.media.jai.codec.TIFFDecodeParam param = null;

								final com.sun.media.jai.codec.ImageDecoder dec =com.sun.media.jai.codec.ImageCodec.createImageDecoder("tiff",s, param);

								// Which of the multiple images in the TIFF file do we want to load
								// 0 refers to the first, 1 to the second and so  on.
								final int imageToLoad = 0;

								ri = new javax.media.jai.NullOpImage(dec.decodeAsRenderedImage(imageToLoad),
										null,null,
										javax.media.jai.OpImage.OP_IO_BOUND);
								// Create color model and color space references
								final ColorModel cm;// = ri.getColorModel();
								//ColorSpace cs = cm.getColorSpace();

                                final RenderingHints hints;
                                
                                // Convert base image to enforce a minimum tile layout to reduce
								// memory resource utilization and enhance performance.
								// Skip conversion if tile already meets minimum tile requirements
								// Tile image first to improve downstream processing
								if( (ri.getTileWidth()*ri.getTileHeight()) > (tileSize.width*tileSize.height) ) {
									cm = ri.getColorModel();
									final SampleModel sm = ri.getSampleModel().createCompatibleSampleModel(tileSize.width,tileSize.height);
									final javax.media.jai.ImageLayout layout = new javax.media.jai.ImageLayout(0,0,tileSize.width,tileSize.height,sm,cm);
                                    hints = new RenderingHints( javax.media.jai.JAI.KEY_IMAGE_LAYOUT,layout);

									// Convert base image using AbsoluteDescriptor
									ri = javax.media.jai.operator.AbsoluteDescriptor.create(ri,hints);
								}

                                //@jason - 3 scaling options and bicubic takes a number parameter
                                // For best rendering performance you should use a buffered image
								image = (javax.media.jai.JAI.create("affine", ri, upside_down, new javax.media.jai.InterpolationBicubic(1))).getAsBufferedImage();
								//image = (javax.media.jai.JAI.create("affine", ri, upside_down, new javax.media.jai.InterpolationBicubic2(2))).getAsBufferedImage();
                                //image = (javax.media.jai.JAI.create("affine", ri, upside_down, new javax.media.jai.InterpolationBicubic(1))).getAsBufferedImage();

                                //image=getAffineTransform(ri, upside_down, hints,quality);

							}catch(final Exception ee){
								imageProcessed=false;
								ee.printStackTrace();
							}catch(final Error err){
								imageProcessed=false;

							}
						}

						if(!imageProcessed) {
                            LogWriter.writeLog("Unable to use JAI for image inversion");
                        }
					}else {
                        imageProcessed = true;
                    }

					//fall back on lower quality standard op
					if(!imageProcessed){

						imageProcessed=true;

						try{
							final AffineTransformOp invert =new AffineTransformOp(upside_down,ColorSpaces.hints);

							image=invert.filter(image,null);
						}catch(final Exception ee){
							imageProcessed=false;
							ee.printStackTrace();
						}catch(final Error err){
							imageProcessed=false;

						}
					}

					if(imageProcessed){

                        Shape rawClip=null;

                        if(isPrinting && dy==0){ //adjust to fit
                            final double[] affValues=new double[6];
                            g2.getTransform().getMatrix(affValues);

                            //for(int i=0;i<6;i++)
                            //System.out.println(i+"="+affValues[i]);

                            dx=affValues[4]/affValues[0];
                            if(dx>0) {
                                dx = -dx;
                            }

                            dy=affValues[5]/affValues[3];

                            if(dy>0) {
                                dy = -dy;
                            }

                            dy=-(dy+image.getHeight());


                        }

                        //stop part pixels causing black lines
                        if(dy!=0){
                            rawClip=g2.getClip();

                            final int xDiff;
                            final double xScale=g2.getTransform().getScaleX();
                            if(xScale<1) {
                                xDiff = (int) (1 / xScale);
                            } else {
                                xDiff = (int) (xScale + 0.5d);
                            }
                            final int yDiff;
                            final double yScale=g2.getTransform().getScaleY();
                            if(yScale<1) {
                                yDiff = (int) (1 / yScale);
                            } else {
                                yDiff = (int) (yScale + 0.5d);
                            }

                            g2.clipRect((int)dx,(int)(dy+1.5),image.getWidth()-xDiff,image.getHeight()-yDiff);
                            
                        }
                        g2.drawImage(image,(int)dx, (int) dy,null);

                        //put it back
                        if(rawClip!=null) {
                            g2.setClip(rawClip);
                        }
                    }else {
                        g2.drawImage(image, upside_down, null);
                    }
				
        }

        return true;  //To change body of implemented methods use File | Settings | File Templates.
    }

// Code for Affine transform
public static synchronized BufferedImage getAffineTransform(final RenderedImage
ri, final AffineTransform src2me, final RenderingHints hints, final float quality) {

    //@jason - value was undefined (I have assumed its final size of image)
    //tile size of an image
    final double[] values=new double[6];
    src2me.getMatrix(values);
    final Dimension preferredTileSize = new Dimension((int)(values[0]*ri.getWidth()),
            (int)(values[3]*ri.getHeight()));

        // Get translated tile grid offsets
        Point2D pt = new Point2D.Double(
            ri.getTileGridXOffset(),
            ri.getTileGridYOffset()
        );
        pt = src2me.transform(pt, null);

        // Get translated image bounds
        final Rectangle $bounds = new Rectangle(
                (int)pt.getX(),
                (int)pt.getY(),
                ri.getWidth(),
                ri.getHeight()
        );
        final Rectangle bounds =
src2me.createTransformedShape($bounds).getBounds();

        System.out.println(">>"+ri );
        System.out.println(">>"+bounds);

        // Create color and sample modle
        final ColorModel cm = ri.getColorModel();
        final SampleModel sm =
cm.createCompatibleSampleModel(ri.getTileWidth(),ri.getTileHeight());

        // Create Destination image
        final TiledImage img = new TiledImage(
                (int)bounds.getMinX(),
                (int)bounds.getMinY(),
                (int)bounds.getWidth(),
                (int)bounds.getHeight(),
                (int)pt.getX(),
                (int)pt.getY(),
                sm,
                cm
        );

        // Create blur kernel
        final KernelJAI kernel = createBlurKernel(
                (float)src2me.getScaleX(),
                (float)src2me.getScaleY(),
                quality
        );
        final int kw = kernel.getWidth();
        final int kh = kernel.getHeight();

        final AffineTransform at = (AffineTransform) src2me.clone();
        at.translate(
                (pt.getX()),
                (pt.getY())
        );

        double $numXTiles = (ri.getWidth()/preferredTileSize.getWidth());
        double $numYTiles = (ri.getHeight()/preferredTileSize.getHeight());

        if( $numXTiles % 2 > 0 ) {
            ++$numXTiles;
        }
        if( $numYTiles % 2 > 0 ) {
            ++$numYTiles;
        }
        final int numXTiles = (int) $numXTiles;
        final int numYTiles = (int) $numYTiles;

        // For best performance & memory transform each tile
        for(int x=0;x<numXTiles;x++){
            for(int y=0;y<numYTiles;y++) {
                int $x = (int)(x*preferredTileSize.getWidth());
                int $y = (int)(y*preferredTileSize.getWidth());
                int $w = (int)(preferredTileSize.getWidth());
                int $h = (int)(preferredTileSize.getHeight());

                if( $x < 0 ) {
                    $x = 0;
                }
                if( $y < 0 ) {
                    $y = 0;
                }
                if( $x+$w > ri.getWidth() ) {
                    $w = ri.getWidth() - $x;
                }
                if( $y+$h > ri.getHeight() ) {
                    $h = ri.getHeight() - $y;
                }

                // Create tile bounds
                final Rectangle tileBounds = new Rectangle($x,$y,$w,$h);

                // Fix Kernel
                $x = tileBounds.x-kw;
                $y = tileBounds.y-kh;
                $w = tileBounds.width+(kw*2);
                $h = tileBounds.height+(kh*2);

                if( $x < 0 ) {
                    $x = 0;
                }
                if( $y < 0 ) {
                    $y = 0;
                }
                if( $x+$w > ri.getWidth() ) {
                    $w = ri.getWidth() - $x;
                }
                if( $y+$h > ri.getHeight() ) {
                    $h = ri.getHeight() - $y;
                }

                // Adjust tile bounds for convolve operation
                final Rectangle $tileBounds = new Rectangle($x,$y,$w,$h);

//                if(!srcBounds.intersects($tileBounds))
//                    continue;

                // Create artificial tile
                WritableRaster wr;
                try {
                    wr = (WritableRaster) ri.getData($tileBounds);
                } catch (final Exception e ) {
                    wr = null;

                    if(LogWriter.isOutput()) {
                        LogWriter.writeLog("Exception with image "+e);
                    }
                }

                if( wr == null ) {
                    System.out.println( ">>>" );
                    continue;
                }

                wr = wr.createWritableTranslatedChild(0,0);

                // Create a buffered image for convolve operation
                final BufferedImage buf = new BufferedImage(
                        ri.getColorModel(),
                        wr,
                        ri.getColorModel().isAlphaPremultiplied(),
                        null
                );

                // Create the convolved image
                RenderedImage $ri = buf;
                if( quality > 0 ) {
                    $ri = JAI.create("convolve", buf, kernel);

                    // Crop convolve edges from the convolved image
                    if( ((wr.getWidth()-(kw*2)) > 0) &&
((wr.getHeight()-(kh*2)) > 0) ) {
                        $ri = CropDescriptor.create(
                                $ri,
                                (float) kw,
                                (float) kh,
                                (float) (wr.getWidth() - (kw * 2)),
                                (float) wr.getHeight() - (kh * 2),
                                hints
                        );
                    }
                }

                // Draw Rendered Image
                final AffineTransform $at = (AffineTransform) at.clone();
                $at.translate(tileBounds.getX(),tileBounds.getY());

                final Graphics2D g2d = img.createGraphics();
                g2d.drawRenderedImage($ri,$at);
                g2d.dispose();
            }
        }

        return img.getAsBufferedImage();

    } // End Method



    /**
     *
     */
    public static KernelJAI createBlurKernel(float scaleX,float scaleY,
final float quality) {
        // Normalize transform
        scaleX = Math.abs(scaleX);
        scaleY = Math.abs(scaleY);

        int sizeX = 1 + Math.round(quality/scaleX);
        int sizeY = 1 + Math.round(quality/scaleY);

        // Temporary fix - grid size of
        // 3,3;4,4;5,5 cause a convolution
        // error >> image is black
        if(sizeX == 4 && sizeY == 4) {
            sizeX = 3; sizeY = 3;
        }
        if(sizeX == 5 && sizeY == 5){
            sizeX = 3; sizeY = 3;
        }
        if(sizeX == 6 && sizeY == 6) {
            sizeX = 7; sizeY = 7;
        }

        final float[] data = new float[sizeX * sizeY];
        final float factor = 1F / data.length;

        for (int i = 0; i < data.length; i++) {
            data[i] = factor;
        }

        return new KernelJAI(sizeX, sizeY, data);

    } // End Method

}
