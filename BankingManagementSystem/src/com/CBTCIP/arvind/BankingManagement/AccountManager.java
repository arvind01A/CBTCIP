package com.CBTCIP.arvind.BankingManagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class AccountManager {
	private Connection conn;
	private Scanner sc;
	
	public AccountManager(Connection conn, Scanner sc) {
		this.conn = conn;
		this.sc = sc;
	}
	
	public void credit_money(long account_number)throws SQLException {
        sc.nextLine();
        System.out.print("Enter Amount: ");
        double amount = sc.nextDouble(); sc.nextLine();
        System.out.print("Enter Security Pin: ");
        String security_pin = sc.nextLine();

        try {
            conn.setAutoCommit(false);
            if(account_number != 0) {
                PreparedStatement ps = conn.prepareStatement("SELECT * FROM ACCOUNTS WHERE ACCOUNT_NUMBER = ? AND SECURITY_PIN = ? ");
                ps.setLong(1, account_number);
                ps.setString(2, security_pin);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    String credit_query = "UPDATE ACCOUNTS SET BALANCE = BALANCE + ? WHERE ACCOUNT_NUMBER = ?";
                    PreparedStatement ps1 = conn.prepareStatement(credit_query);
                    ps1.setDouble(1, amount);
                    ps1.setLong(2, account_number);
                    int rowsAffected = ps1.executeUpdate();
                    if (rowsAffected > 0) {
                        System.out.println("Rs."+amount+" credited Successfully");
                        conn.commit();
                        conn.setAutoCommit(true);
                        return;
                    } else {
                        System.err.println("Transaction Failed!");
                        conn.rollback();
                        conn.setAutoCommit(true);
                    }
                }else{
                    System.err.println("Invalid Security Pin!");
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        conn.setAutoCommit(true);
    } //credit_money(-)
	
	 public void debit_money(long account_number) throws SQLException {
	        sc.nextLine();
	        System.out.print("Enter Amount: ");
	        double amount = sc.nextDouble();
	        sc.nextLine();
	        System.out.print("Enter Security Pin: ");
	        String security_pin = sc.nextLine();
	        try {
	            conn.setAutoCommit(false);
	            if(account_number!=0) {
	                PreparedStatement ps = conn.prepareStatement("SELECT * FROM ACCOUNTS WHERE ACCOUNT_NUMBER = ? AND SECURITY_PIN = ? ");
	                ps.setLong(1, account_number);
	                ps.setString(2, security_pin);
	                ResultSet rs = ps.executeQuery();

	                if (rs.next()) {
	                    double current_balance = rs.getDouble("balance");
	                    if (amount<=current_balance){
	                        String debit_query = "UPDATE ACCOUNTS SET BALANCE = BALANCE - ? WHERE ACCOUNT_NUMBER = ?";
	                        PreparedStatement ps1 = conn.prepareStatement(debit_query);
	                        ps1.setDouble(1, amount);
	                        ps1.setLong(2, account_number);
	                        int rowsAffected = ps1.executeUpdate();
	                        if (rowsAffected > 0) {
	                            System.out.println("Rs."+amount+" debited Successfully");
	                            conn.commit();
	                            conn.setAutoCommit(true);
	                            return;
	                        } else {
	                            System.err.println("Transaction Failed!");
	                            conn.rollback();
	                            conn.setAutoCommit(true);
	                        }
	                    }else{
	                        System.err.println("Insufficient Balance!");
	                    }
	                }else{
	                    System.err.println("Invalid Pin!");
	                }
	            }
	        }catch (SQLException se){
	                se.printStackTrace();
	            }
	        conn.setAutoCommit(true);
	    }//debit_money(-)
	 
	 public void transfer_money(long sender_account_number) throws SQLException {
	        sc.nextLine();
	        System.out.print("Enter Receiver Account Number: ");
	        long receiver_account_number = sc.nextLong();
	        System.out.print("Enter Amount: ");
	        double amount = sc.nextDouble();
	        sc.nextLine();
	        System.out.print("Enter Security Pin: ");
	        String security_pin = sc.nextLine();
	        try{
	            conn.setAutoCommit(false);
	            if(sender_account_number!=0 && receiver_account_number!=0){
	                PreparedStatement ps = conn.prepareStatement("SELECT * FROM ACCOUNTS WHERE ACCOUNT_NUMBER = ? AND SECURITY_PIN = ? ");
	                ps.setLong(1, sender_account_number);
	                ps.setString(2, security_pin);
	                ResultSet rs = ps.executeQuery();

	                if (rs.next()) {
	                    double current_balance = rs.getDouble("balance");
	                    if (amount<=current_balance){

	                        // Write debit and credit queries
	                        String debit_query = "UPDATE ACCOUNTS SET BALANCE = BALANCE - ? WHERE ACCOUNT_NUMBER = ?";
	                        String credit_query = "UPDATE ACCOUNTS SET BALANCE = BALANCE + ? WHERE ACCOUNT_NUMBER = ?";

	                        // Debit and Credit prepared Statements
	                        PreparedStatement creditPreparedStatement = conn.prepareStatement(credit_query);
	                        PreparedStatement debitPreparedStatement = conn.prepareStatement(debit_query);

	                        // Set Values for debit and credit prepared statements
	                        creditPreparedStatement.setDouble(1, amount);
	                        creditPreparedStatement.setLong(2, receiver_account_number);
	                        debitPreparedStatement.setDouble(1, amount);
	                        debitPreparedStatement.setLong(2, sender_account_number);
	                        int rowsAffected1 = debitPreparedStatement.executeUpdate();
	                        int rowsAffected2 = creditPreparedStatement.executeUpdate();
	                        if (rowsAffected1 > 0 && rowsAffected2 > 0) {
	                            System.out.println("Transaction Successful!");
	                            System.out.println("Rs."+amount+" Transferred Successfully");
	                            conn.commit();
	                            conn.setAutoCommit(true);
	                            return;
	                        } else {
	                            System.err.println("Transaction Failed");
	                            conn.rollback();
	                            conn.setAutoCommit(true);
	                        }
	                    }else{
	                        System.err.println("Insufficient Balance!");
	                    }
	                }else{
	                    System.err.println("Invalid Security Pin!");
	                }
	            }else{
	                System.err.println("Invalid account number");
	            }
	        }catch (SQLException se){
	            se.printStackTrace();
	        }
	        conn.setAutoCommit(true);
	    } //transfer_money(-)
	 
	 public void getBalance(long account_number){
	        sc.nextLine();
	        System.out.print("Enter Security Pin: ");
	        String security_pin = sc.nextLine();
	        try{
	            PreparedStatement ps = conn.prepareStatement("SELECT BALANCE FROM ACCOUNTS WHERE ACCOUNT_NUMBER = ? AND SECURITY_PIN = ?");
	            ps.setLong(1, account_number);
	            ps.setString(2, security_pin);
	            ResultSet rs = ps.executeQuery();
	            if(rs.next()){
	                double balance = rs.getDouble("balance");
	                System.out.println("Balance: "+balance);
	            }else{
	                System.err.println("Invalid Pin!");
	            }
	        }catch (SQLException se){
	           se.printStackTrace();
	        }
	    } //getBalance(-)
}
