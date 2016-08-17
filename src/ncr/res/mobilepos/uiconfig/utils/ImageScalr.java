package ncr.res.mobilepos.uiconfig.utils;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ImagingOpException;

public class ImageScalr {
	/**
	 * Used to implement a straight-forward image-scaling operation using Java
	 * 2D.	
	 * 
	 * @param src
	 *            The image that will be scaled.
	 * @param targetWidth
	 *            The target width for the scaled image.
	 * @param targetHeight
	 *            The target height for the scaled image.
	 * @param interpolationHintValue
	 *            The {@link RenderingHints} interpolation value used to
	 *            indicate the method that {@link Graphics2D} should use when
	 *            scaling the image.
	 * 
	 * @return the result of scaling the original <code>src</code> to the given
	 *         dimensions using the given interpolation method.
	 */
	protected static BufferedImage scaleImage(BufferedImage src,
			int targetWidth, int targetHeight, Object interpolationHintValue) {
		// Setup the rendering resources to match the source image's
		BufferedImage result = createOptimalImage(src, targetWidth,
				targetHeight);
		Graphics2D resultGraphics = result.createGraphics();

		// Scale the image to the new buffer using the specified rendering hint.
		resultGraphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
				interpolationHintValue);
		resultGraphics.drawImage(src, 0, 0, targetWidth, targetHeight, null);

		// Just to be clean, explicitly dispose our temporary graphics object
		resultGraphics.dispose();

		// Return the scaled image to the caller.
		return result;
	}
	
	/**
	 * Used to create a  BufferedImage with the given dimensions and the
	 * most optimal RGB TYPE capable of being rendered into from
	 * the given src.
	 * <p/>		
	 * @param src
	 *            The source image that will be analyzed to determine the most
	 *            optimal image type it can be rendered into.
	 * @param width
	 *            The width of the newly created resulting image.
	 * @param height
	 *            The height of the newly created resulting image.
	 * 
	 * @return a new  BufferedImage representing the most optimal target
	 *         image type that src can be rendered into.
	 * 	
	 */
	protected static BufferedImage createOptimalImage(BufferedImage src,
			int width, int height) throws IllegalArgumentException {
		if (width < 0 || height < 0)
			throw new IllegalArgumentException("width [" + width
					+ "] and height [" + height + "] must be >= 0");

		return new BufferedImage(
				width,
				height,
				(src.getTransparency() == Transparency.OPAQUE ? BufferedImage.TYPE_INT_RGB
						: BufferedImage.TYPE_INT_ARGB));
	}
	
	/**
	 * Resize a given image to the target
	 * width and height  to
	 * the result before returning it.	 
	 * 
	 * @param src
	 *            The image that will be scaled.
	 * @param targetWidth
	 *            The target width that you wish the image to have.
	 * @param targetHeight
	 *            The target height that you wish the image to have.
	 * @param ops
	 * 	
	 * @return a new  BufferedImage representing the scaled
	 *         src image.
	 * 	
	 */
	public static BufferedImage resize(BufferedImage src,  int targetWidth, int targetHeight,
			BufferedImageOp... ops) throws IllegalArgumentException,
			ImagingOpException {   	
		BufferedImage result = null;
				result = scaleImage(src, targetWidth, targetHeight,
						RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		return result;
	}
}
