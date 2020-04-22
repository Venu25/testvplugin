package com.gae.scaffolder.plugin;

import io.socket.client.Socket;


public class SocketService {
    private static Socket socket;

    public static synchronized Socket getSocket(){
        return socket;
    }

    public static synchronized void setSocket(Socket socket){
        SocketService.socket = socket;
    }
}
