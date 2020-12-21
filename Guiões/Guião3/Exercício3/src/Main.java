public class Main implements Runnable {
    private Bank bank;
    Main(Bank bank){
        this.bank = bank;
    }

    @Override
    public void run() {
        int id = bank.createAccount(1000);
        System.out.println(bank.balance(id));
        bank.deposit(id, 200);
        System.out.println(bank.balance(id));
    }

    public static void main(String[] args) throws InterruptedException {
        final int N = 4;
        Thread[] threads = new Thread[N];
        Bank b = new Bank();
        Main test = new Main(b);

        for(int i = 0; i < N; i++){
            threads[i] = new Thread(test);
        }

        for(int i = 0; i < N; i++){
            threads[i].start();
        }
        try {
            for (int i = 0; i < N; i++) {
                threads[i].join();
            }
        } catch (InterruptedException e){}
    }
}