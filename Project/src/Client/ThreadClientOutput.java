package Client;


import java.io.DataInputStream;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadClientOutput implements Runnable{
    private DataInputStream readSocket;
    private Menu menu;
    private ReentrantLock lock;
    private Condition cond;

    public ThreadClientOutput(DataInputStream rs, Menu m, ReentrantLock l, Condition c){
        this.readSocket = rs;
        this.menu = m;
        this.lock=l;
        this.cond=c;
    }

    public void run() {
        int count = 0;
        int saude = 0;
        try{
            String line;

            //We'll set menu.option according to the type of user and situation
            //If user hasn't logged in yet, set menu.opcao as 0 -> Login/Register Menu
            //If user is an administrator, set menu.opcao as 2 -> Admin Menu
            //If user is just a client, set menu.opcao as 1 -> User Menu
            //if user has declared himself sick, set menu.opcao as 3 -> Infected Menu

            //We'll act according to the output of executing the program's available tasks

            while((line = readSocket.readUTF()) != null) {
                if(line.equals("Sessão iniciada!")){
                    menu.setOpcao(1);
                    System.out.println("Sessão iniciada!");
                    this.lock.lock();
                    cond.signal();
                    this.lock.unlock();
                }

                else if(line.equals("0")){
                    menu.setOpcao(1);
                    this.lock.lock();
                    cond.signal();
                    this.lock.unlock();
                }

                else if(line.equals("Sessão iniciada Saúde!")){
                    saude = 1;
                    menu.setOpcao(2);
                    System.out.println("Sessão iniciada!");
                    this.lock.lock();
                    cond.signal();
                    this.lock.unlock();
                }

                else if(line.equals("Registado") ||
                        line.equals("Delegado Registado!") ||
                        line.equals("Terminou sessão") ||
                        line.equals("Nome de utilizador não existe!") ||
                        line.equals("A password está incorreta!") ||
                        line.equals("Nome de utilizador já em uso!") ||
                        line.equals("Localização inválida! Efetue novamente o registo!")){
                    menu.setOpcao(0);
                    System.out.println("\n"+line+"\n");
                    this.lock.lock();
                    cond.signal();
                    this.lock.unlock();
                }

                else if(line.equals("Localizacao Atualizada") ||
                        line.equals("Localização inválida!") ||
                        line.equals("Essa é a sua localização!") ||
                        line.contains("Numero de pessoas = ")){
                    if(saude == 0) menu.setOpcao(1);
                    else if (saude == 1) menu.setOpcao(2);
                    System.out.println("\n"+line+"\n");
                    this.lock.lock();
                    cond.signal();
                    this.lock.unlock();
                }
                else if(line.contains("A posição ")){
                    if(saude == 0) menu.setOpcao(1);
                    else if (saude == 1) menu.setOpcao(2);
                    System.out.println("\n"+line+"\n");
                    this.lock.lock();
                    cond.signal();
                    this.lock.unlock();
                }

                else if(line.equals("⚠ ESTÁ EM RISCO DE INFEÇÃO ⚠")){
                    if(saude == 0) menu.setOpcao(1);
                    else if (saude == 1) menu.setOpcao(2);
                    System.out.println("\n"+line+"\n");
                    this.lock.lock();
                    cond.signal();
                    this.lock.unlock();
                }

                else if(line.contains("Mapa dos utilizadores doentes por utilizadores totais numa localização")){
                    menu.setOpcao(2);
                    System.out.println("\n"+line+"\n");
                    this.lock.lock();
                    cond.signal();
                    this.lock.unlock();
                }

                else if(line.equals("Não se encontra livre. Será avisado assim que estiver")){
                    if(saude == 0) menu.setOpcao(1);
                    else if (saude == 1) menu.setOpcao(2);
                    if(count == 0) System.out.println("\n"+line+"\n");
                    count++;
                    this.lock.lock();
                    cond.signal();
                    this.lock.unlock();
                }

                else if(line.equals("Utilizador Doente")){
                    menu.setOpcao(3);
                    this.lock.lock();
                    cond.signal();
                    this.lock.unlock();
                }
            }
        } catch (Exception e){
        }
    }
}
