package net.uchoice.exf.core.trace;


import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;

import net.uchoice.exf.core.runtime.Session;

/**
 * 跟踪带记录
 *
 */
public class Record {

    private RecordType recordType;

    private boolean closed;

    private String target;

    private List<Object> inputs = new ArrayList<>();

    private Object output;

    private long startTime = System.currentTimeMillis();

    private long endTime;

    private List<Record> children = new ArrayList<>();

    private boolean success = true;

    private String errorMsg;

    private Record() {
    }

    public static Record create(RecordType recordType, String target) {
        Record record = new Record();
        record.recordType = recordType;
        record.target = target;
        return record;
    }

    public Record addInput(Object input) {
        if (null != input)
            inputs.add(input);
        return this;
    }

    public Record addInputs(List<Object> inputs) {
        if (null != inputs && !inputs.isEmpty()) {
            this.inputs.addAll(inputs);
        }
        return this;
    }

    public long getDuration() {
        if (!closed || endTime == 0L)
            return System.currentTimeMillis() - startTime;
        return endTime - startTime;
    }

    public RecordType getRecordType() {
		return recordType;
	}

	public void setRecordType(RecordType recordType) {
		this.recordType = recordType;
	}

	public boolean isClosed() {
		return closed;
	}

	public void setClosed(boolean closed) {
		this.closed = closed;
	}

	public List<Object> getInputs() {
		return inputs;
	}

	public void setInputs(List<Object> inputs) {
		this.inputs = inputs;
	}

	public Object getOutput() {
		return output;
	}

	public void setOutput(Object output) {
		this.output = output;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public List<Record> getChildren() {
		return children;
	}

	public void setChildren(List<Record> children) {
		this.children = children;
	}

	public boolean isSuccess() {
		return success;
	}

	public Record setSuccess(boolean success) {
		this.success = success;
		return this;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public Record setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
		return this;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String toString() {
		StringBuilder buffer = new StringBuilder();
		buffer.append(String.format("[%s] ", recordType.name()))
		        .append(String.format("[%s] ", success))
		        .append(String.format("[%s] ", String.valueOf(getDuration())))
				.append(String.format("[%s] ", target))
				.append(String.format("[%s] ", Session.get().getRequest().getSource()))
				.append(String.format("[%s] ", Session.get().getIdentify()));
		if (Session.get().isDebug()) {
			buffer.append(String.format("[%s] ", JSON.toJSONString(inputs)));
			if (success) {
				buffer.append(String.format("[%s]", JSON.toJSONString(output)));
			} else {
				buffer.append(String.format("[%s]", errorMsg));
			}
		}
		return buffer.toString();
	}
}
