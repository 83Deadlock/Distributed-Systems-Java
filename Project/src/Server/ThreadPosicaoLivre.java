package Server;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class ThreadPosicaoLivre implements Runnable{
    private int [][] map;
    private int x;
    private int y;
    private Lock lock;
    private Condition cond;
    ServerBuffer sb;
    
    public ThreadPosicaoLivre(int[][] map, int x, int y, Lock lock, Condition cond, ServerBuffer sb){
        this.map = map;
        this.x = x;
        this.y = y;
        this.lock = lock;
        this.cond = cond;
        this.sb = sb;
    }

    public void run(){
        try {
            while (map[x][y] > 0) {
                sb.setMessages("Não se encontra livre. Será avisado assim que estiver", null);
                this.lock.lock();
                this.cond.await();
                this.lock.unlock();
            }

            sb.setMessages("A posição " + x + " " + y + " está livre", null);

        } catch (InterruptedException e){

        }
    }

}
