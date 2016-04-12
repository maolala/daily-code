package spinLocks;

import java.util.concurrent.atomic.*;

public class TASLock {
    AtomicBoolean state = new AtomicBoolean(false);
    
    public void lock() {
        while (state.getAndSet(true)) {
            ;
        }
    }
    
    public void unlock() {
        state.set(false);
    }
}
