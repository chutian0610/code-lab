package info.victorchu.commontool.utils;

import com.sun.tools.javac.util.ArrayUtils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.StringTokenizer;
import java.util.stream.Stream;

/**
 * <p>
 * see org.apache.commons.lang3.exception.ExceptionUtils
 * </p>
 *
 * @Copyright:www.xiaojukeji.com Inc. All rights reserved.
 * @Description:
 * @Date:2022/12/1 18:22
 * @Author:victorchutian
 */
public class ExceptionUtils {

    private static final int NOT_FOUND = -1;

    public static final Throwable[] EMPTY_THROWABLE_ARRAY = {};
    public static final String[] EMPTY_STRING_ARRAY = {};
    public static final String EMPTY = "";

    static final String WRAPPED_MARKER = " [wrapped] ";


    public static RuntimeException sneakyThrow(Throwable t) {
        if (t == null) {
            throw new NullPointerException("t");
        }
        return ExceptionUtils.eraseType(t);
    }

    @SuppressWarnings("unchecked")
    private static <R, T extends Throwable> R eraseType(final Throwable throwable) throws T {
        throw (T) throwable;
    }

    /**
     * Gets a short message summarizing the exception.
     * @param th
     * @return
     */
    public static String getMessage(final Throwable th) {
        if (th == null) {
            return EMPTY;
        }
        final String clsName = th.getClass().getSimpleName();
        return clsName + ": " + th.getMessage();
    }

    /**
     * Gets a short message summarizing the root cause exception.
     * @param throwable
     * @return
     */
    public static String getRootCauseMessage(final Throwable throwable) {
        final Throwable root = getRootCause(throwable);
        return getMessage(root == null ? throwable : root);
    }

    /**
     * Introspects the {@link Throwable} to obtain the root cause.
     *
     * <p>This method walks through the exception chain to the last element,
     * "root" of the tree, using {@link Throwable#getCause()}, and
     * returns that exception.</p>
     * @param throwable
     * @return
     */
    public static Throwable getRootCause(final Throwable throwable) {
        final List<Throwable> list = getThrowableList(throwable);
        return list.isEmpty() ? null : list.get(list.size() - 1);
    }

    /**
     * Gets a compact stack trace for the root cause of the supplied
     * {@link Throwable}.
     * <p>The output of this method is consistent across JDK versions.
     * It consists of the root exception followed by each of its wrapping
     * exceptions separated by '[wrapped]'. Note that this is the opposite
     * order to the JDK1.4 display.</p>
     * @param throwable
     * @return
     */
    public static String[] getRootCauseStackTrace(final Throwable throwable) {
        return getRootCauseStackTraceList(throwable).toArray(EMPTY_STRING_ARRAY);
    }


    /**
     * Gets a compact stack trace for the root cause of the supplied {@link Throwable}.
     * <p>
     * The output of this method is consistent across JDK versions. It consists of the root exception followed by each of
     * its wrapping exceptions separated by '[wrapped]'. Note that this is the opposite order to the JDK1.4 display.
     * </p>
     * @param throwable
     * @return
     */
    public static List<String> getRootCauseStackTraceList(final Throwable throwable) {
        if (throwable == null) {
            return Collections.emptyList();
        }
        final Throwable[] throwables = getThrowables(throwable);
        final int count = throwables.length;
        final List<String> frames = new ArrayList<>();
        List<String> nextTrace = getStackFrameList(throwables[count - 1]);
        for (int i = count; --i >= 0;) {
            final List<String> trace = nextTrace;
            if (i != 0) {
                nextTrace = getStackFrameList(throwables[i - 1]);
                removeCommonFrames(trace, nextTrace);
            }
            if (i == count - 1) {
                frames.add(throwables[i].toString());
            } else {
                frames.add(WRAPPED_MARKER + throwables[i].toString());
            }
            frames.addAll(trace);
        }
        return frames;
    }

