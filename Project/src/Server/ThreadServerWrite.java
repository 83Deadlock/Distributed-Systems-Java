package Server;

import java.io.DataOutputStream;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadServerWrite implements Runnable{
    private DataOutputStream write_socket;
    private Condition cond;
    private ServerBuffer ms;
    private ReentrantLock lock;

    public ThreadServerWrite(DataOutputStream write_socket, ServerBuffer ms){
        this.write_socket = write_socket;
        this.cond = ms.getCondition();
        this.ms = ms;
        this.lock = ms.getLock();
    }

    public void run(){
        this.lock.lock();
        try{
            String linha;
            while(true){
                while((linha = ms.getMessages())==null)
                    cond.await();
                if(linha.equals("Sair"))
                    break;
                this.write_socket.writeUTF(linha);
                write_socket.flush();
            }
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
        finally{
            this.lock.unlock();
        }
    }
}
