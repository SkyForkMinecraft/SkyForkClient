package cn.langya.utils;

public class StopWatch {
    private long millis;

    public StopWatch() {
        reset();
    }

    public boolean finished(long delay) {
        return (System.currentTimeMillis() - delay >= this.millis);
    }

    public void reset() {
        this.millis = System.currentTimeMillis();
    }

    public long getElapsedTime() {
        return System.currentTimeMillis() - this.millis;
    }
}