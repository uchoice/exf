package net.uchoice.exf.client.exception;


import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class NestableDelegate implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 7406458819109748290L;

    /**
     * Constructor error message.
     */
    private static final String MUST_BE_THROWABLE =
            "The Nestable implementation passed to the NestableDelegate(Nestable) "
                    + "constructor must extend java.lang.Throwable";
    /**
     * Whether to print the stack trace top-down.
     * This public flag may be set by calling code, typically in initialisation.
     *
     * @since 2.0
     */
    private static final boolean TOPDOWN = true;

    /**
     * Whether to trim the repeated stack trace.
     * This public flag may be set by calling code, typically in initialisation.
     *
     * @since 2.0
     */
    private static final boolean TRIM_STACK_FRAMES = true;

    /**
     * Holds the reference to the exception or error that we're.
     */
    private Throwable nestable = null;


    /**
     * Constructs a new <code>NestableDelegate</code> instance to manage the
     * specified <code>Nestable</code>.
     *
     * @param nestable the Nestable implementation (<i>must</i> extend
     *                 {@link Throwable})
     * @since 2.0
     */
    public NestableDelegate(Nestable nestable) {
        if (nestable instanceof Throwable) {
            this.nestable = (Throwable) nestable;
        } else {
            throw new IllegalArgumentException(MUST_BE_THROWABLE);
        }
    }

    /**
     * Returns the error message of the <code>Throwable</code> in the chain
     * of <code>Throwable</code>s at the specified index, numbererd from 0.
     *
     * @param index the index of the <code>Throwable</code> in the chain of
     *              <code>Throwable</code>s
     * @return the error message, or null if the <code>Throwable</code> at the
     * specified index in the chain does not contain a message
     * @throws IndexOutOfBoundsException if the <code>index</code> argument is
     *                                   negative or not less than the count of <code>Throwable</code>s in the
     *                                   chain
     * @since 2.0
     */
    public String getMessage(int index) {
        Throwable t = this.getThrowable(index);
        if (Nestable.class.isInstance(t)) {
            return ((Nestable) t).getMessage(0);
        } else {
            return t.getMessage();
        }
    }

    /**
     * Returns the full message contained by the <code>Nestable</code>
     * and any nested <code>Throwable</code>s.
     *
     * @param baseMsg the base message to use when creating the full
     *                message. Should be generally be called via
     *                <code>nestableHelper.getMessage( super.getMessage() )</code>,
     *                where <code>super</code> is an instance of {@link
     *                Throwable}.
     * @return The concatenated message for this and all nested
     * <code>Throwable</code>s
     * @since 2.0
     */
    public String getMessage(String baseMsg) {
        Throwable nestedCause = null;
        StringBuffer msg = new StringBuffer();
        if (null != baseMsg) {
            msg.append(baseMsg);
        }

        nestedCause = ExceptionUtils.getCause(this.nestable);
        if (null != nestedCause) {
            String causeMsg = nestedCause.getMessage();
            if (null != causeMsg) {
                if (null != baseMsg) {
                    msg.append(": ");
                }
                msg.append(causeMsg);
            }

        }
        return 0 < msg.length() ? msg.toString() : null;
    }

    /**
     * Returns the error message of this and any nested <code>Throwable</code>s
     * in an array of Strings, one element for each message. Any
     * <code>Throwable</code> not containing a message is represented in the
     * array by a null. This has the effect of cause the length of the returned
     * array to be equal to the result of the {@link #getThrowableCount()}
     * operation.
     *
     * @return the error messages
     * @since 2.0
     */
    public String[] getMessages() {
        Throwable[] throwables = this.getThrowables();
        String[] msgs = new String[throwables.length];
        for (int i = 0; i < throwables.length; i++) {
            msgs[i] = Nestable.class.isInstance(throwables[i]) ?
                    ((Nestable) throwables[i]).getMessage(0) :
                    throwables[i].getMessage();
        }
        return msgs;
    }

    /**
     * Returns the <code>Throwable</code> in the chain of
     * <code>Throwable</code>s at the specified index, numbererd from 0.
     *
     * @param index the index, numbered from 0, of the <code>Throwable</code> in
     *              the chain of <code>Throwable</code>s
     * @return the <code>Throwable</code>
     * @throws IndexOutOfBoundsException if the <code>index</code> argument is
     *                                   negative or not less than the count of <code>Throwable</code>s in the
     *                                   chain
     * @since 2.0
     */
    public Throwable getThrowable(int index) {
        Throwable[] throwables = null;
        if (0 == index) {
            return this.nestable;
        }
        throwables = this.getThrowables();
        return throwables[index];
    }

    /**
     * Returns the number of <code>Throwable</code>s contained in the
     * <code>Nestable</code> contained by this delegate.
     *
     * @return the throwable count
     * @since 2.0
     */
    public int getThrowableCount() {
        return ExceptionUtils.getThrowableCount(this.nestable);
    }

    /**
     * Returns this delegate's <code>Nestable</code> and any nested
     * <code>Throwable</code>s in an array of <code>Throwable</code>s, one
     * element for each <code>Throwable</code>.
     *
     * @return the <code>Throwable</code>s
     * @since 2.0
     */
    public Throwable[] getThrowables() {
        return ExceptionUtils.getThrowables(this.nestable);
    }

    /**
     * Returns the index, numbered from 0, of the first <code>Throwable</code>
     * that matches the specified type in the chain of <code>Throwable</code>s
     * held in this delegate's <code>Nestable</code> with an index greater than
     * or equal to the specified index, or -1 if the type is not found.
     *
     * @param type      <code>Class</code> to be found
     * @param fromIndex the index, numbered from 0, of the starting position in
     *                  the chain to be searched
     * @return index of the first occurrence of the type in the chain, or -1 if
     * the type is not found
     * @throws IndexOutOfBoundsException if the <code>fromIndex</code> argument
     *                                   is negative or not less than the count of <code>Throwable</code>s in the
     *                                   chain
     * @since 2.0
     */
    public int indexOfThrowable(Class<?> type, int fromIndex) {
        Throwable[] throwables = null;
        if (0 > fromIndex) {
            throw new IndexOutOfBoundsException("The start index was out of bounds: " + fromIndex);
        }
        throwables = ExceptionUtils.getThrowables(this.nestable);
        if (fromIndex >= throwables.length) {
            throw new IndexOutOfBoundsException("The start index was out of bounds: "
                    + fromIndex + " >= " + throwables.length);
        }
        for (int i = fromIndex; i < throwables.length; i++) {
            if (throwables[i].getClass().equals(type)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Prints the stack trace of this exception the the standar error
     * stream.
     */
    public void printStackTrace() {
        printStackTrace(System.err);
    }

    /**
     * Prints the stack trace of this exception to the specified
     * stream.
     *
     * @param out <code>PrintStream</code> to use for output.
     * @see #printStackTrace(java.io.PrintWriter)
     */
    public void printStackTrace(PrintStream out) {
        synchronized (out) {
            PrintWriter pw = new PrintWriter(out, false);
            printStackTrace(pw);
            // Flush the PrintWriter before it's GC'ed.
            pw.flush();
        }
    }

    /**
     * Prints the stack trace of this exception to the specified
     * writer. If the Throwable class has a <code>getCause</code>
     * method (i.e. running on jre1.4 or higher), this method just
     * uses Throwable's printStackTrace() method. Otherwise, generates
     * the stack-trace, by taking into account the 'topDown' and
     * 'trimStackFrames' parameters. The topDown and trimStackFrames
     * are set to 'true' by default (produces jre1.4-like stack trace).
     *
     * @param out <code>PrintWriter</code> to use for output.
     */
    public void printStackTrace(PrintWriter out) {
        List<String[]> stacks = null;
        String separatorLine = null;
        Throwable throwable = this.nestable;
        // if running on jre1.4 or higher, use default printStackTrace
        if (ExceptionUtils.isThrowableNested()) {
            if (throwable instanceof Nestable) {
                ((Nestable) throwable).printPartialStackTrace(out);
            } else {
                throwable.printStackTrace(out);
            }
            return;
        }

        // generating the nested stack trace
        stacks = new ArrayList<String[]>();
        while (null != throwable) {
            String[] st = getStackFrames(throwable);
            stacks.add(st);
            throwable = ExceptionUtils.getCause(throwable);
        }

        // If NOT topDown, reverse the stack
        separatorLine = "Caused by: ";
        if (!TOPDOWN) {
            separatorLine = "Rethrown as: ";
            Collections.reverse(stacks);
        }

        // Remove the repeated lines in the stack
        if (TRIM_STACK_FRAMES)
            trimStackFrames(stacks);

        synchronized (out) {
            Iterator<String[]> iterator = stacks.iterator();
            while (iterator.hasNext()) {
                String[] st = iterator.next();
                for (int i = 0, len = st.length; i < len; i++) {
                    out.println(st[i]);
                }
                if (iterator.hasNext()) {
                    out.print(separatorLine);
                }
            }
        }
    }

    /**
     * Captures the stack trace associated with the specified
     * <code>Throwable</code> object, decomposing it into a list of
     * stack frames.
     *
     * @param t The <code>Throwable</code>.
     * @return An array of strings describing each stack frame.
     * @since 2.0
     */
    protected String[] getStackFrames(Throwable t) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw, true);

        // Avoid infinite loop between decompose() and printStackTrace().
        if (t instanceof Nestable) {
            ((Nestable) t).printPartialStackTrace(pw);
        } else {
            t.printStackTrace(pw);
        }
        return ExceptionUtils.getStackFrames(sw.getBuffer().toString());
    }

    /**
     * Trims the stack frames. The first set is left untouched. The rest
     * of the frames are truncated from the bottom by comparing with
     * one just on top.
     *
     * @param stacks The list containing String[] elements
     * @since 2.0
     */
    protected void trimStackFrames(List<String[]> stacks) {
        int trimmed = -1;
        for (int size = stacks.size(), i = size - 1; 0 < i; i--) {
            String[] curr = (String[]) stacks.get(i);
            String[] next = (String[]) stacks.get(i - 1);

            List<String> currList = new ArrayList<String>(Arrays.asList(curr));
            List<String> nextList = new ArrayList<String>(Arrays.asList(next));
            ExceptionUtils.removeCommonFrames(currList, nextList);

            trimmed = curr.length - currList.size();
            if (0 < trimmed) {
                currList.add("\t... " + trimmed + " more");
                stacks.set(
                        i,
                        currList.toArray(new String[currList.size()])
                );
            }
        }
    }
}
