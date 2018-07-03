package com.example;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.jws.Oneway;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.apache.tomcat.websocket.WsWebSocketContainer;

import com.mysql.jdbc.PreparedStatement;

@ServerEndpoint(value = "/communication")
public class CommunicationWebSocket {
	private Session session;
	private int userAccount=0;
	private static final Set<CommunicationWebSocket> connections = 
			new CopyOnWriteArraySet<CommunicationWebSocket>();
	
	@OnOpen
	public void start(Session session){
		this.session = session;
		System.out.println("opened");
	}
	
	@OnClose
	public void end(){
		System.out.println(this.userAccount +"断开连接");
		if(userAccount != 0){
			connections.remove(this);
		}
	}
	
	@OnMessage
	public void inComing(String message){
		System.out.println(message);
		String[] block = message.split("_");
        switch (block[0]) {
            case "Record":
            	broadcastStringContent(userAccount,Integer.parseInt(block[1]),block[3],block[6]);
            	break;
            case "Start":
            	userAccount = Integer.parseInt(block[1]);
            	connections.add(this);
            	break;
            case "CheckRecord":
            	broadcastHistoryRecord(userAccount, Integer.parseInt(block[1]), Integer.parseInt(block[2]));
            	break;
            case "CheckGroup":
            	bradcastGroups(userAccount);
            	break;
        }
	}

	
	@OnError
	public void onError(Throwable t) throws Throwable{
		System.out.println(t.toString());
	}
	
