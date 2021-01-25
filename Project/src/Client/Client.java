package Client;

import Server.*;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * This class implements the executable main method for each Client
 */

public class Client {
    public static void main (String[] args){

        //Initializing necessary variables
        Socket socket = null;
        ReentrantLock lock = new ReentrantLock();
        Condition cond = lock.newCondition();

        try{
            socket = new Socket("localhost",12345);

            DataInputStream ler_socket = new DataInputStream(new BufferedInputStream(socket.getInputStream()));

            Menu menu = new Menu();

            Thread tci = new Thread (new ThreadClientInput(socket,menu,lock, cond));
            Thread tco = new Thread (new ThreadClientOutput(ler_socket,menu,lock, cond));

            tci.start();
            tco.start();

            tci.join();
            tco.join();

            ler_socket.close();

            System.out.println("Até uma próxima!\n");
            socket.close();

        } catch (IOException e){
            System.out.println(e.getMessage());
        }
        catch (InterruptedException e){
            System.out.println(e.getMessage());
        }
    }
}
