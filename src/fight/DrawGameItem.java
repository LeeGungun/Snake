package fight;

import gameitem.*;
import java.util.*;
import java.awt.*;
import java.awt.image.ImageObserver;

public class DrawGameItem {
		
		private Random r1[]=new Random[4];    //用于生成物品
		private Random r2[]=new Random[4];    //用于生成坐标x
		private Random r3[]=new Random[4];    //用于生成坐标y
		GameItem items[]=new GameItem[4];
		private int x[]=new int[4];     //用于生成坐标x
		private int y[]=new int[4];     //用于生成坐标y
		private Vector<Water> river=new Vector<Water>();     //用于生成河流    
		private Timer flood;
		private Play pane=null;
		private PlayOnline paneo=null;
		
		class MyTask extends TimerTask{
		
			public void run(){
				if(pane!=null)
				{
				if(!pane.isOver&&pane.state==1)
				{
					addRiver();
					if(pane.fr.getM()==0)
					{
						if(pane.snake[1].isHaveObstacle())
						{
							pane.snake[1].dealObstacle();
							pane.repaint();
						}
					}
				}
				else
					if(pane.isOver)
						this.cancel();
				}
				if(paneo!=null)
				{
					if(!paneo.isOver&&paneo.state==1)
					{
						addRiver(paneo);
					}
					else
						if(paneo.isOver)
							this.cancel();
				}

			}
		}
		
		DrawGameItem()
		{
			
		}
		
		DrawGameItem(Play pane)
		{
			this.pane=pane;
			int i,j=0;
			for(i=0;i<items.length;i++)
				items[i]=new GameItem();
			for( i=0;i<items.length;i++)//生成物品
			{
				Loop:do{        //生成节点
					r2[i]=new Random(System.currentTimeMillis());
					r3[i]=new Random(System.currentTimeMillis());
					x[i]=Math.abs(r2[i].nextInt())%20+1;
					y[i]=Math.abs(r3[i].nextInt())%40+1;
					if(pane.map.getmap(x[i], y[i])!=1)     //检测此地是否为墙壁
						continue Loop;
					if(i>0)   //检测是否与其他物品相冲
					{
						for(j=0;j<items.length;j++)     
							if(i!=j&&items[i]!=null&&x[i]==items[j].getX()&&y[i]==items[j].getY())
								continue Loop;
					}
					//生成点是否在蛇上
					for(j=0;j<pane.snake.length;j++)
						if(pane.snake[j].isEqual(new Point(x[i], y[i])))
							continue Loop;
					break Loop;
				}while(true);
		
				r1[i]=new Random(System.currentTimeMillis());
				switch(Math.abs(r1[i].nextInt())%4)
				{
				case 0:	
					items[i]=new Apple(x[i],y[i]);break;
				case 1:
					items[i]=new Bread(x[i],y[i]);break;
				case 2:
					items[i]=new Bomb(x[i],y[i]);break;
				case 3:	
					items[i]=new Apple(x[i],y[i]);break;
				}
				pane.map.setmap(items[i].getX(), items[i].getY(), 5);
				
			}
			//生成初始河流
			Random r4,r5;
			int x,y;
			Loop2:do{        
				r4=new Random(System.currentTimeMillis());
				r5=new Random(System.currentTimeMillis());
				x=Math.abs(r4.nextInt())%20+1;
				y=Math.abs(r5.nextInt())%40+1;
				if(pane.map.getmap(x, y)!=1)     //检测此地是否为地板
					continue Loop2;
				for(i=0;i<items.length;i++)     
					if(x==items[i].getX()&&y==items[i].getY())
						continue Loop2;
				//生成点是否在蛇上
				for(i=0;i<pane.snake.length;i++)
					if(pane.snake[i].isEqual(new Point(x, y)))
						continue Loop2;
				break Loop2;
			}while(true);
			river.addElement(new Water(x,y));
			pane.map.setmap(x, y, 6);
			flood=new Timer();
			flood.schedule(new MyTask(), 10000, 10000);
			
		}
		