	private static void bradcastGroups(int account) {
		for(CommunicationWebSocket user :connections){
			if(user.userAccount == account){
				ArrayList<Groups> groups = getGroupsofUser(account);
				for(Groups g : groups){
					synchronized(user){
					try {
						user.session.getBasicRemote().sendText("ExistGroup_"+g.groupid+"_"
								+g.userAccount+"_"+g.name+"_"+g.iconVersion);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				}
				
			}
		}
	}
	private static void broadcastHistoryRecord(int account,int groupId,int recordNum) {
		for(CommunicationWebSocket user :connections){
			if(user.userAccount == account){
				ArrayList<Records> records = getHistoryRecords(account, groupId, recordNum);
				for(Records re : records){
				synchronized(user){
					
						try {
						user.session.getBasicRemote().sendText("Record_"+re.groupId+"_"
								+re.userAccount+"_"+re.recordNum+"_"+re.time+"_"+re.isLogState+"_"+re.isImgState+"_"+re.content);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					}
					
				}
			}
		}
	}
	private static void broadcastStringContent(int account,int groupId,String time,String content){
		ArrayList userInGroup = getUserInGroup(groupId);
		int recordNum = getRecordNum(groupId);
		if(saveStringRecord(account, groupId, recordNum, time, content)){
			for(CommunicationWebSocket user :connections){
			if(user.userAccount != account && userInGroup.contains(account)){
				if(recordNum != 0){
					try{
				synchronized(user){
					user.session.getBasicRemote().sendText("Record_"+groupId+"_"
							+account+"_"+recordNum+"_"+time+"_"+false+"_"+false+"_"+content);
				
				}
			}catch(IOException e){
				connections.remove(user);
				try{
					user.session.close();
					
				}catch(IOException e1){
					
				}
			}
				}else{
					System.out.println("数据库读取出错");
				}
				
			}
			
		}
		}
		
	}			
	
	
	private static ArrayList<Records> getHistoryRecords(int account,int groupId,int recordNum) {
		ArrayList<Records> records = new ArrayList<Records>();
		try {
			Class.forName("com.mysql.jdbc.Driver");
			
		} catch (Exception e) {
			System.out.println("数据库加载失败");
			e.printStackTrace();
			// TODO: handle exception
		}
		try{
			Connection connection = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/account","root","654714845");
			
			
			Statement stmt = connection.createStatement();
		    ResultSet rs = stmt.executeQuery("select * from records where groupId = "+groupId
		    		+" order by recordNum Desc");
			while(rs.next()){
				if(recordNum < rs.getInt("recordNum")){
					Records records2 = new Records(rs.getInt("groupId"),
							rs.getInt("userAccount"),
							rs.getInt("recordNum"),
							rs.getString("time"),
							rs.getString("content"),
							(rs.getInt("isLogState")==1)?true:false,
							(rs.getInt("isImgState")==1)?true:false);
					records.add(records2);
				}else{
					break;
				}
			}
			
		}catch(SQLException e){
			e.printStackTrace();
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return records;
	}
	
	private static ArrayList getUserInGroup(int groupId){
		ArrayList usersInGroup = new ArrayList();
		try {
			Class.forName("com.mysql.jdbc.Driver");
			
		} catch (Exception e) {
			System.out.println("数据库加载失败");
			e.printStackTrace();
			// TODO: handle exception
		}
		try{
			Connection connection = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/account","root","654714845");
			
			
			Statement stmt = connection.createStatement();
		    ResultSet rs = stmt.executeQuery("select * from groups where groupId = "+groupId+"");
			while(rs.next()){
				usersInGroup.add(rs.getInt("userAccount"));
			}
			
		}catch(SQLException e){
			e.printStackTrace();
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return usersInGroup;
	}
	private static int getRecordNum(int groupId) {
		int num = 0;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			
		} catch (Exception e) {
			System.out.println("数据库加载失败");
			e.printStackTrace();
			// TODO: handle exception
		}
		try{
			Connection connection = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/account","root","654714845");
			
			
			Statement stmt = connection.createStatement();
		    ResultSet rs = stmt.executeQuery("select * from records where groupId = "+groupId+" order by recordNum DESC");
			if (rs.first()) {
				num = rs.getInt("recordNum")+1;
			}else {
				num = 1;
			}
			
		}catch(SQLException e){
			e.printStackTrace();
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return num;
		
	}
	private static ArrayList<Groups> getGroupsofUser(int account) {
		ArrayList<Groups> groups = new ArrayList<Groups>();
		try {
			Class.forName("com.mysql.jdbc.Driver");
			
		} catch (Exception e) {
			System.out.println("数据库加载失败");
			e.printStackTrace();
			// TODO: handle exception
		}
		try{
			Connection connection = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/account","root","654714845");
			
			
			Statement stmt = connection.createStatement();
		    ResultSet rs = stmt.executeQuery("select * from groups where userAccount = "+account+"");
			while(rs.next()){
				groups.add(new Groups(rs.getInt("groupId"),
						rs.getInt("userAccount")
						, rs.getString("name"),
						rs.getInt("iconVersion")));
			}
			
		}catch(SQLException e){
			e.printStackTrace();
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return groups;
	}
	private static boolean saveStringRecord(int account,int groupId,int recordNum,String time,String content) {
		int i = 0;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			
		} catch (Exception e) {
			System.out.println("数据库加载失败");
			e.printStackTrace();
			// TODO: handle exception
		}
		try{
			Connection connection = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/account","root","654714845");
			
			
			String sql = "insert into records (userAccount,groupId,recordNum,time,content,isLogState,isImgState) "
					+ "values(?,?,?,?,?,?,?)";
		    PreparedStatement pstmt;
		    try {
		        pstmt = (PreparedStatement) connection.prepareStatement(sql);
		        pstmt.setInt(1, account);
		        pstmt.setInt(2, groupId);
		        pstmt.setInt(3, recordNum);
		        pstmt.setString(4, time);
		        pstmt.setString(5, content);
		        pstmt.setInt(6, 0);
		        pstmt.setInt(7, 0);
		        
		        
		        i = pstmt.executeUpdate();
		        System.out.println(i);
		        
		        pstmt.close();
		        connection.close();
		    }catch (SQLException e){
		    	e.printStackTrace();
		    }
			
		}catch(SQLException e){
			e.printStackTrace();
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return (i==1)?true:false;
	}
	
}
