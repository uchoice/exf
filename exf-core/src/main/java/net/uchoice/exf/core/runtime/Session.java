package net.uchoice.exf.core.runtime;

import net.uchoice.exf.client.exception.ErrorMessage;
import net.uchoice.exf.client.exception.ExfRuntimeException;
import net.uchoice.exf.client.service.ExfRequest;
import net.uchoice.exf.core.context.Context;
import net.uchoice.exf.core.trace.ExfTracker;

public class Session {

	private static final ThreadLocal<Session> SESSIONS = new ThreadLocal<>();

	private Context context = new Context();

	/**
	 * 是否调试模式 调试模式打印节点信息
	 */
	private boolean debug;

	/**
	 * 是否开启跟踪带 跟踪带将打印节点执行流水，debug模式将开启跟踪带
	 */
	private boolean trace;

	private ExfRequest request;

	private String identify;
	
	private ExfTracker tracker = new ExfTracker();

	/**
	 * 会话创建每次会话只能触发一次
	 * 多次触发会重置会话
	 * @param request
	 * @return
	 */
	public static Session create(ExfRequest request) {
		Session session = SESSIONS.get();
		if (null != session) {
			// 重复创建会话需要组织，防止会话冲突
			throw new ExfRuntimeException(ErrorMessage.code("C-EXF-02-01-009", request));
		}
		session = new Session();
		session.setDebug(request.isDebug());
		session.setRequest(request);
		if (request.isDebug()) {
			session.setTrace(true);
		} else {
			session.setTrace(request.isTrace());
		}
		SESSIONS.set(session);
		return session;
	}

	public static Session get() {
		return SESSIONS.get();
	}

	public static Context getSessionContext() {
		return SESSIONS.get().getContext();
	}

	public void close() {
		if (null != SESSIONS.get()) {
			this.context = null;
			SESSIONS.set(null);
			SESSIONS.remove();
		}
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public ExfRequest getRequest() {
		return request;
	}

	public void setRequest(ExfRequest request) {
		this.request = request;
	}

	public String getIdentify() {
		return identify;
	}

	public void setIdentify(String identify) {
		this.identify = identify;
	}

	public boolean isTrace() {
		return trace;
	}

	public void setTrace(boolean trace) {
		this.trace = trace;
	}

	public ExfTracker getTracker() {
		return tracker;
	}

	public void setTracker(ExfTracker tracker) {
		this.tracker = tracker;
	}
}
