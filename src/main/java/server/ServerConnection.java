package server;

import logger.Logger;

import java.io.*;
import java.net.Socket;

class ServerConnection extends Thread {
    public static Logger log = Logger.getInstance();

    private final Socket socket;
    private BufferedReader in;
    private BufferedWriter out;

    public ServerConnection(Socket socket, BufferedReader reader, BufferedWriter writer) throws IOException {
        this.socket = socket;
        this.in = reader;
        this.out = writer;
    }

    @Override
    public void run() {
        try {
            while (true) {
                String word = in.readLine();
                if (userIsExit(word)) break;

                for (ServerConnection connection : Server.serverList) {
                    connection.send(word);
                }
            }
        } catch (IOException e) {
            closeService();
        }
    }

    boolean userIsExit(String word) throws IOException {
        if (word == null || word.equals("exit")) {
            log.logging("Клиент вышел из чата");

            closeService();
            return true;
        }
        return false;
    }


    void send(String msg) throws IOException {
        out.write(msg + "\n");
        out.flush();
    }

    void closeService() {
        try {
            if (!socket.isClosed()) {
                socket.close();
                in.close();
                out.close();
                for (ServerConnection connection : Server.serverList) {
                    if (connection.equals(this)) connection.interrupt();
                    Server.serverList.remove(this);
                }
            }
        } catch (IOException ignored) {
        }
    }
}