package com.example;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mysql.jdbc.PreparedStatement;
@WebServlet("/CheckUserIcon")
public class CheckUserIcon extends HttpServlet{
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/plain");
		PrintWriter out=response.getWriter();
		String account = request.getParameter("account");
		
		System.out.println(account);
		
		
		try {
		      Class.forName("com.mysql.jdbc.Driver");     
		      //����MYSQL JDBC��������   
		      //Class.forName("org.gjt.mm.mysql.Driver");
		     System.out.println("Success loading Mysql Driver!");
		    }
		    catch (Exception e) {
		      System.out.print("Error loading Mysql Driver!");
		      e.printStackTrace();
		    }
		    try {
		      Connection connect = DriverManager.getConnection(
		          "jdbc:mysql://localhost:3306/account?useSSL=false","root","654714845");
		           //����URLΪ   jdbc:mysql//��������ַ/���ݿ���  �������2�������ֱ��ǵ�½�û���������

		      System.out.println("Success connect Mysql server!");
		      Statement stmt = connect.createStatement();
		      ResultSet rs = stmt.executeQuery("select * from user where account = "+account+"");
		      //user Ϊ�������
		      int version=-1;
		      
		if (rs.next()) {
		        version = rs.getInt("iconVersion");
		      }
		System.out.println(version);
		if(version == -1){
			out.println("FAIL");
		}else{
			out.println("TRUE_"+version+"_h");
		}
		
		    }
		    catch (Exception e) {
		      System.out.print("get data error!");
		      e.printStackTrace();
		    }
	}
}
