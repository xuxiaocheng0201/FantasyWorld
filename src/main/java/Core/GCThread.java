package Core;

import HeadLibs.Logger.HLog;

public class GCThread implements Runnable {
    @Override
    public void run() {
        Thread.currentThread().setName("GCThread");
        while (true) {
            HLog.saveLogs(Craftworld.LOG_PATH);
            System.gc();
            synchronized (this) {
                try {
                    this.wait(Craftworld.GARBAGE_COLLECTOR_TIME_INTERVAL);
                } catch (InterruptedException exception) {
                    break;
                }
            }
        }
    }
}
