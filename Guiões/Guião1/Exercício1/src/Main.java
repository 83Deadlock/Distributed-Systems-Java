import java.lang.Thread;
import java.util.concurrent.locks.ReentrantLock;

public class Main {
    public static void main (String args[]) throws InterruptedException{

        ReentrantLock l = new ReentrantLock(); //l will be the ReentrantLock we use to apply mutual exclusion

        for(int i = 0; i < 10; i++){                    //Creating 10 threads sequentially
            Thread t = new Thread(new Incrementer());   //Creates a new Thread at each iteration of the cycle

            System.out.println("Thread " + i);          //Informs the user of the thread that is working at each moment

            l.lock();                                   //Grants access to the thread currently working
            try{
                t.start();                              //Executes the run() method defined on class Incrementer
                t.join();                               //Waits fpr the run() method to be over before continuing the cycle's execution
            } finally {                                 //After the thread ended its work
                l.unlock();                             //Unlock access so that the next thread can do its job
            }
        }

        System.out.println("Fim");
    }
}
