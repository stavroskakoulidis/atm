package com.utilities;

import com.client.Client;
import com.data.Customer;
import com.data.Transaction;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class Utils {

    public static SessionFactory getSessionFactory(){
        Configuration configuration = new Configuration();
        configuration.configure("hibernate.cfg.xml");
        configuration.addAnnotatedClass(Customer.class);
        configuration.addAnnotatedClass(Transaction.class);

        SessionFactory sessionFactory = configuration.buildSessionFactory();
        return sessionFactory;
    }


    private static final String[] options = {
            "(I) Client identification",
            "(C) Create Account",
            "(W) Withdraw",
            "(D) Deposit",
            "(B) Balance update",
            "(T) Transaction Logs",
            "(E) Exit"
    };

    private static final String[] usage = {
            "I <username> <password>",
            "C <username> <password>",
            "W <amount_to_withdraw>",
            "D <amount_to_deposit>",
            "B",
            "T",
            "E"

    };

    public static final String[] states = {"Show Options. ", "Identification. ", "Withdraw. ", "Deposit. ", "Balance. ", "Create Account. ", "Exit. "};
    public static final String[] answers = {"User Authenticated.", "Cannot Authenticate User.", "Cannot Create Account"};

    public static void PrintOptions() {
        for (String s: options){
            System.out.println(s);
        }
    }

    public static void PrintUsage(){
        System.out.println("Usage: ");
        for (String s: usage){
            System.out.println(s);
        }
    }


    public static void PrintTransactionLogs(String input) {
        int spaceCount = 0;
        String prefixToRemove = "Log";
        input = input.substring(prefixToRemove.length());

        StringBuilder result = new StringBuilder();

        for (char c : input.toCharArray()) {
            if (c == ' ') {
                spaceCount++;
                if (spaceCount == 6) {
                    result.append('\n');
                    spaceCount = 0;
                }
            }
            result.append(c);
        }
        System.out.println(result.toString());
    }


    public static boolean CheckLine(String line) {
        int amount;
        if (line != null && !line.equals("H") && line.length() < 2 && !line.equals("E") && !line.equals("B") && !line.equals("T")){
            System.out.println("Sorry.. Your selection is not valid. Insert 'H' for Help");
            return false;
        }

        switch (line.charAt(0)){
            case 'I':
            case 'E':
            case 'B':
            case 'C':
            case 'T':
                return true;

            case 'D':
            case 'W':
                if (line.charAt(1)!=' '){
                    System.out.println("Sorry.. Your selection is not valid. Insert 'H' for Help");
                }else {
                    amount = Integer.parseInt(line.substring(2));
                    if (amount <= 0 ){
                        System.out.println("Sorry... Insert a valid amount.");
                    }else {
                        return true;
                    }
                }
                break;
            case 'H':
                PrintOptions();
                PrintUsage();
                return false;
            default:
                System.out.println("Sorry.. Your selection is not valid. Insert 'H' for Help");
                PrintOptions();
                break;

        }
        return false;
    }

}
