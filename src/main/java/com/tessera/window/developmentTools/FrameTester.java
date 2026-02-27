package com.tessera.window.developmentTools;

import com.tessera.window.utils.preformance.Stopwatch;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Desktop frame-performance monitor – rewritten without javax.swing for Android compatibility.
 * Timing data is still collected; output goes to System.out instead of a Swing window.
 */
public class FrameTester {
    private final Stopwatch processWatch = new Stopwatch();
    private final Stopwatch frameWatch = new Stopwatch();
    long lastUpdate = 0;
    boolean enabled = false;

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    private int updateTimeMS = 500;

    public void setUpdateTimeMS(int updateTimeMS) {
        this.updateTimeMS = updateTimeMS;
    }

    private boolean started = false;
    private final Map<String, TimeList> processList = new LinkedHashMap<>();
    private final Map<String, Long> counterList = new LinkedHashMap<>();
    boolean frameStarted = false;

    private long timeOfAllProcesses = 0;
    private int periodFrameCount = 0;

    public void setStarted(boolean started) {
        this.started = started;
    }

    static class TimeList {
        public long totalTime = 0;
    }

    long totalPeriodTime = 0;

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
                updateStatus(totalPeriodTime);
                lastUpdate = System.currentTimeMillis();
                timeOfAllProcesses = 0;
                periodFrameCount = 0;
                totalPeriodTime = 0;
            }
        } else if (frameStarted) {
            processList.clear();
            counterList.clear();
        }
        frameStarted = started;
    }

    public void startProcess() {
        if (frameStarted) {
            processWatch.start();
        }
    }

    public void count(String name, int count) {
        if (frameStarted) {
            counterList.merge(name, (long) count, Long::sum);
        }
    }

    public void set(String name, int value) {
        if (frameStarted) {
            counterList.put(name, (long) value);
        }
    }

    public long endProcess(String name) {
        if (frameStarted) {
            if (!processList.containsKey(name)) {
                processList.put(name, new TimeList());
            }
            processWatch.calculateElapsedTime();
            processList.get(name).totalTime += processWatch.getElapsedNanoseconds();
            timeOfAllProcesses += processWatch.getElapsedNanoseconds();
            processWatch.start();
            return processWatch.getElapsedMilliseconds();
        }
        return 0;
    }

    public static String formatTime(long nanoseconds) {
        if (nanoseconds < 1_000) {
            return nanoseconds + " ns";
        } else if (nanoseconds < 1_000_000) {
            return String.format("%.0f μs", nanoseconds / 1_000.0);
        } else if (nanoseconds < 1_000_000_000) {
            return String.format("%.0f ms", nanoseconds / 1_000_000.0);
        } else {
            return String.format("%.0f s", nanoseconds / 1_000_000_000.0);
        }
    }

    private void updateStatus(long totalPeriodTime) {
        if (periodFrameCount == 0) return;
        StringBuilder sb = new StringBuilder("[FrameTester] ");
        sb.append("period=").append(formatTime(totalPeriodTime));
        sb.append(" frames=").append(periodFrameCount);
        sb.append(" proc=").append(formatTime(timeOfAllProcesses));
        for (Map.Entry<String, TimeList> e : processList.entrySet()) {
            long avg = e.getValue().totalTime / periodFrameCount;
            sb.append(" | ").append(e.getKey()).append("=").append(formatTime(avg));
            e.getValue().totalTime = 0;
        }
        for (Map.Entry<String, Long> e : counterList.entrySet()) {
            sb.append(" | ").append(e.getKey()).append("=").append(e.getValue());
        }
        System.out.println(sb);
    }

    public FrameTester(String title) {
        // No Swing window; title kept for identification only
    }

    public static void main(String[] args) {
        FrameTester tester = new FrameTester("Test");
        tester.setEnabled(true);
        tester.setStarted(true);
        while (true) {
            try {
                tester.__startFrame();
                Thread.sleep(1); tester.endProcess("1ms a");
                Thread.sleep(1); tester.endProcess("1ms b");
                Thread.sleep(20); tester.endProcess("20ms");
                Thread.sleep(1); tester.endProcess("1ms c");
                tester.__endFrame();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}
