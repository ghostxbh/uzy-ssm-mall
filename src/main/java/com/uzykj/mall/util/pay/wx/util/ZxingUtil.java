package com.uzykj.mall.util.pay.wx.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

public class ZxingUtil {
	private static Log log = LogFactory.getLog(ZxingUtil.class);
	private static final int BLACK = -16777216;
	private static final int WHITE = -1;

	public ZxingUtil() {
	}

	private static BufferedImage toBufferedImage(BitMatrix matrix) {
		int width = matrix.getWidth();
		int height = matrix.getHeight();
		BufferedImage image = new BufferedImage(width, height, 1);

		for (int x = 0; x < width; ++x) {
			for (int y = 0; y < height; ++y) {
				image.setRGB(x, y, matrix.get(x, y) ? -16777216 : -1);
			}
		}

		return image;
	}

	private static void writeToFile(BitMatrix matrix, String format, File file) throws IOException {
		BufferedImage image = toBufferedImage(matrix);
		if (!ImageIO.write(image, format, file)) {
			throw new IOException("Could not write an image of format " + format + " to " + file);
		}
	}

	public static File getQRCodeImge(String contents, int width, String imgPath) {
		return getQRCodeImge(contents, width, width, imgPath);
	}

	public static File getQRCodeImge(String contents, int width, int height, String imgPath) {
		try {
			@SuppressWarnings({ "rawtypes", "unchecked" })
			Map<EncodeHintType, Object> hints = new Hashtable();
			hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
			hints.put(EncodeHintType.CHARACTER_SET, "UTF8");
			BitMatrix bitMatrix = (new MultiFormatWriter()).encode(contents, BarcodeFormat.QR_CODE, width, height,
					hints);
			File imageFile = new File(imgPath);
			writeToFile(bitMatrix, "png", imageFile);
			return imageFile;
		} catch (Exception var7) {
			log.error("create QR code error!", var7);
			return null;
		}
	}
}
