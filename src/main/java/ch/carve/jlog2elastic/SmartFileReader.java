package ch.carve.jlog2elastic;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

public class SmartFileReader {

    private long position;
    private File file;
    boolean stop = false;
    NewLinesListener listener = null;
    long interval;
    private int bulkSize;

    public SmartFileReader(String filePath, long position, long interval, int bulkSize) throws IOException {
        file = new File(filePath);
        this.position = position;
        this.interval = interval;
        this.bulkSize = bulkSize;
    }

    public void setListener(NewLinesListener listener) {
        this.listener = listener;
    }

    public void stop() {
        stop = true;
    }

    public void run() {
        while (!stop) {
            try {
                long newLength = file.length();
                if (newLength > position) {
                    readFile();
                }
                sleep();
            } catch (IOException e) {
                e.printStackTrace();
                stop();
            }
        }
    }

    private void readFile() throws FileNotFoundException, IOException {
        RandomAccessFile raf = new RandomAccessFile(file, "r");
        raf.seek(position);
        String line = null;
        List<String> lines = new ArrayList<>();
        int count = 0;
        while ((line = raf.readLine()) != null) {
            lines.add(line);
            if (++count >= bulkSize) {
                position = raf.getFilePointer();
                listener.onNewLines(lines, position);
                lines.clear();
                count = 0;
            }
        }
        position = raf.getFilePointer();
        listener.onNewLines(lines, position);
        raf.close();
    }

    private void sleep() {
        try {
            Thread.sleep(interval);
        } catch (InterruptedException e) {
            e.printStackTrace();
            stop();
            Thread.currentThread().interrupt();
        }
    }

}
