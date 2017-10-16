package ch.carve.jlog2elastic;

import java.io.IOException;

public class Log2Elastic {

    public static void main(String[] args) throws IOException {
        SmartFileReader reader = new SmartFileReader("src/test/resources/test.log", 0, 200, 10);
        reader.setListener((lines, pos) -> lines.stream().forEach(line -> ElasticSender.send(line)));
        // reader.setListener((lines, pos) -> ElasticSender.send(lines));
        reader.run();
        System.out.println("Exit ...");
    }
}
