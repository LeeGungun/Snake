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
	boolean isfirst=false;   //��¼��ʱ����Ƿ������1
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
	
	class MyEvent implements AWTEventListener      //ȫ���¼�
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
				case KeyEvent.VK_W:         //�߶����ϡ��¡����Ҽ��ֱ�ΪW��S��A��D
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
				case KeyEvent.VK_SHIFT:   //���һ��ˮ���ӳٶԷ�    
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
				case KeyEvent.VK_Q:    //��Ҷ���ˮ���ӳٶԷ�
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
	
	void startjoin(String ip,int remoteport,int receiveport)     //��ʼ����
	{
		isBegin=true;
		this.sendport=remoteport;
		this.receiveport=receiveport;
		send("join|"+fr.name1+"|"+String.valueOf(fr.crystal[0])+"|");
		this.isfirst=true;  
		fr.wait.setVisible(true);
		//JOptionPane.showMessageDialog(fr, "�ȴ�����");
		main=new Thread(this);
		main.start();
	
	}
	
	void send(String induction)    //��������
	{
		DatagramSocket socket=null;
		try{
			socket=new DatagramSocket();    //��������ʱָ����ַ3000
			byte buffer[]=new String(induction).getBytes();
			InetAddress host=InetAddress.getLocalHost();
			DatagramPacket packet=new DatagramPacket(buffer,buffer.length,host,sendport);
			socket.send(packet);
			System.out.println("������Ϣ��"+induction);
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
	
	void initplay()   //��ʼ����Ϸ
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
			//socket.connect(InetAddress.getByName(fr.oppoIP), 3000);    //ֻ���ո�Ŀ���ַ��Ϣ
			byte data[]=new byte[100];
			DatagramPacket packet=new DatagramPacket(data,data.length);
			while(isBegin)
			{
				socket.receive(packet);
				String receive[]=new String(data).split("\\|");
				synchronized(data){
				if(receive[0].equals("join"))     //�յ��ȴ�����,�Լ���ʱΪ���2
				{
						fr.wait.setVisible(false);
						this.isfirst=false;
						send("conn|"+fr.name1+"|"+Integer.toString(fr.crystal[0])+"|");
						fr.recordFirst(receive[1],Integer.parseInt(receive[2]));
						JOptionPane.showMessageDialog(fr, "������Ϊ��Ҷ�");
						initplay();
					
				}
				else if(receive[0].equals("conn"))    //�յ��������ӣ��Լ���ʱΪ���һ
				{
						fr.wait.setVisible(false);
						JOptionPane.showMessageDialog(fr, "������Ϊ���һ");
						fr.recordSecond(receive[1], Integer.parseInt(receive[2]));
						send("map|"+Integer.toString(random)+"|");
						initplay();
					
					
				}
				else if(receive[0].equals("snake"))     //�յ��Է���ʼ�����ߵ�ͷ�ڵ�
				{
					if(Integer.parseInt(receive[3])==1)
						snake[0]=new Snake(new SnakePoint(1,Integer.parseInt(receive[1]),Integer.parseInt(receive[2]),Integer.parseInt(receive[4]),1),this,1);
					else if(Integer.parseInt(receive[3])==2)
						snake[1]=new Snake(new SnakePoint(1,Integer.parseInt(receive[1]),Integer.parseInt(receive[2]),Integer.parseInt(receive[4]),2),this,2);
					
				}
				else if(receive[0].equals("gameitem"))      //�յ����1���ɵ�ʳ����Ϣ����ʾ�˷�Ϊ��Ҷ�
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
				else if(receive[0].equals("river"))     //�յ����1���ɵĺ�����Ϣ����ʾ�˷�Ϊ��Ҷ�
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
				else if(receive[0].equals("turn"))     //�յ�ת����Ϣ
				{
					
					snake[Integer.parseInt(receive[1])-1].movetoturn(Integer.parseInt(receive[2]));
				}
				else if(receive[0].equals("continue"))      //�յ���������
				{
					state=1;
				}
				else if(receive[0].equals("quit"))     //�յ��Է��˳���Ϣ
				{
					JOptionPane.showMessageDialog(fr, "�Է��˳��ˣ���Ϸ����");
					fr.isBack=true;
					fr.back();
					break;
				}
				else if(receive[0].equals("map"))    //�յ����1�ĵ�ͼ��Ϣ�������ػص�ͼ
				{
					
					this.updatemap(Integer.parseInt(receive[1]));
				}
				else if(receive[0].equals("pause"))    //�յ��Է���ͣ��Ϣ
				{
					
					state=2;
				}
				else if(receive[0].equals("slow"))     //�յ�����ʹ����Ϣ
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
					if(sleep[0]<=equaltosleep[0])     //��һ���ƶ�
					{
						snake[0].move();
						map.setmap(snake[0].head.getX(), snake[0].head.getY(), 3);
						equaltosleep[0]=1;
					}
					else
						equaltosleep[0]++;
					if(sleep[1]<=equaltosleep[1])     //�߶����ƶ�
					{
						snake[1].move();
						map.setmap(snake[1].head.getX(), snake[1].head.getY(), 4);
						equaltosleep[1]=1;
					}
					else
						equaltosleep[1]++;
					repaint();
					if(this.collisionTogether())
					{//��ײ��ƽ��
						gameover(0);
					}
					int sum=0;   //��¼�������ĸ���
					for(int i=0;i<snake.length;i++)    ////����Ƿ�һ��
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
					if(sleep[0]<=equaltosleep[0])     //��һ���ƶ�
					{
						snake[0].move();
						map.setmap(snake[0].head.getX(), snake[0].head.getY(), 3);
						equaltosleep[0]=1;
					}
					else
						equaltosleep[0]++;
					if(sleep[1]<=equaltosleep[1])     //�߶����ƶ�
					{
						snake[1].move();
						map.setmap(snake[1].head.getX(), snake[1].head.getY(), 4);
						equaltosleep[1]=1;
					}
					else
						equaltosleep[1]++;
					repaint();
					if(this.collisionTogether())
					{//��ײ��ƽ��
						gameover(0);
					}
					int sum=0;   //��¼�������ĸ���
					for(int i=0;i<snake.length;i++)    ////����Ƿ�һ��
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
	
	boolean isNeedItem()     //ֻ�����һ���ж�
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
	
	private void ate()      //��ʳ�Ｐ�����������
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
			Object option[]={"�õ�","���Ǿܾ�"};
			state=2;
			isOver=true;
			int a;
			if(score[0]>score[1])
				a=JOptionPane.showOptionDialog(this, "��ϲ��һ����ʤ�����Ƿ����棿", "Snake say:", JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE,new ImageIcon(Toolkit.getDefaultToolkit().getImage("img/fight.gif")) , option, option[0]);
			else if(score[0]<score[1])
				a=JOptionPane.showOptionDialog(this, "��ϲ�ڶ�����ʤ�����Ƿ����棿", "Snake say:", JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE,new ImageIcon(Toolkit.getDefaultToolkit().getImage("img/fight.gif")) , option, option[0]);
			else
				a=JOptionPane.showOptionDialog(this, "ƽ�֣��Ƿ����棿", "Snake say:", JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE,new ImageIcon(Toolkit.getDefaultToolkit().getImage("img/fight.gif")) , option, option[0]);
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
			Object option[]={"�õ�","���Ǿܾ�"};
			int b=-1,a=snake[0].getDeath(),c=snake[1].getDeath();
			switch(reason)
			{
				case 0:
				{
					if(a==c)
					{
						String select[]={"������ײ��ƽ��","���߾�ײǽ��ƽ��","���߾��Բж���","���������ɱ����","���߾����Ӷ���","���߾���ո���ը������"};
						b=JOptionPane.showOptionDialog(this,select[a]+",�Ƿ����棿" , "Snake say:", JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE, new ImageIcon(Toolkit.getDefaultToolkit().getImage("img/cry.gif")), option, option[0]);	
					}
					else
					{
						String select[][]={{"��һ����ײǽ","��һ���Թ�","��һ����׷β�ڶ�����","��һ��������","��һ���߿ո���ը����"},{"�ڶ�����ײǽ","�ڶ����Թ�","�ڶ�����׷β��һ����","�ڶ���������","�ڶ����߿ո���ը����"}};
						b=JOptionPane.showOptionDialog(this,select[0][a-1]+"��"+select[1][c-1]+"������ƽ��"+",�Ƿ����棿" , "Snake say:", JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE, new ImageIcon(Toolkit.getDefaultToolkit().getImage("img/cry.gif")), option, option[0]);	
					}
				}break;
				case 1:
				{
					String select[]={"��ײǽ","����ɱ","��׷β�Է�","������","����ո���ը������"};
					if(a!=0)
					{
						lose=1;
						b=JOptionPane.showOptionDialog(this,"��"+lose+"��"+select[a-1]+"����ϲ��"+(3-lose)+"����ʤ��"+",�Ƿ����棿" , "Snake say:", JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE, new ImageIcon(Toolkit.getDefaultToolkit().getImage("img/fight.gif")), option, option[0]);
					}
					else
					{
						lose=2;
						b=JOptionPane.showOptionDialog(this,"��"+lose+"��"+select[c-1]+"����ϲ��"+(3-lose)+"����ʤ��"+",�Ƿ����棿", "Snake say:", JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE, new ImageIcon(Toolkit.getDefaultToolkit().getImage("img/fight.gif")), option, option[0]);
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
	
	
