package ch.carve.jlog2elastic;

import org.junit.jupiter.api.Test;

public class LineParserTest {

    @Test
    public void testParse() {
        LineParser parser = new LineParser("test", "HH:mm:ss.SSS");
        LogMessage message = parser.parse("13:01:29.355  [main] DEBUG o.j.logging - Logging Provider: org.jboss.logging.Slf4jLoggerProvider");
        System.out.println(message.getDatetime().toString());
    }
}
