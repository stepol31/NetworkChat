package client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(new File("settings.txt"));
        int port = Integer.parseInt(scanner.nextLine());
        String ip = scanner.nextLine();
        scanner.close();

        Socket socket = new Socket(ip, port);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        ClientConnection clientConnection = new ClientConnection(socket, bufferedReader);
        clientConnection.clientStart();
    }
}

