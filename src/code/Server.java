package code;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import com.mysql.jdbc.ResultSet;
import code.NlpirTest.CLibrary;
import code.SentimentAnalysis;

public class Server
{
	public Server()
	{
		int clientNo=0;
		System.out.println("begin");
		ServerSocket serverSocket;
		try
		{
			serverSocket = new ServerSocket(2048);
			while(true)
			{
				Socket socket = serverSocket.accept();
				HandleClient task = new HandleClient(socket);
				new Thread(task).start();
				clientNo++;
			}
		}catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
class HandleClient implements Runnable
{
	private final static int LOGIN=1;   //登录请求
	private final static int REGISTER=2;   //注册请求
	private final static int SEGMENT=3;  //分词请求
	private final static int EMOTION=4;  //返回情感分析结果请求
	private Socket socket;
	private int flag;
	private ObjectInputStream fromClient;
	private ObjectOutputStream toClient;
	private DataInputStream dataFromClient;
	private DataOutputStream dataToClient;
	private String[] s=new String[100];
	
	public HandleClient(Socket socket)
	{
		this.socket=socket;
	}
	public void run()
	{
		try
		{
			dataFromClient=new DataInputStream(socket.getInputStream());
			dataToClient=new DataOutputStream(socket.getOutputStream());
			flag=dataFromClient.readInt();
			switch(flag)
			{
				case LOGIN:
					login();
				break;
				case REGISTER:
					register();
				break;
				case SEGMENT:
					segment(s);
				break;
				case EMOTION:
					emotion();
				break;
			}
		} catch (IOException e)
		{
			e.printStackTrace();;
		}
	}
	//情感分析请求
	public void segment(String[] s)
	{
		SentimentAnalysis sa=new SentimentAnalysis();
		String content;
		String date;
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
//			System.out.println("driver loaded");
			Connection connection =
					DriverManager.getConnection
					("jdbc:mysql://localhost/softwristband","root","admin");
//			System.out.println("Database connected");
			Statement statement=connection.createStatement();
			
			int len=dataFromClient.readInt();
			
			for(int i=0;i<len;i++)
			{
				content=dataFromClient.readUTF();
				date=dataFromClient.readUTF();
				content=content.substring(1, content.length()-1);
				double score=sa.getEmotion(content);
				statement.executeUpdate
					("insert into content (content,date,score) values('"
					+content+"','"+date+"',"+score+")");
			}
			
		} 
		catch (IOException e1)
		{
			e1.printStackTrace();
		} catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		} catch (SQLException e)
		{
			e.printStackTrace();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	public void login()
	{
		try
		{
			int flag=0;
			System.out.println("hello login");
			String name=dataFromClient.readUTF().toString();
			String pwd=dataFromClient.readUTF().toString();
			System.out.println("name: "+name+" pwd: "+pwd);
			
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection =
					DriverManager.getConnection
					("jdbc:mysql://localhost/softwristband","root","admin");
			Statement statement=connection.createStatement();
			java.sql.ResultSet result=statement.executeQuery("select * from user");
			while(result.next())
			{
				if(name.toString().equals(result.getString(2))&&
						pwd.toString().equals(result.getString(3)))
				{
					System.out.println("登录成功");
					dataToClient.writeInt(1);
					dataToClient.flush();
					flag=1;
					break;
				}
			}
			if(flag==0)
			{
				System.out.println("登录失败");
				dataToClient.writeInt(0);
				System.out.println("登录失败2");
				dataToClient.flush();
			}
			connection.close();
			dataToClient.close();
			dataToClient.close();
			socket.close();				
		} catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		} catch (SQLException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		
	}
	public void register()
	{
		int flag=0;
		String name;
		String pwd;
		try
		{
			name = dataFromClient.readUTF().toString();
		    pwd=dataFromClient.readUTF().toString();
		    System.out.println(name+" what "+pwd);
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("driver loaded");
			Connection connection = 
					DriverManager.getConnection
					("jdbc:mysql://localhost/softwristband","root","admin");
			System.out.println("Database connected");
			Statement statement = connection.createStatement();
			java.sql.ResultSet result=statement.executeQuery
					("select name from user");
			while(result.next())
			{
				if(result.getString(1).equals(name))
				{
					flag=1;
					break;
				}
					
				System.out.println(result.getString(1));
			}
			if(flag==0)
			{
				System.out.println("注册成功");
				statement.executeUpdate
					("insert into user (name,password) values(\'"+name
					+"\',\'"+pwd+"\')");
				dataToClient.writeInt(1);
				dataToClient.flush();
			}
			else
			{
				System.out.println("注册失败");
				dataToClient.writeInt(0);
				System.out.println("0000000000");
				dataToClient.flush();
			}
			connection.close();
			dataToClient.close();
			dataToClient.close();
			socket.close();
			
			
		} 
		catch (IOException e1)
		{
			e1.printStackTrace();
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		} 
		catch (SQLException e)
		{
			e.printStackTrace();
		} 	
	}
	public void emotion()
	{
		
	}
}
