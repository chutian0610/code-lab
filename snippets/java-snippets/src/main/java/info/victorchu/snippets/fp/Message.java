package info.victorchu.snippets.fp;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * Message during Transform
 */
public interface Message {

    static Message of(String msg) {
        if (null == msg) {
            return EmptyMessage.instance();
        }
        return new MessageImpl(Lists.singleton(msg));
    }

    static Message of(String... msgs) {
        return new MessageImpl(Lists.arrayOf(msgs));
    }

    static Message of() {
        return EmptyMessage.instance();
    }

    List<String> msg();

    default Message merge(Message rhs) {
        return new MessageImpl(
                Lists.union(this.msg(), (rhs.msg()))
        );
    }

    final class EmptyMessage implements Message {

        private static final EmptyMessage instance = new EmptyMessage();

        private EmptyMessage() {
        }

        static EmptyMessage instance() {
            return instance;
        }

        @Override
        public List<String> msg() {
            return Lists.empty();
        }

        @Override
        public String toString() {
            return "";
        }
    }

    final class MessageImpl implements Message {

        public final List<String> msg;

        public MessageImpl(List<String> msg) {
            this.msg = Objects.requireNonNull(msg);
        }

        @Override
        public String toString() {
            switch (msg.size()) {
                case 0:
                    return
                            "";
                default:
                    final String expectedStr = String.join(",", msg);
                    return expectedStr;
            }
        }

        @Override
        public List<String> msg() {
            return msg;
        }
    }

    class Lists {
        public static <T> List<T> empty() {
            return Collections.emptyList();
        }

        public static <T> List<T> singleton(T value) {
            LinkedList<T> list = new LinkedList<T>();
            list.add(value);
            return list;
        }

        public static <T> List<T> arrayOf(T[] values) {
            LinkedList<T> list = new LinkedList<T>();
            Collections.addAll(list, values);
            return list;
        }

        public static <T> List<T> union(List<? extends T> lhs, List<? extends T> rhs, List<T> out) {
            out.addAll(lhs);
            out.addAll(rhs);
            return out;
        }

        public static <T> List<T> union(List<T> lhs, List<T> rhs) {
            LinkedList<T> ts = new LinkedList<T>();
            return union(lhs, rhs, ts);
        }
    }
}
