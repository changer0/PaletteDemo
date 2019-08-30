package com.lulu.palettedemo;

import android.graphics.Bitmap;
import android.os.SystemClock;
import android.util.Log;


public class GaussianBlurBitmap {
	private static final String TAG = "GaussianBlurBitmap";
	private static final boolean DBG = true;
	
	private static final int RED_MASK = 0xff0000;
	private static final int RED_MASK_SHIFT = 16;
	private static final int GREEN_MASK = 0x00ff00;
	private static final int GREEN_MASK_SHIFT = 8;
	private static final int BLUE_MASK = 0x0000ff;
	
	private GaussianBlurBitmap(){
	}
	
	public static Bitmap createBlurredBitmap(Bitmap bitmap) {
		if (DBG) log("createBlurredBitmap()...");
		long startTime = SystemClock.uptimeMillis();
		if (bitmap == null) {
			Log.w(TAG, "createBlurredBitmap: null bitmap");
			return null;
		}
		 
		if (DBG) log("- input bitmap: " + bitmap.getWidth() + " x " + bitmap.getHeight()); 
		// The bitmap we pass to gaussianBlur() needs to have a width
		// that's a power of 2, so scale up to 128x128.
		final int scaledSize = 128;
		bitmap = Bitmap.createScaledBitmap(bitmap,scaledSize, scaledSize,true /* filter */);
		if (DBG) log("- after resize: " + bitmap.getWidth() + " x " + bitmap.getHeight());
		 
		bitmap = gaussianBlur(bitmap);
		if (DBG) log("- after blur: " + bitmap.getWidth() + " x " + bitmap.getHeight());
		 
		long endTime = SystemClock.uptimeMillis();
		if (DBG) log("createBlurredBitmap() done (elapsed = " + (endTime - startTime) + " msec)");
		return bitmap;
	}
	
	public static Bitmap gaussianBlur(Bitmap source) {
		int width = source.getWidth();
		int height = source.getHeight();
		if (DBG) log("gaussianBlur(): input: " + width + " x " + height);
		 
		// Create a source and destination buffer for the image.
		int numPixels = width * height;
		int[] in = new int[numPixels];
		int[] tmp = new int[numPixels];
		
		// Get the source pixels as 32-bit ARGB.
		source.getPixels(in, 0, width, 0, 0, width, height);
		
		// Gaussian is a separable kernel, so it is decomposed into a horizontal
		// and vertical pass.
		// The filter function applies the kernel across each row and transposes
		// the output.
		// Hence we apply it twice to provide efficient horizontal and vertical
		// convolution.
		// The filter discards the alpha channel.
		gaussianBlurFilter(in, tmp, width, height);
		gaussianBlurFilter(tmp, in, width, height);
		
		// Return a bitmap scaled to the desired size.
		Bitmap filtered = Bitmap.createBitmap(in, width, height, Bitmap.Config.ARGB_8888);
		source.recycle();
		return filtered;
	}

	private static void gaussianBlurFilter(int[] in, int[] out, int width, int height) {
		// This function is currently hardcoded to blur with RADIUS = 4.
		// (If you change RADIUS, you'll have to change the weights[] too.)
		final int RADIUS = 4;
		final int[] weights = { 13, 23, 32, 39, 42, 39, 32, 23, 13}; // Adds up to 256
		int inPos = 0;
		int widthMask = width - 1; // width must be a power of two.
		for (int y = 0; y < height; ++y) {
			// Compute the alpha value.
			int alpha = 0xff;
			// Compute output values for the row.
			int outPos = y;
			for (int x = 0; x < width; ++x) {
				int red = 0;
				int green = 0;
				int blue = 0;
				for (int i = -RADIUS; i <= RADIUS; ++i) {
					int argb = in[inPos + (widthMask & (x + i))];
					int weight = weights[i+RADIUS];
					red += weight *((argb & RED_MASK) >> RED_MASK_SHIFT);
					green += weight *((argb & GREEN_MASK) >> GREEN_MASK_SHIFT);
					blue += weight *(argb & BLUE_MASK);
				}
				// Output the current pixel.
				out[outPos] = (alpha << 24) | ((red >> 8) << RED_MASK_SHIFT)
						| ((green >> 8) << GREEN_MASK_SHIFT)
						| (blue >> 8);
				outPos += height;
			}
			inPos += width;
		}
	}

	private static void log(String msg) {
//		Log.d(TAG, msg);
	}	

}
