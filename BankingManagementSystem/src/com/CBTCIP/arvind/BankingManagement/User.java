package com.CBTCIP.arvind.BankingManagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class User {
	private Connection conn;
	private Scanner sc;
	
	public User(Connection conn, Scanner sc) {
		this.conn = conn;
		this.sc = sc;
	}
	
	public void register() {
		sc.nextLine();
		System.out.print("Full Name:    ");
		String full_name = sc.nextLine();
		System.out.print("Email:   ");
		String email = sc.nextLine();
		System.out.print("Password:    ");
		String password = sc.nextLine();
		
		if(user_exist(email)) {
			System.err.println("User Already Exists for this Email Address!!");
			return;
		}
		
		String register_query = "INSERT INTO USER VALUES(?, ?, ?)";
		try(PreparedStatement ps = conn.prepareStatement(register_query);
			){
			ps.setString(1, full_name);
			ps.setString(2, email);
			ps.setString(3, password);
			
			int result = ps.executeUpdate();
			if(result > 0)
				System.out.println("Registration Successfull!");
			else 
				System.out.println("Registration Failed");
		} catch(SQLException se) {
			se.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace();
		} //try-catch
		
	}

	public String login() {
		sc.nextLine();
		System.out.print("\nEmail:     ");
		String email = sc.nextLine();
		System.out.print("Password:   ");
		String password = sc.nextLine();
		String login_query = "SELECT * FROM USER WHERE email = ? AND password = ? ";
		try(PreparedStatement ps = conn.prepareStatement(login_query);
				){
			ps.setString(1, email);
			ps.setString(2, password);
			
			ResultSet rs = ps.executeQuery();
				if(rs.next())
					return email;
				else 
					return null;
			//}//inner-try
			
		} catch(SQLException se) {
			se.printStackTrace();
		}// outer-try-catch
		return null;
		
	}//login

	private boolean user_exist(String email) {
		String query = "SELECT * FROM USER WHERE EMAIL = ?";
		try(PreparedStatement ps = conn.prepareStatement(query);
				){
			ps.setString(1, email);
			
			try(ResultSet rs = ps.executeQuery();
					){
				if(rs.next())
					return true;
				else 
					return false;
			}
		} catch(SQLException se) {
			se.printStackTrace();
		}//try-catch
		return false;
	}//register()
	
	
}//class
