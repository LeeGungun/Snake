package fight;

import gameitem.Apple;
import gameitem.Bomb;
import gameitem.Bread;
import gameitem.Water;

import javax.swing.*;

import java.awt.*;
import java.util.*;
import java.awt.event.*;

import java.net.*;
import java.io.*;

public class PlayOnline extends Play implements Runnable{
	
	OnlineFrame fr;
	int sendport;
	int receiveport;
	boolean isfirst=false;   //记录此时玩家是否是玩家1
	boolean havainititem=false;
	boolean isBegin=false;
	JLabel message=null;
 

	PlayOnline(LayoutManager lm,boolean isDoubleBuffered,OnlineFrame fr)
	{
		super(lm,isDoubleBuffered);
		this.fr=fr;
		this.setBounds(90,150,840,440);
		this.setFocusable(true);
		this.setVisible(true);
		initmap();
		setFocusable(true);
		this.setOpaque(false);
        Toolkit.getDefaultToolkit().addAWTEventListener(new MyEvent(),AWTEvent.KEY_EVENT_MASK);
	}
	
	class MyEvent implements AWTEventListener      //全局事件
	{
		public void eventDispatched(AWTEvent e)
		{
			int key=((KeyEvent)e).getKeyCode();
			switch(key)
			{
				case KeyEvent.VK_UP:
					if(isfirst&&state==1&&(snake[0].head.getD()==2||snake[0].head.getD()==3))
					{
						snake[0].movetoturn(0);
						send("turn|"+Integer.toString(1)+"|"+Integer.toString(0)+"|");
					}
					break;
				case KeyEvent.VK_DOWN:
					if(isfirst&&state==1&&(snake[0].head.getD()==2||snake[0].head.getD()==3))
					{
						snake[0].movetoturn(1);
						send("turn|"+Integer.toString(1)+"|"+Integer.toString(1)+"|");
					}
					break;
				case KeyEvent.VK_LEFT:
					if(isfirst&&state==1&&(snake[0].head.getD()==0||snake[0].head.getD()==1))
					{
						snake[0].movetoturn(2);
						send("turn|"+Integer.toString(1)+"|"+Integer.toString(2)+"|");
					}
					break;
				case KeyEvent.VK_RIGHT:
					if(isfirst&&state==1&&(snake[0].head.getD()==0||snake[0].head.getD()==1))
					{
						snake[0].movetoturn(3);
						send("turn|"+Integer.toString(1)+"|"+Integer.toString(3)+"|");
					}
					break;
				case KeyEvent.VK_W:         //蛇二的上、下、左、右键分别为W、S、A、D
					if(!isfirst&&state==1&&(snake[1].head.getD()==2||snake[1].head.getD()==3))
					{
						snake[1].movetoturn(0);
						send("turn|"+Integer.toString(2)+"|"+Integer.toString(0)+"|");
					}
					break;
				case KeyEvent.VK_S:
					if(!isfirst&&state==1&&(snake[1].head.getD()==2||snake[1].head.getD()==3))
					{
						snake[1].movetoturn(1);
						send("turn|"+Integer.toString(2)+"|"+Integer.toString(1)+"|");
					}
					break;
				case KeyEvent.VK_A:
					if(!isfirst&&state==1&&(snake[1].head.getD()==0||snake[1].head.getD()==1))
					{
						snake[1].movetoturn(2);
						send("turn|"+Integer.toString(2)+"|"+Integer.toString(2)+"|");
					}
					break;
				case KeyEvent.VK_D:
					if(!isfirst&&state==1&&(snake[1].head.getD()==0||snake[1].head.getD()==1))
					{
						snake[1].movetoturn(3);
						send("turn|"+Integer.toString(2)+"|"+Integer.toString(3)+"|");
					}
					break;
				case KeyEvent.VK_SHIFT:   //玩家一用水晶延迟对方    
				{
					if(isfirst&&sleep[1]==1&&FightFrame.crystal[0]>0)
					{
						fr.localcrystal--;	
						fr.crystal[0]--;
						sleep[1]=3;
						FightFrame.title2.setVisible(true);
						send("slow|"+Integer.toString(1)+"|"+Integer.toString(2)+"|");
						java.util.Timer slow1=new java.util.Timer();
						slow1.schedule(new TimerTask(){
							public void run()
							{
								sleep[1]=1;
								FightFrame.title2.setVisible(false);
							}
						}, 5000);
					}
				}break;
				case KeyEvent.VK_Q:    //玩家二用水晶延迟对方
				{
					if(!isfirst&&sleep[0]==1&&FightFrame.crystal[1]>0)
					{
						fr.localcrystal--;
						fr.crystal[1]--;
						sleep[0]=3;
						FightFrame.title1.setVisible(true);
						send("slow|"+Integer.toString(2)+"|"+Integer.toString(1)+"|");
						java.util.Timer slow2=new java.util.Timer();
						slow2.schedule(new TimerTask(){
							public void run()
							{
								sleep[0]=1;
								FightFrame.title1.setVisible(false);
							}
						}, 5000);
					}
				}break;
			}
			
		}
	}
	
