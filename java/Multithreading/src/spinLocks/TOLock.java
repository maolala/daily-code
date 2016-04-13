package spinLocks;

import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

public class TOLock {
    static QNode AVAILABLE = new QNode();
    AtomicReference<QNode> tail;
    ThreadLocal<QNode> myNode;
    
    public TOLock() {
        tail = new AtomicReference<QNode>(null);
        myNode = new ThreadLocal<QNode>();
    }
    
    public boolean tryLock(long time, TimeUnit unit) {
        long startTime = System.currentTimeMillis();
        long patience = TimeUnit.MILLISECONDS.convert(time, unit);
        QNode qnode = new QNode();
        myNode.set(qnode);
        QNode myPred = tail.getAndSet(qnode);
        if (myPred == null || myPred.pred == AVAILABLE) {
            return true;
        }
        while (System.currentTimeMillis() - startTime < patience) {
            if (myPred.pred == AVAILABLE) {
                return true;
            } else {
                if (myPred.pred != null) {
                    myPred = myPred.pred;
                }
            }
        }
        if (! tail.compareAndSet(qnode, myPred)) {
            qnode.pred = myPred;
        }
        return false;
    }
    
    public void unlock() {
        QNode qnode = myNode.get();
        if (! tail.compareAndSet(qnode, null)) {
            qnode.pred = AVAILABLE;
        }
    }
    
    /*
     * 方便测试使用
     */
    public void lock() {
        tryLock(10, TimeUnit.MILLISECONDS);
    }
    
    static class QNode {
        public QNode pred = null;
    }
}
