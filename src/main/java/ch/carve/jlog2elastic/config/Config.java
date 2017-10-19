package ch.carve.jlog2elastic.config;

import java.util.List;

public class Config {
    private String url;
    private String username;
    private String password;
    private int bulkSize;
    private int interval;
    private String index;
    private List<Logfile> logfiles;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getBulkSize() {
        return bulkSize;
    }

    public void setBulkSize(int bulkSize) {
        this.bulkSize = bulkSize;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public List<Logfile> getLogfiles() {
        return logfiles;
    }

    public void setLogfiles(List<Logfile> logfiles) {
        this.logfiles = logfiles;
    }
}
