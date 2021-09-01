package fight;

import gameitem.Apple;
import gameitem.Bomb;
import gameitem.Bread;

import javax.swing.*;

import java.awt.*;
import java.util.*;
import java.awt.event.*;

public class Play extends JPanel implements Runnable{
	//人机对战时，电脑为玩家二
	protected DrawMap map;
	protected int random;
	protected Snake snake[]=new Snake[2];
	protected DrawGameItem items=null;
	protected boolean isOver=false;      //判断游戏是否结束
	protected int lose=0;   //记录获胜玩家
	protected int state=0;   //0表示未开始，1表示开始，2表示暂停
	protected Thread main;    //主线程
	protected int score[]={0,0};
	FightFrame fr;
	protected int sleep[]={1,1};   //用于延迟蛇move
	protected int equaltosleep[]={1,1};  //用于延迟计数，当与相应的sleep值相等时才move
	
	public Play()
	{
		
	}

	public Play(LayoutManager lm,boolean isDoubleBuffered)
	{
		super(lm,isDoubleBuffered);
	}
	
	Play(LayoutManager lm,boolean isDoubleBuffered,FightFrame fr)
	{
		super(lm,isDoubleBuffered);
		this.fr=fr;
		this.setBounds(90,150,840,440);
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
				case KeyEvent.VK_ENTER:
				{//Enter键开始、继续
					if(state==0)
					{
						initplay();
						fr.player1.initP();
						fr.player2.initP();
					}
					else if(state==2)
						state=1;
				}
					break;
				case KeyEvent.VK_SPACE:       //空格键暂停
						if(state==1)  //置为暂停状态
							state=2;        
				break;
				case KeyEvent.VK_UP:
					if(state==1&&(snake[0].head.getD()==2||snake[0].head.getD()==3))
						snake[0].movetoturn(0);
					break;
				case KeyEvent.VK_DOWN:
					if(state==1&&(snake[0].head.getD()==2||snake[0].head.getD()==3))
						snake[0].movetoturn(1);
					break;
				case KeyEvent.VK_LEFT:
					if(state==1&&(snake[0].head.getD()==0||snake[0].head.getD()==1))
						snake[0].movetoturn(2);
					break;
				case KeyEvent.VK_RIGHT:
					if(state==1&&(snake[0].head.getD()==0||snake[0].head.getD()==1))
						snake[0].movetoturn(3);
					break;
				case KeyEvent.VK_W:         //蛇二的上、下、左、右键分别为W、S、A、D
					if(fr.getM()>0&&state==1&&(snake[1].head.getD()==2||snake[1].head.getD()==3))
						snake[1].movetoturn(0);
					break;
				case KeyEvent.VK_S:
					if(fr.getM()>0&&state==1&&(snake[1].head.getD()==2||snake[1].head.getD()==3))
						snake[1].movetoturn(1);
					break;
				case KeyEvent.VK_A:
					if(fr.getM()>0&&state==1&&(snake[1].head.getD()==0||snake[1].head.getD()==1))
						snake[1].movetoturn(2);
					break;
				case KeyEvent.VK_D:
					if(fr.getM()>0&&state==1&&(snake[1].head.getD()==0||snake[1].head.getD()==1))
						snake[1].movetoturn(3);
					break;
				case KeyEvent.VK_SHIFT:   //玩家一用水晶延迟对方    
				{
					if(fr.getM()>0&&sleep[1]==1&&FightFrame.crystal[0]>0)
					{
						FightFrame.crystal[0]--;
						sleep[1]=3;
						FightFrame.title2.setVisible(true);
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
					if(fr.getM()>0&&sleep[0]==1&&FightFrame.crystal[1]>0)
					{
						FightFrame.crystal[1]--;
						sleep[0]=3;
						FightFrame.title1.setVisible(true);
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
	
	
	
	void initplay()   //初始化游戏
	{
		snake[0]=new Snake(1,this);
		snake[1]=new Snake(2,this);
		items=new DrawGameItem(this);
		state=1;
		repaint();
		main=new Thread(this);
		main.start();
	}
	
	protected void initmap()    //读取地图
	{
		Random ran=new Random(System.currentTimeMillis());
		random=Math.abs(ran.nextInt())%3;
		map=new DrawMap(random);
	}
	
	
	public void run()
	{
		if(fr.getM()>0)
		{
			Loop:while(!isOver)
			{
				if(state==1)
				{
					if(isNeedItem())
					{
						items.addToDraw();
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
						break Loop;
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
						case 1:gameover(1);break Loop;
						case 2:gameover(0);break Loop;
					}
					ate();
					try{
						Thread.sleep(300);
					}catch(InterruptedException e){
						e.printStackTrace();
					}
					
				}
			}
		}
		else    //人机对战模式
		{
			Loop:while(!isOver)
			{
				if(state==1)
				{
					if(isNeedItem())
					{
						items.addToDraw();
						repaint();
					}
					snake[0].move();
					map.setmap(snake[0].head.getX(), snake[0].head.getY(), 3);
					if(snake[1].isHaveObstacle())
						snake[1].dealObstacle();
					snake[1].searchFood();
					if(snake[1].isHaveObstacle())
						snake[1].dealObstacle();
					snake[1].move();
					map.setmap(snake[1].head.getX(), snake[1].head.getY(), 4);
					repaint();
					if(this.collisionTogether())
					{
						gameover(0);
						break Loop;
					}
					int sum=0;   //记录单方死的个数
					for(int i=0;i<snake.length;i++)
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
					case 1:gameover(1);break Loop;
					case 2:gameover(0);break Loop;
					}
					ate();
					
					try{
						Thread.sleep(300);
					}catch(InterruptedException e){
						e.printStackTrace();
					}
					
				}
			}
		}
		
	}
	
	private boolean isNeedItem()     //检测是否有游戏物件
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
	
	protected boolean collisionTogether()      //判断蛇是否相撞
	{
		if(snake[0].head.getX()==snake[1].head.getX()&&snake[0].head.getY()==snake[1].head.getY())
			return true;
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
					items.addToDraw();
					if(j==1&&fr.getM()==0)
					{
						snake[1].setAim(null);
						snake[1].setPath(10000);
					}
					if(fr.getM()==0)
					{
						snake[1].searchFood();
						repaint();
					}
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
				fr.isBack=true;
				fr.back();
			}break;
		}
	}
	
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		map.drawMap(g, this);
		if(state>0)
		{
			items.drawAllItem(g, this);
			for(int i=0;i<snake.length;i++)
				snake[i].drawSnake(g, this);
		}
	}

}
