package challenge;

import java.awt.*;
import java.util.*;
import java.awt.image.ImageObserver;

public class Box  implements Runnable{
	
	private int sort;     //��������,0��ʾ��ֹʳ�ú����ӣ�1��ʾ��ֹʹ�ú���٣�2��ʾ��̬ʳ�ú����ӣ�3��ʾ��̬ʹ�ú����
	private int sleepTime=0;     //�����ٶ�
	private Thread thread=null;
	private int direction=-1;
	private int x,y;
	private Image img=Toolkit.getDefaultToolkit().getImage("cimg/box.gif");
	private Image wimg=Toolkit.getDefaultToolkit().getImage("cimg/stage2w.gif");
	private Image timg=Toolkit.getDefaultToolkit().getImage("cimg/toxicfog.gif");
	boolean isAte=false;
	private CPlay pane=null;
	boolean isStop=false;    //���ͨ���˱��������ƴ����߳�
	Mbackground mbg;
	int haveCollision=0;    //��̬ʱת���3������ʧ
	boolean isClear=false;
	
	public Box(CPlay pane)
	{
		super();
		this.pane=pane;
		Random s=new Random(System.currentTimeMillis());
		int h=Math.abs(s.nextInt())%100;
		if(!(h>89||h<=49&&h>=40))
		{
			if(h%2==0)
				sort=2;
			else
				sort=0;
		}
		else
		{
			if(h%2==0)
				sort=3;
			else
				sort=1;
		}
		Loop:while(true)
		{
			Random r1=new Random(System.currentTimeMillis());
			Random r2=new Random(System.currentTimeMillis());
			int i=Math.abs(r1.nextInt())%18+1;
			int j=Math.abs(r2.nextInt())%38+1;
			if(pane.map.getmap(i, j)!=1)
				continue Loop;
			if(pane.snake.isEqual(new Point(i, j)))    //�������λ���Ƿ�������
				continue Loop;
			this.x=i;
			this.y=j;
			break Loop;	
		}
		pane.haveFood=true;
		if(sort>1)
		{
			sleepTime=(Math.abs((new Random(System.currentTimeMillis())).nextInt())%4)*50+400;
			while(true)
			{
				int d=Math.abs((new Random(System.currentTimeMillis())).nextInt())%4;
				if((d==1&&pane.snake.head.getD()==0)||(d==0&&pane.snake.head.getD()==1)||(d==2&&pane.snake.head.getD()==3)||(d==3&&pane.snake.head.getD()==2))
					continue;
				else
				{
					direction=d;
					break;
				}
			}
			activeinit();
		}
		
		
	}
	
	void activeinit()
	{
		thread=new Thread(this);
		thread.start();
	}

	
	public int getX()
	{
		return this.x;
	}
	
	public int getY()
	{
		return this.y;
	}
	
	int getS()
	{
		return this.sort;
	}
	
	boolean isAte()
	{
		if(pane.snake.isEqual(new Point(x,y)))
		{
			isAte=true;
			return true;
		}
		else
			return false;
	}
	
	synchronized void message()
	{
		switch(sort)
		{
			case 1:
			{
				mbg=new Mbackground(1);
			}break;
			case 3:
			{
				mbg=new Mbackground(3);
			}break;
		}
		CFrame.messagePane.add(mbg);
		new Thread(new Runnable(){
			public void run()
			{
				while(true)
				{
					if(!mbg.close)
						CFrame.messagePane.setVisible(true);
					else
					{
						CFrame.messagePane.setVisible(false);
						mbg=null;
						break;
					}
					try
					{
						Thread.sleep(500);
					}catch(InterruptedException e){
						e.printStackTrace();
					}
				}
			}
		}).start();
	}
	
	void dealEat()     //���Ե�ʱ�Ĵ���
	{
		switch(sort)
		{
			case 0:
			{
				pane.snake.addPoint();
				CFrame.message.dps.add();
				destory();
			}break;
			case 1:
			{
				if(pane.snake.sum>4)
				{
					pane.snake.reducePoint();
					CFrame.message.dps.reduce();
					message();
				}
				else
				{
					CFrame.message.dps.dead();
					pane.gameover(4);
				}
				destory();
			}break;
			case 2:
			{
				pane.snake.addPoint();
				CFrame.message.dps.add();
				destory();
			}break;
			case 3:
			{
				if(pane.snake.sum>4)
				{
					pane.snake.reducePoint();
					CFrame.message.dps.reduce();
					message();
				}
				else
				{
					CFrame.message.dps.dead();
					pane.gameover(5);
				}
				destory();
			}break;
		}
	}
	
	void clear()
	{
		this.isClear=true;
		this.isStop=true;
	}
	
