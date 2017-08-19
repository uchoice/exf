package net.uchoice.exf.client.exception;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Nonnull;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.PropertyKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ErrorCode {
    /**
     * 约定的错误码的路径
     */
    static final String BUNDLE = "exf.i18n.errors";
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ErrorCode.class);
    /**
     * 展示默认的可读错误码
     */
    private static final String DEFAULT_DISPLAY_ERROR_MESSAGE = "SYSTEM BUSY.";
    /**
     * 默认LOG文案
     */
    private static final String DEFAULT_LOG_ERROR_MESSAGE = "ERROR MESSAGE IS MISSING";

    /**
     * file path for displaying error
     */
    private final static String displayFilePath = "exf/i18n/errors.properties";
    private final static String internalErrorMessageFilePath = "exf/i18n/errors_internal.properties";
    private final static String readableErrorCodeFilePath = "exf/i18n/errors_code.properties";
    
    /**
     * 用于透出的错误文案
     */
    private static Map<Object, Object> displayErrorCodes;
    /**
     * 内部错误代码，用于日志
     */
    private static Map<Object, Object> internalErrorMessage;
    /**
     * 用于业务透出的错误代码（一般由业务方进行解析识别处理）
     */
    private static Map<Object, Object> readableErrorCode;

    private static Map<String, String> cachedLogMessage = new ConcurrentHashMap<String, String>();

    static {
        internalErrorMessage = extractErrorCodes(internalErrorMessageFilePath);
        readableErrorCode = extractErrorCodes(readableErrorCodeFilePath);
        displayErrorCodes = extractErrorCodes(displayFilePath);
    }


    /**
     * 获取供日志使用的内部错误文案。 输出内容同时包含 code 和 message
     *
     * @param key    error code
     * @param params params to build the error message
     * @return
     */
    public static String logMessage(@PropertyKey(resourceBundle = BUNDLE)
                                    @Nonnull String key, Object... params) {
        boolean canNotCache = params != null && params.length > 0;
        if (!canNotCache) {
            String result = cachedLogMessage.get(key);
            if (result != null) {
                return result;
            }
        }
        String logMessage = "log message cannot be retrieved properly";
        try {
            logMessage = searchKeyInAllResourceFile(internalErrorMessage, key, DEFAULT_LOG_ERROR_MESSAGE, params);
        } catch (Exception e) {
            LOGGER.error(logMessage, e);
        }
        String result = "{c:" + key + "," + "m:" + logMessage + "}";
        if (!canNotCache) {
            cachedLogMessage.put(key, result);
        }
        return result;
    }

    /**
     * 获取供日志使用的内部错误文案。
     *
     * @param key    error code
     * @param params params to build the error message
     * @return
     */
    private static String logMessageWithoutCode(@PropertyKey(resourceBundle = BUNDLE)
                                                @Nonnull String key, Object... params) {
        boolean canNotCache = params != null && params.length > 0;
        if (!canNotCache) {
            String result = cachedLogMessage.get(key);
            if (result != null) {
                return result;
            }
        }
        String logMessage = "log message cannot be retrieved properly";
        try {
            logMessage = searchKeyInAllResourceFile(internalErrorMessage, key, DEFAULT_LOG_ERROR_MESSAGE, params);
        } catch (Exception e) {
            LOGGER.error(logMessage, e);
        }
        String result = logMessage;
        if (!canNotCache) {
            cachedLogMessage.put(key, result);
        }
        return result;
    }

    /**
     * 获取供展示用的中文错误文案。
     *
     * @param key    error code
     * @param params params to build the error message
     * @return
     */
    public static String displayMessage(@PropertyKey(resourceBundle = BUNDLE) String key, Object... params) {
        return searchKeyInAllResourceFile(displayErrorCodes, key, DEFAULT_DISPLAY_ERROR_MESSAGE, params);
    }


    private static String searchKeyInAllResourceFile(Map<Object, Object> props,
                                                     String key,
                                                     String defaultValue, Object... params) {
        if (!props.containsKey(key)) return defaultValue;

        Object obj = props.get(key);
        String message = buildMessage(obj, params);
        return StringUtils.isNotBlank(message) ? message : defaultValue;
    }


    private static String buildMessage(Object obj, Object[] params) {
        String message = String.valueOf(obj);

        if (params != null && params.length > 0 && message != null && message.indexOf('{') >= 0) {
            message = MessageFormat.format(message, params);
        }

        //可能是编码的问题,有些 bug,上面的 format 可能会失败.
        if (params != null && params.length > 0 && message != null && message.contains("{0}")) {
            message = MessageFormat.format(message, params);
        }

        return message;
    }

    private static Map<Object, Object> extractErrorCodes(String resourceFilePath) {
        return extractErrorCodes(resourceFilePath, false, null);
    }

    /**
     * @param resourceFilePath 错误码资源文件路径
     * @param replaceEnglishWords 如果全是英文,是否要替换为默认的中文文案
     * @param replaceText 用于替换的文案
     * @return
     */
    private static Map<Object, Object> extractErrorCodes(String resourceFilePath,
                                                         boolean replaceEnglishWords,
                                                         String replaceText) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Map<Object, Object> props = new HashMap<Object, Object>();
        try {
            Enumeration<URL> resources = classLoader.getResources(resourceFilePath);
            while (resources.hasMoreElements()) {
                URL url = resources.nextElement();
                InputStream in = url.openStream();
                Properties prop = new Properties();
                prop.load(new InputStreamReader(in, "UTF-8"));
                if (replaceEnglishWords) {
                    for (Map.Entry<Object, Object> entry : prop.entrySet()) {
                        //如果全是英文,则替换为默认文案
                        if (!hasChineseCharacter(String.valueOf(entry.getValue()))) {
                            entry.setValue(replaceText);
                        }
                    }
                }
                props.putAll(prop);
            }
        } catch (IOException e) {
            return Collections.emptyMap();
        }
        return props;
    }

    /**
     * 是否拥有中文字符
     * @param chineseStr
     * @return
     */
    public static final boolean hasChineseCharacter(String chineseStr) {
        char[] charArray = chineseStr.toCharArray();
        for (int i = 0; i < charArray.length; i++) {
            if ((charArray[i] >= 0x4e00) && (charArray[i] <= 0x9fbb)) {
                return true;
            }
        }
        return false;
    }


    /**
     * 这个方法会从当前模块的resources/i18n/errors.properties文件中解析出错误码和错误文案
     *
     * @param key    bundle key
     * @param params 参数，bundle的值采用MessageFormat的格式化方式
     * @return bundle值，如果bundle key不存在，返回特定key丢失格式
     */
    static ErrorMessage toErrorMessage(@PropertyKey(resourceBundle = BUNDLE) String key, Object... params) {
        String logMessage = logMessageWithoutCode(key, params);
        String displayMessage = displayMessage(key, params);
        String readableCode = searchKeyInAllResourceFile(readableErrorCode, key, key);
        return ErrorMessage.of(key, logMessage, displayMessage, readableCode);


    }

    /**
     * 根据可阅读的错误码，找内部错误码
     *
     * @param readableCode 可阅读的错误码，比如 ITEM_CANNOT_BUY
     * @return 内部的错误码列表，因为可阅读错误码对应多个不同的内部吗
     */
    public static List<String> findErrorCodeViaReadableCode(final String readableCode) {
        List<String> results = Lists.newArrayList();
        if (StringUtils.isEmpty(readableCode))
            return results;
        for (Map.Entry<Object, Object> entry : readableErrorCode.entrySet()) {
            if (!StringUtils.equals(readableCode, String.valueOf(entry.getValue())))
                continue;
            results.add(String.valueOf(entry.getKey()));
        }
        return results;
    }

    /**
     * 内部错误编码是否存在
     *
     * @param errorCode 错误码
     * @return true or false.
     */
    public static boolean isInternalCodeExisted(String errorCode) {
        return null != internalErrorMessage.get(errorCode);
    }

}
