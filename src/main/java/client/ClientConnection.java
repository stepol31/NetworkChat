package client;

import logger.Logger;

import java.io.*;
import java.net.Socket;

class ClientConnection {
    private final Logger log = Logger.getInstance();

    private final Socket socket;
    private final BufferedReader inputUser;

    private String name;

    public ClientConnection(Socket socket, BufferedReader inputUser) {
        this.socket = socket;
        this.inputUser = inputUser;

    }

    public void clientStart() {
        try {
            this.name = setName();

            log.logging("Пользователь " + name + " зашел в чат");

            setReaderAndWriter();
        } catch (IOException e) {
            closeService();
        }
    }

    void setReaderAndWriter() {
        new Read().start();
        new Write().start();
    }

    String setName() throws IOException {
        System.out.print("Введите свое имя: ");
        String name = inputUser.readLine();
        System.out.println("Добро пожаловать, " + name + " !");

        return name;
    }

    void closeService() {
        try {
            if (!socket.isClosed()) {
                socket.getInputStream().close();
                socket.getOutputStream().close();
            }
        } catch (IOException ignored) {
        }
    }

    private class Read extends Thread {
        @Override
        public void run() {
            String str;
            BufferedReader in;
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                while (true) {
                    str = in.readLine();
                    if (str == null || str.equals("exit")) {

                        log.logging("Пользователь " + name + " вышел из чата");

                        closeService();
                        break;

                    }
                    System.out.println(str);
                    log.logging("В чате сообщение от пользователя  " + " " + str);
                }
            } catch (IOException e) {
                closeService();
            }
        }
    }

    public class Write extends Thread {

        @Override
        public void run() {
            BufferedWriter out;
            try {
                out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            while (true) {
                String userWord;
                try {
                    userWord = inputUser.readLine();
                    if (userWord.equals("exit")) {
                        out.write("exit" + "\n");
                        out.flush();
                        break;
                    } else {
                        out.write(name + ": " + userWord + "\n");
                        log.logging("Пользователь " + name + " отправил(а) сообщение на сервер: " + userWord);
                    }
                    out.flush();
                } catch (IOException e) {
                    closeService();
                }
            }
        }
    }
}

