package ch.carve.jlog2elastic;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class SmartFileReader {

    private WatchService watchService;
    private long position;
    private String path;
    
    public static void main(String[] args) throws Exception {
        SmartFileReader reader = new SmartFileReader("src/main/resources/", 0);
        while (true) {
            reader.readNewLines().stream().forEach(line -> System.out.println(line));
        }
    }
    
    public SmartFileReader(String path, long position) throws IOException {
        this.path = path;
        this.position = position;
        watchService = FileSystems.getDefault().newWatchService();
        Path filePath = Paths.get(path);
        filePath.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);
    }
    
    public List<String> readNewLines() throws IOException, InterruptedException {
        WatchKey watchKey = watchService.poll(20, TimeUnit.MILLISECONDS);
        if (watchKey != null) {
            for (WatchEvent<?> event : watchKey.pollEvents()) {
                if (event.kind() == StandardWatchEventKinds.ENTRY_MODIFY) {
                    System.out.println("Modified " + (Path)event.context());
                    BufferedReader reader = new BufferedReader(new FileReader(path + (Path)event.context()));
                    //reader.skip(position);
                    String line = null;
                    List<String> lines = new ArrayList<>();
                    while ((line = reader.readLine()) != null) {
                        lines.add(line);
                    }
                    watchKey.reset();
                    reader.close();
                    return lines;
                }
            }
        }
        return Collections.emptyList();
    }
}
