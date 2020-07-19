package Finall_Project_2.Client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;



public class Client {
    /**
     * this method return every message frome server to this client
     * @param serverSocket socket
     * @return string
     * @throws IOException io
     */
    public static String messageFromServer(Socket serverSocket) throws IOException {
        InputStream inputStream=serverSocket.getInputStream();
        Scanner scanner=new Scanner(inputStream);
        String messageFromServer = scanner.nextLine();
        return messageFromServer;
    }
    public static void messageToServer(Socket serverSocket , String message) {
        try( OutputStream out =serverSocket.getOutputStream()) {
            PrintStream printStream=new PrintStream(out);
            printStream.println(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) throws IOException{

        Socket serverSocket = new Socket("localhost" , 8080);
        System.out.println(messageFromServer(serverSocket));
        Scanner scanner=new Scanner(System.in);
        //frequent communication between serversocket and this socket in this while
        while (scanner.hasNext()){
            String clientString=scanner.nextLine();
            messageToServer(serverSocket , clientString );
            String messageFromServer =messageFromServer(serverSocket);
            if (messageFromServer!=null){
                if (messageFromServer.equals("goodbye"))
                    return;
                System.out.println(messageFromServer);
            }
        }
    }
}


