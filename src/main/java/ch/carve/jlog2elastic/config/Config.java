package ch.carve.jlog2elastic.config;

import java.util.List;

import lombok.Data;

@Data
public class Config {
    private String url;
    private String username;
    private String password;
    private int bulkSize;
    private int interval;
    private String index;
    private List<Logfile> logfiles;
}
