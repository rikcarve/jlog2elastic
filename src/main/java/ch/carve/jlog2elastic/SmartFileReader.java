package ch.carve.jlog2elastic;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SmartFileReader {
    private static final Logger logger = LoggerFactory.getLogger(SmartFileReader.class);

    private long position;
    private File file;
    private boolean stop = false;
    private NewLinesListener listener = null;
    private long interval;
    private int bulkSize;

    public SmartFileReader(String filePath, long position, long interval, int bulkSize) {
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
                logger.error("Excpetion occurred: ", e);
                stop();
            }
        }
    }

    private void readFile() throws IOException {
        try (RandomAccessFile raf = new RandomAccessFile(file, "r")) {
            raf.seek(position);
            String line = null;
            List<String> lines = new ArrayList<>();
            int count = 0;
            while ((line = raf.readLine()) != null) {
                lines.add(line);
                if (++count >= bulkSize) {
                    notify(raf, lines);
                    lines.clear();
                    count = 0;
                }
            }
            notify(raf, lines);
        }
    }

    private void notify(RandomAccessFile raf, List<String> lines) throws IOException {
        long newPosition = raf.getFilePointer();
        if (listener.onNewLines(lines, newPosition)) {
            position = newPosition;
        } else {
            throw new IOException("Notification failed");
        }
    }

    private void sleep() {
        try {
            Thread.sleep(interval);
        } catch (InterruptedException e) {
            logger.info("Interrupted, stop", e);
            stop();
            Thread.currentThread().interrupt();
        }
    }

}
