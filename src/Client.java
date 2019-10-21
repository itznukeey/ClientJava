import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private static final int PORT = 1234;


    public static void main(String[] args) {

        BufferedReader serverOutput;
        PrintWriter clientOutput;
        Socket serverSocket;
        var keyboard = new BufferedReader(new InputStreamReader(System.in));

        try {
            serverSocket = new Socket("127.0.0.1", PORT);
            serverOutput = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
            clientOutput = new PrintWriter(serverSocket.getOutputStream(), true);
            System.out.println("Connected to: " + serverSocket.getInetAddress().toString() + ":" + serverSocket.getPort());
        } catch (IOException ex) {
            System.err.println("error while connecting to the server");
            return;
        }


        var sendThread = new Thread(() -> {

            while (true) {
                String message;
                try {
                    message = keyboard.readLine();
                    clientOutput.println(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        var readThread = new Thread(() -> {
            while (true) {
                try {
                    String recievedMessage = serverOutput.readLine();
                    System.out.println(recievedMessage);
                } catch (IOException e) {
                    System.err.println("Error while communicating with server");
                }
            }
        });

        sendThread.start();
        readThread.start();
    }
}
