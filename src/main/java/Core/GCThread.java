package Core;

public class GCThread implements Runnable {
    @Override
    public void run() {
        Thread.currentThread().setName("GCThread");
        while (true) {
            System.gc();
            synchronized (this) {
                try {
                    wait(Craftworld.GARBAGE_COLLECTOR_TIME_INTERVAL);
                } catch (InterruptedException exception) {
                    break;
                }
            }
        }
    }
}
