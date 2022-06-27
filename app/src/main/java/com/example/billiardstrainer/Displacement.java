package com.example.billiardstrainer;

import java.util.LinkedList;
import java.util.Queue;

public class Displacement {

    private Queue<DisplacementEntry> entries;
    private final int limit;

    public Displacement() {
        entries = new LinkedList<>();
        limit = 1000;

        initializeData();
    }

    public Queue<DisplacementEntry> getEntries(){
        return entries;
    }

    public DisplacementEntry addEntry(double xAxis, double yAxis, long time) {
        DisplacementEntry entry = new DisplacementEntry(xAxis, yAxis, time);
        entries.add(entry);
        entries.remove();
        return entry;
    }

    private void initializeData() {
        DisplacementEntry initialEntry = new DisplacementEntry(0.0d, 0.0d, 0L);
        for (int i = 0; i < limit; i++) {
            entries.add(initialEntry);
        }
    }
}
