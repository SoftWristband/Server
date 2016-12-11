package DBAction;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBQuery
{
	private Connection connection;
	private Statement statement;
	public ResultSet resultSet;

	public DBQuery(String query)
	{
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost/softwristband","root","admin");
			statement = connection.createStatement();
			resultSet=statement.executeQuery(query);
			
		} catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		} catch (SQLException e)
		{
			e.printStackTrace();
		}	
	}
	public void close()
	{
			try
			{
				if(resultSet!=null)
					resultSet.close();
				if(statement!=null)
					statement.close();
				if(connection!=null)
					connection.close();
			} catch (SQLException e)
			{
				e.printStackTrace();
			}
		
	}
}
