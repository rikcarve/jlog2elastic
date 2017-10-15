package ch.carve.jlog2elastic;

import java.util.List;

public interface NewLinesListener {
    void onNewLines(List<String> lines, long lastPosition);
}
