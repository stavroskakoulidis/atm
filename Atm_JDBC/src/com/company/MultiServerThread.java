package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.SQLException;

public class MultiServerThread extends Thread{

    private Socket socket =null;

    private final Customer customer;

    public MultiServerThread(Socket socket){
        this.socket = socket;
        customer = new Customer();
    }

    public void run(){

        try(PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        ){
            String inputLine, outputLine;

            DataBase conn = new DataBase();

            if (!conn.doConnection())
                System.exit(1);

            String stateString = "Current State is: ";
            outputLine = stateString + Utils.states[0];
            out.println(outputLine);

            int amount;

            while ((inputLine = in.readLine()) != null){
                System.out.println("Received message from client " + currentThread().getName() + " : " + inputLine);

                char input = inputLine.charAt(0);
                String[] customerCredentials = inputLine.split("\\s+");

                switch (input){
                    case 'I':
                        if (!customer.isAuthenticated()){
                            outputLine = stateString + Utils.states[1];

                            if (customerCredentials.length != 3){
                                outputLine += Utils.answers[2];
                                break;
                            }
                            if (conn.identifyCustomer(customerCredentials[1], customerCredentials[2])){
                                customer.setAuthenticated(true);
                                customer.setUserName(customerCredentials[1]);
                                customer.setPassWord(customerCredentials[2]);
                                customer.setId(conn.getID(customer.getUserName(), customer.getPassWord()));
                                customer.setBalance(conn.getBalance(customer.getId()));
                                outputLine += Utils.answers[0];
                            }else {
                                outputLine += Utils.answers[1];
                            }
                        }else {
                            outputLine = " You are already authenticated. ";
                        }
                        break;

                    case 'C':
                        if (!customer.isAuthenticated()){
                            outputLine = stateString + Utils.states[5];
                            if (customerCredentials.length != 3){
                                outputLine += Utils.answers[2];
                                break;
                            }
                            customer.setUserName(customerCredentials[1]);
                            customer.setPassWord(customerCredentials[2]);

                            if (conn.checkForUniqueUserName(customer.getUserName())){
                                if (conn.insertCustomer(customer.getUserName(), customer.getPassWord())){
                                    customer.setAuthenticated(true);
                                    customer.setId(conn.getID(customer.getUserName(), customer.getPassWord()));
                                    customer.setBalance(conn.getBalance(customer.getId()));
                                    outputLine += Utils.answers[0];
                                }else{
                                    outputLine += Utils.answers[1];
                                }
                            }else {
                                outputLine += " Username is taken. Try another one.";
                            }

                        }else {
                            outputLine = " You must exit your current session to create a new account. ";
                        }
                        break;

                    case 'T':
                        if (customer.isAuthenticated()){
                            outputLine = conn.printTransactions(customer.getId());
                        }else {
                            outputLine = "You must authenticate yourself first (I)";
                        }
                        break;

                    case 'E':
                        outputLine = stateString + Utils.states[6];
                        break;

                    default:
                        if (customer.isAuthenticated()){
                            if (input == 'W'){
                                outputLine = stateString + Utils.states[2];
                                amount = Integer.parseInt(inputLine.substring(2));

                                if (amount > customer.getBalance()){
                                    outputLine += "Not enough balance.";
                                }else{
                                    if (conn.updateBalance(customer.getId(), customer.getBalance() - amount)){
                                        conn.logTransaction(customer.getId(), "Withdraw", amount);
                                        customer.setBalance(customer.getBalance() - amount);
                                        outputLine += " Transaction completed. New balance is: " + customer.getBalance();
                                    }else {
                                        outputLine += " Error.... Could not complete transaction.";
                                    }
                                }
                            }else if (input == 'D'){
                                outputLine = stateString + Utils.states[3];
                                amount = Integer.parseInt(inputLine.substring(2));

                                if (conn.updateBalance(customer.getId(), customer.getBalance() + amount)){
                                    conn.logTransaction(customer.getId(), "Deposit", amount);
                                    customer.setBalance(customer.getBalance() + amount);
                                    outputLine += " Transaction completed. New balance is: " + customer.getBalance();
                                }else {
                                    outputLine += " Error... Could not complete transaction.";
                                }

                            }else if (input == 'B'){
                                outputLine = stateString + Utils.states[4];
                                customer.setBalance(conn.getBalance(customer.getId()));
                                outputLine += " Current balance is: " + customer.getBalance();
                            }
                        }else{
                            outputLine = "You must authenticate yourself first (I)";
                        }
                        break;
                }
                if (input == 'E'){
                    out.println("CLOSE");
                    break;
                }

                out.println(outputLine);

            }

            socket.close();

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }

    }
}
