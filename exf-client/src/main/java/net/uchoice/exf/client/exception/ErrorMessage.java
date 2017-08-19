package net.uchoice.exf.client.exception;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.PropertyKey;

public class ErrorMessage implements Serializable {

	/**
	 * serialVersionUID.
	 */
	private static final long serialVersionUID = 3017472277626865964L;
	/**
	 * 对于没有参数的错误码 cache 起来
	 */
	private final static Map<String, ErrorMessage> cachedMessages = new ConcurrentHashMap<String, ErrorMessage>(500);
	/**
	 * the error code.
	 */
	private String errorCode = null;
	/**
	 * the error message.
	 */
	private String errorMessage = null;
	/**
	 * the error message which will be displayed.
	 */
	private String displayErrorMessage = null;
	/**
	 * display error code
	 */
	private String readableErrorCode = null;
	/**
	 * the error content of the error.
	 */
	private Map<String, Object> contents = new HashMap<String, Object>();

	/**
	 * Whether force throw the exception.
	 */
	private boolean forceThrowException = true;

	private ErrorMessage(String code, String message, String displayMessage, String readableCode) {
		this.errorCode = code;
		this.errorMessage = message;
		this.displayErrorMessage = displayMessage;
		this.readableErrorCode = readableCode;
	}

	public static ErrorMessage defaultError() {
		return ErrorMessageHelper.defaultOne();
	}

	/**
	 * @param errorCode
	 *            the code.
	 * @param message
	 *            the message.
	 * @return
	 */
	public static ErrorMessage of(String errorCode, String message) {
		return of(errorCode, message, null);
	}

	/**
	 * 这个构造函数会从当前模块的resources/i18n/errors.properties文件
	 * 中读取相关的错误码和错误文案，并且可以通过intellij的代码完成功能展示出相关的错误码。
	 *
	 * @param key
	 * @param params
	 * @return
	 */
	public static ErrorMessage code(@PropertyKey(resourceBundle = ErrorCode.BUNDLE) String key, Object... params) {

		if (params != null && params.length > 0) {
			return ErrorCode.toErrorMessage(key, params);
		} else { // 没有参数的才可以cache
			ErrorMessage message = cachedMessages.get(key);
			if (message == null) {
				message = ErrorCode.toErrorMessage(key, params);
				cachedMessages.put(key, message);
			}
			return message;
		}

	}

	public static ErrorMessage code(boolean forceThrowException,
			@PropertyKey(resourceBundle = ErrorCode.BUNDLE) String key, Object... params) {

		ErrorMessage errorMessage = code(key, params);
		errorMessage.forceThrowException = forceThrowException;
		return errorMessage;
	}

	/**
	 * @param code
	 *            the code.
	 * @param message
	 *            the message.
	 * @param displayMessage
	 *            the front display message.
	 * @return
	 */
	public static ErrorMessage of(String code, String message, String displayMessage) {
		return of(code, message, displayMessage, "");
	}

	/**
	 * @param code
	 *            the code.
	 * @param message
	 *            the message.
	 * @param displayMessage
	 *            the front display message.
	 * @return
	 */
	public static ErrorMessage of(String code, String message, String displayMessage, String readableCode) {
		return new ErrorMessage(code, message, displayMessage, readableCode);
	}

	public ErrorMessage addErrorContents(Map<String, Object> errorContents) {
		if (MapUtils.isNotEmpty(errorContents)) {
			contents.putAll(errorContents);
		}
		return this;
	}

	/**
	 * toSting 值显示核心内容
	 *
	 * @return
	 */
	@Override
	public String toString() {
		String message = "ErrorMessage{" + "c='" + errorCode + '\'' + ", rC='" + readableErrorCode + '\'' + ", m='"
				+ errorMessage + '\'';
		String content = contentToString();
		if (StringUtils.isNotBlank(content)) {
			message = message + " ct=' " + content + '\'';
		}

		return message + "}";

	}

	public String getFullText() {
		return "ErrorMessage{" + "errorCode='" + errorCode + '\'' + ", errorMessage='" + errorMessage + '\''
				+ ", displayErrorMessage='" + displayErrorMessage + '\'' + ", readableErrorCode='" + readableErrorCode
				+ '\'' + ", contents=" + contents + ", forceThrowException=" + forceThrowException + '}';
	}

	public String contentToString() {
		if (MapUtils.isEmpty(contents))
			return StringUtils.EMPTY;

		StringBuilder buffer = new StringBuilder(128);
		for (Map.Entry<String, Object> p : contents.entrySet()) {
			if (buffer.length() > 0) {
				buffer.append(", ");
			}
			buffer.append(p.getKey()).append(" => ").append(p.getKey());
		}
		return "{" + buffer.toString() + "}";
	}

	public static Map<String, ErrorMessage> getCachedmessages() {
		return cachedMessages;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public void setDisplayErrorMessage(String displayErrorMessage) {
		this.displayErrorMessage = displayErrorMessage;
	}

	public void setReadableErrorCode(String readableErrorCode) {
		this.readableErrorCode = readableErrorCode;
	}

	public void setForceThrowException(boolean forceThrowException) {
		this.forceThrowException = forceThrowException;
	}

	public Map<String, Object> getContents() {
		return contents;
	}

	public void setContents(Map<String, Object> contents) {
		this.contents = contents;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public String getDisplayErrorMessage() {
		return displayErrorMessage;
	}

	public String getReadableErrorCode() {
		return readableErrorCode;
	}

	public boolean isForceThrowException() {
		return forceThrowException;
	}

}
