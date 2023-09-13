package com.company;

import java.io.IOException;
import java.net.ServerSocket;

public class Server {

    public static void main(String[] args) throws IOException {

        if (args.length !=1 ){
            System.out.println("usage: java Server <port number>");
            System.exit(1);
        }

        int portNumber = Integer.parseInt(args[0]);
        boolean listening = true;

        try(ServerSocket serverSocket = new ServerSocket(portNumber)){

            System.out.println("Server started on port: " + portNumber);

            while (listening){
                new MultiServerThread(serverSocket.accept()).start();
            }
        }catch (IOException e){
            System.err.println("Could not listen on port " + portNumber);
            System.exit(-1);
        }

    }


}
