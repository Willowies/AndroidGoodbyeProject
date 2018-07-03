package com.example;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.apache.tomcat.util.http.fileupload.servlet.ServletRequestContext;

import com.mysql.jdbc.PreparedStatement;
@WebServlet("/UploadUserIcon")
public class UploadUserIcon extends HttpServlet{

	private static final String UPLOAD_DIRECTORY = "upload";
	  
    // �ϴ�����
    private static final int MEMORY_THRESHOLD   = 1024 * 1024 * 1;  // 1MB
    private static final int MAX_FILE_SIZE      = 1024 * 1024 * 4; // 4MB
    private static final int MAX_REQUEST_SIZE   = 1024 * 1024 * 5; // 5MB
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/plain");
		PrintWriter out=response.getWriter();
		System.out.println("upload user icon");
		 // �����ϴ�����
        DiskFileItemFactory factory = new DiskFileItemFactory();
        // �����ڴ��ٽ�ֵ - �����󽫲�����ʱ�ļ����洢����ʱĿ¼��
        factory.setSizeThreshold(MEMORY_THRESHOLD);
        // ������ʱ�洢Ŀ¼
        factory.setRepository(new File(System.getProperty("java.io.tmpdir")));
		
        ServletFileUpload servletFileUpload = new ServletFileUpload(factory);
        
        // ��������ļ��ϴ�ֵ
        servletFileUpload.setFileSizeMax(MAX_FILE_SIZE);
          
        // �����������ֵ (�����ļ��ͱ�����)
        servletFileUpload.setSizeMax(MAX_REQUEST_SIZE);
  
        // ������ʱ·�����洢�ϴ����ļ�
        // ���·����Ե�ǰӦ�õ�Ŀ¼
        String path ;
        String account = "0";
        int first=0;
        try{
        	List<FileItem> list = servletFileUpload.parseRequest(new ServletRequestContext(request));

			for (FileItem item : list) {
				String name = item.getFieldName();
				InputStream is = item.getInputStream();

				if (item.isFormField()) {
					account = item.getString();
					
				} 
			}
			for (FileItem item : list){
				if (!item.isFormField()) {
					InputStream is = item.getInputStream();
					try {
						
						path = "C:"+File.separator+"homepic"+File.separator+account+File.separator+account+".png";
						inputStream2File(is, path);
						break;
						
						
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
        }catch (FileUploadException e) {
			e.printStackTrace();
			System.out.println("failure");
			out.write("FAIL");
		}

        
	
		
		System.out.println("account "+account);
		
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
		          "jdbc:mysql://localhost:3306/account","root","654714845");
		           //����URLΪ   jdbc:mysql//��������ַ/���ݿ���  �������2�������ֱ��ǵ�½�û���������

		      System.out.println("Success connect Mysql server!");
		      Statement stmt = connect.createStatement();
		      ResultSet rs = stmt.executeQuery("select * from user where account = "+account+"");
		      int version = -1;
		      if(rs.next()){
		    	  version = rs.getInt("iconVersion");
		      }
		int i = version +1;
		String sql = "update user set iconVersion="
				+ i + " where account='" + account + "'";
	    PreparedStatement pstmt;
	    try {
	        pstmt = (PreparedStatement) connect.prepareStatement(sql);
	        i = pstmt.executeUpdate();
	        System.out.println(i);
	        if(i==1){
	        	out.println("TRUE");
	        }else{
	        	out.println("FAIL");
	        }
	        
	        pstmt.close();
	        connect.close();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
		    }
		    catch (Exception e) {
		      System.out.print("get data error!");
		      e.printStackTrace();
		    }
	}
	public static String inputStream2String(InputStream is) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int i = -1;
		while ((i = is.read()) != -1) {
			baos.write(i);
		}
		return baos.toString();
	}

	// ��ת�����ļ�
	public static void inputStream2File(InputStream is, String savePath) throws Exception {
		System.out.println("�ļ�����·��Ϊ:" + savePath);
		File file = new File(savePath);
		InputStream inputSteam = is;
		BufferedInputStream fis = new BufferedInputStream(inputSteam);
		FileOutputStream fos = new FileOutputStream(file);
		int f;
		while ((f = fis.read()) != -1) {
			fos.write(f);
		}
		fos.flush();
		fos.close();
		fis.close();
		inputSteam.close();

	}
}
