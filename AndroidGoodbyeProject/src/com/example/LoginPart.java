package com.example;

import java.sql.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class FirstExample
 */
@WebServlet("/Login")
public class LoginPart extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
   
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/plain");
		PrintWriter pw=response.getWriter();
		String account = request.getParameter("account");
		String password = request.getParameter("password");
		System.out.println(account);
		System.out.println(password);
		try {
		      Class.forName("com.mysql.jdbc.Driver");     
		      //加载MYSQL JDBC驱动程序   
		      //Class.forName("org.gjt.mm.mysql.Driver");
		     System.out.println("Success loading Mysql Driver!");
		    }
		    catch (Exception e) {
		      System.out.print("Error loading Mysql Driver!");
		      e.printStackTrace();
		    }
		    try {
		      Connection connect = DriverManager.getConnection(
		          "jdbc:mysql://localhost:3306/account","root","654714845");
		           //连接URL为   jdbc:mysql//服务器地址/数据库名  ，后面的2个参数分别是登陆用户名和密码

		      System.out.println("Success connect Mysql server!");
		      Statement stmt = connect.createStatement();
		      ResultSet rs = stmt.executeQuery("select * from user");
		      
		      //user 为表的名称
		      
		      int i=0;
		while (rs.next()) {
		        System.out.println(rs.getString("account"));
		        System.out.println(rs.getString("account"));
		        if(rs.getString("account").equals(account)){
		        	if(rs.getString("password").equals(password)){
		        		System.out.println("1");
		        		pw.write("TRUE_"+rs.getString("name"));
		        		pw.flush();
						pw.close();
		        		i=1;
		        		break;
		        	}else {
		        		System.out.println("2");
						pw.write("PASS");
						pw.flush();
						pw.close();
						i=1;
						break;
					}
		        }
		      }
		if(i==0){
			pw.write("ACCOUNT");
			pw.flush();
			pw.close();
		}
		connect.close();
		
		    }
		    catch (Exception e) {
		      System.out.print("get data error!");
		      e.printStackTrace();
		    }
		
		
		System.out.println("END");
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//doGet(request, response);
		response.setContentType("text/plain");
		PrintWriter pw=response.getWriter();
		String account = request.getParameter("account");
		String password = request.getParameter("password");
		System.out.println(account);
		System.out.println(password);
		try {
		      Class.forName("com.mysql.jdbc.Driver");     
		      //加载MYSQL JDBC驱动程序   
		      //Class.forName("org.gjt.mm.mysql.Driver");
		     System.out.println("Success loading Mysql Driver!");
		    }
		    catch (Exception e) {
		      System.out.print("Error loading Mysql Driver!");
		      e.printStackTrace();
		    }
		    try {
		      Connection connect = DriverManager.getConnection(
		          "jdbc:mysql://localhost:3306/account","root","654714845");
		           //连接URL为   jdbc:mysql//服务器地址/数据库名  ，后面的2个参数分别是登陆用户名和密码

		      System.out.println("Success connect Mysql server!");
		      Statement stmt = connect.createStatement();
		      ResultSet rs = stmt.executeQuery("select * from user");
		      
		      //user 为表的名称
		      
		      int i=0;
		while (rs.next()) {
		        System.out.println(rs.getString("account"));
		        System.out.println(rs.getString("account"));
		        if(rs.getString("account").equals(account)){
		        	if(rs.getString("password").equals(password)){
		        		System.out.println("1");
		        		pw.write("TRUE_"+rs.getString("name"));
		        		pw.flush();
						pw.close();
		        		i=1;
		        		break;
		        	}else {
		        		System.out.println("2");
						pw.write("PASS");
						pw.flush();
						pw.close();
						i=1;
						break;
					}
		        }
		      }
		if(i==0){
			pw.write("ACCOUNT");
			pw.flush();
			pw.close();
		}
		connect.close();
		
		    }
		    catch (Exception e) {
		      System.out.print("get data error!");
		      e.printStackTrace();
		    }
		
		
		System.out.println("END");
		
	}

}
