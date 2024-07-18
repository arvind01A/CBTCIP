package com.CBTCIP.arvind.BankingManagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Accounts {
	private Connection conn;
	private Scanner sc;
	
	public Accounts(Connection conn, Scanner sc) {
		this.conn = conn;
		this.sc = sc;
	}
	
	public long open_account(String email) {
		if(!account_exist(email)) {
			String open_account_query = "INSERT INTO ACCOUNTS(ACCOUNT_NUMBER, FULL_NAME, EMAIL, BALANCE, SECURITY_PIN) VALUES(?, ?, ?, ?, ?)";
			sc.nextLine();
			System.out.print("Enter Full Name:    ");
			String full_name = sc.nextLine();
			System.out.print("Enter Initial Amount:    ");
			double balance = sc.nextDouble(); sc.nextLine();
			System.out.print("Enter Security Pin:    ");
			String security_pin = sc.nextLine();
			
			try {
				long account_number = generateAccountNumber();
				PreparedStatement ps = conn.prepareStatement(open_account_query);
				ps.setLong(1, account_number);
				ps.setString(2, full_name);
				ps.setString(3, email);
				ps.setDouble(4, balance);
				ps.setString(5, security_pin);
				
				int result = ps.executeUpdate();
				if(result > 0)
					return account_number;
				else 
					throw new RuntimeException("Account Creation failed!!");
				
			} catch(SQLException se) {
				se.printStackTrace();
			}//try-catch
		}
		throw new RuntimeException("Account Already Exist");
	}//open_account(-)

	public long getAccount_number(String email) {
		String query = "SELECT ACCOUNT_NUMBER FROM ACCOUNTS WHERE EMAIL = ?";
		try {
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, email);
			ResultSet rs = ps.executeQuery();
			if(rs.next())
				return rs.getLong("account_number");
		} catch(SQLException se) {
			se.printStackTrace();
		}//try-catch
		
		throw new RuntimeException("Account Number Doesn't Exist");
	} 
	
	public boolean account_exist(String email) {
		String query = "SELECT ACCOUNT_NUMBER FROM ACCOUNTS WHERE EMAIL=?";
		try(PreparedStatement ps = conn.prepareStatement(query);
				){
			ps.setString(1, email);
			
			ResultSet rs = ps.executeQuery();
				
			if(rs.next())
					return true;
			else 
					return false;
			
		} catch(SQLException se) {
			se.printStackTrace();
		}
		return false;
	}

	private long generateAccountNumber() {
		try( Statement st = conn.createStatement();
				ResultSet rs = st.executeQuery("SELECT ACCOUNT_NUMBER FROM ACCOUNTS ORDER BY ACCOUNT_NUMBER DESC LIMIT 1");
			){
			
			if(rs.next()) {
				long last_account_number = rs.getLong("account_number");
				return last_account_number+1;
			} else {
				return 10000100;
			}
		} catch(SQLException se) {
			se.printStackTrace();
		} //try-catch
		
		return 10000100;
	}//generateAccountNumber()
	
}//class
