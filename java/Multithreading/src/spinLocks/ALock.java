package spinLocks;

import java.util.concurrent.atomic.*;

public class ALock {
    ThreadLocal<Integer> mySlotIndex = new ThreadLocal<Integer>();
    AtomicInteger tail;
    volatile boolean [] flag;
    int size;
    
    public ALock() {
        size = 100;
        tail = new AtomicInteger(0);
        flag = new boolean[size];
        flag[0] = true;
    }
    
    public void lock() {
        int slot = tail.getAndIncrement() % size;
        mySlotIndex.set(slot);
        while (! flag[slot]) {
            ;
        }
    }
    
    public void unlock() {
        int slot = mySlotIndex.get();
        flag[slot] = false;
        flag[(slot + 1) % size] = true;
    }
}
