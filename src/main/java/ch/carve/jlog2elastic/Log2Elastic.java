package ch.carve.jlog2elastic;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Log2Elastic {

    private static final Logger logger = LoggerFactory.getLogger(Log2Elastic.class);

    public static void main(String[] args) throws IOException {
        HttpElasticSender sender = new HttpElasticSender("http://localhost:9200", "user", "password", "log");
        SmartFileReader reader = new SmartFileReader("src/test/resources/test.log", 0, 1000, 100);
        reader.setListener((lines, pos) -> sender.send(lines));
        logger.info("Start...");
        reader.run();
        logger.info("Exit ...");
    }
}