	void destory()     //�������
	{
		if(pane==null)
			return;
		clear();
		pane.repaint();
		pane.haveFood=false;
		pane=null;    //��ֹ�߳�
		
	}
	
	void turn(int direction)
	{
		this.direction=direction;
		this.haveCollision++;
		if(this.haveCollision==3)
			destory();
	}
	
	void move()
	{
		switch(direction)
   		{
   			case 0:
   			{
   				if(x-1<=0||pane.map.getmap(x-1, y)!=1)
   				{
   					if(y-1<=0||pane.map.getmap(x, y-1)!=1)
   					{
   						if(y+1>=39||pane.map.getmap(x, y+1)!=1)
   						{
   							turn(1);
   							x++;
   						}
   						else
   						{
   							turn(3);
   							y++;
   						}
   					}
   					else
   					{
   						turn(2);
   						y--;
   					}
   				}
   				else
   					x--;
   			}break;
   			case 1:
   			{
   				if(x+1>=19||pane.map.getmap(x+1, y)!=1)
   				{
   					if(y-1<=0||pane.map.getmap(x, y-1)!=1)
   					{
   						if(y+1>=39||pane.map.getmap(x, y+1)!=1)
   						{
   							turn(0);
   							x--;
   						}
   						else
   						{
   							turn(3);
   							y++;
   						}
   					}
   					else
   					{
   						turn(2);
   						y--;
   					}
   				}
   				else
   					x++;
   			}break;
   			case 2:
   			{
   				if(y-1<=0||pane.map.getmap(x, y-1)!=1)
   				{
   					if(x-1<=0||pane.map.getmap(x-1, y)!=1)
   					{
   						if(x+1>=19||pane.map.getmap(x+1, y)!=1)
   						{
   							turn(3);
   							y++;
   						}
   						else
   						{
   							turn(1);
   							x++;
   						}
   					}
   					else{
   						turn(0);
   						x--;
   					}
   				}
   				else
   					y--;
   			}break;
   			case 3:
   			{
   				if(y+1>=39||pane.map.getmap(x, y+1)!=1)
   				{
   					if(x-1<=0||pane.map.getmap(x-1, y)!=1)
   					{
   						if(x+1>=19||pane.map.getmap(x+1, y)!=1)
   						{
   							turn(2);
   							y--;
   						}
   						else
   						{
   							turn(1);
   							x++;
   						}
   					}
   					else
   					{
   						turn(0);
   						x--;
   					}
   				}
   				else
   					y++;
   			}break;
   		}
		
	}
	
	public void run()
	{
		while(pane!=null&&!isStop)
		{
			if(!isStop)
			{
				move();
				if(pane!=null)
					pane.repaint();
				try{
					Thread.sleep(sleepTime);
				}catch(InterruptedException e){
					e.printStackTrace();
				}
			
			}
		}
	}
	
	boolean draw()     //�ж��Ƿ��ڵ�ǰ��Ұ
	{
		if((x>=DrawMap.x1&&x<=DrawMap.x2))
			if((y>=DrawMap.y1&&y<=DrawMap.y2))
				return true;
		return false;
	}
	
	
	
	void paint(Graphics g,ImageObserver im)
	{
		
		if(CPlay.state==1||(CPlay.state==2&&!CPlay.isStop))
		{
			if(draw())
			{
				int i=Math.abs(y-DrawMap.y1),j=Math.abs(x-DrawMap.x1);
				if(!isClear)
				{
					if(!isAte)
						g.drawImage(img, i*CFrame.px[1], j*CFrame.px[1], CFrame.px[1],CFrame.px[1],im);
					else
					{
						switch(sort)
						{
						case 0:g.drawImage(wimg, i*CFrame.px[1], j*CFrame.px[1],(i+1)*CFrame.px[1], (j+1)*CFrame.px[1],0,0, 30,30,im);break;
						case 1:g.drawImage(wimg,   i*CFrame.px[1], j*CFrame.px[1],(i+1)*CFrame.px[1], (j+1)*CFrame.px[1],60,0,90,30,im);break;
						case 2:g.drawImage(wimg,  CFrame.px[1], CFrame.px[1],(i+1)*CFrame.px[1], (j+1)*CFrame.px[1],30,0,60,30,im);break;
						case 3:g.drawImage(timg,  i*CFrame.px[1], j*CFrame.px[1],im);break;
						}
					}
				}
				else
				{
					int kind=pane.map.getmap(x, y);
					g.drawImage(pane.map.img, i*pane.map.px,j*pane.map.px, (i+1)*pane.map.px, (j+1)*pane.map.px, (kind-1)*30, 0, kind*30, 30, im);
				}
			}
		}
		else
		{
			g.drawImage(img,y*CFrame.px[0], x*CFrame.px[0], CFrame.px[0], CFrame.px[0], im);
		}
	}
	



}
