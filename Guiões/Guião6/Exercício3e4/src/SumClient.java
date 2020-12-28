import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SumClient {

    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost",12345);

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream());

            BufferedReader systemIn = new BufferedReader(new InputStreamReader(System.in));

            String userInput;
            String response;
            while((userInput = systemIn.readLine()) != null) {
                out.println(userInput);
                out.flush();

                response = in.readLine();
                System.out.println("Current sum = " + response);
            }

            socket.shutdownOutput();
            response = in.readLine();
            System.out.println("Average = " + response);
            socket.shutdownInput();
            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}