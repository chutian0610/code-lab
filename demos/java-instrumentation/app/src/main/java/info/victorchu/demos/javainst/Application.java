package info.victorchu.demos.javainst;

import com.sun.tools.attach.AgentInitializationException;
import com.sun.tools.attach.AgentLoadException;
import com.sun.tools.attach.AttachNotSupportedException;
import com.sun.tools.attach.VirtualMachine;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * @author victorchu
 */
public class Application
{

    public static Integer count = 0;

    public static boolean count()
    {
        if (count == 9) {
            count = 0;
            return true;
        }
        count++;
        return false;
    }

    public static void main(String[] args)
            throws InterruptedException, IOException, AttachNotSupportedException, AgentLoadException, AgentInitializationException
    {
        if (args.length > 0) {
            VirtualMachine vm = VirtualMachine.attach(args[0]);
            vm.loadAgent(args[1]); // agent.jar path
        }
        else {
            // run app
            while (true) {
                if (count()) {
                    System.out.println("count 10 times");
                    String runtimeName = ManagementFactory.getRuntimeMXBean().getName();
                    System.out.println("pid=" + runtimeName.substring(0, runtimeName.indexOf('@')));
                }
                Thread.sleep(1000);
            }
        }
    }
}
