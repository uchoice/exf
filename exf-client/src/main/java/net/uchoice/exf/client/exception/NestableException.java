package net.uchoice.exf.client.exception;

import java.io.PrintStream;
import java.io.PrintWriter;

public class NestableException extends Exception implements Nestable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -9180025274569698354L;

    /**
     * The helper instance which contains much of the code which we delegate to.
     */
    protected NestableDelegate delegate = new NestableDelegate(this);

    /**
     * Holds the reference to the exception or error that caused this exception
     * to be thrown.
     */
    private Throwable cause = null;

    /**
     * Constructs a new <code>NestableException</code> without specified
     * detail message.
     */
    public NestableException() {
        super();
    }

    /**
     * Constructs a new <code>NestableException</code> with specified detail
     * message.
     *
     * @param msg The error message.
     */
    public NestableException(String msg) {
        super(msg);
    }

    /**
     * Constructs a new <code>NestableException</code> with specified nested
     * <code>Throwable</code>.
     *
     * @param cause the exception or error that caused this exception to be thrown
     */
    public NestableException(Throwable cause) {
        super();
        this.cause = cause;
    }

    /**
     * Constructs a new <code>NestableException</code> with specified detail
     * message and nested <code>Throwable</code>.
     *
     * @param msg   the error message
     * @param cause the exception or error that caused this exception to be thrown
     */
    public NestableException(String msg, Throwable cause) {
        super(msg);
        this.cause = cause;
    }

    public final void printPartialStackTrace(PrintWriter out) {
        super.printStackTrace(out);
    }

    public Throwable getCause() {
        return cause;
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
        } else if (null != cause) {
            return cause.toString();
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
