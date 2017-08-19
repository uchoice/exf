package net.uchoice.exf.client.exception;

import java.io.PrintStream;
import java.io.PrintWriter;

public class NestableRuntimeException extends RuntimeException implements Nestable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -9180025274569698354L;

    /**
     * The helper instance which contains much of the code which we delegate to.
     */
    protected NestableDelegate delegate = new NestableDelegate(this);

    /**
     * Constructs a new <code>NestableException</code> without specified
     * detail message.
     */
    public NestableRuntimeException() {
        super();
    }

    /**
     * Constructs a new <code>NestableException</code> with specified detail
     * message.
     *
     * @param msg The error message.
     */
    public NestableRuntimeException(String msg) {
        super(msg);
    }

    /**
     * Constructs a new <code>NestableException</code> with specified nested
     * <code>Throwable</code>.
     *
     * @param cause the exception or error that caused this exception to be thrown
     */
    public NestableRuntimeException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a new <code>NestableException</code> with specified detail
     * message and nested <code>Throwable</code>.
     *
     * @param msg   the error message
     * @param cause the exception or error that caused this exception to be thrown
     */
    public NestableRuntimeException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public final void printPartialStackTrace(PrintWriter out) {
        super.printStackTrace(out);
    }

    /**
     * Returns the detail message string of this throwable. If it was created
     * with a null message, returns the following: ( cause==null ? null :
     * cause.toString() ).
     *
     * @return String
     */
    public String getMessage() {
        if (null != super.getMessage()) {
            return super.getMessage();
        } else if (null != getCause()) {
            return getCause().toString();
        } else {
            return null;
        }
    }

    public String getMessage(int index) {
        if (0 == index) {
            return super.getMessage();
        } else {
            return delegate.getMessage(index);
        }
    }

    public String[] getMessages() {
        return delegate.getMessages();
    }

    public Throwable getThrowable(int index) {
        return delegate.getThrowable(index);
    }

    public int getThrowableCount() {
        return delegate.getThrowableCount();
    }

    public Throwable[] getThrowables() {
        return delegate.getThrowables();
    }

    public int indexOfThrowable(Class<?> type) {
        return delegate.indexOfThrowable(type, 0);
    }

    public int indexOfThrowable(Class<?> type, int fromIndex) {
        return delegate.indexOfThrowable(type, fromIndex);
    }

    public void printStackTrace() {
        delegate.printStackTrace();
    }

    public void printStackTrace(PrintStream out) {
        delegate.printStackTrace(out);
    }

    public void printStackTrace(PrintWriter out) {
        delegate.printStackTrace(out);
    }

}
