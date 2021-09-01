package pass;

//可以考虑是否运用系统托盘

import javax.swing.*;
import main.*;
import java.awt.*;
import java.util.*;
import java.awt.event.*;

public class Select implements ActionListener {
	
	JFrame jfame;
    Level level[]=new Level[3];
    int thegame=1;
	Pane pane;
	static int key;  //连接数据库
	boolean isBack=false;   //如果返回，则需做一些处理，此值则为标志
	java.util.Timer mThread;
	static int crystal=0;   //记录通关奖励水晶数
	int main1x;
	int main2x;
	private boolean state=false;
	private static JButton b1=new JButton("帮助");
	private static JButton b2=new JButton("第一关");
	private static JButton b3=new JButton("第二关");
	private static JButton b4=new JButton("第三关");
	private static JButton b5=new JButton("返回");
	private static Image background1=Toolkit.getDefaultToolkit().getImage("img/main1.png");
	private static Image background2=Toolkit.getDefaultToolkit().getImage("img/main2.png");
	private Control menu;
	
	
	public Select(int key,int crystal,Control menu)
	{
		Select.key=key;
		this.menu=menu;
		this.crystal=crystal;
		jfame=new JFrame("贪吃蛇——关卡模式");
		jfame.setBounds(250, 100,950,500);
		jfame.setResizable(false);
		pane=new Pane(null,true);
		pane.setBounds(0, 0,750,500);
		jfame.getContentPane().setLayout(null);
		jfame.getContentPane().add(pane);
		jfame.getContentPane().setBackground(new Color(120,220,244));
		b1.setBounds(800, 30, 100, 60);
		b2.setBounds(800, 120, 100, 60);
		b3.setBounds(800, 210, 100, 60);
		b4.setBounds(800, 300, 100, 60);
		b5.setBounds(800, 390, 100, 60);
		b1.setBackground(Color.DARK_GRAY);
		b2.setBackground(Color.DARK_GRAY);
		b3.setBackground(Color.DARK_GRAY);
		b4.setBackground(Color.DARK_GRAY);
		b5.setBackground(Color.DARK_GRAY);
		b1.setFont(new Font("楷体",Font.BOLD,20));
		b2.setFont(b1.getFont());
		b3.setFont(b1.getFont());
		b4.setFont(b1.getFont());
		b5.setFont(b1.getFont());
		b1.setForeground(Color.WHITE);
		b2.setForeground(Color.WHITE);
		b3.setForeground(Color.WHITE);
		b4.setForeground(Color.WHITE);
		b5.setForeground(Color.WHITE);
		b1.setOpaque(true);
		b2.setOpaque(true);
		b3.setOpaque(true);
		b4.setOpaque(true);
		b5.setOpaque(true);
		b1.addActionListener(this);
		b2.addActionListener(this);
		b3.addActionListener(this);
		b4.addActionListener(this);
		b5.addActionListener(this);
		b1.setVisible(true);
		b2.setVisible(true);
		b3.setVisible(true);
		b4.setVisible(true);
		b5.setVisible(true);
		jfame.getContentPane().add(b1);
		jfame.getContentPane().add(b2);
		jfame.getContentPane().add(b3);
		jfame.getContentPane().add(b4);
		jfame.getContentPane().add(b5);
		jfame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		jfame.addWindowListener(new WindowAdapter()
					{
						public void windowClosing(WindowEvent e)
						{
							gomenu();
						}
					});
		jfame.setVisible(true);
		init();
		
	}
	
	class Pane extends JPanel
	{		
		public Pane(LayoutManager l,boolean isDoubleBuffered)
		{
			super(l,isDoubleBuffered);
			this.setPreferredSize(new Dimension(750,500));
			this.setBackground(new Color(209,238,245));
			main1x=0;
			main2x=-750;
		}
		
