package com.example;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.bean.User;
import com.example.utils.DBUtil;
import com.google.gson.Gson;

/**
 * Servlet implementation class CheckGroup
 */

public class CheckGroup extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CheckGroup() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/plain");

		String action = request.getParameter("action");
		try {
			if ("checkGroupIcon".equals(action)) {
				checkIcon(request, response);
			} else if ("getUsers".equals(action)) {
				getUsers(request, response);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void getUsers(HttpServletRequest request, HttpServletResponse response) throws SQLException {
		PrintWriter out = null;
		try {
			out = response.getWriter();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		String groupId = request.getParameter("groupId");
		int id = 0;
		if (groupId != null && groupId != "") {
			id = Integer.parseInt(groupId);
		}
		if (id != 0) {
			Connection connection = DBUtil.getConn();
			PreparedStatement ps = connection.prepareStatement(
					"select distinct b.* from groups a,user b" + " where a.groupId=? and a.userAccount=b.account ");
			ps.setInt(1, id);

			ResultSet rs = ps.executeQuery();
			List<User> users = new ArrayList<>();
			while (rs.next()) {
				User u = new User();
				u.setName(rs.getString("name"));
				u.setAccount(rs.getInt("userAccount"));
				users.add(u);
			}
			Gson gson = new Gson();
			String s = gson.toJson(users);
			out.println(s);
			rs.close();
			connection.close();
		}
	}

	private void checkIcon(HttpServletRequest request, HttpServletResponse response) {
		int groupId = Integer.parseInt(request.getParameter("groupId"));
		PrintWriter out = null;
		try {
			out = response.getWriter();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			Class.forName("com.mysql.jdbc.Driver");
			// 加载MYSQL JDBC驱动程序
			// Class.forName("org.gjt.mm.mysql.Driver");
			System.out.println("Success loading Mysql Driver!");
		} catch (Exception e) {
			System.out.print("Error loading Mysql Driver!");
			e.printStackTrace();
		}
		try {
			Connection connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/account?useSSL=false", "root",
					"654714845");
			// 连接URL为 jdbc:mysql//服务器地址/数据库名 ，后面的2个参数分别是登陆用户名和密码

			System.out.println("Success connect Mysql server!");
			PreparedStatement ps = connect.prepareStatement(" select * from group where groupId=? ");
			ps.setInt(1, groupId);
			ResultSet rs = ps.executeQuery();
			// group 为表的名称
			int version = -1;

			if (rs.next()) {
				version = rs.getInt("iconVersion");
			}
			System.out.println(version);
			if (version == -1) {
				out.println("FAIL_0");
			} else {
				out.println("TRUE_" + version + "_h");
			}
			rs.close();
			connect.close();
		} catch (Exception e) {
			System.out.print("get data error!");
			e.printStackTrace();
		}
	}
}
