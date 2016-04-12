package spinLocks;

import java.util.Random;
import java.util.concurrent.atomic.*;

public class BackoffLock {
    private AtomicBoolean state = new AtomicBoolean(false);
    private static final int MIN_DELAY = 10;
    private static final int MAX_DELAY = 100;
    
    public void lock() {
        Backoff backoff = new Backoff(MIN_DELAY, MAX_DELAY);
        while (true) {
            while (state.get()) {
                ;
            }
            if (! state.getAndSet(true)) {
                return;
            } else {
                backoff.backoff();
            }
        }
    }
    
    public void unlock() {
        state.set(false);
    }
}
class Backoff {
    private final int minDelay, maxDelay;
    int limit;
    final Random random;
    
    public Backoff(int min, int max) {
        minDelay = min;
        maxDelay = max;
        limit = minDelay;
        random = new Random();
    }
    
    public void backoff() {
        int delay = random.nextInt(limit);
        limit = Math.min(maxDelay, 2 * limit);
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            ;
        }
    }
}
