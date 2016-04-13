package spinLocks;

import java.util.concurrent.atomic.*;

public class CLHLock {
    AtomicReference<QNode> tail;
    ThreadLocal<QNode> myPred;
    ThreadLocal<QNode> myNode;
    
    public CLHLock() {
        tail = new AtomicReference<QNode>(new QNode());
        myPred = new ThreadLocal<QNode>() {
            protected QNode initialValue() {
                return null;
            }
        };
        myNode = new ThreadLocal<QNode>() {
            protected QNode initialValue() {
                return new QNode();
            }
        };
    }
    
    public void lock() {
        QNode qnode = myNode.get();
        qnode.locked = true;
        myPred.set(tail.getAndSet(qnode));
        while (myPred.get().locked) {
            ;
        }
    }
    
    public void unlock() {
        QNode qnode = myNode.get();
        qnode.locked = false;
        myNode.set(myPred.get());
    }
    
    class QNode {
        boolean locked = false;
    }
}