		public void paint(Graphics g)
		{
			super.paint(g);
			if(!state)
			{
				g.drawImage(background1, main1x,0 ,750,500, this);
				g.drawImage(background2, main2x,0 ,750,500, this);
			}
			else
			{
				Font f=new Font("迷你简胖头鱼",Font.PLAIN,18 );
				g.setFont(f);
				g.setColor(Color.DARK_GRAY);
				g.drawImage(Toolkit.getDefaultToolkit().getImage("img/apple.gif"), 250, 100,40, 40, this);
				g.drawString("吃掉可加3分，蛇增长", 300, 120);
				g.drawImage(Toolkit.getDefaultToolkit().getImage("img/bread.gif"), 250, 160,40, 40, this);
				g.drawString("吃掉可加5分，蛇增长", 300, 180);
				g.drawImage(Toolkit.getDefaultToolkit().getImage("img/death.gif"), 250, 220,40, 40, this);
				g.drawString("吃掉可减3分，蛇变短，如果得分小于3分即死", 300, 240);
				g.drawImage(Toolkit.getDefaultToolkit().getImage("img/pass.gif"),250, 280,40, 40, this);
				g.drawString("吃掉可直接通关", 300, 300);
			}
		}
	}
	

	
		public void actionPerformed(ActionEvent e)
		{
			
			if(e.getSource()==Select.b1)
			{
				if(!state)
				{
					state=true;
					pane.repaint();
					b1.setText("关闭");
				}
				else
				{
					state=false;
					pane.repaint();
					b1.setText("帮助");
				}
			
			}
			else
			{
				if(e.getSource()==Select.b2)
				{
					level[0]=new Level(1);
					thegame=1;
					jfame.setVisible(false);
				}
				if(e.getSource()==Select.b3)
				{
					if(key<2)
					{
						Object option[]={"确定"};
						JOptionPane.showOptionDialog(jfame, "Sorry,此关未解锁", "Snake say:", JOptionPane.YES_OPTION, JOptionPane.WARNING_MESSAGE,new ImageIcon(Toolkit.getDefaultToolkit().getImage("img/sorry.gif")) , option, option[0]);
					}
					else
					{
						level[1]=new Level(2);
						thegame=2;
						jfame.setVisible(false);
					}

				}
				if(e.getSource()==Select.b4)
				{
					if(key<3)
					{
						Object option[]={"确定"};
						JOptionPane.showOptionDialog(jfame, "Sorry,此关未解锁", "Snake say:", JOptionPane.YES_OPTION, JOptionPane.WARNING_MESSAGE,new ImageIcon(Toolkit.getDefaultToolkit().getImage("img/sorry.gif")) , option, option[0]);
					}
					else
					{
						level[2]=new Level(3);
						thegame=3;
						jfame.setVisible(false);
					}
				}
				if(level[thegame-1]!=null)
				{
					level[thegame-1].jf.addWindowListener(new WindowAdapter()
					{
						public void windowClosing(WindowEvent e)
						{
							back();
						}
					});
					mThread.schedule(new TimerTask(){
						public void run()
						{
							if(level[thegame-1]!=null)
							{
								if(Level.state>0)
								{

									if(level[thegame-1].pane.isOver)
										gameover();
									else
										if(level[thegame-1].pane.isCrown||level[thegame-1].pane.getScore()>=Play.passScore[thegame-1])
											gamepass();

								}
							}
						}
					}, 50, 50);
				}
			}
			if(e.getSource()==Select.b5)
			{
				 gomenu();
			}

		}
		
	void gomenu()
	{
		menu.usercrystal.setElementAt(new Integer(Select.crystal), 0);
		menu.userlevel.setElementAt(new Integer(Select.key), 0);
		jfame.setVisible(false);
		jfame.dispose();
		menu.back();
	}
	
	
	 void stop()    //结束游戏，返回关卡模式
	{
		if(isBack)
		{
			level[thegame-1].jf.dispose();
			level[thegame-1]=null;
			isBack=false;
			jfame.setVisible(true);
		}
		
	}

	void start()     //新关卡开始
	{
		if(isBack)
		{
			level[thegame-1].jf.dispose();
			level[thegame-1]=null;
			thegame++;
			level[thegame-1]=new Level(thegame);
			isBack=false;			
		}
	}
	
