import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Warehouse {
    private Map<String, Product> m =  new HashMap<String, Product>();           //Map (Key = NomeDoProduto, Value = Produto)
    private Lock l = new ReentrantLock();

    private class Product {
        int q = 0;                                                              //Quantidade do produto a cada momento
        private Condition cond = l.newCondition();
    }

    private Product get(String s) {
        Product p = m.get(s);
        if (p != null) return p;
        p = new Product();
        m.put(s, p);
        return p;
    }

    public void supply(String s, int q) {
        l.lock();                                                               //Bloqueamos o acesso das outras threads ao armazem
        try {
            Product p = get(s);                                                 //Procuramos pelo produto que queremos
            p.q += q;                                                           //Somamos a quantidade recebida à previamente existente em stock
            p.cond.signalAll();                                                 //Avisamos todas as outras threads que o armazém recebeu produtos
        } finally {
            l.unlock();                                                         //Libertamos o lock
        }
    }

    public void consume(String[] a) throws InterruptedException {
        l.lock();                                                               //Bloqueamos o acesso das outras threads ao armazem
        try {
            for(int i = 0; i < a.length; ){                                     //Este ciclo representa um pre-consume (intenção de consumir)
                Product p = get(a[i]);                                          //Vamos esperar que todos os produtos da lista dada sejam possíveis de consumir
                i++;
                if(p.q == 0){
                    p.cond.await();
                    i = 0;
                }
            }
            for(String s: a)                                                    //Assim que todos estiverem em stock, vamos consumir todos de uma vez
                get(s).q--;
        } finally {
            l.unlock();                                                         //Libertamos o lock
        }
    }
}