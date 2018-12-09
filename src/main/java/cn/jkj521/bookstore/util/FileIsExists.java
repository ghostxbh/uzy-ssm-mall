package cn.jkj521.bookstore.util;

import java.io.File;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 文件路径判断
 * @DateTime 2018-10-28 晚上20：07
 * @author xu
 *
 */
public class FileIsExists {

	private static final Logger logger = LoggerFactory.getLogger(FileIsExists.class);

	/**
	 * 判断文件是否存在
	 * 
	 * @author xu
	 * @DateTime 2018-10-28 晚上20：07
	 * @param file
	 */
	public static boolean judeFileExists(File file) {
		boolean flag = false;
		if (file.exists()) {
			logger.info("文件已存在");
			flag = true;
		} else {
			logger.info("文件已存在，准备创建····");
			flag = false;
		}
		return flag;
	}

	/**
	 * 判断文件夹是否存在
	 * 
	 * @author xu
	 * @DateTime 2018-10-28 晚上20：07
	 * @param file
	 */
	public static boolean judeDirExists(File file) {
		boolean flag = false;
		if (file.exists()) {
			if (file.isDirectory()) {
				logger.info("文件夹已存在");
				flag = true;
			}
		} else {
			logger.info("文件夹不存在，准备创建····");
			if (file.mkdir()) {
				flag = true;
			} else {
				flag = false;
			}
		}
		return flag;
	}

}
