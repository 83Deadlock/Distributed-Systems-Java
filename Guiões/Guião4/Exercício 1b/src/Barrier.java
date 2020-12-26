import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Barrier {
    private int c = 0;                  //Número de threads em espera
    private int n;                      //Número de threads total
    Lock l = new ReentrantLock();
    Condition cond = l.newCondition();
    private int epoch = 0;

    public Barrier(int N){
        this.n = N;
    }

    void await () throws InterruptedException {
        l.lock();                       //As threads têm de dar lock antes de executar o await()
        try{
            int e = epoch;              //Estabelece a variável de condição como verdadeira
            c += 1;                     //Assim que garantem o lock, as threads incremental o valor de c (corresponde ao número de threads em espera)
            if (c < n)                  //Se a thread não for a última a atingir o lock, deve ficar em espera
                while (epoch == e)
                    cond.await();       //Mecanismo de espera
            else{                       //Se a thread for a última a atingir o lock, deve sinalizar todas as outras para resumirem a sua execução
                cond.signalAll();       //Mecanismo de sinalização
                c = 0;                  //Reset ao número de threads em espera
                epoch += 1;             //Reset à variável de condição
            }
        } finally {
            l.unlock();                 //Por final, a thread deve soltar o lock
        }
    }
}