		DrawGameItem(PlayOnline pane)
		{
			this.paneo=pane;
			int i,j=0;
			for(i=0;i<items.length;i++)
				items[i]=new GameItem();
			for( i=0;i<items.length;i++)//生成物品
			{
				Loop:do{        //生成节点
					r2[i]=new Random(System.currentTimeMillis());
					r3[i]=new Random(System.currentTimeMillis());
					x[i]=Math.abs(r2[i].nextInt())%20+1;
					y[i]=Math.abs(r3[i].nextInt())%40+1;
					if(pane.map.getmap(x[i], y[i])!=1)     //检测此地是否为墙壁
						continue Loop;
					if(i>0)   //检测是否与其他物品相冲
					{
						for(j=0;j<items.length;j++)     
							if(i!=j&&items[i]!=null&&x[i]==items[j].getX()&&y[i]==items[j].getY())
								continue Loop;
					}
					//生成点是否在蛇上
					for(j=0;j<pane.snake.length;j++)
						if(pane.snake[j].isEqual(new Point(x[i], y[i])))
							continue Loop;
					break Loop;
				}while(true);
		
				r1[i]=new Random(System.currentTimeMillis());
				switch(Math.abs(r1[i].nextInt())%4)
				{
				case 0:	
					items[i]=new Apple(x[i],y[i]);break;
				case 1:
					items[i]=new Bread(x[i],y[i]);break;
				case 2:
					items[i]=new Bomb(x[i],y[i]);break;
				case 3:	
					items[i]=new Apple(x[i],y[i]);break;
				}
				pane.map.setmap(items[i].getX(), items[i].getY(), 5);
				
			}
			//生成初始河流
			Random r4,r5;
			int x,y;
			Loop2:do{        
				r4=new Random(System.currentTimeMillis());
				r5=new Random(System.currentTimeMillis());
				x=Math.abs(r4.nextInt())%20+1;
				y=Math.abs(r5.nextInt())%40+1;
				if(pane.map.getmap(x, y)!=1)     //检测此地是否为地板
					continue Loop2;
				for(i=0;i<items.length;i++)     
					if(x==items[i].getX()&&y==items[i].getY())
						continue Loop2;
				//生成点是否在蛇上
				for(i=0;i<pane.snake.length;i++)
					if(pane.snake[i].isEqual(new Point(x, y)))
						continue Loop2;
				break Loop2;
			}while(true);
			river.addElement(new Water(x,y));
			pane.map.setmap(x, y, 6);
			flood=new Timer();
			flood.schedule(new MyTask(), 10000, 10000);
			
		}
		
		void drawAllItem(Graphics g,ImageObserver im)     //画物品
		{
			int i;
			for(i=0;i<this.items.length;i++)
				if(items[i]!=null)
					items[i].drawFightItem(g, im);
			for(i=0;i<this.river.size();i++)
				river.elementAt(i).drawFightItem(g, im);
		}

		Vector<Water> getR()
		{
			return river;
		}
		
	    void addToDraw()
		{
	    	int i,j=0;
			for(i=0;i<items.length;i++)     //得到消失的item
				if(items[i]==null)
				{
					Loop:do{        //生成节点
						r2[i]=new Random(System.currentTimeMillis());
						r3[i]=new Random(System.currentTimeMillis());
						x[i]=Math.abs(r2[i].nextInt())%20+1;
						y[i]=Math.abs(r3[i].nextInt())%40+1;
						if(pane.map.getmap(x[i], y[i])!=1)     //检测此地是否为墙壁
							continue Loop;
						//检测是否与其他物品相冲
						for(j=0;j<items.length;j++)     
							if(j!=i&&items[j]!=null&&x[i]==items[j].getX()&&y[i]==items[j].getY())
								continue Loop;
						for(j=0;j<river.size();j++)     //生成点是否用于河流相冲
							if(x[i]==river.elementAt(j).getX()&&y[i]==river.elementAt(j).getY())
								continue Loop;
						//生成点是否在蛇上
						for(j=0;j<pane.snake.length;j++)
							if(pane.snake[j].isEqual(new Point(x[i], y[i])))
								continue Loop;
						break;
					}while(true);
					r1[i]=new Random(System.currentTimeMillis());
					switch(Math.abs(r1[i].nextInt())%8)
					{
					case 0:	
						items[i]=new Apple(x[i],y[i]);break;
					case 1:
						items[i]=new Bread(x[i],y[i]);break;
					case 2:
						items[i]=new Bomb(x[i],y[i]);break;
					case 3:	
						items[i]=new Apple(x[i],y[i]);break;
					case 4:
						items[i]=new Bread(x[i],y[i]);break;
					case 5:	
						items[i]=new Apple(x[i],y[i]);break;
					case 6:
						items[i]=new Bread(x[i],y[i]);break;
					case 7:
						items[i]=new Bomb(x[i],y[i]);break;
					}
					pane.map.setmap(x[i], y[i], 5);

			}
		}
	    
