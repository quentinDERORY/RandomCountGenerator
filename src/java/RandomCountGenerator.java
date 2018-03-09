import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;


public class RandomCountGenerator {
    private Random r = new Random();
    private LinkedList<Integer> hist = new LinkedList<Integer>();
    private final static int HISTORY_LENGTH=100;


    public HashMap frequencyMap(){
        HashMap result = new HashMap<Integer,Float>();
        float one = 0;
        float two = 0;
        float three = 0;
        float four = 0;
        float five = 0;
        for(Integer i : hist){
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
        if(hist.size()==HISTORY_LENGTH)
            hist.remove();
        int rand = r.nextInt(100);
        if(rand<50){
            hist.add(1);
            System.out.println("1");
            return 1;
        }else if(rand<75){
            hist.add(2);
            System.out.println("2");
            return 2;
        }else if (rand<90){
            hist.add(3);
            System.out.println("3");
            return 3;
        }else if(rand<95){
            hist.add(4);
            System.out.println("4");
            return 4;
        }else{
            hist.add(5);
            System.out.println("5");
            return 5;
        }

    }
    public void writeLastNumberToDisk() throws IOException {
        int last = hist.getLast();
        BufferedWriter fw = new BufferedWriter(new FileWriter("LastNumberGenerated",true));
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/YYYY  hh:mm:ss");
        Calendar calendar = Calendar.getInstance();
        fw.write("Last Number Generated " + last + " and writed at " + dateFormat.format(calendar.getTime()) + "\n" );
        fw.close();
    }

    public static void main(String[] args){
        RandomCountGenerator rd = new RandomCountGenerator();
        for(int i=0;i<100;i++)
            rd.randomInt();

        System.out.println("Frequency:");
        HashMap<Integer,Float> map = rd.frequencyMap();
        for(Integer key : map.keySet()){
            System.out.println("key:" + key.toString() + " frequency:" + map.get(key).toString());
        }

        try {
            rd.writeLastNumberToDisk();
        }catch(IOException e){
            System.out.println(e);
        }
    }
}
