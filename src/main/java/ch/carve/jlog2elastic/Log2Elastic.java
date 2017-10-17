package ch.carve.jlog2elastic;

import java.io.IOException;

public class Log2Elastic {

    public static void main(String[] args) throws IOException {
        HttpElasticSender sender = new HttpElasticSender("user", "password");
        SmartFileReader reader = new SmartFileReader("src/test/resources/test.log", 0, 200, 10);
        reader.setListener((lines, pos) -> lines.stream().forEach(line -> sender.send(line)));
        reader.run();
        System.out.println("Exit ...");
    }
}
