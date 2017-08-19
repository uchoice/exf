package net.uchoice.exf.core.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * 文件处理工具类
 *
 */
public class FileUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileUtils.class);

    private FileUtils() {
    }

    /**
     * 读取文件内容
     *
     * @param filePath
     * @return
     */
    public static String read(String filePath) {
        try {
            File file = new File(filePath);
            return org.apache.commons.io.FileUtils.readFileToString(file, "UTF-8");
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * 字符串写磁盘
     * 写文件采用追加的形式
     *
     * @param filePath
     * @param content
     * @return
     */
    public static boolean write(String filePath, String content) {
        if (StringUtils.isNotBlank(filePath) && StringUtils.isNotBlank(content)) {
            try {
                RandomAccessFile randomFile = new RandomAccessFile(filePath, "rw");
                long fileLength = randomFile.length();
                randomFile.seek(fileLength);
                randomFile.writeBytes(content + "\r\n");
                randomFile.close();
            } catch (IOException e) {
                LOGGER.error("Write config file to local disk error!", e);
                return false;
            }
        }
        return false;
    }
}
