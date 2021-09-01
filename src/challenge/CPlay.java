package challenge;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.awt.event.*;

public class CPlay extends JPanel implements Runnable{
	
	static int state=0;
	DrawMap map;
	Snake snake=null;
	Rabbit rabbit=null;
	Mouse mouse=null;
	Box box=null;
	Thread mthread;
	boolean haveFood=false;
	private CFrame fr;
	boolean holing=false;
	int show=0;   //钻洞时统计过程
	int speedup=0;
	static int death=0;    //记录死因
	Hole showHole=null;
	Over over;
	Pass pass;
	static boolean isStop=false;
	
	
	CPlay(CFrame fr)
	{
		state=0;
		this.fr=fr;
		this.setOpaque(false);
		this.setLayout(null);
		map=new DrawMap();
		this.setFocusable(true);
		Toolkit.getDefaultToolkit().addAWTEventListener(new MyEvent(),AWTEvent.KEY_EVENT_MASK);
		
	}
	
	class MyEvent implements AWTEventListener      //全局事件
	{
		public void eventDispatched(AWTEvent e)
		{
			int key=((KeyEvent)e).getKeyCode();
			switch(snake.head.getD())
			{
				case 0:{
					switch(key)
					{
						case KeyEvent.VK_UP:   //加速
							if(state==1)
								speedup++;
							break;
						case KeyEvent.VK_DOWN:   //还原
							if(state==1)
								speedup=0;
							break;
						case KeyEvent.VK_LEFT:
							if(state==1)
							{
								snake.turn(2);
								repaint();
							}
							break;
						case KeyEvent.VK_RIGHT:
								if(state==1)
								{
									snake.turn(3);
									repaint();
								}
							break;
					}
				}break;	
				case 1:{
					switch(key)
					{
						case KeyEvent.VK_UP:   //加速
							if(state==1)
								speedup=0;
							break;
						case KeyEvent.VK_DOWN:   //还原
							if(state==1)
								speedup++;
							break;
						case KeyEvent.VK_LEFT:
							if(state==1)
							{
								snake.turn(2);
								repaint();
							}
							break;
						case KeyEvent.VK_RIGHT:
								if(state==1)
								{
									snake.turn(3);
									repaint();
								}
							break;
					}
				}break;
				case 2:{
					switch(key)
					{
						case KeyEvent.VK_LEFT:   //加速
							if(state==1)
								speedup++;
							break;
						case KeyEvent.VK_RIGHT:   //还原
							if(state==1)
								speedup=0;
							break;
						case KeyEvent.VK_UP:
							if(state==1)
							{
								snake.turn(0);
								repaint();
							}
							break;
						case KeyEvent.VK_DOWN:
								if(state==1)
								{
									snake.turn(1);
									repaint();
								}
							break;
					}
				}break;
				case 3:{
					switch(key)
					{
						case KeyEvent.VK_LEFT:   //加速
							if(state==1)
								speedup=0;
							break;
						case KeyEvent.VK_RIGHT:   //还原
							if(state==1)
								speedup++;
							break;
						case KeyEvent.VK_UP:
							if(state==1)
							{
								snake.turn(0);
								repaint();
							}
							break;
						case KeyEvent.VK_DOWN:
								if(state==1)
								{
									snake.turn(1);
									repaint();
								}
							break;
					}
				}break;
			}
			
			
		}
	}
	
	class Hole extends JPanel 
	{
		private Image img=Toolkit.getDefaultToolkit().getImage("cimg/hole.png");
		private Image timg=Toolkit.getDefaultToolkit().getImage("cimg/star.gif");
		double degree=0,leftangle;
		double x0,y0,x1=200,y1=100,x2=180,y2=120; 
		
		Hole()
		{
			this.setOpaque(false);
			this.setLayout(null);
			this.setVisible(true);
			this.setBounds(130, 100, 600, 300);
			x0=180;
			y0=150;
			leftangle=Math.atan(5/12);
		}
		
		
		public void paintComponent(Graphics g)
		{
			g.drawImage(img, 0, 0, this);
			g.drawImage(timg,(int)x1,(int)y1,(int)x2,(int)y2,20,0,0,20,this);
		}
	}
	
	class Over extends JPanel implements ActionListener
	{
		private Image bg;
		public  JButton yes=new JButton();
		public  JButton no=new JButton();
		private JLabel label;
		int stage,reason;
		
