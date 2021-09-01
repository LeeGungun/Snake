package challenge;

import javax.swing.*;
import java.util.*;
import java.text.*;
import java.awt.*;
import java.awt.geom.*;

public class ShowGM extends JPanel{
	
	BackgroundPane bg;
	TimeScreen time;
	DrawPointSum dps;
	
	ShowGM()
	{
		this.setLayout(null);
		this.setOpaque(false);
		this.setSize(501, 700);
		bg=new BackgroundPane();
		this.add(bg);
		time=new TimeScreen();
		bg.add(time);
		dps=new DrawPointSum();
		bg.add(dps);
	}
	
	class DrawPointSum extends JPanel 
	{
		private Integer sum=0;
		private int advance=200;
		public boolean isDraw=false;     //记录是否已画变化时的图
		public int change=0;
		private Image iadd=Toolkit.getDefaultToolkit().getImage("cimg/add.png");
		private Image ireduce=Toolkit.getDefaultToolkit().getImage("cimg/reduce.png");
		private int step=0;    //动画计步器

		
		DrawPointSum()
		{	
			this.setLayout(null);
			this.setBounds(50, 80, 250, 330);
			this.setOpaque(false);	
		}
		
		public void setSum(int sum)
		{
			this.sum=sum;
		}
		
		public synchronized void add()
		{
			synchronized(sum){
			change=1;
			isDraw=true;
			new Thread(new Runnable(){
				public void run()
				{
					while(step<7&&isDraw)
					{
						repaint();
						step++;
						
						try{
							Thread.sleep(200);
						}catch(InterruptedException e){
							e.printStackTrace();
						}
						if(step==7)
						{
							isDraw=false;
							step=0;
							sum++;
						}
					}
				}
			}).start();
			repaint();
			}
		}
		
		public synchronized void reduce()
		{
			synchronized(sum)
			{
			change=-1;
			isDraw=true;
			new Thread(new Runnable(){
				public void run()
				{
					while(step<10&&isDraw)
					{
						repaint();
						step++;
						
						try{
							Thread.sleep(200);
						}catch(InterruptedException e){
							e.printStackTrace();
						}
						if(step==10)
						{
							isDraw=false;
							step=0;
							sum--;
						}
					}
				}
			}).start();
			repaint();
			}
		}
		
		public void dead()
		{
			sum=0;
			repaint();
		}
		
		public void init()
		{
			sum=4;
			repaint();
		}
		
		public void paintComponent(Graphics g)
		{
			
			super.paintComponent(g);
			Graphics2D g2=(Graphics2D)g;
			g2.setColor(new Color(145,61,30));
			g2.setStroke(new BasicStroke(20.0f,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND,0));
			Line2D l1=new Line2D.Double(50, 300,190 ,300);
			Line2D l2=new Line2D.Double(90, 30, 90, 300);
			Line2D l3=new Line2D.Double(45, 70, 90, 30);
			Line2D l4=new Line2D.Double(90, 30, 135, 70);
			g2.draw(l1);
			g2.draw(l2);
			g2.draw(l3);
			g2.draw(l4);
			g2.setColor(Color.magenta);
			g2.setStroke(new BasicStroke(10.0f,BasicStroke.CAP_BUTT,BasicStroke.JOIN_BEVEL,0,new float[]{10,5},0));
			Line2D l5=new Line2D.Double(50, 290-5-advance, 190, 290-5-advance);
			g2.draw(l5);
			g.setColor(Color.MAGENTA);
			g.setFont(new Font("迷你简胖娃",Font.PLAIN,20));
			g.drawString(sum.toString(), 50, 290-sum%advance);
			g.drawString(new Integer((sum/advance)*advance+advance).toString(), 140, 60);
			if(change>=0)
			{
				g.setColor(Color.red);
				
			}
			else
			{
				g.setColor(Color.BLACK);
			}
			if(sum>0)
				g.fillRect(100,290-(sum%advance) , 40, sum%advance);
			if(isDraw)
			{
				switch(change)
				{
					case -1:
					{
						g.drawImage(ireduce, 80, 160, 170, 220, 0, step*60, 90, step*60+60, this);
					}break;
					case 1:
					{
						g.drawImage(iadd, 80, 160, 180, 210, 0, step*50, 100, step*50+50, this);
					}break;
				}
			}
			
		}
		
		
	}
	
	class TimeScreen extends JLabel{
		
		public boolean isStop=false;    //过关窗口弹出或重玩
		Date before,stop,now;
		java.util.Timer t;
		Task task;
		final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm:ss");
		
		TimeScreen()
		{
			this.setOpaque(false);
			this.setBounds(25, 400, 300, 80);
			this.setHorizontalAlignment(CENTER);
			this.setForeground(Color.BLUE);
			Font f=new Font("迷你简胖头鱼",Font.PLAIN,36);
			this.setFont(f);
			String s="00:00:00";
			this.setText(s);
			this.setVisible(true);
			t=new java.util.Timer();
		}
		
	    class Task extends TimerTask
	    {
	    	public void run()
	    	{
	    		if(CPlay.state>0)
	    		{
	    			if(!isStop)
	    			{
	    				before=now;
	    				now=new Date(before.getTime()+1000);
	    				stop=now;
	    				write();
	    			}
	    			else
	    			{
	    				before=stop;
	    				now=new Date(before.getTime()+1000);
	    				write();  			
	    			}
	    		}
	    		else
	    			this.cancel();
	    	}
	    }
		public void TimeScreenStart()
		{
			before=new Date();
			before.setHours(0);
			before.setMinutes(0);
			before.setSeconds(0);
			now=before;
			stop=before;
			task=new Task();
			t.schedule(task, 1000,1000);
		}

		
		private void write()
		{
			if(!isStop)
			{
				
				String s=TIME_FORMAT.format(now);
				this.setText(s);
			}
			else
			{
				String s=TIME_FORMAT.format(stop);
				this.setText(s);
			}
		}	
	}
	
	 class BackgroundPane extends JPanel     //背景面板类
	{
		Image bgimg=Toolkit.getDefaultToolkit().getImage("cimg/label.gif");
		
		BackgroundPane()
		{
			this.setBounds(0, 0, 501, 700);
			this.setLayout(null);
			this.setOpaque(false);
		}
		
		public void paintComponent(Graphics g)
		{
			super.paintComponent(g);
			g.drawImage(bgimg, 0, 0, this);
		}
	}

}
