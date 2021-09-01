package challenge;

import java.awt.*;
import java.util.*;
import java.awt.image.ImageObserver;

public class Mouse  implements Runnable{
	
	private int sort;     //生成种类,0表示静止食用后增加，1表示静止使用后减少，2表示动态食用后增加，3表示动态使用后减少
	private int sleepTime=0;     //控制速度
	private Thread thread=null;
	private int direction=-1;
	private int x,y;
	private Image img=Toolkit.getDefaultToolkit().getImage("cimg/mouse2.gif");
	private Image imgappear=Toolkit.getDefaultToolkit().getImage("cimg/mouse1.gif");
	private Image imgr=Toolkit.getDefaultToolkit().getImage("cimg/imounse.gif");
	private CPlay pane=null;
	boolean isStop=false;    //外界通过此变量来控制此类线程
	Mbackground mbg;
	int haveCollision=0;    //动态时转向第3次则消失
	private boolean turnup=false;
	private int step=0;
	private boolean cheat=false;
	boolean isClear=false;
	
	public Mouse(CPlay pane)
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
			if(pane.snake.isEqual(new Point(i, j)))    //检测生成位置是否在蛇上
				continue Loop;
			this.x=i;
			this.y=j;
			break Loop;	
		}
		pane.haveFood=true;
		turnup=true;
		final CPlay pane_up=pane;
		new Thread(new Runnable(){
			public void run()
			{
				while(step<3)
				{
					pane_up.repaint();
					try{
						Thread.sleep(80);
					}catch(InterruptedException e)
					{
						e.printStackTrace();
					}
					step++;
				}
				turnup=false;
			}
		}).start();
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
			return true;
		else
			return false;
	}
	
	synchronized void message()
	{

		mbg=new Mbackground();
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
	
	void dealEat()     //被吃到时的处理
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
					pane.gameover(3);
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
					pane.gameover(3);
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
	
	void destory()     //销毁组件
	{
		if(pane==null)
			return;
		clear();
		pane.repaint();
		pane.haveFood=false;
		pane=null;    //终止线程
		
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
   				if(x-1<=0||!(pane.map.getmap(x-1, y)==1||pane.map.getmap(x-1, y)==3))
   				{
   					if(y-1<=0||!(pane.map.getmap(x, y-1)==1||pane.map.getmap(x, y-1)==3))
   					{
   						if(y+1>=39||!(pane.map.getmap(x, y+1)==1||pane.map.getmap(x, y+1)==3))
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
   				if(x+1>=19||!(pane.map.getmap(x+1, y)==1||pane.map.getmap(x+1, y)==3))
   				{
   					if(y-1<=0||!(pane.map.getmap(x, y-1)==1||pane.map.getmap(x, y-1)==3))
   					{
   						if(y+1>=39||!(pane.map.getmap(x, y+1)==1||pane.map.getmap(x, y+1)==3))
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
   				if(y-1<=0||!(pane.map.getmap(x, y-1)==1||pane.map.getmap(x, y-1)==3))
   				{
   					if(x-1<=0||!(pane.map.getmap(x-1, y)==1||pane.map.getmap(x-1, y)==3))
   					{
   						if(x+1>=19||!(pane.map.getmap(x+1, y)==1||pane.map.getmap(x+1, y)==3))
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
   				if(y+1>=39||!(pane.map.getmap(x, y+1)==1||pane.map.getmap(x, y+1)==3))
   				{
   					if(x-1<=0||!(pane.map.getmap(x-1, y)==1||pane.map.getmap(x-1, y)==3))
   					{
   						if(x+1>=19||!(pane.map.getmap(x+1, y)==1||pane.map.getmap(x+1, y)==3))
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
	
	boolean draw()     //判断是否在当前视野
	{
		if((x>=DrawMap.x1&&x<=DrawMap.x2)||(x>=DrawMap.x2&&x<=DrawMap.x1))
			if((y>=DrawMap.y1&&y<=DrawMap.y2)||((y>=DrawMap.y2&&y<=DrawMap.y1)))
				return true;
		return false;
	}
	
	/*int iconNum()      //确定当前需要显示的图片在集合图片的位置
	{
		int num=3;
		if(this.direction!=-1)
		{
			int d1=pane.snake.head.getD(),d2=this.direction,i=pane.snake.head.getX(),j=pane.snake.head.getY();
			switch(d1)
			{
				case 0:
				{
					num=d2;
				}break;
				case 1:
				{
					if(d2==0)
						num=3;
					else if(d2==1)
						num=0;
					else if(d2==2)
						num=2;
					else
						num=1;
				}break;
				case 2:
				{
					if(d2==0)
						num=2;
					else if(d2==1)
						num=1;
					else if(d2==2)
						num=0;
					else
						num=3;
				}break;
				case 3:
				{
					if(d2==0)
						num=1;
					else if(d2==1)
						num=2;
					else if(d2==2)
						num=3;
					else
						num=0;
				}break;
			}
		}
		return num;
	}*/
	
	 void paint(Graphics g,ImageObserver im)
	{
		
		if(CPlay.state==1||(CPlay.state==2&&!CPlay.isStop))
		{
			if(draw())
			{
				int i=Math.abs(y-DrawMap.y1),j=Math.abs(x-DrawMap.x1);
				if(this.turnup)
				{
					g.drawImage(imgappear, i*CFrame.px[1], j*CFrame.px[1], (i+1)*CFrame.px[1],(j+1)* CFrame.px[1], 0, step*30, 30, step*30+30, im);
				}
				else if(cheat)
				{
					cheat=false;
					g.drawImage(imgr, i*CFrame.px[1], j*CFrame.px[1],  im);
				}
				else if(!isClear)
				{
					if(sort>1)
					{
						g.drawImage(img, i*CFrame.px[1], j*CFrame.px[1], (i+1)*CFrame.px[1],(j+1)* CFrame.px[1],direction*30,30,direction*30+30, 60, im);
					}
					else
						g.drawImage(img,i*CFrame.px[1], j*CFrame.px[1], (i+1)*CFrame.px[1],(j+1)* CFrame.px[1], 90, 0, 120, 30, im);
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
			g.drawImage(img,y*CFrame.px[0], x*CFrame.px[0], (y+1)*CFrame.px[0], (x+1)*CFrame.px[0], 90, 0, 120, 30, im);
		}
	}
	

}
