package fight;

import javax.swing.*;

import fight.FightFrame.Help;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.*;
import java.util.*;

public class OnlineFrame extends FightFrame implements ActionListener,Runnable {    //判断当前用户机的玩家编号
	//player1为本地玩家
	int crystal[]={0,0};
	PlayOnline pane=null;
	String oppoIP=null;
	private int oppoPort=0;
	JButton start=new JButton("开始");
	JButton state=new JButton("暂停");
	JButton button=new JButton("返回");
	private  Help text=new Help();
	String localname=null;
	int localcrystal=0;
	JLabel wait=new JLabel("等待开始……");
	AllIP receiveOppo=null;
	Thread getOppo=null;
	boolean haveOppo=false;
	private Select select=null;
	
	public OnlineFrame(int Acrystal,String name,Select select)
	{
		crystal[0]=Acrystal;
		localname=name;
		localcrystal=Acrystal;
		this.name1=name;
		this.select=select;
		this.setTitle("贪吃蛇——联机对战模式（"+name+"）");
		this.setSize(1050,700);
		int width=Toolkit.getDefaultToolkit().getScreenSize().width;
		this.setLocation((width-1050)/2, 0);
		this.setResizable(false);
		container=this.getContentPane();
		container.setLayout(null);
		bgp=new BackgroundPane();
		bgp.setBounds(0,0,1050,700);
		container.add(bgp);
		start.setBounds(980,540,70,40);
		start.addActionListener(this);
		state.setBounds(980,480,70,40);
		state.addActionListener(this);
		button.setBounds(950,600,70,40);
		button.addActionListener(this);
		bgp.setLayout(null);
		bgp.add(button);
		bgp.add(start);
		bgp.add(state);
		bgp.add(text);
		this.setFocusable(true);
		this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
	    pane=new PlayOnline(null,true,this);  
		this.getLayeredPane().add(pane,JLayeredPane.MODAL_LAYER);
		player1=new Progress(null,true,1,this,name1);
		player2=new Progress(null,true,2,this,"");
		player1.setLocation(30,0);
		player2.setLocation(600, 0);
		this.getLayeredPane().add(player1,JLayeredPane.PALETTE_LAYER);
		this.getLayeredPane().add(player2,JLayeredPane.PALETTE_LAYER);
		title1=new JLabel(new ImageIcon(Toolkit.getDefaultToolkit().getImage("img/title1.gif")));
		title1.setBounds(5, 150, 80, 160);
		this.getLayeredPane().add(title1,JLayeredPane.PALETTE_LAYER);
		title2=new JLabel(new ImageIcon(Toolkit.getDefaultToolkit().getImage("img/title2.gif")));
		title2.setBounds(935, 150, 80, 160);
		this.getLayeredPane().add(title2,JLayeredPane.PALETTE_LAYER);
		title1.setVisible(false);
		title2.setVisible(false);
		wait.setSize(200, 100);
		wait.setFont(new Font("华文行楷",Font.BOLD,24));
		wait.setOpaque(false);
		wait.setLocation((this.getWidth()-200)/2,(this.getHeight()-100)/2);
		this.getLayeredPane().add(wait,JLayeredPane.POPUP_LAYER);
		wait.setVisible(false);
		this.setVisible(true);
		oppoIP=JOptionPane.showInputDialog(this,"如果知道对方IP，请输入对方IP:","Snake say:",JOptionPane.INFORMATION_MESSAGE);
		oppoPort=Integer.parseInt(JOptionPane.showInputDialog(this,"请输入对方端口号:","Snake say:",JOptionPane.INFORMATION_MESSAGE));
		if(oppoIP==null)
		{
			getOppo=new Thread(this);
			getOppo.start();
			wait.setText("等待连接……");
			wait.setVisible(true);
			ready();
		}
		this.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e)
			{
				pane.send("quit|");
				isBack=true;
				back();
			}
		});
		
	}
	
	void ready()
	{
		if(this.oppoIP==null&&!haveOppo)
		{
			receiveOppo=new AllIP();     
			receiveOppo.PingAll();     //获取局域网所有IP
			init();
		}
	}
	
	void init()
	{
		DatagramSocket socket=null;
		try{
			socket=new DatagramSocket();
			byte buffer[]=new String("ask to play with me").getBytes();
			Loop:while(true)
			{
				for(int i=0;i<receiveOppo.getPing().size();i++)
				{
					if(!haveOppo)
					{
						InetAddress host=InetAddress.getByName(receiveOppo.getPing().get(i));
						DatagramPacket packet=new DatagramPacket(buffer,buffer.length,host,3003);
						socket.send(packet);
					}
					else
						break Loop;
				}
			}
		}catch(IOException e){
			System.out.println(e.toString());
		}finally{
			if(socket!=null)
				socket.close();
		}
			
	}
	
	public void run()
	{
		try{
			
			DatagramSocket receive=new DatagramSocket(3004);
			byte data[]=new byte[100];
			DatagramPacket request=new DatagramPacket(data,data.length);
			Date before=null,stop=null;
			while(!haveOppo)
			{
				if(before!=null)
				{
					stop=new Date(System.currentTimeMillis());
					if(stop.getTime()-before.getTime()>560)
						this.oppoIP=null;
				}	
				receive.receive(request);
				String sdata=new String(data);
				if(sdata.equals("ask to play with me")&&oppoIP==null)
				{
					this.oppoIP=request.getAddress().toString();
					this.oppoPort=request.getPort();
					DatagramSocket send=new DatagramSocket();
					byte buffer[]=new String("agree").getBytes();
					DatagramPacket agree=new DatagramPacket(buffer,buffer.length,InetAddress.getByName(oppoIP),oppoPort);
					send.send(agree);
					send.close();
					before=new Date(System.currentTimeMillis());
				}
				else if(oppoIP!=null&&sdata.equals("test"))
				{
					haveOppo=true;
					receive.close();
					break;
				}
				else if(oppoIP==null&&sdata.equals("agree"))
				{
					this.oppoIP=request.getAddress().toString();
					this.oppoPort=request.getPort();
					DatagramSocket send=new DatagramSocket();
					byte buffer[]=new String("test").getBytes();
					DatagramPacket test=new DatagramPacket(buffer,buffer.length,InetAddress.getByName(oppoIP),oppoPort);
					send.send(test);
					send.close();
					haveOppo=true;
					receive.close();
					break;
				}
			}
			wait.setVisible(false);
			wait.setText("等待开始……");
			JOptionPane.showMessageDialog(this, "连接成功");
		}catch(IOException e){
			System.out.println(e.toString());
		}
		getOppo.stop();
	}
	
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource()==button)
		{
			pane.send("quit|");
			isBack=true;
			back();
		}
		else if(e.getSource()==start)
		{
			if(pane.state==0)
			{
				int receiveport,remoteport=oppoPort;
				if(remoteport==3000)
					receiveport=3001;
				else
					receiveport=3000;
				pane.startjoin(oppoIP,remoteport,receiveport);
					
			}
		}
		else if(e.getSource()==state)
		{
			if(pane.state==1)  //置为暂停状态
			{
				pane.send("pause|");
				pane.state=2;
				state.setText("继续");	
			}
			else if(pane.state==2)
			{
				pane.send("continue|");
				pane.state=1;
				state.setText("暂停");
			}
		}
	}
	
	void recordFirst(String name,int Acrystal)    //获得对方信息,此时这里是玩家二客户端
	{
		this.name2=this.name1;
		this.name1=name;
		crystal[1]=crystal[0];
		crystal[0]=Acrystal;
		player1.update(name);
		player2.update(name2);
	}
	
	void recordSecond(String name,int Bcrystal)    //获得对方信息,此时这里是玩家一客户端
	{
		this.name2=name;
		crystal[1]=Bcrystal;
		player2.update(name);
	}
	
	void back()    //返回选择模式
	{
		if(isBack)
		{
			select.update();
			//将自己money的信息变化写回数据库
		}
	}
	
	 void regame()    //重新游戏
		{
			//游戏模块
			pane.isOver=false;
			pane.isBegin=false;
			pane.state=0;
			pane.lose=0;
			pane.score[0]=0;
			pane.score[1]=0;
			this.name1=localname;
			this.name2="";
			this.crystal[0]=localcrystal;
			this.crystal[1]=0;
			player1.reset(localname, localcrystal);
			player2.reset(name2, crystal[1]);
			pane.initmap();
			pane.repaint();
			//计时模块
			
		}

	 
	 static class Help extends JLabel
	 {
		 Help()
		 {
			 super();
			 this.setOpaque(false);
			 this.setBounds(90,600,845,80);
		 }

		 public void paintComponent(Graphics g)
		 {
			 super.paintComponent(g);
			 Graphics2D g2=(Graphics2D)g;
			 Stroke stroke=new BasicStroke(5.0f,BasicStroke.CAP_BUTT,BasicStroke.JOIN_BEVEL,0,new float[]{10,5},0);
			 g2.setStroke(stroke);
			 g2.setColor(Color.ORANGE);
			 g2.drawRect(10, 10, 830, 50);
			 g.setColor(Color.BLACK);
			 g.setFont(new Font("迷你简方隶",Font.BOLD,17));
			 g.drawString("使用水晶可延迟对方动作5秒；玩家一用shift键，玩家二用Q键；", 35, 30);
			 g.drawString("玩家一方向键为键盘方向键，玩家二方向键为W/A/S/D键；绿蛇为玩家一，蓝蛇为玩家二", 35, 45);
		 }
	 }

	/*public static void main(String args[])
	{
		new OnlineFrame(1,"haha");
		new OnlineFrame(2,"li");
	}*/


}
