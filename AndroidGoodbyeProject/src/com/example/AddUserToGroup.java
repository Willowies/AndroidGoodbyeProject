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
@WebServlet("/AddUserToGroup")
public class AddUserToGroup extends HttpServlet{

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/plain");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out=response.getWriter();
		String account = request.getParameter("account");
		String groupId = request.getParameter("groupId");
		System.out.println(account);
		System.out.println(groupId);
		/*int i = 9999;
		out.println("TRUE_"+i+"_hh");
		out.flush();
		out.close();*/
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
		      ResultSet rs = stmt.executeQuery("select * from groups where groupId = "+groupId+"");
		      //user 为表的名称
		      int num=0;
		      int version = -1;
		      String groupName = null;
		if (rs.next()) {
			version = rs.getInt("iconVersion");
			groupName = rs.getString("name");
			System.out.println(groupName);
		        num = 1;
		      }
		if(num == 0){
			out.println("NULL");
			connect.close();
		}else if(groupName != null && num==1 && version != -1){
			
		int i = 0;
	    String sql = "insert into groups (groupId,userAccount,name,iconVersion) values(?,?,?,?)";
	    PreparedStatement pstmt;
	    try {
	        pstmt = (PreparedStatement) connect.prepareStatement(sql);
	        pstmt.setInt(1, Integer.parseInt(groupId));
	        pstmt.setInt(2, Integer.parseInt(account));
	        pstmt.setString(3, groupName);
	        pstmt.setInt(4, version);
	        
	        i = pstmt.executeUpdate();
	        System.out.println(i);
	        if(i == 1){
	        	out.println("TRUE_"+groupName+"_"+version+"_hh");
	        }else{
	        	out.println("FAIL");
	        }
	    
	        pstmt.close();
	        connect.close();
	        
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
		    }else {
		    	out.println("FAIL");
		    	connect.close();
		    }
		    }
		    catch (Exception e) {
		      System.out.print("get data error!");
		      e.printStackTrace();
		    }
	}

}
