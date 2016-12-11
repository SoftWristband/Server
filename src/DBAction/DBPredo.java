package DBAction;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.tomcat.jdbc.pool.DataSource;

public class DBPredo
{
	private Context Ctx;
	private DataSource ds;
    private Connection connection = null;
    public  PreparedStatement preStatement = null;
    private String SQL;
	public DBPredo(String sql)
	{
		SQL=sql;	
	}
	public void close()
	{
		try
		{
			if(preStatement!=null)
			    preStatement.close();
			if(connection!=null)
				connection.close();
		} catch (SQLException e)
		{
			e.printStackTrace();
		}	
	}
	public boolean getPreStatement()
	{
		try
		{
			Ctx = new InitialContext();
			ds = (DataSource) Ctx.lookup("java:comp/env/SoftwristBandConnectionPool");
			//Class.forName("com.mysql.jdbc.Driver");
			
			//connection = DriverManager.getConnection("jdbc:mysql://localhost/softwristband", "root", "xiaofei");
			//String sql = "insert into article values(?,?,?,null,?)";// null必不可少，article表插入数据
			connection =ds.getConnection();
			preStatement = connection.prepareStatement(SQL);
		} catch (SQLException e)
		{
			System.out.println("pre错误2");
			e.printStackTrace();
			return false;
		} catch (NamingException e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
