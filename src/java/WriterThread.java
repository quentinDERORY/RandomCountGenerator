import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.AbstractMap;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

class WriterThread extends Thread {
    private final FileChannel fileChannel;
    private final FileOutputStream fos;
    private final SimpleDateFormat dateFormat;
    private boolean isRunning = false;
    public static LinkedBlockingQueue<Map.Entry<Integer,Date>> queue =new LinkedBlockingQueue<Map.Entry<Integer, Date>>();;


    public WriterThread(String outputFile) throws IOException{
        dateFormat = new SimpleDateFormat("MM/dd/YYYY  hh:mm:ss");

        fos = new FileOutputStream(outputFile);

        fileChannel = fos.getChannel();
        }
    public void setRunning(boolean setRunning){
        this.isRunning=setRunning;
    }
    public void writeLastNumberToDisk(AbstractMap.SimpleEntry<Integer,Date> last) throws IOException {
        if (last != null) {
            int key = last.getKey();
            String value = dateFormat.format(last.getValue());
            String input = "Last Number Generated " + key + " and writed at " + value + "\n";
            byte[] inputBytes = input.getBytes();
            ByteBuffer buffer = ByteBuffer.wrap(inputBytes);
            fileChannel.write(buffer);
        }
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
