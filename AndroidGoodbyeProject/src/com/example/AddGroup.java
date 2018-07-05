package com.example;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
@WebServlet("/AddGroup")
public class AddGroup extends HttpServlet{

	private String dir = "C:"+File.separator+"homepic"+File.separator+"groups";
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/plain");
		request.setCharacterEncoding("UTF-8");
		PrintWriter out=response.getWriter();
		String account = request.getParameter("account");
		String groupName = request.getParameter("groupName");
		System.out.println(account);
		System.out.println(groupName);
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
		      ResultSet rs = stmt.executeQuery("select * from groups order by groupId ASC");
		      //user 为表的名称
		      int num=0;
		      
		if (rs.last()) {
		        num = rs.getInt("groupId");
		      }
		if(num == 0){
			num += 10001;
		}else{
			num ++;
		}
		int i = 0;
	    String sql = "insert into groups (groupId,userAccount,name,iconVersion) values(?,?,?,?)";
	    PreparedStatement pstmt;
	    try {
	        pstmt = (PreparedStatement) connect.prepareStatement(sql);
	        pstmt.setInt(1, num);
	        pstmt.setInt(2, Integer.parseInt(account));
	        pstmt.setString(3, groupName);
	        pstmt.setInt(4, 0);
	        
	        i = pstmt.executeUpdate();
	        System.out.println(i);
	        if(i == 1){
	        	out.println("TRUE_"+num+"_hh");
	        }else{
	        	out.println("FAIL");
	        }
	        
	        Statement stmt1 = connect.createStatement();
		    ResultSet rs1 = stmt1.executeQuery("select count(*) c from groups where groupId="+num+";");
		    int firstGroup=0;
		    if (rs1.next()) {
				firstGroup=rs1.getInt("c");
			}
	        
		    if (firstGroup==0) {
		    	pstmt = (PreparedStatement) connect.prepareStatement("insert into group(groupId,iconVersion) values(?,?)");
		        pstmt.setInt(1, num);
		        pstmt.setInt(2, 0);
		        pstmt.executeUpdate();
			}

	        pstmt.close();
	        connect.close();
	        File f = new File(dir);  
          if(!f.exists()){  
              f.mkdirs();    
          } 
          File fileP=new File(dir+File.separator+num);
          if(!fileP.exists()){  
              fileP.mkdir();    
          }
          copyFile(dir+File.separator+"defaultGroupIcon.png",dir+File.separator+num+File.separator+num+".png");
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
		    }
		    catch (Exception e) {
		      System.out.print("get data error!");
		      e.printStackTrace();
		    }
	}

	public void copyFile(String oldPath, String newPath) { 
		try { 
		int bytesum = 0; 
		int byteread = 0; 
		File oldfile = new File(oldPath); 
		if (oldfile.exists()) { //文件存在时 
		InputStream inStream = new FileInputStream(oldPath); //读入原文件 
		FileOutputStream fs = new FileOutputStream(newPath); 
		byte[] buffer = new byte[1444]; 
		int length; 
		while ( (byteread = inStream.read(buffer)) != -1) { 
		bytesum += byteread; //字节数 文件大小 
		fs.write(buffer, 0, byteread); 
		} 
		inStream.close(); 
		fs.close();
		} 
		} 
		catch (Exception e) { 
		System.out.println("复制单个文件操作出错"); 
		e.printStackTrace(); 

		} 

		} 
}