    /**
     * Removes common frames from the cause trace given the two stack traces.
     *
     * @param causeFrames  stack trace of a cause throwable
     * @param wrapperFrames  stack trace of a wrapper throwable
     * @throws NullPointerException if either argument is null
     * @since 2.0
     */
    public static void removeCommonFrames(final List<String> causeFrames, final List<String> wrapperFrames) {
        Objects.requireNonNull(causeFrames, "causeFrames");
        Objects.requireNonNull(wrapperFrames, "wrapperFrames");
        int causeFrameIndex = causeFrames.size() - 1;
        int wrapperFrameIndex = wrapperFrames.size() - 1;
        while (causeFrameIndex >= 0 && wrapperFrameIndex >= 0) {
            // Remove the frame from the cause trace if it is the same
            // as in the wrapper trace
            final String causeFrame = causeFrames.get(causeFrameIndex);
            final String wrapperFrame = wrapperFrames.get(wrapperFrameIndex);
            if (causeFrame.equals(wrapperFrame)) {
                causeFrames.remove(causeFrameIndex);
            }
            causeFrameIndex--;
            wrapperFrameIndex--;
        }
    }

    /**
     * Gets a {@link List} of stack frames - the message
     * is not included. Only the trace of the specified exception is
     * returned, any caused by trace is stripped.
     *
     * <p>This works in most cases - it will only fail if the exception
     * message contains a line that starts with:
     * {@code &quot;&nbsp;&nbsp;&nbsp;at&quot;.}</p>
     *
     * @param throwable is any throwable
     * @return List of stack frames
     */
    static List<String> getStackFrameList(final Throwable throwable) {
        final String stackTrace = getStackTrace(throwable);
        final String linebreak = System.lineSeparator();
        final StringTokenizer frames = new StringTokenizer(stackTrace, linebreak);
        final List<String> list = new ArrayList<>();
        boolean traceStarted = false;
        while (frames.hasMoreTokens()) {
            final String token = frames.nextToken();
            // Determine if the line starts with <whitespace>at
            final int at = token.indexOf("at");
            if (at != NOT_FOUND && token.substring(0, at).trim().isEmpty()) {
                traceStarted = true;
                list.add(token);
            } else if (traceStarted) {
                break;
            }
        }
        return list;
    }

    /**
     * Gets the stack trace from a Throwable as a String.
     *
     * <p>The result of this method vary by JDK version as this method
     * uses {@link Throwable#printStackTrace(java.io.PrintWriter)}.
     * On JDK1.3 and earlier, the cause exception will not be shown
     * unless the specified throwable alters printStackTrace.</p>
     *
     * @param throwable  the {@link Throwable} to be examined, may be null
     * @return the stack trace as generated by the exception's
     * {@code printStackTrace(PrintWriter)} method, or an empty String if {@code null} input
     */
    public static String getStackTrace(final Throwable throwable) {
        if (throwable == null) {
            return EMPTY;
        }
        final StringWriter sw = new StringWriter();
        throwable.printStackTrace(new PrintWriter(sw, true));
        return sw.toString();
    }

    /**
     * Gets an array where each element is a line from the argument.
     *
     * <p>The end of line is determined by the value of {@link System#lineSeparator()}.</p>
     *
     * @param stackTrace  a stack trace String
     * @return an array where each element is a line from the argument
     */
    static String[] getStackFrames(final String stackTrace) {
        final String linebreak = System.lineSeparator();
        final StringTokenizer frames = new StringTokenizer(stackTrace, linebreak);
        final List<String> list = new ArrayList<>();
        while (frames.hasMoreTokens()) {
            list.add(frames.nextToken());
        }
        return list.toArray(EMPTY_STRING_ARRAY);
    }

    /**
     * Gets the stack trace associated with the specified
     * {@link Throwable} object, decomposing it into a list of
     * stack frames.
     *
     * <p>The result of this method vary by JDK version as this method
     * uses {@link Throwable#printStackTrace(java.io.PrintWriter)}.
     * On JDK1.3 and earlier, the cause exception will not be shown
     * unless the specified throwable alters printStackTrace.</p>
     *
     * @param throwable  the {@link Throwable} to examine, may be null
     * @return an array of strings describing each stack frame, never null
     */
    public static String[] getStackFrames(final Throwable throwable) {
        if (throwable == null) {
            return EMPTY_STRING_ARRAY;
        }
        return getStackFrames(getStackTrace(throwable));
    }

