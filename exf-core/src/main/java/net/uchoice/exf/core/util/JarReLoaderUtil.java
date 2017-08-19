package net.uchoice.exf.core.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JarReLoaderUtil extends ClassReLoaderUtil {

    private static final Logger logger = LoggerFactory.getLogger(JarReLoaderUtil.class);

    private static final String APPNAME_REGEX = "[a-zA-Z\\-]{1,}";

    /**
     * 根据类所归属jar的绝对路径获取该类归属的业务
     * @param classJarPathFile
     * @return
     */
    public static String getAppJarName(String classJarPathFile) {
        if(StringUtils.isBlank(classJarPathFile)) {
            return null;
        }

        String appJarName = "";//类所归属的业务包名
        try {
            if(classJarPathFile.endsWith("!/")) {
                classJarPathFile = classJarPathFile.substring(0, classJarPathFile.lastIndexOf("!/"));
            }
            int jarFileIndex = classJarPathFile.lastIndexOf("/");
            String jar = classJarPathFile.substring(jarFileIndex + 1);
            Pattern pattern = Pattern.compile(APPNAME_REGEX);
            Matcher matcher = pattern.matcher(jar);
            if (matcher.find()){
                appJarName = matcher.group();
            }
            if(StringUtils.isNotBlank(appJarName)) {
                int index = appJarName.lastIndexOf("-");
                if(index > 0) {
                    appJarName = appJarName.substring(0, appJarName.lastIndexOf("-"));
                }
            }
        }catch (Exception e) {
            logger.error("getAppJarName error for:" + classJarPathFile, e);
        }

        return appJarName;
    }

}
