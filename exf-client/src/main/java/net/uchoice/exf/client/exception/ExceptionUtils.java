package net.uchoice.exf.client.exception;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.lang3.StringUtils;

public final class ExceptionUtils {

	/**
	 * WRAPPED_MARKER.
	 */
	public static final String WRAPPED_MARKER = " [wrapped] ";

	/**
	 * LINE_SEPARATOR.
	 */
	private static final String LINE_SEPARATOR = System.getProperty("line.separator");

	/**
	 * the CAUSE_METHOD_NAMES.
	 */
	private static final List<String> CAUSE_METHOD_NAMES = Arrays.asList("getCause", "getNextException",
			"getTargetException", "getException", "getSourceException", "getRootCause", "getCausedByException",
			"getNested");

	/**
	 * <p>
	 * The Method object for JDK1.4 getCause.
	 * </p>
	 */
	private static final Method THROWABLE_CAUSE_METHOD;

	static {
		Method getCauseMethod = null;
		try {
			getCauseMethod = Throwable.class.getMethod("getCause");
		} catch (Exception e) {
			getCauseMethod = null;
		}
		THROWABLE_CAUSE_METHOD = getCauseMethod;
	}

	/**
	 * @param throwable
	 *            the cause throwable.
	 * @return the Throwable.
	 */
	public static Throwable getCause(Throwable throwable) {
		return getCause(throwable, CAUSE_METHOD_NAMES);
	}

	public static Throwable getCause(Throwable throwable, List<String> methodNames) {
		Throwable cause = null;
		if (null == throwable) {
			return null;
		}
		cause = getCauseUsingWellKnownTypes(throwable);
		if (null == cause) {
			if (null == methodNames) {
				methodNames = CAUSE_METHOD_NAMES;
			}
			for (String methodName : methodNames) {
				if (null != methodName) {
					cause = getCauseUsingMethodName(throwable, methodName);
					if (null != cause) {
						break;
					}
				}
			}

			if (null == cause) {
				cause = getCauseUsingFieldName(throwable, "detail");
			}
		}
		return cause;
	}

	public static Throwable getRootCause(Throwable throwable) {
		Throwable cause = getCause(throwable);
		if (null != cause) {
			throwable = cause;
			while (null != (throwable = getCause(throwable))) {
				cause = throwable;
			}
		}
		return cause;
	}

	public static boolean isThrowableNested() {
		return null != THROWABLE_CAUSE_METHOD;
	}

	/**
	 * @param throwable
	 *            throwable
	 * @return true or false.
	 */
	public static boolean isNestedThrowable(Throwable throwable) {
		Class<?> cls = null;
		if (null == throwable) {
			return false;
		}

		if (throwable instanceof Nestable) {
			return true;
		} else if (throwable instanceof SQLException) {
			return true;
		} else if (throwable instanceof InvocationTargetException) {
			return true;
		} else if (isThrowableNested()) {
			return true;
		}

		cls = throwable.getClass();
		for (String methodName : CAUSE_METHOD_NAMES) {
			try {
				Method method = cls.getMethod(methodName);
				if (Throwable.class.isAssignableFrom(method.getReturnType())) {
					return true;
				}
			} catch (Exception ignored) {
				ignored.printStackTrace();
			}
		}

		try {
			cls.getField("detail");
			return true;
		} catch (Exception ignored) {
			ignored.printStackTrace();
		}

		return false;
	}

	public static int getThrowableCount(Throwable throwable) {
		int count = 0;
		while (null != throwable) {
			count++;
			throwable = ExceptionUtils.getCause(throwable);
		}
		return count;
	}

	public static Throwable[] getThrowables(Throwable throwable) {
		List<Throwable> list = new ArrayList<Throwable>();
		while (null != throwable) {
			list.add(throwable);
			throwable = ExceptionUtils.getCause(throwable);
		}
		return (Throwable[]) list.toArray(new Throwable[list.size()]);
	}

	public static int indexOfThrowable(Throwable throwable, Class<?> type) {
		return indexOfThrowable(throwable, type, 0);
	}

	public static int indexOfThrowable(Throwable throwable, Class<?> type, int fromIndex) {
		Throwable[] throwables = null;
		if (null == throwable) {
			return -1;
		}
		if (0 > fromIndex) {
			fromIndex = 0;
		}
		throwables = ExceptionUtils.getThrowables(throwable);
		if (fromIndex >= throwables.length) {
			return -1;
		}
		for (int i = fromIndex; i < throwables.length; i++) {
			if (throwables[i].getClass().equals(type)) {
				return i;
			}
		}
		return -1;
	}

	public static void printRootCauseStackTrace(Throwable throwable) {
		printRootCauseStackTrace(throwable, System.err);
	}

	public static void printRootCauseStackTrace(Throwable throwable, PrintStream stream) {
		String[] trace = null;
		if (null == throwable) {
			return;
		}
		if (null == stream) {
			throw new IllegalArgumentException("The PrintStream must not be null");
		}
		trace = getRootCauseStackTrace(throwable);
		for (int i = 0; i < trace.length; i++) {
			stream.println(trace[i]);
		}
		stream.flush();
	}

