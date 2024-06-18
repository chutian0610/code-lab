package info.victorchu.jdk.lab.time;

public class JavaTime
{
    public static void main(String[] args)
    {
        long lastTime=0L;
        while (true){
            long currentTime = System.currentTimeMillis();
            if(currentTime<lastTime){
                break;
            }
        }
    }
}