	    void addToDraw(PlayOnline pane)
		{
	    	int i,j=0;
			for(i=0;i<items.length;i++)     //得到消失的item
				if(items[i]==null)
				{
					Loop:do{        //生成节点
						r2[i]=new Random(System.currentTimeMillis());
						r3[i]=new Random(System.currentTimeMillis());
						x[i]=Math.abs(r2[i].nextInt())%20+1;
						y[i]=Math.abs(r3[i].nextInt())%40+1;
						if(pane.map.getmap(x[i], y[i])!=1)     //检测此地是否为墙壁
							continue Loop;
						//检测是否与其他物品相冲
						for(j=0;j<items.length;j++)     
							if(j!=i&&items[j]!=null&&x[i]==items[j].getX()&&y[i]==items[j].getY())
								continue Loop;
						for(j=0;j<river.size();j++)     //生成点是否用于河流相冲
							if(x[i]==river.elementAt(j).getX()&&y[i]==river.elementAt(j).getY())
								continue Loop;
						//生成点是否在蛇上
						for(j=0;j<pane.snake.length;j++)
							if(pane.snake[j].isEqual(new Point(x[i], y[i])))
								continue Loop;
						break;
					}while(true);
					r1[i]=new Random(System.currentTimeMillis());
					switch(Math.abs(r1[i].nextInt())%8)
					{
					case 0:	
						items[i]=new Apple(x[i],y[i]);break;
					case 1:
						items[i]=new Bread(x[i],y[i]);break;
					case 2:
						items[i]=new Bomb(x[i],y[i]);break;
					case 3:	
						items[i]=new Apple(x[i],y[i]);break;
					case 4:
						items[i]=new Bread(x[i],y[i]);break;
					case 5:	
						items[i]=new Apple(x[i],y[i]);break;
					case 6:
						items[i]=new Bread(x[i],y[i]);break;
					case 7:
						items[i]=new Bomb(x[i],y[i]);break;
					}
					pane.map.setmap(x[i], y[i], 5);
					pane.send("gameitem|"+Integer.toString(i+1)+"|"+Integer.toString(x[i])+"|"+Integer.toString(y[i])+"|"+Integer.toString(pane.kind(i))+"|");

			}
		}
	    
	    void addRiver()    //注：此方法含重画
	    {
	    	Random r4,r5;
			int x,y,i=0;
			Loop:do{        
				r4=new Random(System.currentTimeMillis());
				r5=new Random(System.currentTimeMillis());
				x=Math.abs(r4.nextInt())%20+1;
				y=Math.abs(r5.nextInt())%40+1;
				if(pane.map.getmap(x, y)!=1)     //检测此地是否为墙壁
					continue ;
				for(i=0;i<items.length;i++)     
					if(x==items[i].getX()&&y==items[i].getY())
						continue Loop;
				for(i=0;i<river.size();i++)
					if(x==river.elementAt(i).getX()&&y==river.elementAt(i).getY())
						continue Loop;
				//生成点是否在蛇上
				for(i=0;i<pane.snake.length;i++)
					if(pane.snake[i].isEqual(new Point(x, y)))
						continue Loop;
				break;
			}while(true);
			river.addElement(new Water(x,y));
			pane.map.setmap(x, y, 6);
			pane.repaint();
	    }
	    
	    void addRiver(PlayOnline pane)    //注：此方法含重画
	    {
	    	Random r4,r5;
			int x,y,i=0;
			Loop:do{        
				r4=new Random(System.currentTimeMillis());
				r5=new Random(System.currentTimeMillis());
				x=Math.abs(r4.nextInt())%20+1;
				y=Math.abs(r5.nextInt())%40+1;
				if(pane.map.getmap(x, y)!=1)     //检测此地是否为墙壁
					continue ;
				for(i=0;i<items.length;i++)     
					if(x==items[i].getX()&&y==items[i].getY())
						continue Loop;
				for(i=0;i<river.size();i++)
					if(x==river.elementAt(i).getX()&&y==river.elementAt(i).getY())
						continue Loop;
				//生成点是否在蛇上
				for(i=0;i<pane.snake.length;i++)
					if(pane.snake[i].isEqual(new Point(x, y)))
						continue Loop;
				break;
			}while(true);
			river.addElement(new Water(x,y));
			pane.map.setmap(x, y, 6);
			pane.send("river|"+Integer.toString(x)+"|"+Integer.toString(y)+"|");
			pane.repaint();
	    }
	    
	    DrawGameItem(PlayOnline pane,int i)
	    {
	    	if(i==0)
	    		this.paneo=pane;
	    }
	    
	    void createWater(int x,int y)
	    {
	    	river.addElement(new Water(x,y));
	    	paneo.map.setmap(x, y, 6);
	    }
	    
	    void createItem(int num,int x,int y,int kind)
	    {
	    	switch(kind)
	    	{
	    	case 0:
	    		items[num]=new Apple(x,y);break;
	    	case 1:
	    		items[num]=new Bread(x,y);break;
	    	case 2:
	    		items[num]=new Bomb(x,y);break;
	    	}
	    	paneo.map.setmap(x, y, 5);
	    }

}


