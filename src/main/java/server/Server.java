package server;

import logger.Logger;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;


public class Server {
    public static Logger log = Logger.getInstance();
    public static List<ServerConnection> serverList = new LinkedList<>();

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(new File("settings.txt"));
        int port = Integer.parseInt(scanner.nextLine());
        scanner.close();

        ServerSocket server = new ServerSocket(port);

        System.out.println("Server start");
        log.logging("Server start");

        try {
            while (true) {
                Socket socket = server.accept();
                try {
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                    ServerConnection connection = new ServerConnection(socket, in, out);
                    connection.start();
                    serverList.add(connection);
                } catch (IOException e) {
                    socket.close();
                }
            }
        } finally {
            server.close();
        }
    }
}




