package net.uchoice.exf.core.trace;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import net.uchoice.exf.core.runtime.Session;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExfTracker {

	/**
	 * LOGGER.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(ExfTracker.class);

	/**
	 * The records in time line ordered.
	 */
	@Getter
	private List<Record> records = new ArrayList<>();
	private Stack<Record> recordStack = new Stack<>();
	@Getter
	private Record current;

	/**
	 * start a new record. if last record not closed, then push to the stack.
	 */
	public static void start(Record newRecord) {
		if (null != Session.get() && !Session.get().isTrace()) {
			return;
		}
		ExfTracker tracker = currentTracker();
		if (null == tracker)
			return;
		if (null == newRecord) {
			newRecord = Record.create(RecordType.OTHER, "EMPTY");
		}

		tracker.records.add(newRecord);
		tracker.recordStack.push(newRecord);
		if (null == tracker.current) {
			tracker.current = newRecord;
		} else {
			if (!tracker.current.isClosed()) {
				tracker.current.getChildren().add(newRecord);
			}
			tracker.current = newRecord;
		}
	}

	public static void success(boolean success, String errorMsg) {
		ExfTracker tracker = currentTracker();
		if (null == tracker || null == tracker.current) {
			return;
		}
		tracker.current.setSuccess(success);
		tracker.current.setErrorMsg(errorMsg);
	}

	public static void success(boolean success) {
		success(success, null);
	}
	
	public static void addInput(Object input) {
		ExfTracker tracker = currentTracker();
		if (null == tracker || null == tracker.current) {
			return;
		}
		tracker.current.addInput(input);
	}

	public static void stop() {
		stop(null);
	}

	/**
	 * stop the record.
	 */
	public static void stop(Object output) {
		if (null != Session.get() && !Session.get().isTrace()) {
			return;
		}
		ExfTracker tracker = currentTracker();
		if (null == tracker || null == tracker.current) {
			return;
		}
		tracker.current.setEndTime(System.currentTimeMillis());
		tracker.current.setOutput(output);
		tracker.current.setClosed(true);
		if (!tracker.recordStack.isEmpty())
			tracker.recordStack.pop();
		if (!tracker.recordStack.isEmpty()) {
			tracker.current = tracker.recordStack.lastElement();
		}
	}

	public static void log() {
		log(null);
	}

	public static void log(Object output) {
		stop(output);
		ExfTracker tracker = currentTracker();
		if (null == tracker || null == tracker.current) {
			return;
		}
		log(tracker.current, "1");
	}

	private static void log(Record record, String level) {
		if (null == record) {
			return;
		}
		LOGGER.info(String.format("[%s] %s", level, record.toString()));
		if (null != record.getChildren() && !record.getChildren().isEmpty()) {
			for (int i = 0; i < record.getChildren().size(); i++) {
				log(record.getChildren().get(i), level + "." + (i + 1));
			}
		}
	}

	private static ExfTracker currentTracker() {
		Session bizSession = Session.get();
		if (null == bizSession)
			return null;
		return bizSession.getTracker();
	}
}
