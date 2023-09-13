package com.company;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class DataBase {

    private Connection connection = null;
    String driverName = "com.mysql.cj.jdbc.Driver";
    String serverName = "localhost";
    String portNumber = "3306";
    String databaseName = "atm";
    String url = "jdbc:mysql://" + serverName + ":" + portNumber + "/" + databaseName;
    String username = <YOR_USERNAME>;
    String password = <YOUR_PASSWORD>;


    public boolean doConnection(){
        try {
            Class.forName(driverName);
            connection = DriverManager.getConnection(url, username, password);
        }catch (ClassNotFoundException e){
            System.out.println("ClassNotFoundException : "+e.getMessage());
            return false;
        }catch (SQLException e){
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    public boolean identifyCustomer(String username, String password) throws SQLException{

        String query = "select count(*) from atm.customer where username = '" + username + "' and password = '" + password + "'";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(query);

        if (rs.next()){
            int count = rs.getInt(1);
            return count == 1;
        }
        return false;

    }

    public boolean updateBalance(int cId, double newBalance) throws SQLException{

        String query = "update atm.customer set balance = " + newBalance + "where id = " + cId;
        Statement stmt = connection.createStatement();
        int rows = stmt.executeUpdate(query);

        return rows != 0;
    }

    public double getBalance(int cId) throws SQLException{

        String query = "select balance from atm.customer where id = " + cId;
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(query);

        rs.next();
        return rs.getDouble(1);
    }

    public int getID(String username, String password) throws SQLException{
        String query = "select id from atm.customer where username = '" + username + "' and password = '" + password + "'";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(query);

        if (rs.next()){
            return rs.getInt(1);
        }
        return -1;
    }

    public boolean insertCustomer(String username, String password) throws SQLException{

        String query = "insert into atm.customer(username,password, balance) values(" + "'" + username + "'" + "," + "'" + password + "'" + ", " + 0 + ")";
        Statement stmt = connection.createStatement();
        int rows = stmt.executeUpdate(query);

        return rows != 0;
    }

    public boolean checkForUniqueUserName(String username) throws SQLException{

        String query = "select username from atm.customer";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(query);

        while (rs.next()){
            if (rs.getString("username").equals(username)){
                return false;
            }
        }
        return true;
    }

    public void logTransaction(int cId, String transactionType, double amount) throws SQLException {

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        java.util.Date date = new java.util.Date();

        String query = "INSERT INTO atm.transactions (date, transaction_type, amount, customer_id) VALUES ('" + dateFormat.format(date) + "', '" + transactionType + "', '" + amount + "', " + cId + ")";
        Statement stmt = connection.createStatement();
        stmt.executeUpdate(query);

    }

    public String printTransactions(int cId) throws SQLException{

        String query = "select customer.username, transactions.transaction_id, transactions.transaction_type," +
                " transactions.amount, transactions.date from atm.customer join atm.transactions on customer.id = " +
                "transactions.customer_id where customer.id = " + cId;
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(query);

        String name;
        int id;
        String transaction_type;
        double amount;
        String date;
        String space = " ";
        StringBuilder str = new StringBuilder("Log ");


        System.out.println("Name - " + " Transaction ID - " + " Transaction Type - " + " Amount - " + " Date. ");

        while (rs.next()){
            name = rs.getString(1);
            id = rs.getInt(2);
            transaction_type = rs.getString(3);
            amount = rs.getDouble(4);
            date = rs.getString(5);
            str.append(name);
            str.append(space);
            str.append(id);
            str.append(space);
            str.append(transaction_type);
            str.append(space);
            str.append(amount);
            str.append(space);
            str.append(date);
            str.append(space);
            System.out.println(name + " " + id + " " + transaction_type + " " + amount + " " + date + ".");
        }
        return str.toString();

    }


}
