import java.util.Date;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @brief Thread running an instance of a RandomCountGenerator generating random Integer
 */
public class GeneratorThread extends Thread{
    private LinkedBlockingQueue<Map.Entry<Integer, Date>> queue;
    private boolean running= false;
    private int loopNumber= 100;

    /**
     *
     * @param loopNumber Number of Integer to generated
     * @param queue The queue to log the generated numbers with their timestamps
     */
    public GeneratorThread(int loopNumber,LinkedBlockingQueue<Map.Entry<Integer,Date>> queue){
        this.loopNumber=loopNumber;
        this.queue = queue;
    }

    /**
     *
     * @param setRunning
     */
    public void setRunning(boolean setRunning){
        this.running=setRunning;
    }

    @Override
    public void run() {
        super.run();
        RandomCountGenerator rd = new RandomCountGenerator(queue);
        while(loopNumber>0 && running){
            rd.randomInt();
            loopNumber--;
        }
    }
}
