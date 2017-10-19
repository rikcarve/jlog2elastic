package ch.carve.jlog2elastic.config;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.FileReader;
import java.io.IOException;

import org.junit.jupiter.api.Test;

import com.esotericsoftware.yamlbeans.YamlReader;

public class ConfigTest {

    @Test
    public void testYamlRead() throws IOException {
        YamlReader reader = new YamlReader(new FileReader("src/test/resources/config.yml"));
        Config config = reader.read(Config.class);
        assertEquals("user", config.getUsername());
        assertEquals("HH:mm:ss.SSS", config.getLogfiles().get(0).getTimeFormat());
    }
}
