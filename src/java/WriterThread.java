import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @brief Thread polling and logging numbers with timestamps from a shared queue
 */
class WriterThread extends Thread {
    private final FileChannel fileChannel;
    private final FileOutputStream fos;
    private final SimpleDateFormat dateFormat;
    private boolean isRunning = false;
    public static LinkedBlockingQueue<Map.Entry<Integer,Date>> queue =new LinkedBlockingQueue<Map.Entry<Integer, Date>>();
    private Map.Entry<Integer,Date> lastItem = null;
    public static ConcurrentHashMap<Date,Long> datePositionMap = new ConcurrentHashMap<>();

    /**
     *
     * @param outputFile Path to the log file
     * @throws IOException If there is a problem with the manipulation of the file
     */
    public WriterThread(String outputFile) throws IOException{
        dateFormat = new SimpleDateFormat("MM/dd/YYYY  hh:mm:ss SSS");

        fos = new FileOutputStream(outputFile);

        fileChannel = fos.getChannel();
        }

    /**
     *
     * @param setRunning
     */
    public void setRunning(boolean setRunning){
        this.isRunning=setRunning;
    }

    /**
     *
     * @param item Integer with timestamp to write to log
     * @throws IOException If there is a problem during the write to the file
     */
    public void writeLastNumberToDisk(AbstractMap.SimpleEntry<Integer,Date> item) throws IOException {
        if (item != null) {
            fileChannel.position(getPosition(item.getValue()));
            if (lastItem ==null || lastItem.getValue().before(item.getValue()) || lastItem.getValue().equals(item.getValue())) {
                lastItem=item;
                }
            int key = item.getKey();
            String input = toLog(key, item.getValue());
            byte[] inputBytes = input.getBytes();
            ByteBuffer buffer = ByteBuffer.wrap(inputBytes);
            fileChannel.write(buffer);
            datePositionMap.put(item.getValue(),fileChannel.position());
            if(!lastItem.getValue().equals(item.getValue())) {
                fileChannel.position(getPosition(lastItem.getValue()));
            }

            }
        }

    /**
     *
     * @param value the timestamp of the generated int
     * @return the position in the file for this date
     */
    private long getPosition(Date value) {
        if(lastItem==null)
            return 0;
        if(value.after(lastItem.getValue())){
            return datePositionMap.get(lastItem.getValue());
        }

        if(datePositionMap.keySet().contains(value)) {
            return datePositionMap.get(value);
        }

        if(value.before(Collections.min(Collections.list(datePositionMap.keys())))){
            return 0;
        }
        value.setTime(value.getTime() - 1);
        return getPosition(value);

}

    /**
     *
     * @param key the generated integer
     * @param value the timestamp associated
     * @return The Human readable log of the Integer with its timestamp
     */
    private String toLog(int key, Date value){
        String date = dateFormat.format(value);
        return "Last Number Generated " + key + " and writed at " + date + "\n";
    }
    @Override
    public void run() {
        super.run();
        while(isRunning){
            try {
                writeLastNumberToDisk((AbstractMap.SimpleEntry<Integer, Date>) queue.poll());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            fileChannel.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
