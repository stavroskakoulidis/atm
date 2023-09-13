package com.server;

import com.data.Customer;
import com.data.Database;
import com.data.Transaction;
import com.utilities.Utils;
import org.hibernate.SessionFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MultiServerThread extends Thread{

    private Socket socket = null;
    private SessionFactory sessionFactory = null;

    private Customer customer;
    private final Transaction transaction;

    public MultiServerThread(Socket socket, SessionFactory sessionFactory){
        this.socket = socket;
        this.sessionFactory = sessionFactory;
        customer = new Customer();
        transaction = new Transaction();
    }

    public void run(){

        try(PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        ){
            String inputLine, outputLine;

            Database conn = new Database(sessionFactory);


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
                                customer = conn.getCustomer(customerCredentials[1], customerCredentials[2]);
                                customer.setAuthenticated(true);
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
                            customer.setBalance(0);

                            if (conn.checkForUniqueUserName(customer.getUserName())){
                                conn.insertCustomer(customer);
                                customer.setAuthenticated(true);
                                outputLine += Utils.answers[0];

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

                                    customer.setBalance(customer.getBalance() - amount);
                                    conn.updateBalance(customer);
                                    outputLine += " Transaction completed. New balance is: " + customer.getBalance();
                                    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                                    Date date = new Date();
                                    transaction.setDate(dateFormat.format(date));
                                    transaction.setTransactionType("Withdraw");
                                    transaction.setAmount(amount);
                                    transaction.setCustomer(customer);
                                    conn.logTransaction(transaction);

                                }
                            }else if (input == 'D'){
                                outputLine = stateString + Utils.states[3];
                                amount = Integer.parseInt(inputLine.substring(2));

                                customer.setBalance(customer.getBalance() + amount);
                                conn.updateBalance(customer);
                                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                                java.util.Date date = new java.util.Date();
                                transaction.setDate(dateFormat.format(date));
                                transaction.setTransactionType("Deposit");
                                transaction.setAmount(amount);
                                transaction.setCustomer(customer);
                                conn.logTransaction(transaction);
                                outputLine += " Transaction completed. New balance is: " + customer.getBalance();

                            }else if (input == 'B'){
                                outputLine = stateString + Utils.states[4];
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

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
