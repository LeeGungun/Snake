package pass;

import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.LayoutManager;
import java.awt.event.*;
import java.awt.*;

import javax.swing.*;

import gameitem.*;

import java.util.*;

public class Play extends JPanel implements Runnable{
	
	protected int grade;
	protected static int passScore[]={90,160,230};
	protected int maxmap;
	protected int minlength;
	protected Snake snake;
	protected DrawGameItem items;
	protected Thread mThread=null;
	protected static int speed1=0;
	protected static int speed2=0;
	private int i=0;
	protected boolean isCrown=false;    //吃到皇冠时通关用
	protected int score=85;
	protected boolean isOver=false;
	private Vector<String> store;      //存储蛇和物件生成顺序,第一个元素代表存储grade，1代表存储sanke，2代表存储items,3代表存储方向
	
	protected Play(LayoutManager lm,boolean isDoubleBuffered,int grade)
	{
		super(lm,isDoubleBuffered);
		this.grade=grade;
		different();
		this.setBounds(0,0,780,686); //面板位置
		this.setBackground(new Color(194,211,223));
        
	}
	
/*	class MyEvent implements AWTEventListener      //全局事件
	{
		public void eventDispatched(AWTEvent e)
		{
			if(!isOver)
			{
				int key=((KeyEvent)e).getKeyCode();
				if(snake.head.getD()==0||snake.head.getD()==1)
				{
					switch(key)
					{
					case KeyEvent.VK_LEFT: 
						snake.movetoturn(-Play.speed1, 0, 2);
						break;
					case KeyEvent.VK_RIGHT: 
						snake.movetoturn(Play.speed1, 0, 3);
						break;
					default:
						snake.move();
					}
				}
				else
				{
					switch(key)
					{
					case KeyEvent.VK_UP: 
						snake.movetoturn(0, -Play.speed1, 0);
						break;
					case KeyEvent.VK_DOWN: 
						snake.movetoturn(0, Play.speed1, 1);
						break;
					default:
						snake.move();
					}
				}
				if(snake.isCollision())
					isOver=true;
				ate();
				repaint();
			}
		}
	}*/
	
	class MyEvent implements AWTEventListener      //全局事件
	{
		public void eventDispatched(AWTEvent e)
		{
			if(!isOver)
			{
				int key=((KeyEvent)e).getKeyCode();
				if(snake.head.getD()==0||snake.head.getD()==1)
				{
					switch(key)
					{
					case KeyEvent.VK_LEFT:
					{
						snake.movetoturn(2);
						store("3-2-"+snake.head.getX()+"-"+snake.head.getY());
					}
						break;
					case KeyEvent.VK_RIGHT:
					{
						snake.movetoturn(3);
						store("3-3-"+snake.head.getX()+"-"+snake.head.getY());
					}
						break;
					}
				}
				else
				{
					switch(key)
					{
					case KeyEvent.VK_UP: 
					{
						snake.movetoturn(0);
						store("3-0-"+snake.head.getX()+"-"+snake.head.getY());
					}
						break;
					case KeyEvent.VK_DOWN:
					{
						snake.movetoturn(1);
						store("3-1-"+snake.head.getX()+"-"+snake.head.getY());
					}
						break;
					}
					repaint();
				}
				
			}
		}
	}
	
	protected void init()     //游戏初始化
	{
		setFocusable(true);
        Toolkit.getDefaultToolkit().addAWTEventListener(new MyEvent(),AWTEvent.KEY_EVENT_MASK);    //开启键盘全局监听
        store=new Vector<String>();
        store(Integer.toString(grade));
		snake=new Snake(this);
		items=new DrawGameItem(this);
		Level.state=1;
		repaint();
		if(!isOver||(mThread!=null&&!mThread.isAlive()))
		{
			mThread=new Thread(this);
			mThread.start();
		}
		isOver=false;
	}
	
	 void store(String s)
	{
		store.addElement(s);
	}
	
