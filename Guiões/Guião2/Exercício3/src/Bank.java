import java.util.concurrent.locks.ReentrantLock;

class Bank {
    private static class Account {
        private int balance;
        private ReentrantLock lock = new ReentrantLock();

        Account(int balance) {
            this.balance = balance;
            this.lock = lock;
        }

        int balance() {
            lock.lock();
            try{
                return balance;
            } finally {
                lock.unlock();
            }
        }

        boolean deposit(int value) {
            lock.lock();
            try {
                balance += value;
                return true;
            } finally {
                lock.unlock();
            }
        }

        boolean withdraw(int value) {
            if (value > balance)
                return false;
            lock.lock();
            try{
                balance -= value;
                return true;
            } finally {
                lock.unlock();
            }
        }
    }

    // Bank slots and vector of accounts
    private int slots;
    private Account[] av;

    public Bank(int n) {
        slots=n;
        av=new Account[slots];
        for (int i=0; i<slots; i++) av[i]=new Account(0);
    }

    // Account balance
    public int balance(int id) {
        if (id < 0 || id >= slots) return 0;
        return av[id].balance();
    }

    // Deposit
    boolean deposit(int id, int value) {
        if (id < 0 || id >= slots) return false;
            return av[id].deposit(value);
    }

    // Withdraw; fails if no such account or insufficient balance
    public boolean withdraw(int id, int value) {
        if (id < 0 || id >= slots) return false;
        return av[id].withdraw(value);
    }

    //Transfer; fails if if no such accounts or insufficient balance on source account
    public boolean transfer (int from, int to, int value){
        if(from < 0 || to < 0 || from >= slots || to >= slots || value <= 0) return false;
        int minID = Math.min(from,to);
        int maxID = Math.max(from,to);

        av[minID].lock.lock();
        av[maxID].lock.lock();

        try{
            if(av[from].balance < value) return false;
            av[from].withdraw(value);
            av[to].deposit(value);
            return true;
        } finally {
            av[maxID].lock.unlock();
            av[minID].lock.unlock();
        }
    }

    public int totalBalance(){
        int sum = 0;
        for(Account a : av) a.lock.lock();
        for(Account a : av){
            sum += a.balance();
            a.lock.unlock();
        }
        return sum;
    }
}