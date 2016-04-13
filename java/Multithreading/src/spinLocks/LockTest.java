package spinLocks;

/*
 * 通过继承不同的父类来测试锁算法
 */
public class LockTest extends TOLock {
    public static final int NACCOUNTS = 100;
    public static double INITIAL_BALANCE = 1000;
    
    public static void main(String [] args) {
        Bank b = new Bank(NACCOUNTS, INITIAL_BALANCE);
        for(int i = 0; i < NACCOUNTS; i++) {
            TransferRunnable r = new TransferRunnable(b, i, INITIAL_BALANCE);
            Thread t = new Thread(r);
            t.start();
        }
    }
}

class Bank {
    private final double[] accounts;
    private LockTest bankLock;

    public Bank(int n, double initialBalance) {
        accounts = new double[n];
        for (int i = 0; i < accounts.length; i++) {
            accounts[i] = initialBalance;
        }
        bankLock = new LockTest();
    }

    public void transfer(int from, int to, double amount) throws InterruptedException {
        bankLock.lock();
        try {
            if (accounts[from] < amount) {
                return;
            }
            System.out.print(Thread.currentThread());
            accounts[from] -= amount;
            System.out.printf("%10.2f from %d to %d", amount, from, to);
            accounts[to] += amount;
            // 锁生效时，用户间转账不会造成所有用户总金额变动
            System.out.printf("   Total Balance %10.2f\n", getTotalBalance());
        } finally {
            bankLock.unlock();
        }
    }
    
    /*
     * 由于实现的锁不支持重入，故使用synchronized关键字简化测试
     */
    public synchronized double getTotalBalance() {
        double sum = 0;
        for (double a : accounts) {
            sum += a;
        }
        return sum;
    }

    public int size() {
        return accounts.length;
    }
}

class TransferRunnable implements Runnable {
    private Bank bank;
    private int fromAccount;
    private double maxAmount;
    private int DEALY = 10;

    public TransferRunnable(Bank b, int from, double max) {
        bank = b;
        fromAccount = from;
        maxAmount = max;
    }

    public void run() {
        try {
            while (true) {
                int toAccount = (int) (bank.size() * Math.random());
                double amount = maxAmount * Math.random();
                bank.transfer(fromAccount, toAccount, amount);
                Thread.sleep((long) (DEALY * Math.random()));
            }
        } catch (InterruptedException e) {
            ;
        }
    }
}

class EmptyLock {
    public void lock() {
        ;
    }
    
    public void unlock() {
        ;
    }
}
