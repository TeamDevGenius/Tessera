package com.tessera.window.developmentTools;

import com.tessera.window.utils.preformance.Stopwatch;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/** No-op stub replacing the Swing-based FrameTester for Android/LibGDX. */
public class FrameTester {
    private final Stopwatch processWatch = new Stopwatch();
    private final Stopwatch frameWatch = new Stopwatch();
    long lastUpdate = 0;
    boolean enabled = false;
    private boolean started = false;
    private final Map<String, TimeList> processList = new LinkedHashMap<>();
    private final Map<String, Long> counterList = new LinkedHashMap<>();
    boolean frameStarted = false;
    private long timeOfAllProcesses = 0;
    private int periodFrameCount = 0;
    private int updateTimeMS = 500;
    long totalPeriodTime = 0;

    static class TimeList {
        public long totalTime = 0;
    }

    public FrameTester(String title) {}

    public void setEnabled(boolean enabled) { this.enabled = enabled; }
    public void setUpdateTimeMS(int updateTimeMS) { this.updateTimeMS = updateTimeMS; }
    public void setStarted(boolean started) { this.started = started; }

    public void __startFrame() {
        if (started && enabled) {
            frameWatch.start();
            processWatch.start();
        }
    }

    public void __endFrame() {
        if (started && enabled) {
            frameWatch.calculateElapsedTime();
            totalPeriodTime += frameWatch.getElapsedNanoseconds();
            periodFrameCount++;
            if (System.currentTimeMillis() - lastUpdate > updateTimeMS) {
                lastUpdate = System.currentTimeMillis();
                timeOfAllProcesses = 0;
                periodFrameCount = 0;
                totalPeriodTime = 0;
            }
        }
        frameStarted = started;
    }

    public void startProcess() {
        if (frameStarted) processWatch.start();
    }

    public void count(String name, int count) {
        if (frameStarted) {
            counterList.merge(name, (long) count, Long::sum);
        }
    }

    public void set(String name, int value) {
        if (frameStarted) counterList.put(name, (long) value);
    }

    public long endProcess(String name) {
        if (frameStarted) {
            processList.computeIfAbsent(name, k -> new TimeList());
            processWatch.calculateElapsedTime();
            processList.get(name).totalTime += processWatch.getElapsedNanoseconds();
            timeOfAllProcesses += processWatch.getElapsedNanoseconds();
            processWatch.start();
        }
        return 0;
    }

    public static String formatTime(long nanoseconds) {
        if (nanoseconds < 1_000) return nanoseconds + " ns";
        else if (nanoseconds < 1_000_000) return String.format("%.0f μs", nanoseconds / 1_000.0);
        else if (nanoseconds < 1_000_000_000) return String.format("%.0f ms", nanoseconds / 1_000_000.0);
        else return String.format("%.0f s", nanoseconds / 1_000_000_000.0);
    }
}
