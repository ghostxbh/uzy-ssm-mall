package com.uzykj.mall.util;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.UUID;

/**
 * 文件路径判断
 *
 * @author ghostxbh
 * @DateTime 2018-10-28 晚上20：07
 */
@Slf4j
public class FileUtil {
    /**
     * 判断文件是否存在
     *
     * @param file
     * @DateTime 2018-10-28 晚上20：07
     */
    public static boolean judeFileExists(File file) {
        boolean flag = false;
        if (file.exists()) {
            log.info("文件已存在");
            flag = true;
        } else {
            log.info("文件已存在，准备创建····");
            flag = false;
        }
        return flag;
    }

    /**
     * 判断文件夹是否存在
     *
     * @param file
     * @DateTime 2018-10-28 晚上20：07
     */
    public static boolean judeDirExists(File file) {
        boolean flag = false;
        if (file.exists()) {
            if (file.isDirectory()) {
                log.info("文件夹已存在");
                flag = true;
            }
        } else {
            log.info("文件夹不存在，准备创建····");
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
            log.info("目录 " + descDirNames + " 已存在!");
            return false;
        }
        // 创建目录
        if (descDir.mkdirs()) {
            log.info("目录 " + descDirNames + " 创建成功!");
            return true;
        } else {
            log.info("目录 " + descDirNames + " 创建失败!");
            return false;
        }

    }

    /**
     * 生成文件URL
     *
     * @param fileSubfix
     * @param imageType
     * @param name
     * @return
     */
    public static String generFileUrl(String fileSubfix, String imageType, String name) {
        StringBuilder builder = new StringBuilder(fileSubfix)
                .append("/")
                .append(imageType)
                .append("/")
                .append(name);
        return new String(builder);
    }

    /**
     * 生成新的文件名（UUID）
     *
     * @param originalFileName
     * @return
     */
    public static String generNewFileName(String originalFileName) {
        String extension = originalFileName.substring(originalFileName.lastIndexOf('.'));
        return UUID.randomUUID() + extension;
    }

    /**
     * 获取根路径
     *
     * @param session
     * @param path
     * @param imageType
     * @return
     */
    public static String generLocalFilePath(HttpSession session, String path, String imageType) {
        String realPath = session.getServletContext().getRealPath("/");
        StringBuilder builder = new StringBuilder(realPath)
                .append(path)
                .append("/")
                .append(imageType);
        return new String(builder);
    }
}
