package info.victorchu.jdk.lab.jfr;

import jdk.jfr.Configuration;
import jdk.jfr.consumer.RecordingStream;

import java.io.IOException;
import java.text.ParseException;
import java.time.Duration;
import java.util.Map;

public class JfrStream
{
    public static void main(String[] args)
            throws IOException, ParseException
    {
        Configuration config = Configuration.getConfiguration("default");
        try (var es = new RecordingStream(config)) {
            //启用 TLAB 事件监控
            es.enable("jdk.ObjectAllocationOutsideTLAB");
            es.enable("jdk.ObjectAllocationInNewTLAB");

            es.onEvent("jdk.ObjectAllocationOutsideTLAB", System.out::println);
            es.onEvent("jdk.ObjectAllocationInNewTLAB", System.out::println);
            es.onEvent("jdk.GarbageCollection", System.out::println);
            es.setMaxAge(Duration.ofSeconds(10));
            es.start();
        }
    }
}
