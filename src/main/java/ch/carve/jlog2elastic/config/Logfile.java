package ch.carve.jlog2elastic.config;

import lombok.Data;

@Data
public class Logfile {
    private String application;
    private String path;
    private String timeFormat;
}
