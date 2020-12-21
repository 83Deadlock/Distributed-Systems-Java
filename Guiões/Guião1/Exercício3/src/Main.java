import java.lang.Thread;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class Main {

    public static final int N = 10;     //Number of threads
    public static final int I = 1000;   //Number of deposits
    public static final int V = 100;    //Value of each deposit

    public static void main(String[] args) throws InterruptedException{

        List<Thread> threads = new ArrayList<>();       //Using a list to store every created thread
        Bank b = new Bank();                            //The bank will be shared memory for all threads created inside main

        for(int i = 0; i < N; i++){                     //Creating N threads and adding them to the list
            Thread t = new Thread(new Depositer(b,I,V));    //The run() method is defined on class Depositer
            threads.add(t);
            t.start();
        }

        for(Thread t : threads){                        //For every thread on the list, we wait for the end of it's execution
            t.join();
        }

        System.out.println("valor na conta - " + b.balance());
    }


}

class Depositer implements Runnable{
    private Bank bank;
    private int nrDeposits;
    private int valueDeposit;

    public Depositer (Bank b, int I, int V){
        this.bank = b;
        this.nrDeposits = I;
        this.valueDeposit = V;
    }

    public void run() {
        for(int i = 0; i < nrDeposits; i++){
            this.bank.deposit(valueDeposit);
        }
    }
}