	void updatemap(int num)
	{
		map=new DrawMap(num);
	}
	
	void startjoin(String ip,int remoteport,int receiveport)     //开始连接
	{
		isBegin=true;
		this.sendport=remoteport;
		this.receiveport=receiveport;
		send("join|"+fr.name1+"|"+String.valueOf(fr.crystal[0])+"|");
		this.isfirst=true;  
		fr.wait.setVisible(true);
		//JOptionPane.showMessageDialog(fr, "等待联机");
		main=new Thread(this);
		main.start();
	
	}
	
	void send(String induction)    //发送命令
	{
		DatagramSocket socket=null;
		try{
			socket=new DatagramSocket();    //真正联机时指定地址3000
			byte buffer[]=new String(induction).getBytes();
			InetAddress host=InetAddress.getLocalHost();
			DatagramPacket packet=new DatagramPacket(buffer,buffer.length,host,sendport);
			socket.send(packet);
			System.out.println("发送信息："+induction);
		}catch(IOException e){
			System.out.println(e.toString());
		}finally{
			if(socket!=null)
				socket.close();
		}
	}
	
	int kind(int num)
	{
		if(items.items[num] instanceof Apple)
			return 0;
		else if(items.items[num] instanceof Bread)
			return 1;
		else if(items.items[num] instanceof Bomb)
			return 2;
		return -1;
	}
	
	void initplay()   //初始化游戏
	{
		if(isfirst)
		{
			snake[0]=new Snake(1,this);
			send("snake|"+Integer.toString(snake[0].head.getX())+"|"+Integer.toString(snake[0].head.getY())+"|"+Integer.toString(1)+"|"+Integer.toString(snake[0].head.getD())+"|");
			snake[1]=new Snake(2,this);
			send("snake|"+Integer.toString(snake[1].head.getX())+"|"+Integer.toString(snake[1].head.getY())+"|"+Integer.toString(2)+"|"+Integer.toString(snake[1].head.getD())+"|");
			items=new DrawGameItem(this);
			for(int i=0;i<items.items.length;i++)
				send("gameitem|"+Integer.toString(i)+"|"+Integer.toString(items.items[i].getX())+"|"+Integer.toString(items.items[i].getY())+"|"+Integer.toString(kind(i))+"|");
			send("river|"+Integer.toString(items.getR().elementAt(0).getX())+"|"+Integer.toString(items.getR().elementAt(0).getY())+"|");
			state=1;
			repaint();
			fr.player1.initP();
			fr.player2.initP();
		}
		else
		{
			items=new DrawGameItem(this,0);
		}
		
		
	}
	
