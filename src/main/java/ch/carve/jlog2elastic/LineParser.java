package ch.carve.jlog2elastic;

import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

public class LineParser {

    private DateTimeFormatter dtf;
    private int dtfLength;
    private String application;

    public LineParser(String application, String dateTimeFormat) {
        this.application = application;
        dtf = DateTimeFormatter.ofPattern(dateTimeFormat);
        dtfLength = dateTimeFormat.length();
    }

    public LogMessage parse(String line) {
        LogMessage message = new LogMessage();
        OffsetDateTime now = OffsetDateTime.now();
        message.setDatetime(LocalTime.parse(line.substring(0, dtfLength), dtf).atOffset(now.getOffset()).atDate(now.toLocalDate()));
        message.setApplication(application);
        message.setLogMessage(line);
        return message;
    }
}
