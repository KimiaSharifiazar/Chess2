package Finall_Project_2.Server.Communications;


import Finall_Project_2.Server.DataBase.DataBase;
import Finall_Project_2.Server.User.User;

import javax.swing.plaf.multi.MultiSeparatorUI;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;


enum PlaceOfUser {LoginPage, MainPanel, UserInformation, GameReq, ScoreBoard, History, ProgrammerInfo};

class ClientHandler implements Runnable {
    final Socket clientSocket;
    String username;
    String password;
    PlaceOfUser placeOfUser = PlaceOfUser.LoginPage;//this help the server to know witch page should be shown to the client
    DataBase dataBase = DataBase.getInstance();//hame etelaate karbara o bazia injas


    public ClientHandler(Socket s) {
        this.clientSocket = s;
    }


    @Override
    public void run() {
        System.out.println("connected");
        messageToClient(" have an account  ?\n 1)yes 2)no", this.clientSocket);
        while (true) {
            try {
                handleMessageFromClient(messageFromClient());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * get and return the message from client and then decide what to do with that string
     *
     * @return
     * @throws IOException
     */
    public String messageFromClient() throws IOException {
        InputStream inputStream = clientSocket.getInputStream();
        Scanner scanner = new Scanner(inputStream);
        String messageFromClient = scanner.nextLine();
        return messageFromClient;
    }

    /**
     * parameter is the message in string formt and second parameter is the socket that the message should send to it .
     *
     * @param messageToClient
     */
    public void messageToClient(String messageToClient, Socket clientSocket) {
        try (OutputStream out = clientSocket.getOutputStream()) {
            PrintStream printStream = new PrintStream(out);
            printStream.println(messageToClient);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleMessageFromClient(String messageOfClient) throws IOException {
        switch (placeOfUser) {
            case LoginPage: {
                if (messageOfClient.equals("2"))
                    messageToClient("to make an account please enter your user name \nEXAMPLE : \nnewuser Kimia \npay attention that the word new should be written before your username !! ", this.clientSocket);
                else if (messageOfClient.equals("1")) {
                    messageToClient("please enter your username \nEXAMPLE :\nuser kimia \npay attention that the word user should be written before your username !!", this.clientSocket);
                } else if (messageOfClient.startsWith("user ")) {
                    this.username = messageOfClient.substring(6);
                    messageToClient("enter your password\nEXAMPLE :\npass 12345678\npay attention that the word pass should be written before your password !!", this.clientSocket);

                } else if (messageOfClient.startsWith("pass ")) {
                    this.password = messageOfClient.substring(6);
                    if (dataBase.AllUsers.contains(new User(username, password,clientSocket))) {
                        placeOfUser = PlaceOfUser.MainPanel;
                        messageToClient("Welcome ...now you are in main panel", this.clientSocket);
                    }
                } else if (messageOfClient.startsWith("newuser ")) {
                    this.username = messageOfClient.substring(9);
                    messageToClient("enter your password\nEXAMPLE :\nnewpass 12345678\npay attention that the word pass should be written before your password !!", this.clientSocket);
                } else if (messageOfClient.startsWith("newpass ")) {
                    this.password = messageOfClient.substring(9);
                    String userAdded = this.dataBase.addUser(new User(username, password ,  this.clientSocket));
                    if (userAdded.startsWith("this") || userAdded.startsWith("invalid")) {
                        messageToClient(userAdded, this.clientSocket);
                    } else {
                        placeOfUser = PlaceOfUser.MainPanel;
                        messageToClient("Welcome ...now you are in main panel\nwitch page you wanna go to ?\n1)My Account\n2)My History\n3)Score Board\n4)Request to Play\n5)App Information\n6)exit", this.clientSocket);
                    }
                }
                else {
                    messageToClient("invalid order",this.clientSocket);
                }

            }
            break;
            case MainPanel:{

                //1) My Account    2) My History   3) Score Board   4) Request to Play 5)app info 6)exit

                if (messageOfClient.equals("1")){
                    placeOfUser=PlaceOfUser.UserInformation;
                    messageToClient("1)delete account\n2)change password\n3)main panel",clientSocket);
                    //hala karbar har kalame E k vared kard az qesmate enum e k set shod besh mipardazim
                    //ok e
                }
                else if (messageOfClient.equals("2")){
                    placeOfUser=PlaceOfUser.History;
                    messageToClient("",clientSocket);
                    //
                }
                else if (messageOfClient.equals("3")){
                    placeOfUser=PlaceOfUser.ScoreBoard;
                    //dataBase.AllUsers.sort();
                    for (User i : dataBase.AllUsers) {
                        messageToClient(i.toString(),clientSocket);
                    }

                    //badesh yekio vared mikone bad get user o mizanim miarATESH , badam vasl mishan miran to bazi chon sockete har user malome
                    //
                }
                else if (messageOfClient.equals("4")){
                    placeOfUser=PlaceOfUser.GameReq;
                    messageToClient("enter your opponent username . if you know complete name enter like EXAMPLE : \nopponent Kimia \nif you wanna search enter like : \nsearch Kimi ",clientSocket);

                }
                else if (messageOfClient.equals("5")){
                    messageToClient("Kimia Sharifiazar\n98243082\nchess :| ",clientSocket);
                   //ok e
                }
                else if (messageOfClient.equals("6")){
                    messageToClient("goodbye",clientSocket);
                    //ok e
                }

            }
                break;

            case ProgrammerInfo:
                break;
            case GameReq:{
                if (messageOfClient.startsWith("opponent ")){
                    User opp =dataBase.getUser(messageOfClient.substring(10));
                    messageToClient("user "+username+"has requested to plat game with you . you accept ? 1)yes 2)no",opp.socket);
                }
                if (messageOfClient.startsWith("search ")){
                    ArrayList<User> subUsers = new ArrayList<>();
                    subUsers.addAll(dataBase.listOfUsersWithThisSubName(messageOfClient.substring(8)));
                    for (User i : subUsers) {
                        messageToClient(i.toString(),clientSocket);
                    }
                }
                if (messageOfClient.equals("1")){
                    //varede bazi mishan
                }
                else{
                    //req rad mishe
                }
            }
                break;
            case History:
                break;
            case ScoreBoard:
                break;
            //1)delete account 2)change password 3)main panel
            case UserInformation:{
                switch (messageOfClient){
                    case "1":{
                        dataBase.deleteUser(new User(username,password,clientSocket));
                    }break;
                    case "2": {
                        messageToClient("enter your new password\nEXAMPLE :\nnewpass 12345678", clientSocket);
                    }
                        break;
                    case "3":{
                        placeOfUser=PlaceOfUser.MainPanel;
                       messageToClient("now you are in main panel\nwitch page you wanna go to ?\n1)My Account\n2)My History\n3)Score Board\n4)Request to Play\n5)App Information", this.clientSocket);
                    }
                        break;
                }
                if (messageOfClient.startsWith("newpass ")){
                    String isPassChanged=dataBase.getUser(new User(username,password,clientSocket)).setNewPass(messageOfClient.substring(9));
                    if (isPassChanged!=null)
                        messageToClient(isPassChanged,clientSocket);
                    else{
                        dataBase.getUser(new User(username,password,clientSocket)).password=messageOfClient.substring(9);
                        this.password=messageOfClient.substring(9);
                    }
                }
            }
                break;
        }
    }

}

public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8080);

        while (true) {
            Socket s = serverSocket.accept();
            System.out.println("one connected");
            ClientHandler ch = new ClientHandler(s);
            (new Thread(ch)).start();
        }
    }
}
