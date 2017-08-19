package net.uchoice.exf.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ShellUtils {

    /**
     * LOGGER.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ShellUtils.class);

    public static String execute(String command) {
        try {
            Process process = Runtime.getRuntime().exec(command);
            // 定义shell返回值
            return getResultFromProcess(process);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
            return "";
        }
    }

    private static String getResultFromProcess(Process process) {
        // 定义shell返回值
        String result = null;
        BufferedInputStream in = null;
        BufferedReader br = null;
        try {
            // 获取shell返回流
            in = new BufferedInputStream(process.getInputStream());
            // 字符流转换字节流
            br = new BufferedReader(new InputStreamReader(in));
            // 这里也可以输出文本日志

            String lineStr;
            while ((lineStr = br.readLine()) != null) {
                result = lineStr;
            }
            // 关闭输入流
            br.close();
            in.close();
            return result;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return "";
        } finally {
            if (null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    //ignore.
                }
            }
            if (null != br) {
                try {
                    br.close();
                } catch (IOException e) {
                    //ignore.
                }
            }
        }
    }
}
