package com.example;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.StringTokenizer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mysql.jdbc.PreparedStatement;
@WebServlet("/Register.do")
public class RegisterPart extends HttpServlet {
	
	private String dir = "C:"+File.separator+"homepic";

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		response.setContentType("text/plain");
		PrintWriter out=response.getWriter();
		String account = request.getParameter("account");;
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
		          "jdbc:mysql://localhost:3306/account?useSSL=false","root","654714845");
		           //连接URL为   jdbc:mysql//服务器地址/数据库名  ，后面的2个参数分别是登陆用户名和密码

		      System.out.println("Success connect Mysql server!");
		      Statement stmt = connect.createStatement();
		      ResultSet rs = stmt.executeQuery("select * from user");
		      //user 为表的名称
		while (rs.next()) {
		        System.out.println(rs.getString("account"));
		        if(rs.getString("account").equals(account)){
		        	out.println("ACCOUNT");
		        	return;
		        }	
		      }
		int i = 0;
	    String sql = "insert into user (account,name,password,iconVersion) values(?,?,?,?)";
	    PreparedStatement pstmt;
	    try {
	        pstmt = (PreparedStatement) connect.prepareStatement(sql);
	        pstmt.setString(1, account);
	        pstmt.setString(2, account);
	        pstmt.setString(3, password);
	        pstmt.setInt(4, 0);
	        
	        i = pstmt.executeUpdate();
	        System.out.println(i);
	        out.println(i);
	        pstmt.close();
	        connect.close();
	        File f = new File(dir);  
            if(!f.exists()){  
                f.mkdir();    
            } 
            File fileP=new File(dir+File.separator+account);
            if(!fileP.exists()){  
                fileP.mkdir();    
            }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
		    }
		    catch (Exception e) {
		      System.out.print("get data error!");
		      e.printStackTrace();
		    }
		
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		resp.setContentType("text/plain");
		PrintWriter out=resp.getWriter();
		out.println("Hello The Register");
	}

}
