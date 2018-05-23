package com.example.zhiweixu.mailclient;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

class MySocket extends Socket {
    private static MySocket socket = null;
    private static final String host = "47.106.157.18";
    private static final int port = 9091;

    private static ObjectInputStream ois = null;
    private static ObjectOutputStream oos = null;

    static MySocket getInstance() throws IOException {
        if (socket == null) {
            socket = new MySocket(host, port);
        }
        return socket;
    }

    private MySocket(String host, int port) throws IOException {
        super(host,port);
    }

    public static ObjectInputStream getOis() throws IOException {
        if (ois == null) {
            ois = new ObjectInputStream(socket.getInputStream());
        }
        return ois;
    }

    public static ObjectOutputStream getOos() throws IOException {
        if (oos==null) {
            oos = new ObjectOutputStream(socket.getOutputStream());
        }
        return oos;
    }
}
