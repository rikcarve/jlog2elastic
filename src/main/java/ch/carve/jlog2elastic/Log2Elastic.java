package ch.carve.jlog2elastic;

import java.io.FileReader;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.esotericsoftware.yamlbeans.YamlReader;

import ch.carve.jlog2elastic.config.Config;

public class Log2Elastic {

    private static final Logger logger = LoggerFactory.getLogger(Log2Elastic.class);

    public static void main(String[] args) throws IOException {
        YamlReader yaml = new YamlReader(new FileReader("src/test/resources/config.yml"));
        Config config = yaml.read(Config.class);

        HttpElasticSender sender = new HttpElasticSender(config.getUrl(), config.getUsername(), config.getPassword(), config.getIndex(), config.getLogfiles().get(0).getApplication());
        SmartFileReader reader = new SmartFileReader(config.getLogfiles().get(0).getPath(), 0, config.getInterval(), config.getBulkSize());
        reader.setListener((lines, pos) -> sender.send(lines));

        logger.info("Start...");
        reader.run();
        logger.info("Exit ...");
    }
}
