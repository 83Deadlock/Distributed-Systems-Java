import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Bank {

    private static class Account {
        private int balance;

        Account(int balance) {
            this.balance = balance;
        }
        int balance() {
            return balance;
        }

        boolean deposit(int value) {
            balance += value;
            return true;
        }
    }

    // Our single account, for now
    private Account savings = new Account(0);
    private Lock lock = new ReentrantLock();        //Since the bank is shared memory, every thread will "know" this lock exists

    // Account balance
    public int balance() {
        return savings.balance();
    }

    // Deposit
    boolean deposit(int value) {
        lock.lock();
        try{
            return savings.deposit(value);
        } finally {
            lock.unlock();
        }

    }
}