    /**
     * Gets the list of {@link Throwable} objects in the exception chain.
     * <p>A throwable without cause will return an array containing
     * one element - the input throwable.
     * A throwable with one cause will return an array containing
     * two elements. - the input throwable and the cause throwable.
     * A {@code null} throwable will return an array of size zero.</p>
     *
     * @param throwable
     * @return
     */
    public static Throwable[] getThrowables(final Throwable throwable) {
        return getThrowableList(throwable).toArray(EMPTY_THROWABLE_ARRAY);
    }

    /**
     * Gets the list of {@link Throwable} objects in the exception chain.
     * <p>A throwable without cause will return a list containing
     * one element - the input throwable.
     * A throwable with one cause will return a list containing
     * two elements. - the input throwable and the cause throwable.
     * A {@code null} throwable will return a list of size zero.</p>
     * @param throwable
     * @return
     */
    public static List<Throwable> getThrowableList(Throwable throwable) {
        final List<Throwable> list = new ArrayList<>();
        while (throwable != null && !list.contains(throwable)) {
            list.add(throwable);
            throwable = throwable.getCause();
        }
        return list;
    }
    /**
     * Tests if the throwable's causal chain have an immediate or wrapped exception
     * of the given type?
     *
     * @param chain
     *            The root of a Throwable causal chain.
     * @param type
     *            The exception type to test.
     * @return true, if chain is an instance of type or is an
     *         UndeclaredThrowableException wrapping a cause.
     * @since 3.5
     * @see #wrapAndThrow(Throwable)
     */
    public static boolean hasCause(Throwable chain,
                                   final Class<? extends Throwable> type) {
        if (chain instanceof UndeclaredThrowableException) {
            chain = chain.getCause();
        }
        return type.isInstance(chain);
    }
    /**
     * Throws a checked exception without adding the exception to the throws
     * clause of the calling method. For checked exceptions, this method throws
     * an UndeclaredThrowableException wrapping the checked exception. For
     * Errors and RuntimeExceptions, the original exception is rethrown.
     * <p>
     * The downside to using this approach is that invoking code which needs to
     * handle specific checked exceptions must sniff up the exception chain to
     * determine if the caught exception was caused by the checked exception.
     * </p>
     *
     * @param throwable
     *            The throwable to rethrow.
     * @param <R> The type of the returned value.
     * @return Never actually returned, this generic type matches any type
     *         which the calling site requires. "Returning" the results of this
     *         method will satisfy the java compiler requirement that all code
     *         paths return a value.
     * @since 3.5
     * @see #hasCause(Throwable, Class)
     */
    public static <R> R wrapAndThrow(final Throwable throwable) {
        if (throwable instanceof RuntimeException) {
            throw (RuntimeException) throwable;
        }
        if (throwable instanceof Error) {
            throw (Error) throwable;
        }
        throw new UndeclaredThrowableException(throwable);
    }

    /**
     * Returns the first {@link Throwable}
     * that matches the specified type in the exception chain from
     * a specified index.
     * if subclass is true ,Subclasses of the specified class match, otherwise do not match
     *
     * @param <T> the type of Throwable you are searching.
     * @param throwable  the throwable to inspect, may be null
     * @param type  the type to search, subclasses match, null returns null
     * @param fromIndex  the (zero-based) index of the starting position,
     *  negative treated as zero, larger than chain size returns null
     * @param subclass if {@code true}, compares with {@link Class#isAssignableFrom(Class)}, otherwise compares
     * using references
     * @return throwable of the {@code type} within throwables nested within the specified {@code throwable}
     */
    private static <T extends Throwable> T firstThrowableOf(final Throwable throwable, final Class<T> type, int fromIndex, final boolean subclass) {
        if (throwable == null || type == null) {
            return null;
        }
        if (fromIndex < 0) {
            fromIndex = 0;
        }
        final Throwable[] throwables = getThrowables(throwable);
        if (fromIndex >= throwables.length) {
            return null;
        }
        if (subclass) {
            for (int i = fromIndex; i < throwables.length; i++) {
                if (type.isAssignableFrom(throwables[i].getClass())) {
                    return type.cast(throwables[i]);
                }
            }
        } else {
            for (int i = fromIndex; i < throwables.length; i++) {
                if (type.equals(throwables[i].getClass())) {
                    return type.cast(throwables[i]);
                }
            }
        }
        return null;
    }
}