	void init()
	{
		mThread=new java.util.Timer();
		mThread.schedule(new TimerTask()
				{
			public void run()
			{
				main1x+=30;
				main2x+=30;
				if(main1x==750)
					main1x=-750;
				if(main2x==750)
					main2x=-750;
				pane.repaint();
				
			}
		}, 100, 100);
	}
	
	class GameOver     //游戏结束类
	{
		int a,b;
		String s=new String("OoO,Game Over! "+Level.label[a]+",再来一次？");
		Object option[]={"好的","我拒绝"};
		GameOver()
		{
			
			if(level[thegame-1].pane.getScore()<20)
				a=0;
			else if(level[thegame-1].pane.getScore()<60)
				a=1;
			else if(level[thegame-1].pane.getScore()<100)
				a=2;
			else
				a=3;
			b=JOptionPane.showOptionDialog(level[thegame-1].jf, s, "Snake say:", JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE, new ImageIcon(Toolkit.getDefaultToolkit().getImage("img/cry.gif")), option, option[0]);
			action(b);
		}
		 void action(int b)
		{
			switch(b)
			{
				case 0:
				{
					level[thegame-1].regame();
				}break;
				case 1:
				{
					back();
				}break;
			}
		}
		
	}
	
	class GamePass      //游戏过关类
	{
		int a;
		String s[]={"×1,恭喜过关，得到水晶一颗,是否继续下一关？","真好运！得到皇冠，您已过关"};
		Object option[]={"好的","不了，谢谢"};
		GamePass(int b)
		{
			
			level[thegame-1].grade++;
			if(b==0)
			{
				level[thegame-1].pane.store("1-score-"+level[thegame-1].pane.snake.head.getX()+"-"+level[thegame-1].pane.snake.head.getY());
				level[thegame-1].isSave();
				a=JOptionPane.showOptionDialog(level[thegame-1].jf, s[b], "Snake say:", JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE,new ImageIcon(Level.img) , option, option[0]);
			}
			else
			{
				level[thegame-1].isSave();
				a=JOptionPane.showOptionDialog(level[thegame-1].jf, s[b], "Snake say:", JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE,new ImageIcon(Toolkit.getDefaultToolkit().getImage("img/pass.gif")),option, option[0]);
			}
			action(a);
		}
		void action(int a)
		{
			switch(a)
			{
				case 0:
				{
					advence();  
				}break;
				case 1:
				{
					back();  
				}break;
			}
		}
	}
	
	
	private void advence()    //前进到下一关,别忘了计时器
	{
		Select.key++;
		Level.state=0;
		level[thegame-1].time.task.cancel();
		level[thegame-1].scorePane.task.cancel();
		if(thegame==3)
		{
			Object option[]={"确定"};
			JOptionPane.showOptionDialog(level[thegame-1].jf, "恭喜您完成所有关卡！", "Snake say:", JOptionPane.YES_OPTION, JOptionPane.INFORMATION_MESSAGE,new ImageIcon(Toolkit.getDefaultToolkit().getImage("img/ok.gif")) , option, option[0]);
			back();
		}
		else
		{
			isBack=true;
			start();
		}
		
	}
	
	private void back()     //进行页面转换,回到选关，别忘了计时器,ps:由gameover()和gamepass()调用有不同，特别是关卡解锁、关卡保存这里
	{
		if(Level.state>0)
		{
			level[thegame-1].time.task.cancel();
			level[thegame-1].scorePane.task.cancel();
		}
		Level.state=0;
		if(Select.key!=level[thegame-1].grade)
			Select.key=level[thegame-1].grade;
		isBack=true;
		stop();
	}
	

	
	public void gameover()   
	{
	
		level[thegame-1].time.isStop=true;
		level[thegame-1].isSave();
		new GameOver();
	}
	
	public void gamepass()
	{
	
		Select.key++;
		level[thegame-1].time.isStop=true;
		if(level[thegame-1].pane.isCrown)
			new GamePass(1);
		else
		{
			Select.crystal++;
			new GamePass(0);
			
		}
	}
	

}