    Vector<String> getStore()    //返回存储数据
	{
		return store;
	}
	
    protected int getScore()
	{
		return this.score;
	}
	
    protected void setScore(int score)
	{
		this.score=score;
	}
	
	public  void run()    //主线程
	{
			while(!isOver)
			{
				if(Level.state==1)
				{

					if(this.isNeedItem())
					{
						items.addToDraw(this);
						repaint();
					}
					snake.move();
					repaint();
					if(snake.isCollision())
					{
						this.store("1-dead-"+snake.head.getX()+"-"+snake.head.getY());
						Level.state=2;
						isOver=true;
						break;
					}
					ate();
				}
				try{
					Thread.sleep(speed2);
				}catch(InterruptedException e){
					e.printStackTrace();
				}
			}
			
		
	}
	
	protected boolean isNeedItem()     //检测是否有游戏物件
	{
		int sum=0;
		for(i=0;i<items.items.length;i++)
			if(items.items[i]==null)
				sum++;
		if(sum==items.items.length)
			return true;
		else
			return false;
	}
	
	
	protected void different()     //根据关数设定不同值
	{                
		switch(grade)
		{
			case 1:
			{
				this.maxmap=20;
				this.minlength=30;
				Play.speed1=30;
				Play.speed2=300;
			}break;
			case 2:
			{
				this.maxmap=20;
				this.minlength=30;
				Play.speed1=30;
				Play.speed2=200;
			}break;
			case 3:
			{
				this.maxmap=30;
				this.minlength=20;
				Play.speed1=20;
				Play.speed2=150;
			}break;
				
		}

	}
			
	protected void ate()      //吃食物及其他物件处理
	{
		Point p=new Point(snake.head.getX(),snake.head.getY());
		Loop:for(i=0;i<items.items.length;i++)
		{
			if(items.items[i]!=null&&p.equals(new Point(items.items[i].getX(),items.items[i].getY())))
			{
				if(items.items[i] instanceof Apple)
				{
					score+=3;
					items.items[i].cleanItem();
					snake.addSnake();
					snake.isAte=true;
				}
				else if(items.items[i] instanceof Bread)
				{
					score+=5;
					items.items[i].cleanItem();
					snake.addSnake();
					snake.isAte=true;
				}
				else if(items.items[i] instanceof Bomb)
				{
					if(score<3)
					{
						isOver=true;
						this.store("1-dead-"+snake.head.getX()+"-"+snake.head.getY());
						break Loop;
					}
					else
					{
						score-=3;
						items.items[i].cleanItem();
						snake.reduceSnake();
					}
					
				}
				else
				{
					isCrown=true;
					this.store("1-crown-"+snake.head.getX()+"-"+snake.head.getY());
					items.items[i].cleanItem();
					repaint();
					items.items[i]=null;
					break Loop;
				}
				repaint();
				items.items[i]=null;
				items.addToDraw(this );
				break;
			}
			
		}
				
	}
	
	public void paint(Graphics g)
	{
		super.paint(g);
		g.clearRect(0, 0, 780, 686);
		Color c1=new Color(136,188,13);
		g.setColor(c1);
		g.fillRoundRect(60, 13,660, 660, 10, 10);
		Color c2=new Color(0,0,0);
		g.setColor(c2);
		g.fillRoundRect(78, 31, 624, 624, 6, 6);
		Color c3=new Color(255,255,255);
		g.setColor(c3);
		g.fillRect(90, 43, 600, 600);
		Color c4=new Color(71,70,71);
		g.setColor(c4);
		for (int i=0;i<this.maxmap;i++)
			for(int j=0;j<this.maxmap;j++)
				g.fillRect(i*this.minlength+91, j*this.minlength+44, minlength-2, minlength-2);
		if(Level.state>0)
		{
				items.drawAllItem(g, this,this.minlength-2);
				snake.drawSnake(g, this,this.minlength);

		}

	}
	

}
