import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.locks.ReentrantLock;

class ServerCounter {
    private int sum;
    private int count;
    private ReentrantLock lock;

    public ServerCounter(){
        sum = 0;
        count = 0;
        lock = new ReentrantLock();
    }

    public double getAverage () {
        double avg;
        lock.lock();
        try{
            avg = (double) sum / count;
            return avg;
        } finally {
            lock.unlock();
        }
    }

    public void addNumber(int number){
        lock.lock();
        try{
            this.sum += number;
            this.count++;
        } finally {
            lock.unlock();
        }
    }
}

class ServerWorker implements Runnable {
    private Socket socket;
    private ServerCounter server;

    public ServerWorker (Socket socket, ServerCounter server) {
        this.socket = socket;
        this.server = server;
    }

    public void run(){
        try{
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream());
            int sum = 0;
            int n = 0;
            int curr;
            String line;
            while((line = in.readLine()) != null){
                try {
                    curr = Integer.parseInt(line);
                    sum += curr;
                    this.server.addNumber(curr);
                    n++;
                } catch (NumberFormatException e){

                }
                out.println(sum);
                out.flush();
            }
            if(n < 1){
                n = 1;
            }

            out.println(this.server.getAverage());
            out.flush();

            socket.shutdownOutput();
            socket.shutdownInput();
            socket.close();

        } catch (IOException e){
            e.printStackTrace();
        }
    }
}

public class MultipleClientsServer {
    public static void main(String[] args) throws IOException{
        ServerSocket ss = new ServerSocket(12345);
        ServerCounter server = new ServerCounter();

        while(true) {
            Socket socket = ss.accept();
            Thread worker = new Thread(new ServerWorker(socket,server));
            worker.start();
        }
    }

}