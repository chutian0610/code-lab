package info.victorchu.jdk.lab.usage.socket.nio;

import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public class Dispatcher {

    private Map<Integer, Supplier<EventHandler>> registeredHandlers =
            new ConcurrentHashMap<Integer, Supplier<EventHandler>>();
    private Selector demultiplexer;

    public Dispatcher() throws Exception {
        demultiplexer = Selector.open();
    }

    public Selector getDemultiplexer() {
        return demultiplexer;
    }

    public void registerEventHandler(
            int eventType, Supplier<EventHandler> eventHandler) {
        registeredHandlers.put(eventType, eventHandler);
    }

    // Used to register ServerSocketChannel with the
    // selector to accept incoming client connections
    public void registerChannel(
            int eventType, SelectableChannel channel) throws Exception {
        channel.register(demultiplexer, eventType);
    }

    public void run() {
        try {
            while (true) {
                demultiplexer.select();

                Set<SelectionKey> readyHandles =
                        demultiplexer.selectedKeys();
                Iterator<SelectionKey> handleIterator =
                        readyHandles.iterator();

                while (handleIterator.hasNext()) {
                    SelectionKey handle = handleIterator.next();
                    handleIterator.remove();

                    if (handle.isAcceptable()) {
                        EventHandler handler =
                                registeredHandlers.get(SelectionKey.OP_ACCEPT).get();
                        handler.handleEvent(handle);
                        continue;
                    }

                    if (handle.isReadable()) {
                        EventHandler handler =
                                registeredHandlers.get(SelectionKey.OP_READ).get();
                        handler.handleEvent(handle);
                        continue;
                    }
                    if (handle.isWritable()) {
                        EventHandler handler =
                                registeredHandlers.get(SelectionKey.OP_WRITE).get();
                        handler.handleEvent(handle);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}