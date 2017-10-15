package ch.carve.jlog2elastic;

import java.io.File;
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

    public SmartFileReader(String filePath, long position, long interval) throws IOException {
        file = new File(filePath);
        this.position = position;
        this.interval = interval;
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
                    RandomAccessFile raf = new RandomAccessFile(file, "r");
                    raf.seek(position);
                    String line = null;
                    List<String> lines = new ArrayList<>();
                    while ((line = raf.readLine()) != null) {
                        lines.add(line);
                    }
                    position = raf.getFilePointer();
                    listener.onNewLines(lines, position);
                    raf.close();
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    stop();
                    Thread.currentThread().interrupt();
                }
            } catch (IOException e) {
                stop();
            }
        }
    }
}
