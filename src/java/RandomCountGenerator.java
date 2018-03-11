
import java.io.IOException;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;


public class RandomCountGenerator {

    private Random r = new Random();
    public LinkedBlockingQueue<Map.Entry<Integer,Date>> queue;
    private LinkedList<Integer> hist = new LinkedList<Integer>();
    private final static int HISTORY_LENGTH=100;

    /**
     *
     * @param queue The queue where to put the generated integer with their timestamps
     */
    public RandomCountGenerator(LinkedBlockingQueue<Map.Entry<Integer,Date>> queue){
        this.queue = queue;
    }

    /**
     *
     * @param s
     * @return is an integer
     */
    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch(NumberFormatException e) {
            return false;
        } catch(NullPointerException e) {
            return false;
        }
        // only got here if we didn't return false
        return true;
    }

    /**
     * Compute the frequency over the last 100 numbers generated
     * @return The different frequency of each generated numbers
     */
    public HashMap frequencyMap(){
        HashMap result = new HashMap<Integer,Float>();
        float one = 0;
        float two = 0;
        float three = 0;
        float four = 0;
        float five = 0;
        for( Integer i : hist){
            switch(i){
                case 1:
                    one++;
                    break;
                case 2:
                    two++;
                    break;
                case 3:
                    three++;
                    break;
                case 4:
                    four ++;
                    break;
                case 5:
                    five++;
                    break;
                default:
                    break;
            }
        }
        float sizeList = (float) hist.size();
        result.put(1,one/sizeList);
        result.put(2,two/sizeList);
        result.put(3,three/sizeList);
        result.put(4,four/sizeList);
        result.put(5,five/sizeList);
        return result;
    }

    /**
     * Generate an integer between one and five with a specific distribution probability
     * @return the generated integer
     */
    public int randomInt(){
        Calendar calendar = Calendar.getInstance();
        if(hist.size()==HISTORY_LENGTH)
            hist.remove();
        int rand = r.nextInt(100);
        if(rand<50){
            queue.add(new AbstractMap.SimpleEntry<>(1,calendar.getTime()));
            hist.add(1);
            System.out.println("1");
            return 1;
        }else if(rand<75){
            queue.add(new AbstractMap.SimpleEntry<>(2,calendar.getTime()));
            System.out.println("2");
            hist.add(2);
            return 2;
        }else if (rand<90){
            queue.add(new AbstractMap.SimpleEntry<>(3,calendar.getTime()));
            System.out.println("3");
            hist.add(3);
            return 3;
        }else if(rand<95){
            queue.add(new AbstractMap.SimpleEntry<>(4,calendar.getTime()));
            System.out.println("4");
            hist.add(4);
            return 4;
        }else{
            queue.add(new AbstractMap.SimpleEntry<>(5,calendar.getTime()));
            System.out.println("5");
            hist.add(5);
            return 5;
        }

    }


    public static void main(String[] args){

        // Argument parsing
        String outputFile = "LastNumberGenerated.txt";
        if(args!=null && args.length>1 && args[1]!=""){
            outputFile = args[1];
        }
        int loopNumber = 100000;
        if(args.length > 2 && args[2]!="" && isInteger(args[2])){
            loopNumber = Integer.parseInt(args[2]);
        }
        // Threads initialisation

        WriterThread writerThread = null;
        try {
            writerThread = new WriterThread(outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<Thread> pool = new ArrayList<Thread>();

        // Start of generator threads
        for(int i=0;i<5;i++){
            GeneratorThread t = new GeneratorThread(loopNumber,WriterThread.queue);
            t.start();
            t.setRunning(true);
            pool.add(t);
        }

        // Start the writer thread
        writerThread.setRunning(true);
        writerThread.start();

        // Join the main thread to generator threads to close the log file correctly
        pool.forEach((Thread x)-> {
            try {
                x.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        writerThread.setRunning(false);
    }
}
