package com.company;

public class Customer {

    private int id;
    private String userName;
    private String passWord;
    private double balance;
    private boolean authenticated = false;

    public Customer(int id, String userName, String passWord, double balance, boolean authenticated) {
        this.id = id;
        this.userName = userName;
        this.passWord = passWord;
        this.balance = balance;
        this.authenticated = authenticated;
    }

    public Customer() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }

}
