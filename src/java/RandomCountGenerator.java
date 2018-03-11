import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;


public class RandomCountGenerator {

    private Random r = new Random();
    public LinkedBlockingQueue<Map.Entry<Integer,Date>> queue;
    private LinkedList<Integer> hist = new LinkedList<Integer>();
    private final static int HISTORY_LENGTH=100;

    public RandomCountGenerator(LinkedBlockingQueue<Map.Entry<Integer,Date>> queue){
        this.queue = queue;
    }
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
        String outputFile = "LastNumberGenerated.txt";
        if(args!=null && args.length>1 && args[1]!=""){
            outputFile = args[1];
        }
        int loopNumber = 100;
        if(args.length > 2 && args[2]!="" && isInteger(args[2])){
            loopNumber = Integer.parseInt(args[2]);
        }
        WriterThread writerThread = null;
        try {
            writerThread = new WriterThread(outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        RandomCountGenerator rd = new RandomCountGenerator(WriterThread.queue);

        writerThread.setRunning(true);
        writerThread.start();
        while(loopNumber>0){
            rd.randomInt();
            loopNumber--;
        }
        writerThread.setRunning(false);
    }
}
