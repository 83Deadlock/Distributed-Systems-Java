package Server;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ServerBuffer {
    private Condition cond;
    private ReentrantLock lock;
    private List<String> messages;
    private int i;

    public ServerBuffer(Condition c, ReentrantLock l) {
        this.cond = c;
        this.lock = l;
        this.messages = new ArrayList<>();
        this.i = 0;
    }

    public void setMessages(String msg, ArrayList<String> lista){
        this.lock.lock();
        try{
            if(lista == null)
                this.messages.add(msg);
            else {
                for(String m : lista)
                    this.messages.add(m);
            }
            cond.signal();
        }
        finally{
            this.lock.unlock();
        }
    }

    public String getMessages(){
        this.lock.lock();
        try{
            if(i!=messages.size()){
                return this.messages.get((i++));
            }
            else return null;
        }
        finally{
            this.lock.unlock();
        }
    }

    public Condition getCondition(){
        return this.cond;
    }

    public ReentrantLock getLock(){
        return this.lock;
    }
}