	public void run()
	{
		try{
			DatagramSocket socket=new DatagramSocket(receiveport);    
			//socket.connect(InetAddress.getByName(fr.oppoIP), 3000);    //只接收该目标地址信息
			byte data[]=new byte[100];
			DatagramPacket packet=new DatagramPacket(data,data.length);
			while(isBegin)
			{
				socket.receive(packet);
				String receive[]=new String(data).split("\\|");
				synchronized(data){
				if(receive[0].equals("join"))     //收到等待连接,自己此时为玩家2
				{
						fr.wait.setVisible(false);
						this.isfirst=false;
						send("conn|"+fr.name1+"|"+Integer.toString(fr.crystal[0])+"|");
						fr.recordFirst(receive[1],Integer.parseInt(receive[2]));
						JOptionPane.showMessageDialog(fr, "现在您为玩家二");
						initplay();
					
				}
				else if(receive[0].equals("conn"))    //收到和你连接，自己此时为玩家一
				{
						fr.wait.setVisible(false);
						JOptionPane.showMessageDialog(fr, "现在您为玩家一");
						fr.recordSecond(receive[1], Integer.parseInt(receive[2]));
						send("map|"+Integer.toString(random)+"|");
						initplay();
					
					
				}
				else if(receive[0].equals("snake"))     //收到对方初始化的蛇的头节点
				{
					if(Integer.parseInt(receive[3])==1)
						snake[0]=new Snake(new SnakePoint(1,Integer.parseInt(receive[1]),Integer.parseInt(receive[2]),Integer.parseInt(receive[4]),1),this,1);
					else if(Integer.parseInt(receive[3])==2)
						snake[1]=new Snake(new SnakePoint(1,Integer.parseInt(receive[1]),Integer.parseInt(receive[2]),Integer.parseInt(receive[4]),2),this,2);
					
				}
				else if(receive[0].equals("gameitem"))      //收到玩家1生成的食物信息，表示此方为玩家二
				{
					if(!this.havainititem)
					{
						items.createItem(Integer.parseInt(receive[1]), Integer.parseInt(receive[2]), Integer.parseInt(receive[3]), Integer.parseInt(receive[4]));
						
					}
					else
					{
						items.createItem(Integer.parseInt(receive[1]), Integer.parseInt(receive[2]), Integer.parseInt(receive[3]), Integer.parseInt(receive[4]));
						repaint();
					}
				}
				else if(receive[0].equals("river"))     //收到玩家1生成的河流信息，表示此方为玩家二
				{
					if(!this.havainititem)
					{
						items.getR().addElement(new Water(Integer.parseInt(receive[1]),Integer.parseInt(receive[2])));
						this.havainititem=true;
						state=1;
						repaint();
						fr.player1.initP();
						fr.player2.initP();
					}
					else
					{
						items.createWater(Integer.parseInt(receive[1]), Integer.parseInt(receive[2]));
						repaint();
					}
					
				}
				else if(receive[0].equals("turn"))     //收到转向信息
				{
					
					snake[Integer.parseInt(receive[1])-1].movetoturn(Integer.parseInt(receive[2]));
				}
				else if(receive[0].equals("continue"))      //收到继续命令
				{
					state=1;
				}
				else if(receive[0].equals("quit"))     //收到对方退出信息
				{
					JOptionPane.showMessageDialog(fr, "对方退出了，游戏结束");
					fr.isBack=true;
					fr.back();
					break;
				}
				else if(receive[0].equals("map"))    //收到玩家1的地图信息，按此重回地图
				{
					
					this.updatemap(Integer.parseInt(receive[1]));
				}
				else if(receive[0].equals("pause"))    //收到对方暂停信息
				{
					
					state=2;
				}
				else if(receive[0].equals("slow"))     //收到道具使用信息
				{
					
					fr.crystal[Integer.parseInt(receive[1])-1]--;
					sleep[Integer.parseInt(receive[2])-1]=3;
					if(Integer.parseInt(receive[1])==1)
						FightFrame.title2.setVisible(true);
					else
						FightFrame.title1.setVisible(true);
					java.util.Timer slow1=new java.util.Timer();
					final int a=Integer.parseInt(receive[2])-1;
					slow1.schedule(new TimerTask(){
						public void run()
						{
							sleep[a]=1;
							if(a==0)
								FightFrame.title1.setVisible(false);
							else
								FightFrame.title2.setVisible(false);
						}
					}, 5000);
				}
				else if(receive[0].equals("move"))
				{
					if(sleep[0]<=equaltosleep[0])     //蛇一的移动
					{
						snake[0].move();
						map.setmap(snake[0].head.getX(), snake[0].head.getY(), 3);
						equaltosleep[0]=1;
					}
					else
						equaltosleep[0]++;
					if(sleep[1]<=equaltosleep[1])     //蛇二的移动
					{
						snake[1].move();
						map.setmap(snake[1].head.getX(), snake[1].head.getY(), 4);
						equaltosleep[1]=1;
					}
					else
						equaltosleep[1]++;
					repaint();
					if(this.collisionTogether())
					{//相撞则平局
						gameover(0);
					}
					int sum=0;   //记录单方死的个数
					for(int i=0;i<snake.length;i++)    ////检测是否一方
					{
						int reason=snake[i].isCollision(snake[1-i], this);
						if(reason!=0)    
						{
							snake[i].death(reason);
							sum++;
						}
						else
							if(snake[i].dealDrop())
							{
								repaint();
								snake[i].death(4);
								sum++;
							}
						
					}
					switch(sum)
					{
						case 0:break;
						case 1:gameover(1);break;
						case 2:gameover(0);break;
					}
					ate();
					send("ok|");
					try{
						Thread.sleep(350);
					}catch(InterruptedException e){
						e.printStackTrace();
					}
				}
				if((isfirst&&state==1)||receive[0].equals("ok"))
				{
					if(isNeedItem())
					{
						items.addToDraw(this);
						repaint();
					}
					if(sleep[0]<=equaltosleep[0])     //蛇一的移动
					{
						snake[0].move();
						map.setmap(snake[0].head.getX(), snake[0].head.getY(), 3);
						equaltosleep[0]=1;
					}
					else
						equaltosleep[0]++;
					if(sleep[1]<=equaltosleep[1])     //蛇二的移动
					{
						snake[1].move();
						map.setmap(snake[1].head.getX(), snake[1].head.getY(), 4);
						equaltosleep[1]=1;
					}
					else
						equaltosleep[1]++;
					repaint();
					if(this.collisionTogether())
					{//相撞则平局
						gameover(0);
					}
					int sum=0;   //记录单方死的个数
					for(int i=0;i<snake.length;i++)    ////检测是否一方
					{
						int reason=snake[i].isCollision(snake[1-i], this);
						if(reason!=0)    
						{
							snake[i].death(reason);
							sum++;
						}
						else
							if(snake[i].dealDrop())
							{
								repaint();
								snake[i].death(4);
								sum++;
							}
						
					}
					switch(sum)
					{
						case 0:break;
						case 1:gameover(1);break;
						case 2:gameover(0);break;
					}
					ate();
					send("move|");
					try{
						Thread.sleep(350);
					}catch(InterruptedException e){
						e.printStackTrace();
					}
				}
				
				}	 
			}
			//socket.disconnect();
			socket.close();
		}catch(SocketException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	boolean isNeedItem()     //只有玩家一才判断
	{
		if(isfirst) 
		{
			int sum=0,i;
			for(i=0;i<items.items.length;i++)
				if(items.items[i]==null)
					sum++;
			if(sum==items.items.length)
				return true;
			else
				return false;
		}
		return false;
	}
	
	private void ate()      //吃食物及其他物件处理
	{
		Point p[]=new Point[2];
		p[0]=new Point(snake[0].head.getX(),snake[0].head.getY());
		p[1]=new Point(snake[1].head.getX(),snake[1].head.getY());
		int i,j,sum=0;
		for(j=0;j<snake.length;j++)
			for(i=0;i<items.items.length;i++)
			{
				if(items.items[i]!=null&&p[j].equals(new Point(items.items[i].getX(),items.items[i].getY())))
				{
					if(items.items[i] instanceof Apple)
					{
						score[j]+=3;
						items.items[i].cleanItem();
						snake[j].addSnake();
						snake[j].isAte=true;
						
					}
					else if(items.items[i] instanceof Bread)
					{
						score[j]+=5;
						items.items[i].cleanItem();
						snake[j].addSnake();
						snake[j].isAte=true;
						
					}
					else
					{
						if(score[j]<3)
						{
							snake[j].death(5);
							sum++;
							break;
						}
						else
						{
							score[j]-=3;
							items.items[i].cleanItem();
							snake[j].reduceSnake();
							
						}

					}
					repaint();
					map.setmap(items.items[i].getX(), items.items[i].getY(), j+3);
					items.items[i]=null;
					if(isfirst)
						items.addToDraw(this);
					break;
				}

			}
		switch(sum)
		{
			case 0:break;
			case 1:gameover(1);break;
			case 2:gameover(0);break;
		}
				
	}
	
	 void whichWin()
		{
			Object option[]={"好的","我们拒绝"};
			state=2;
			isOver=true;
			int a;
			if(score[0]>score[1])
				a=JOptionPane.showOptionDialog(this, "恭喜第一条蛇胜出，是否重玩？", "Snake say:", JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE,new ImageIcon(Toolkit.getDefaultToolkit().getImage("img/fight.gif")) , option, option[0]);
			else if(score[0]<score[1])
				a=JOptionPane.showOptionDialog(this, "恭喜第二条蛇胜出，是否重玩？", "Snake say:", JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE,new ImageIcon(Toolkit.getDefaultToolkit().getImage("img/fight.gif")) , option, option[0]);
			else
				a=JOptionPane.showOptionDialog(this, "平局，是否重玩？", "Snake say:", JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE,new ImageIcon(Toolkit.getDefaultToolkit().getImage("img/fight.gif")) , option, option[0]);
			switch(a)
			{
				case 0:
				{
					fr.regame();
				}break;
				case 1:
				{
					send("quit|");
					fr.isBack=true;
					fr.back();
				}break;
			}
		}
		
		void gameover(int reason)
		{
			isOver=true;
			state=2;
			Object option[]={"好的","我们拒绝"};
			int b=-1,a=snake[0].getDeath(),c=snake[1].getDeath();
			switch(reason)
			{
				case 0:
				{
					if(a==c)
					{
						String select[]={"两蛇相撞而平局","两蛇均撞墙，平局","两蛇均自残而亡","两蛇自相残杀而亡","两蛇均跳河而亡","两蛇均因空腹吃炸弹而亡"};
						b=JOptionPane.showOptionDialog(this,select[a]+",是否重玩？" , "Snake say:", JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE, new ImageIcon(Toolkit.getDefaultToolkit().getImage("img/cry.gif")), option, option[0]);	
					}
					else
					{
						String select[][]={{"第一条蛇撞墙","第一条自宫","第一条蛇追尾第二条蛇","第一条蛇跳河","第一条蛇空腹吃炸弹死"},{"第二条蛇撞墙","第二条自宫","第二条蛇追尾第一条蛇","第二条蛇跳河","第二条蛇空腹吃炸弹死"}};
						b=JOptionPane.showOptionDialog(this,select[0][a-1]+"，"+select[1][c-1]+"，二者平局"+",是否重玩？" , "Snake say:", JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE, new ImageIcon(Toolkit.getDefaultToolkit().getImage("img/cry.gif")), option, option[0]);	
					}
				}break;
				case 1:
				{
					String select[]={"蛇撞墙","蛇自杀","蛇追尾对方","蛇跳河","蛇因空腹吃炸弹而亡"};
					if(a!=0)
					{
						lose=1;
						b=JOptionPane.showOptionDialog(this,"第"+lose+"条"+select[a-1]+"，恭喜第"+(3-lose)+"条蛇胜出"+",是否重玩？" , "Snake say:", JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE, new ImageIcon(Toolkit.getDefaultToolkit().getImage("img/fight.gif")), option, option[0]);
					}
					else
					{
						lose=2;
						b=JOptionPane.showOptionDialog(this,"第"+lose+"条"+select[c-1]+"，恭喜第"+(3-lose)+"条蛇胜出"+",是否重玩？", "Snake say:", JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE, new ImageIcon(Toolkit.getDefaultToolkit().getImage("img/fight.gif")), option, option[0]);
					}
							
				}break;
			}
			switch(b)
			{
				case 0:
				{
					fr.regame();
				}break;
				case 1:
				{
					send("quit|");
					fr.isBack=true;
					fr.back();
				}break;
			}
		}
	
}
	
	