	public static void printRootCauseStackTrace(Throwable throwable, PrintWriter writer) {
		String[] trace = null;
		if (null == throwable) {
			return;
		}
		if (null == writer) {
			throw new IllegalArgumentException("The PrintWriter must not be null");
		}
		trace = getRootCauseStackTrace(throwable);
		for (int i = 0; i < trace.length; i++) {
			writer.println(trace[i]);
		}
		writer.flush();
	}

	public static String[] getRootCauseStackTrace(Throwable throwable) {
		Throwable[] throwables = null;
		int count = -1;
		ArrayList<String> frames = null;
		List<String> nextTrace = null;
		if (null == throwable) {
			return new String[] {};
		}
		throwables = getThrowables(throwable);
		count = throwables.length;
		frames = new ArrayList<String>();
		nextTrace = getStackFrameList(throwables[count - 1]);

		for (int i = count - 1; 0 <= i; i--) {
			List<String> trace = nextTrace;
			if (0 != i) {
				nextTrace = getStackFrameList(throwables[i - 1]);
				removeCommonFrames(trace, nextTrace);
			}
			if (i == count - 1) {
				frames.add(throwables[i].toString());
			} else {
				frames.add(WRAPPED_MARKER + throwables[i].toString());
			}
			for (int j = 0; j < trace.size(); j++) {
				frames.add(trace.get(j));
			}
		}
		return (String[]) frames.toArray(new String[0]);
	}

	public static void removeCommonFrames(List<String> causeFrames, List<String> wrapperFrames) {
		int causeFrameIndex = -1;
		int wrapperFrameIndex = -1;
		if (null == causeFrames || null == wrapperFrames) {
			throw new IllegalArgumentException("The List must not be null");
		}
		causeFrameIndex = causeFrames.size() - 1;
		wrapperFrameIndex = wrapperFrames.size() - 1;
		while (0 <= causeFrameIndex && 0 <= wrapperFrameIndex) {
			// Remove the frame from the cause trace if it is the same
			// as in the wrapper trace
			String causeFrame = (String) causeFrames.get(causeFrameIndex);
			String wrapperFrame = (String) wrapperFrames.get(wrapperFrameIndex);
			if (causeFrame.equals(wrapperFrame)) {
				causeFrames.remove(causeFrameIndex);
			}
			causeFrameIndex--;
			wrapperFrameIndex--;
		}
	}

	public static String getStackTrace(Throwable throwable) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw, true);
		throwable.printStackTrace(pw);
		return sw.getBuffer().toString();
	}

	public static String getFullStackTrace(Throwable throwable) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw, true);
		Throwable[] ts = getThrowables(throwable);
		for (int i = 0; i < ts.length; i++) {
			ts[i].printStackTrace(pw);
			if (isNestedThrowable(ts[i])) {
				break;
			}
		}
		return sw.getBuffer().toString();
	}

	public static String[] getStackFrames(Throwable throwable) {
		if (null == throwable) {
			return new String[] {};
		}
		return getStackFrames(getStackTrace(throwable));
	}

	public static String[] getStackFrames(String stackTrace) {
		String linebreak = LINE_SEPARATOR;
		StringTokenizer frames = new StringTokenizer(stackTrace, linebreak);
		List<String> list = new LinkedList<String>();
		while (frames.hasMoreTokens()) {
			list.add(frames.nextToken());
		}
		return (String[]) list.toArray(new String[list.size()]);
	}

	public static List<String> getStackFrameList(Throwable t) {
		String stackTrace = getStackTrace(t);
		String linebreak = LINE_SEPARATOR;
		StringTokenizer frames = new StringTokenizer(stackTrace, linebreak);
		List<String> list = new LinkedList<String>();
		boolean traceStarted = false;
		while (frames.hasMoreTokens()) {
			String token = frames.nextToken();
			// Determine if the line starts with <whitespace>at
			int at = token.indexOf("at");
			if (-1 != at && StringUtils.isBlank(token.substring(0, at))) {
				traceStarted = true;
				list.add(token);
			} else if (traceStarted) {
				break;
			}
		}
		return list;
	}

	private static Throwable getCauseUsingWellKnownTypes(Throwable throwable) {
		if (throwable instanceof Nestable) {
			return ((Nestable) throwable).getCause();
		} else if (throwable instanceof SQLException) {
			return ((SQLException) throwable).getNextException();
		} else if (throwable instanceof InvocationTargetException) {
			return ((InvocationTargetException) throwable).getTargetException();
		} else {
			return null;
		}
	}

	private static Throwable getCauseUsingMethodName(Throwable throwable, String methodName) {
		Method method = null;
		try {
			method = throwable.getClass().getMethod(methodName);
		} catch (Exception ignored) {
			ignored.printStackTrace();
		}

		if (null != method && Throwable.class.isAssignableFrom(method.getReturnType())) {
			try {
				return (Throwable) method.invoke(throwable);
			} catch (Exception ignored) {
				ignored.printStackTrace();
			}
		}
		return null;
	}

	private static Throwable getCauseUsingFieldName(Throwable throwable, String fieldName) {
		Field field = null;
		try {
			field = throwable.getClass().getField(fieldName);
		} catch (Exception ignored) {
			ignored.printStackTrace();
		}

		if (null != field && Throwable.class.isAssignableFrom(field.getType())) {
			try {
				return (Throwable) field.get(throwable);
			} catch (Exception ignored) {
				ignored.printStackTrace();
			}
		}
		return null;
	}

	private ExceptionUtils() {
	}

}
