package ch.carve.jlog2elastic;

import java.util.List;

public interface NewLinesListener {
    boolean onNewLines(List<String> lines, long lastPosition);
}
