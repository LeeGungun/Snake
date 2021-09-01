package main;

import javax.swing.*;
import java.awt.*;
import javax.sound.sampled.*;
import java.io.*;
import java.awt.event.*;
import java.sql.*;
import java.util.*;
import pass.*;
import challenge.*;
import fight.*;

public class Control extends JFrame implements ActionListener{
	
	private BackgroundImg bg;
	public Sound sound=new Sound();
	JLabel username=new  JLabel("游戏账户:");
	JLabel password=new JLabel("密码:");
	JButton register=new JButton("注册");
	JButton login=new JButton("登录");
	JTextField name=new JTextField();
	JPasswordField pwd=new JPasswordField();
	JButton pass=new JButton();
	JButton fight=new JButton();
	JButton challenge=new JButton();
	public String framework = "embedded";
	public String driver = "org.apache.derby.jdbc.EmbeddedDriver";
    public String protocol = "jdbc:derby:";
    public String dbname="snakedb";
    public Vector<Integer> usercrystal=new Vector<Integer>();
    public Vector<Integer> userlevel=new Vector<Integer>();
    public Vector<String> user=new Vector<String>();
    public int model=0;     //1表示关卡，2表示对战，3表示挑战
    public pass.Select passmodel=null;
    public fight.Select fightmodel=null;
    public CFrame challengemodel=null;
	    
