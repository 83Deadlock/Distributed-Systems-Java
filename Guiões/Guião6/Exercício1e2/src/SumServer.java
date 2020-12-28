import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class SumServer {
    public static void main(String[] args) {
        try{
            ServerSocket ss = new ServerSocket(12345);

            while(true){
                Socket socket = ss.accept();
                int sum = 0;
                int count = 0;

                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream());

                String line;
                while((line = in.readLine()) != null){
                    sum += Integer.parseInt(line);
                    count++;
                    out.println(sum);
                    out.flush();
                }

                double med = sum / count;
                out.println(med);
                out.flush();
                socket.shutdownOutput();
                socket.shutdownInput();
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