		Over(int stage,int reason)
		{
			this.stage=stage;
			this.reason=reason;
			this.setLayout(null);
			if((stage==1&&reason==3)||(stage==2&&reason==3))
				bg=Toolkit.getDefaultToolkit().getImage("cimg/over3.png");
			else if(stage==3&&reason==2)
				bg=Toolkit.getDefaultToolkit().getImage("cimg/over2.png");
			else if((stage==2&&reason==0)||(stage==3&&reason==0))
				bg=Toolkit.getDefaultToolkit().getImage("cimg/over0.png");
			else 
			{
				if(stage==1&&reason==5)
				{
					label=new JLabel(new ImageIcon(Toolkit.getDefaultToolkit().getImage("cimg/clue.gif")));
					label.setOpaque(false);
					label.setBounds(338, 75, 70, 60);
					this.add(label);
				}
				else if(stage==3&&reason==1)
				{
					label=new JLabel(new ImageIcon(Toolkit.getDefaultToolkit().getImage("cimg/king.gif")));
					label.setOpaque(false);
					label.setBounds(338, 75, 88, 80);
					this.add(label);
				}
			
				bg=Toolkit.getDefaultToolkit().getImage("cimg/over"+stage+"-"+reason+".png");
			}
			this.setBounds(130, 100, 600, 300);
			this.setVisible(true);
			this.setOpaque(false);
			this.setFocusable(true);
			yes.setBounds(38, 65, 160, 65);
			yes.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("cimg/replay.gif")));
			yes.setRolloverIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("cimg/click_r.gif")));
			yes.setContentAreaFilled(false);
			yes.setBorderPainted(false);
			yes.addActionListener(this);
			yes.setFocusable(false);
			this.add(yes);
			no.setBounds(38, 130, 160, 65);
			no.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("cimg/no.gif")));
			no.setRolloverIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("cimg/click_n.gif")));
			no.setContentAreaFilled(false);
			no.setBorderPainted(false);
			no.addActionListener(this);
			no.setFocusable(false);
			this.add(no);
			
		}
		
		public void actionPerformed(ActionEvent e)
		{
			if(e.getSource()==yes)
			{
				this.setVisible(false);
				fr.getLayeredPane().remove(this);
				destroy();
				fr.replay();
				
			}
			else if(e.getSource()==no)
			{
				this.setVisible(false);
				fr.getLayeredPane().remove(this);
				destroy();
				fr.back();
			}
			
		}
		
		public void paintComponent(Graphics g)
		{
			super.paintComponent(g);
			g.drawImage(bg, 0, 0, 600, 300, this);
		}
	}
	
	class Pass extends JPanel implements ActionListener
	{
		private Image bg=Toolkit.getDefaultToolkit().getImage("cimg/pass.png");
		public JButton yes=new JButton();
		public JButton no=new JButton();
		
		Pass()
		{
			this.setBounds(130, 100, 600, 300);
			this.setVisible(true);
			this.setOpaque(false);
			this.setFocusable(true);
			yes.setBounds(38, 65, 160, 65);
			yes.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("cimg/yes.gif")));
			yes.setRolloverIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("cimg/click_y.gif")));
			yes.setContentAreaFilled(false);
			yes.setBorderPainted(false);
			yes.addActionListener(this);
			yes.setFocusable(false);
			this.add(yes);
			no.setBounds(38, 130, 160, 65);
			no.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("cimg/no.gif")));
			no.setRolloverIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("cimg/click_n.gif")));
			no.setContentAreaFilled(false);
			no.setBorderPainted(false);
			no.addActionListener(this);
			no.setFocusable(false);
			this.add(no);
		}
		
		public void actionPerformed(ActionEvent e)
		{
			if(e.getSource()==yes)
			{
				this.setVisible(false);
				fr.getLayeredPane().remove(this);
				destroy();
				state=1;
				CFrame.message.time.isStop=false;
				isStop=false;
				repaint();
			}
			else if(e.getSource()==no)
			{
				this.setVisible(false);
				fr.getLayeredPane().remove(this);
				destroy();
				fr.back();
			}
		}
		
		public void paintComponent(Graphics g)
		{
			super.paintComponent(g);
			g.drawImage(bg, 0, 0, 600, 300, this);
		}
	}
	
	void init()
	{
		snake=new Snake(this);
		CFrame.message.dps.init();
		switch(CFrame.stage)
		{
			case 1:
				{
					rabbit=new Rabbit(this);
				}break;
			case 2:
			{
				box=new Box(this);
			}break;
			case 3:
			{
				mouse=new Mouse(this);
			}break;
		}
		state=1;
		repaint();
		if(mthread==null||(mthread!=null&&!mthread.isAlive()))
		{
			mthread=new Thread(this);
			mthread.start();
		}	
	}
	
	void eat()  //实例不为null情况下
	{
		switch(CFrame.stage)
		{
			case 1:
			{
				if(rabbit!=null&&rabbit.isAte())      
				{
					state=2;
					rabbit.isStop=true;
					rabbit.dealEat();
					rabbit=null;
					rabbit=new Rabbit(this);
				}
			}break;
			case 2:
			{
				if(box!=null&&box.isAte())      
				{
					state=2;
					box.isStop=true;
					box.dealEat();
					box=null;
					box=new Box(this);
				}
			}break;
			case 3:
			{
				if(mouse!=null&&mouse.isAte())      
				{
					state=2;
					mouse.isStop=true;
					mouse.dealEat();
					mouse=null;
					mouse=new Mouse(this);
				}
			}break;
		}
		
	}
	
	void isNeed()     //是否需要物品
	{
		if(!haveFood)
		{
			switch(CFrame.stage)
			{
				case 1:
				{
					rabbit=new Rabbit(this);
				}break;
				case 2:
				{
					box=new Box(this);
				}break;
				case 3:
				{
					mouse=new Mouse(this);
				}break;
			}
		}
		repaint();
		
	}
	
	boolean isHole()
	{
		if(map.getmap(snake.head.getX(), snake.head.getY())==5)
			return true;
		return false;
	}
	
	void dealHole()
	{
		CPlay.state=3;
		switch(CFrame.stage)
		{
			case 1:
			{
				//销毁当前游戏物件
				if(rabbit!=null&&rabbit.getS()>1)
					rabbit.isStop=true;
				rabbit=null;
				CFrame.stage=2;	
			}break;
			case 2:
			{
				//销毁当前游戏物件
				if(box!=null&&box.getS()>1)
					box.isStop=true;
				box=null;
				CFrame.stage=3;	
			}break;
			case 3:
			{
				//销毁当前游戏物件
				if(mouse!=null&&mouse.getS()>1)
					mouse.isStop=true;
				mouse=null;
				CFrame.stage=1;	
			}break;
		}
		CFrame.message.time.isStop=true;
		map=new DrawMap();
		snake.head=null;
		snake.body.removeAllElements();
		snake.tail=null;
		this.holing=true;
		snake.snakeToHole();
		fr.bg.repaint();
		repaint();
		showHoling();
		
	}
	
	public void run()
	{
		Loop:while(state>0)
		{
			if(state==1)    //记得最后检测snake.sum是否》100
			{
				while(speedup>=0)
				{
					if(this.isHole())
						this.dealHole();
					this.isNeed();
					snake.move();
					repaint();
					if(snake.isCollision())
					{
						gameover(snake.death);
						break Loop;
					}
					eat();
					speedup--;
				}
				speedup=0;
			}
			try{
				Thread.sleep(350);
			}catch(InterruptedException e){
				e.printStackTrace();
			}
		}
	}
	
	void showHoling()
	{
		showHole=new Hole();
		fr.getLayeredPane().add(showHole,JLayeredPane.POPUP_LAYER);
		final double a=Math.PI/180;
		new Thread(new Runnable(){
			public void run()
			{
				while(showHole.degree<361.0)
				{
					showHole.degree+=5.0;
					showHole.x1=showHole.x0+Math.sin(showHole.degree*a+showHole.leftangle)*65;
					showHole.y1=showHole.y0-Math.cos(showHole.degree*a+showHole.leftangle)*65;
					showHole.x2=showHole.x0+Math.sin(showHole.degree*a)*30;
					showHole.y2=showHole.y0-Math.cos(showHole.degree*a)*30;
					showHole.repaint();
					try{
						Thread.sleep(100);
					}catch(InterruptedException e){
						e.printStackTrace();
					}
					
				}
				showHole.setVisible(false);
				fr.getLayeredPane().remove(showHole);
				showHole=null;
				CFrame.message.time.isStop=false;
				state=1;
			}
		}).start();
	}
	
	void gameover(int death)     //记录结束信息
	{
		state=2;
		CFrame.message.time.isStop=true;
		isStop=true;
		clearW();
		over=new Over(CFrame.stage,death);
		fr.getLayeredPane().add(over,JLayeredPane.POPUP_LAYER);
	}
	
	void gamepass()
	{
		state=2;    //修改游戏状态
		CFrame.message.time.isStop=true;
		isStop=true;
		clearW();
		pass=new Pass();
		fr.getLayeredPane().add(pass,JLayeredPane.POPUP_LAYER);
	}
	
	void destroy()
	{
		if(over!=null)
			over=null;
		if(pass!=null)
			pass=null;
	}
	
	void clearW()
	{
		switch(CFrame.stage)
		{
		case 1:
			if(rabbit!=null)
			{
				rabbit.clear();
				repaint();
				rabbit=null;
			}
			break;
		case 2:
			if(box!=null)
			{
				box.clear();
				repaint();
				box=null;
			}
			break;
		case 3:
			if(mouse!=null)
			{
				mouse.clear();
				repaint();
				mouse=null;
			}
			break;
		}
	}

	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		map.drawMap(g,this,snake);
		if(state>0)
		{
			snake.drawSnake(g, this);
			switch(CFrame.stage)
			{
			case 1:if(rabbit!=null)
				rabbit.paint(g,this);
			break;
			case 2:if(box!=null)
				box.paint(g,this);
			break;
			case 3:if(mouse!=null)
				mouse.paint(g,this);
			break;
			}
		}
		
	}
}
