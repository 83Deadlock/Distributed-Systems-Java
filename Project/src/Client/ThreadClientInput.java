package Client;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;


public class ThreadClientInput implements Runnable{
    private final int size = 5;
    private BufferedReader tecladoIn;
    private DataOutputStream writeSocket;
    private Socket socket;
    private Menu menu;
    private ReentrantLock lock;
    private Condition cond;

    public ThreadClientInput (Socket s, Menu m, ReentrantLock l, Condition c){
        try {
            this.tecladoIn = new BufferedReader(new InputStreamReader(System.in));
            this.writeSocket = new DataOutputStream(s.getOutputStream());
            this.socket = s;
            this.menu = m;
            this.lock = l;
            this.cond = c;
        } catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

    public void run() {
        String input;

        try {
            menu.showMenu();                                        //Presents the menu to the user
            while((input = tecladoIn.readLine()) != null){          //As long as the BufferedReader is open, we'll stay inside this cycle
                //Menu displayed is the login/register Menu
                if(menu.getOpcao() == 0){
                    //Login
                    if(input.equals("1")){
                        writeSocket.writeUTF("1");              //Write to the Server the option selected by the user
                        writeSocket.flush();

                        System.out.println("Username: ");          //Username and Password Request and consequent message sent to the Server
                        input = tecladoIn.readLine();
                        writeSocket.writeUTF(input);
                        writeSocket.flush();

                        System.out.println("Password: ");
                        input = tecladoIn.readLine();
                        writeSocket.writeUTF(input);
                        writeSocket.flush();

                        this.lock.lock();
                        cond.await();
                        this.lock.unlock();

                        input= "1";
                    }
                    //Register
                    else if (input.equals("2")){
                        writeSocket.writeUTF("2");              //Write to the Server the option selected by the user
                        writeSocket.flush();

                        System.out.println("Username: ");           //Username, Password and Location Request and consequent message sent to the Server
                        input = tecladoIn.readLine();
                        writeSocket.writeUTF(input);
                        writeSocket.flush();

                        System.out.println("Password: ");
                        input = tecladoIn.readLine();
                        writeSocket.writeUTF(input);
                        writeSocket.flush();

                        System.out.println("Localização (X) (0 a " + (size - 1) + "): ");
                        input = tecladoIn.readLine();
                        writeSocket.writeUTF(input);
                        writeSocket.flush();

                        System.out.println("Localização (Y) (0 a " + (size - 1) + "): ");
                        input = tecladoIn.readLine();
                        writeSocket.writeUTF(input);
                        writeSocket.flush();

                        //If (input == "0") the user will be treated by the system as an administrator
                        System.out.println("Credencial de Delegado de Saúde (0 se não for): ");
                        input = tecladoIn.readLine();
                        writeSocket.writeUTF(input);
                        writeSocket.flush();

                        this.lock.lock();
                        cond.await();
                        this.lock.unlock();

                        input= "2" ;
                    }
                    //Exit
                    else if(input.equals("0")){
                        break;                                      //To exit the running program, just break the cycle
                    }

                    //If the chosen option is one of the 3 valid to this menu, we print back the menu to the user. If not, we simply print an invalid input message
                    if(input.equals("1") || input.equals("2")) menu.showMenu();
                    else System.out.println("Opção Inválida");
                }

                //Menu displayed is the user's menu
                else if(menu.getOpcao() == 1){
                    //Logout
                    if(input.equals("0")) {
                        break;
                    }

                    //Number of users on a current location
                    else if (input.equals("1")){
                        writeSocket.writeUTF("1.1");
                        writeSocket.flush();

                        System.out.println("Localização (X) (0 a " + (size - 1) + "): ");
                        input = tecladoIn.readLine();
                        writeSocket.writeUTF(input);
                        writeSocket.flush();

                        System.out.println("Localização (Y) (0 a " + (size - 1) + "): ");
                        input = tecladoIn.readLine();
                        writeSocket.writeUTF(input);
                        writeSocket.flush();

                        this.lock.lock();
                        cond.await();
                        this.lock.unlock();

                        input="1";
                    }

                    //Change the user's current location
                    else if(input.equals("2")){ //Alterar localização

                        writeSocket.writeUTF("1.2");
                        writeSocket.flush();
                        System.out.println("Localização (X) (0 a " + (size - 1) + "): ");
                        input = tecladoIn.readLine();
                        writeSocket.writeUTF(input);
                        writeSocket.flush();

                        System.out.println("Localização (Y) (0 a " + (size - 1) + "): ");
                        input = tecladoIn.readLine();
                        writeSocket.writeUTF(input);
                        writeSocket.flush();

                        this.lock.lock();
                        cond.await();
                        this.lock.unlock();

                        input="2";
                    }

                    //Check if a location is free (#users_there == 0). If not, send a warning to the user when it becomes free
                    else if(input.equals("3")){

                        writeSocket.writeUTF("1.3");
                        writeSocket.flush();
                        System.out.println("Localização (X) (0 a " + (size - 1) + "): ");
                        input = tecladoIn.readLine();
                        writeSocket.writeUTF(input);
                        writeSocket.flush();

                        System.out.println("Localização (Y) (0 a " + (size - 1) + "): ");
                        input = tecladoIn.readLine();
                        writeSocket.writeUTF(input);
                        writeSocket.flush();

                        this.lock.lock();
                        cond.await();
                        this.lock.unlock();

                        input="3";
                    }

                    //User declares himself sick and gets lokced down
                    else if(input.equals("4")){
                        writeSocket.writeUTF("1.4");
                        writeSocket.flush();

                        this.lock.lock();
                        cond.await();
                        this.lock.unlock();

                        input="4";
                    }

                    //If the chosen option is one of the 5 valid, we print the menu back to the user
                    if(input.equals("1") ||input.equals("2") || input.equals("3") || input.equals("4")) menu.showMenu();
                    else System.out.println("Opção Inválida");
                }

                //Menu displayed is the admin's menu
                else if(menu.getOpcao() == 2){
                    //Logout
                    if(input.equals("0")) {
                        break;
                    }

                    //Number of users on a current location
                    else if (input.equals("1")){
                        writeSocket.writeUTF("1.1");
                        writeSocket.flush();
                        System.out.println("Localização (X) (0 a " + (size - 1) + "): ");
                        input = tecladoIn.readLine();
                        writeSocket.writeUTF(input);
                        writeSocket.flush();

                        System.out.println("Localização (Y) (0 a " + (size - 1) + "): ");
                        input = tecladoIn.readLine();
                        writeSocket.writeUTF(input);
                        writeSocket.flush();

                        this.lock.lock();
                        cond.await();
                        this.lock.unlock();

                        input="1";
                    }

                    //Change the user's current location
                    else if(input.equals("2")){ //Alterar localização

                        writeSocket.writeUTF("1.2");
                        writeSocket.flush();
                        System.out.println("Localização (X) (0 a " + (size - 1) + "): ");
                        input = tecladoIn.readLine();
                        writeSocket.writeUTF(input);
                        writeSocket.flush();

                        System.out.println("Localização (Y) (0 a " + (size - 1) + "): ");
                        input = tecladoIn.readLine();
                        writeSocket.writeUTF(input);
                        writeSocket.flush();

                        this.lock.lock();
                        cond.await();
                        this.lock.unlock();

                        input="2";
                    }

                    //Check if a location is free (#users_there == 0). If not, send a warning to the user when it becomes free
                    else if(input.equals("3")){

                        writeSocket.writeUTF("1.3");
                        writeSocket.flush();
                        System.out.println("Localização (X) (0 a " + (size - 1) + "): ");
                        input = tecladoIn.readLine();
                        writeSocket.writeUTF(input);
                        writeSocket.flush();

                        System.out.println("Localização (Y) (0 a " + (size - 1) + "): ");
                        input = tecladoIn.readLine();
                        writeSocket.writeUTF(input);
                        writeSocket.flush();

                        this.lock.lock();
                        cond.await();
                        this.lock.unlock();

                        input="3";
                    }

                    //User declares himself sick and gets lokced down
                    else if(input.equals("4")){
                        writeSocket.writeUTF("1.4");
                        writeSocket.flush();

                        this.lock.lock();
                        cond.await();
                        this.lock.unlock();

                        input="4";
                    }

                    //Admin asks to see a map that displays how many people got sick from all people that visited a location
                    else if(input.equals("5")){
                        writeSocket.writeUTF("2.5");
                        writeSocket.flush();

                        this.lock.lock();
                        cond.await();
                        this.lock.unlock();

                        input="5";
                    }

                    //If the chosen option is one of the 5 valid, we print the menu back to the user
                    if(input.equals("1") ||input.equals("2") || input.equals("3") || input.equals("4") || input.equals("5")) menu.showMenu();
                    else System.out.println("Opção Inválida");
                }

                //Menu displayed is the Infected's menu, only allows for session termination
                else if(menu.getOpcao() == 3){
                    if(input.equals("0")){
                        break;
                    }
                    else { System.out.println("Opção Inválida");}
                }

            }

            writeSocket.close();
            socket.shutdownOutput();

        } catch(Exception e) {
        }
    }
}
