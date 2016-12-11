package servlet;
import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import DBAction.DBPredo;

@WebServlet("/Login1")
public class Login extends HttpServlet
{

	private static final long serialVersionUID = 1L;	
	public Login()
	{
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		doPost(request,response);
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		response.setContentType("text/html;charset=utf-8");
		request.setCharacterEncoding("utf-8");
		
		String sql= "insert into article values(?,?,?,null,?)";
		DBPredo pre = new DBPredo(sql);
		if(pre.getPreStatement()==true)
		{
			System.out.println("连接成功");
		}
		else
		{
			System.out.println("连接失败");
		}
		try
		{
			pre.preStatement.setInt(1, 4);
			pre.preStatement.setString(2, "现磨闹钟11111");
			pre.preStatement.setString(3, "text/arti1_naozhong");
			pre.preStatement.setString(4, "res/img/arti1.jpg");
			int i=pre.preStatement.executeUpdate();
			if(i==1)
				response.getWriter().append("OK");
			else
				response.getWriter().append("fail");
			System.out.println(i);
		
		} catch (SQLException e)
		{
			System.out.println("错误");
			e.printStackTrace();
			pre.close();
		}
		pre.close();
		
		String s="{\"id\": 1,\"text\": \"系统管理\",\"children\": [{\"id\": 11,\"text\": \"角色\"},{\"id\": 12,\"text\": \"系统公告\" }]}";
		JSONObject json = new JSONObject(s);
		System.out.println(json.getString("text"));

	}

}
