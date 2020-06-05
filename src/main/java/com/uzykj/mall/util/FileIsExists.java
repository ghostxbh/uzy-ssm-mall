package com.uzykj.mall.util;

import java.io.File;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 文件路径判断
 * @DateTime 2018-10-28 晚上20：07
 * @author gostxbh
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

	/**
	 * 创建目录
	 *
	 * @param descDirName 目录名,包含路径
	 * @return 如果创建成功，则返回true，否则返回false
	 */
	public static boolean createDirectory(String descDirName) {
		String descDirNames = descDirName;
		if (!descDirNames.endsWith(File.separator)) {
			descDirNames = descDirNames + File.separator;
		}
		File descDir = new File(descDirNames);
		if (descDir.exists()) {
			logger.info("目录 " + descDirNames + " 已存在!");
			return false;
		}
		// 创建目录
		if (descDir.mkdirs()) {
			logger.info("目录 " + descDirNames + " 创建成功!");
			return true;
		} else {
			logger.info("目录 " + descDirNames + " 创建失败!");
			return false;
		}

	}
}
