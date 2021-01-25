package Server;


import java.io.DataInputStream;


public class ThreadServerRead implements Runnable{
    private DataInputStream readSocket;
    private ListUsers listUsers;
    private Utilizador user;
    private ServerBuffer ms;

    public ThreadServerRead (DataInputStream rs, ListUsers lu, ServerBuffer sb) {
        this.readSocket = rs;
        this.listUsers = lu;
        this.user = null;
        this.ms = sb;
    }

    public void run(){
        try{
            String input;
            while((input = readSocket.readUTF()) != null) {
                if(input.equals("1")){
                    String username, password;
                    username = readSocket.readUTF();
                    password = readSocket.readUTF();
                    try{
                        this.user = listUsers.loginUser(username, password, ms);
                       if(user.getCredencial() == 0) ms.setMessages("Sessão iniciada!", null);
                       else ms.setMessages("Sessão iniciada Saúde!", null);
                    } catch (Exception e){
                        ms.setMessages(e.getMessage() ,null);
                    }
                }
                else if(input.equals("2")){
                    String user,pass, x, y, c;
                    user = readSocket.readUTF();
                    pass = readSocket.readUTF();
                    x = readSocket.readUTF();
                    y = readSocket.readUTF();
                    c = readSocket.readUTF();
                    try{
                        listUsers.registerUser(user,pass,x,y,c,ms);
                        ms.setMessages("Registado",null);
                    }
                    catch(Exception e){
                        ms.setMessages(e.getMessage(),null);
                    }
                }

                else if(input.equals("1.1")){
                    String x,y;
                    x = readSocket.readUTF();
                    y = readSocket.readUTF();
                    try{
                        int count = this.listUsers.numeroPorLocalizacao(x,y,ms);
                        ms.setMessages("Numero de pessoas = " + count,null);
                    }
                    catch (Exception e){
                        ms.setMessages(e.getMessage(),null);
                    }

                }

                else if(input.equals("1.2")){
                    String x,y;
                    x = readSocket.readUTF();
                    y = readSocket.readUTF();
                    try{
                        this.listUsers.validaLocalizacao(user.getNome(),x,y,ms);
                        ms.setMessages("Localizacao Atualizada",null);
                    }
                    catch (Exception e){
                        ms.setMessages(e.getMessage(),null);
                    }

                }

                else if(input.equals("1.3")){
                    String x,y;
                    x = readSocket.readUTF();
                    y = readSocket.readUTF();
                    try{
                        listUsers.estaLivre(x , y, ms, this.user.getNome());
                        ms.setMessages("Aguardando por posição livre", null);
                    }
                    catch (Exception e){
                        ms.setMessages(e.getMessage(),null);
                    }
                }

                else if(input.equals("1.4")){
                    try{
                        listUsers.comunicaInfecao(this.user.getNome(), ms);
                        ms.setMessages("Utilizador Doente", null);
                    }
                    catch (Exception e){
                        ms.setMessages(e.getMessage(),null);
                    }
                }

                else if(input.equals("2.5")){
                    try{
                        String map = listUsers.mapToString();
                        ms.setMessages(map,null);
                    }
                    catch (Exception e){
                        ms.setMessages(e.getMessage(),null);
                    }
                }
            }
            readSocket.close();
            ms.setMessages("Sair",null);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
