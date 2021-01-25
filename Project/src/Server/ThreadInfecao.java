package Server;

import java.util.Map;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class ThreadInfecao implements Runnable{
    private Lock lock;
    private Utilizador user;
    private Condition cond;
    private ServerBuffer sb;
    private Map<String,Utilizador> utilizadores;

    public ThreadInfecao(Map<String,Utilizador> utilizadores,Condition cond, Lock lock, Utilizador u, ServerBuffer sb){
        this.lock = lock;
        this.cond = cond;
        this.utilizadores = utilizadores;
        this.user = u;
        this.sb = sb;
    }

    public boolean existeDoente(){
        for(Utilizador u: utilizadores.values()){
            if(u.isSick()) return true;
        }
        return false;
    }

    public void run() {
        int flag = 0;
        try {
            while (!existeDoente() || flag == 0) {
                this.lock.lock();
                this.cond.await();
                this.lock.unlock();

                if (!utilizadores.get(user.getNome()).isSick()) {
                    for (Utilizador u : utilizadores.values()) {
                        if (user.cruzou(u.getHistorico()) && !user.getNome().equals(u.getNome()) && u.isSick()) {
                            flag = 1;
                        }
                    }
                    if (flag == 1) sb.setMessages("⚠ ESTÁ EM RISCO DE INFEÇÃO ⚠", null);
                }
            }
        } catch (InterruptedException e){

        }
    }
}
