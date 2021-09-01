package fight;

import gameitem.Apple;
import gameitem.Bomb;
import gameitem.Bread;

import javax.swing.*;

import java.awt.*;
import java.util.*;
import java.awt.event.*;

public class Play extends JPanel implements Runnable{
	//�˻���սʱ������Ϊ��Ҷ�
	protected DrawMap map;
	protected int random;
	protected Snake snake[]=new Snake[2];
	protected DrawGameItem items=null;
	protected boolean isOver=false;      //�ж���Ϸ�Ƿ����
	protected int lose=0;   //��¼��ʤ���
	protected int state=0;   //0��ʾδ��ʼ��1��ʾ��ʼ��2��ʾ��ͣ
	protected Thread main;    //���߳�
	protected int score[]={0,0};
	FightFrame fr;
	protected int sleep[]={1,1};   //�����ӳ���move
	protected int equaltosleep[]={1,1};  //�����ӳټ�����������Ӧ��sleepֵ���ʱ��move
	
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
	
	class MyEvent implements AWTEventListener      //ȫ���¼�
	{
		public void eventDispatched(AWTEvent e)
		{
			int key=((KeyEvent)e).getKeyCode();
			switch(key)
			{
				case KeyEvent.VK_ENTER:
				{//Enter����ʼ������
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
				case KeyEvent.VK_SPACE:       //�ո����ͣ
						if(state==1)  //��Ϊ��ͣ״̬
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
				case KeyEvent.VK_W:         //�߶����ϡ��¡����Ҽ��ֱ�ΪW��S��A��D
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
				case KeyEvent.VK_SHIFT:   //���һ��ˮ���ӳٶԷ�    
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
				case KeyEvent.VK_Q:    //��Ҷ���ˮ���ӳٶԷ�
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
	
	
	
	void initplay()   //��ʼ����Ϸ
	{
		snake[0]=new Snake(1,this);
		snake[1]=new Snake(2,this);
		items=new DrawGameItem(this);
		state=1;
		repaint();
		main=new Thread(this);
		main.start();
	}
	
	protected void initmap()    //��ȡ��ͼ
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
						break Loop;
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
		else    //�˻���սģʽ
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
					int sum=0;   //��¼�������ĸ���
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
	
	private boolean isNeedItem()     //����Ƿ�����Ϸ���
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
	
	protected boolean collisionTogether()      //�ж����Ƿ���ײ
	{
		if(snake[0].head.getX()==snake[1].head.getX()&&snake[0].head.getY()==snake[1].head.getY())
			return true;
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
