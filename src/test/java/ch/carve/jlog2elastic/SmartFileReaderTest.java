package ch.carve.jlog2elastic;

import java.io.IOException;

import org.junit.jupiter.api.Test;

public class SmartFileReaderTest {

    @Test
    public void readTest() throws IOException {
        SmartFileReader reader = new SmartFileReader("src/test/resources/test.log", 0, 200);
        reader.setListener((lines, pos) -> {
            lines.stream().forEach(line -> System.out.println(line));
            reader.stop();
        });
        reader.run();
    }
}
