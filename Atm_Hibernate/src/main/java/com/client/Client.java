package com.client;

import com.utilities.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

    private static final String SHOW_OPTIONS_IDENTIFIER = "Show Options.";


    public static void main(String[] args) throws IOException {

        if (args.length != 2){
            System.err.println("Usage: java Client <host name> <port number>");
            System.exit(1);
        }

        String hostName = args[0];
        int portNumber = Integer.parseInt(args[1]);

        try (Socket dataSocket = new Socket(hostName,portNumber);
             PrintWriter out = new PrintWriter(dataSocket.getOutputStream(),true);
             BufferedReader in = new BufferedReader(new InputStreamReader(dataSocket.getInputStream()));
        ){
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

            String fromServer;
            while ((fromServer = in.readLine()) != null){

                if (fromServer.startsWith("Log")){
                    System.out.println("Server:");
                    Utils.PrintTransactionLogs(fromServer);
                }else {
                    System.out.println("Server:\n" + fromServer);
                }

                if (fromServer.contains(SHOW_OPTIONS_IDENTIFIER)){
                    Utils.PrintOptions();
                    Utils.PrintUsage();
                }else if (fromServer.equals("CLOSE")){
                    break;
                }

                System.out.println("\nSelect Function: ");
                String fromUser = stdIn.readLine();
                while (!Utils.CheckLine(fromUser)){
                    System.out.println("Select Function: ");
                    fromUser = stdIn.readLine();
                }

                out.println(fromUser);

                if(fromUser.charAt(0) == 'E'){
                    dataSocket.close();
                    System.out.println("Data Socket closed");
                    System.exit(1);
                }

            }
        }catch (UnknownHostException e){
            System.err.println("Don't know about host "+ hostName);
            System.exit(1);
        }catch (IOException e){
            System.err.println("Couldn't get I/O for the connection to " + hostName);
            System.exit(1);
        }
    }
}
