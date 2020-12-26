import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static java.lang.Math.max;

public class Agreement {
    private static class Instance {
        int result = Integer.MIN_VALUE;     //Inicialmente, o menor valor inteiro possível -> será o valor máximo dado pelas threads para o contrato
        int c = 0;                          //Número de threads em espera
    }

    private int n;                          //Número total de threads
    private Instance agmt = new Instance(); //Inicia o contrato
    Lock l = new ReentrantLock();
    Condition cond = l.newCondition();

    public Agreement(int n){
        this.n = n;
    }

    int propose (int choice) throws InterruptedException {
        l.lock();
        try{
            Instance agmt = this.agmt;                  //É criada uma nova instância do contrato (temporária)
            agmt.c += 1;                                //Incrementa-se o número de threads em espera
            agmt.result = max(agmt.result, choice);     //Calcula-se o maior entre o valor do contrato e o dado pela thread e guarda-se o resultado no contrato

            if(agmt.c < n) {                            //Se a thread não for a última a atignir o lock
                while (this.agmt == agmt){              //Enquanto este contrato for válido, a thread fica em espera
                    cond.await();                       //Mecanismo de espera
                }
            } else {                                    //Se a thread for a última a atingir o lock
                cond.signalAll();                       //Sinaliza todas as threads para prosseguirem com a sua execução
                this.agmt = new Instance();             //Invalida o contrato anterior e cria um novo (isto aliado à condição do cliclo while permite a reutilização do agreement)
            }
            return agmt.result;                         //Devolve o valor máximo encontrado entre os dados pelas threads (este contrato é o temporário)
        } finally {
            l.unlock();                                 //Liberta o lock
        }
    }
}