	public Control()
	{
		this.setTitle("Lee's 贪吃蛇");
		bg=new BackgroundImg();
		this.setLayout(null);
		this.getContentPane().add(bg);
		this.setBounds(350, 100, 650, 452);
		sound.soundLoad();
		sound.play();
		username.setOpaque(false);
		username.setForeground(Color.RED);
		username.setFont(new Font("华文行楷",Font.BOLD,17));
		username.setBounds(20, 20, 80, 40);
		bg.add(username);
		password.setOpaque(false);
		password.setForeground(Color.RED);
		password.setFont(new Font("华文行楷",Font.BOLD,17));
		password.setBounds(20, 70, 80, 40);
		bg.add(password);
		name.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				pwd.requestFocusInWindow();
			}
		});
		pwd.setEchoChar('*');
		name.setBounds(105, 20, 120, 40);
		pwd.setBounds(105, 70, 120, 40);
		bg.add(name);
		bg.add(pwd);
		register.setBounds(20,120 , 60, 30);
		bg.add(register);
		login.setBounds(20, 160, 60, 30);
		bg.add(login);
		register.addActionListener(this);
		login.addActionListener(this);
		pass.setIcon(new ImageIcon("img/snake1.gif"));
		pass.setRolloverIcon(new ImageIcon("img/snake1-o.gif"));
		pass.setContentAreaFilled(false);
		pass.setFocusable(false);
		pass.setBorderPainted(false);
		pass.addActionListener(this);
		pass.setBounds(360, 20, 100, 70);
		bg.add(pass);
		fight.setIcon(new ImageIcon("img/snake3.gif"));
		fight.setRolloverIcon(new ImageIcon("img/snake3-o.gif"));
		fight.setContentAreaFilled(false);
		fight.setFocusable(false);
		fight.setBorderPainted(false);
		fight.addActionListener(this);
		fight.setBounds(480, 20, 100, 70);
		bg.add(fight);
		challenge.setIcon(new ImageIcon("img/snake2.gif"));
		challenge.setRolloverIcon(new ImageIcon("img/snake2-o.gif"));
		challenge.setContentAreaFilled(false);
		challenge.setFocusable(false);
		challenge.setBorderPainted(false);
		challenge.addActionListener(this);
		challenge.setBounds(450, 110, 100, 70);
		bg.add(challenge);
		this.setFocusable(true);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e)
			{
				System.exit(0);
			}
		});
	}
	
	public void actionPerformed(ActionEvent e)      
	{
		if(e.getSource()==register)
		{
			goinsert();
		}
		else if(e.getSource()==login)
		{
			goselect();
		}
		else if(e.getSource()==pass)
		{
			if(user.size()==0)
				JOptionPane.showMessageDialog(this, "请登录");
			else
			{
				model=1;
				this.setVisible(false);
				passmodel=new pass.Select(this.userlevel.elementAt(0),this.usercrystal.elementAt(0),this);
			}
		}
		else if(e.getSource()==fight)
		{
			if(user.size()==0)
				JOptionPane.showMessageDialog(this, "请登录");
			else
			{
				model=2;
				this.setVisible(false);
				System.out.println("测试-踪");
				fightmodel=new fight.Select(this);
			}
		}
		else if(e.getSource()==challenge)
		{
			model=3;
			this.setVisible(false);
			challengemodel=new CFrame(this);
		}
	}
	
	class BackgroundImg extends JPanel
	{
		Image bgimg=Toolkit.getDefaultToolkit().getImage("img/Snake.gif");
		
		BackgroundImg()
		{
			this.setBounds(0, 0, 650, 452);
			this.setLayout(null);
		}
		
		public void paintComponent(Graphics g)
		{
			super.paintComponent(g);
			g.drawImage(bgimg, 0, 0, 650, 452, this);
		}
	}
	
	class Sound 
	{
		File file;
		AudioInputStream stream;
		AudioFormat format;
		DataLine.Info info;
		Clip clip;
		
		Sound()
		{
			file=new File("sound/bgm.wav");
		}
		
		public void soundLoad()
		{
			
			try{
				stream=AudioSystem.getAudioInputStream(file);
			}catch(Exception e){
				System.out.println(e.toString());
			}
			format=stream.getFormat();
		}
		
		public void play()
		{
			info=new DataLine.Info(Clip.class, format);
			try{
				clip=(Clip)AudioSystem.getLine(info);
			}catch(LineUnavailableException e){}
			try{
				clip.open(stream);
			}catch(Exception e){System.out.println(e.toString());}
			clip.loop(-1);
		}
		
		public void stop()
		{
			clip.stop();
		}
	}
	
	void goinsert()
	{
		Connection conn = null;
		try{
			Class.forName(driver).newInstance();
			conn=DriverManager.getConnection(protocol+dbname);     
			if(name.getText().length()>20||pwd.getPassword().toString().length()>20)
				JOptionPane.showMessageDialog(this, "用户名与密码长度均不能大于20！请重新输入");
			else 
			{
				if(name.getText().equals("")||pwd.getPassword().toString().equals(""))
					JOptionPane.showMessageDialog(this, "用户名与密码长度均不能为空！请输入");
				else
				{
					ResultSet result=conn.createStatement().executeQuery("select count(name) from snakeuser where "+"name='"+name.getText()+"'");
					result.next();
					if(result.getInt(1)!=0)
					{
						JOptionPane.showMessageDialog(this, "用户名已注册，请重新注册或登录");
						conn.close();
						result.close();
					}
					else
					{
						result.close();
						ResultSet re=conn.createStatement().executeQuery("select count(*) from snakeuser ");
						re.next();
						int i=re.getInt(1)+1;
						re.close();
						//System.out.println(i);
						String s=String.valueOf(pwd.getPassword());
						PreparedStatement ps=conn.prepareStatement("insert into snakeuser values("+i+",'"+name.getText()+"','"+s+"',0,1)");
						ps.execute();
						ps.close();
						conn.close();
						JOptionPane.showMessageDialog(this, "注册成功,请您去登录");
					}
				}
			}	
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	void goselect()
	{
		Connection conn=null;
		try{
			Class.forName(driver).newInstance();
			conn=DriverManager.getConnection(protocol+dbname);
			if(name.getText().length()>20||pwd.getPassword().toString().length()>20)
				JOptionPane.showMessageDialog(this, "用户名与密码长度均不能大于20！请重新输入");
			else 
			{
				if(name.getText().equals("")||pwd.getPassword().toString().equals(""))
					JOptionPane.showMessageDialog(this, "用户名与密码长度均不能为空！请输入");
				else
				{
					ResultSet result=conn.createStatement().executeQuery("select count(name) from snakeuser where "+"name='"+name.getText()+"'");
					result.next();
					if(result.getInt(1)==0)
					{
						JOptionPane.showMessageDialog(this, "用户名不存在，请注册或重新输入");
						conn.close();
						result.close();
					}
					else
					{
						System.out.println(result.getInt(1));
						result.close();
						ResultSet check=conn.createStatement().executeQuery("select password from snakeuser where "+"name='"+name.getText()+"'");
						String s=String.valueOf(pwd.getPassword());
						check.next();
						if(check.getString(1).equals(s))
						{
							ResultSet rs=conn.createStatement().executeQuery("select crystal,level from snakeuser where "+"name='"+name.getText()+"'");
							user.addElement(name.getText());
							rs.next();
							usercrystal.addElement(new Integer(rs.getInt(1)));
							userlevel.addElement(new Integer(rs.getInt(2)));
							rs.close();
							JOptionPane.showMessageDialog(this, "成功登录！");
						}
						else
							JOptionPane.showMessageDialog(this,"密码输入错误，请重新输入！");
						check.close();
						conn.close();
					}
				}
			}	
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	public void update(int crystal,int level,String name)
	{
		try{
			Class.forName(driver).newInstance();
			Connection conn;
			conn=DriverManager.getConnection(this.protocol+this.dbname);
			PreparedStatement ps=conn.prepareStatement("update snakeuser set crystal="+crystal+",level="+level+" where name='"+name+"'");
			ps.execute();
			ps.close();
			conn.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void back()
	{
		switch(model)
		{
		case 1:
		{
			update(this.usercrystal.elementAt(0),this.userlevel.elementAt(0),this.user.elementAt(0));
			passmodel=null;    
			this.setVisible(true);
		}break;
		case 2:
		{
			fightmodel.dispose();
			fightmodel=null;
			this.setVisible(true);
		}break;
		case 3:
		{
			challengemodel.dispose();
			challengemodel=null;
			this.setVisible(true);
		}break;
		}
		if(user.size()>0)
		{
			user.removeAllElements();
			usercrystal.removeAllElements();
			userlevel.removeAllElements();
		}
		System.out.println("信息已清空");
		
	}
	
	public static void main(String args[])
	{
		new Control();
	}


}
