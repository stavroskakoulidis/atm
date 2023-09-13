package com.data;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class Database {

    private SessionFactory sessionFactory = null;

    public Database(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public boolean identifyCustomer(String username, String password){

        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        String hql = "select count(*) from Customer where username = :username and password = :password";
        Query<Long> query = session.createQuery(hql,Long.class);
        query.setParameter("username", username);
        query.setParameter("password", password);

        long count = query.uniqueResult();

        transaction.commit();
        session.close();

        return count == 1;

    }

    public Customer getCustomer(String username, String password){

        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        String hql = "from Customer where username = :username and password = :password";

        Query<Customer> query = session.createQuery(hql, Customer.class);
        query.setParameter("username", username);
        query.setParameter("password", password);

        Customer customer = query.uniqueResult();

        transaction.commit();
        session.close();

        return customer;
    }

    public boolean checkForUniqueUserName(String username){

        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        String hql = "select userName from Customer";
        Query<String> query = session.createQuery(hql, String.class);
        List<String> results = query.getResultList();

        transaction.commit();
        session.close();

        for (String rs: results){
            if (rs.equals(username)){
                return false;
            }
        }
        return true;

    }

    public void insertCustomer(Customer customer){

        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        session.save(customer);

        transaction.commit();
        session.close();

    }

    public void updateBalance(Customer customer){
        
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        session.update(customer);

        transaction.commit();
        session.close();
    }

    public String printTransactions(int cId){

        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        String hql = "from Transaction t join fetch t.customer c where c.id = :customerId";

        Query<com.data.Transaction> query = session.createQuery(hql, com.data.Transaction.class);
        query.setParameter("customerId", cId);

        List<com.data.Transaction> results = query.getResultList();

        transaction.commit();
        session.close();

        String name;
        int id;
        String transaction_type;
        double amount;
        String date;
        String space = " ";
        StringBuilder str = new StringBuilder("Log");

        System.out.println("Name - " + " Transaction ID - " + " Transaction Type - " + " Amount - " + " Date. ");

        for (com.data.Transaction rs : results){
            name = rs.getCustomer().getUserName();
            id = rs.getId();
            transaction_type = rs.getTransactionType();
            amount = rs.getAmount();
            date = rs.getDate();
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


    public void logTransaction(com.data.Transaction trans) {

        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        session.save(trans);

        transaction.commit();
        session.close();
    }
}
