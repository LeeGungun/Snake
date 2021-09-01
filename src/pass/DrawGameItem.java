package pass;

import gameitem.*;
import java.util.*;
import java.awt.*;
import java.awt.image.ImageObserver;

public class DrawGameItem {
	
	private Random r1[]=new Random[2];
	private Random r2[]=new Random[2];
	private Random r3[]=new Random[2];
	protected GameItem items[]=new GameItem[2];
	private int i,j;
	private int x[]=new int[2];
	private int y[]=new int[2];
	private Vector<String> itemsSave=new Vector<String>();     //存储物品,存储时0代表苹果，1代表面包，2代表炸弹，3代表皇冠
	
	class Mytask extends TimerTask{
		
		Play play;
		Crown crown;
		int i=0;
		public Mytask(Play p,Crown c,int i)
		{
			play=p;
			crown=c;
			this.i=i;
		}
		public void run(){
			if(crown!=null)
			{
				if(!play.isOver&&!play.isCrown&&play.getScore()<Play.passScore[play.grade])
				{
					crown.cleanItem();
					play.repaint();
					removeCrown(i);
					addToDraw(play);
				}
				
			}
			this.cancel();

		}
	}
	
	protected DrawGameItem()
	{
		
	}
	
	protected DrawGameItem(Play p)
	{
		items[0]=new GameItem();
		items[1]=new GameItem();
		for( i=0;i<items.length;i++)//生成两个随机点
		{
			Loop:do{
				r2[i]=new Random(System.currentTimeMillis());
				r3[i]=new Random(System.currentTimeMillis());
				x[i]=(Math.abs(r2[i].nextInt())%p.maxmap)*p.minlength+90;
				if(x[i]<90||x[i]>690-p.minlength)
					continue;
				y[i]=(Math.abs(r3[i].nextInt())%p.maxmap)*p.minlength+43;
				if(y[i]<43||y[i]>643-p.minlength)
					continue;
				if(i>0)
				{
					for(j=0;j<items.length;j++)
						if(i!=j&&x[i]==items[j].getX()&&y[i]==items[j].getY())
							continue Loop;
				}
				//生成点是否在蛇上
				if(!p.snake.isEqual(new Point(x[i], y[i])))
					break;
			}while(true);
	
			r1[i]=new Random(System.currentTimeMillis());
			if(i==0)
			{
				switch(Math.abs(r1[i].nextInt())%4)
				{
				case 0:	
				{
					items[i]=new Apple(x[i],y[i]);
					save(items[i],0,i);
				}
					break;
				case 1:
				{
					items[i]=new Bread(x[i],y[i]);
					save(items[i],1,i);
				}
					break;
				case 2:
				{
					items[i]=new Bomb(x[i],y[i]);
					save(items[i],2,i);
				}
					break;
				case 3:
				{
					items[i]=new Crown(x[i],y[i]);
					save(items[i],3,i);
					Timer timer=new Timer(false);
					timer.schedule(new Mytask(p,(Crown)items[i],i), 5000);
				}
				break;
				}
				p.store("2");
			}
			else
			{
				switch(Math.abs(r1[i].nextInt())%3)
				{
				case 0:
				{
					items[i]=new Apple(x[i],y[i]);
					save(items[i],0,i);
				}
					break;
				case 1:
				{
					items[i]=new Bread(x[i],y[i]);
					save(items[i],1,i);
				}
					break;
				case 2:
				{
					items[i]=new Bomb(x[i],y[i]);
					save(items[i],2,i);
				}
					break;
				}
				p.store("2");
			}
		}
		
	}
	
	private void save(GameItem g,int i,int j)    //保存节点
	{
		itemsSave.addElement(g.getX()+"-"+g.getY()+"-"+i+"-"+j);
	}
	
	protected void drawAllItem(Graphics g,ImageObserver im,int m)
	{
	
		for(i=0;i<this.items.length;i++)
			if(items[i]!=null)
				items[i].drawItem(g, im,m);
	}
	
	 Vector<String> getSave()      //得到物品存储
	{
		return itemsSave;
	}
	
	 protected void removeCrown(int i)
	{
			if(items[i]!=null&&items[i] instanceof Crown)
				items[i]=null;
	}
	
     protected void addToDraw(Play p)
	{
	
		for(i=0;i<items.length;i++)     //得到消失的item
			if(items[i]==null)
			{
				r1[i]=new Random(System.currentTimeMillis());
				Loop:do{
					r2[i]=new Random(System.currentTimeMillis());
					r3[i]=new Random(System.currentTimeMillis());
					x[i]=(Math.abs(r2[0].nextInt())%p.maxmap)*p.minlength+90;
					if(x[i]<90||x[i]>690-p.minlength)
						continue;
					y[i]=(Math.abs(r3[0].nextInt())%p.maxmap)*p.minlength+43;
					if(y[i]<43||y[i]>643-p.minlength)
						continue;
					for(j=0;j<items.length;j++)
							if(j!=i&&items[j]!=null&&x[i]==items[j].getX()&&y[i]==items[j].getY())   //是否与其他道具相冲
								continue Loop;
					//生成点是否在蛇上
					if(!p.snake.isEqual(new Point(x[i], y[i])))
						break;
				}while(true);
				if(i<items.length)
				{
					switch(Math.abs(r1[i].nextInt())%6)
					{
					case 0:
					{
						items[i]=new Apple(x[i],y[i]);
						save(items[i],0,i);
					}
						break;
					case 1:
					{
						items[i]=new Bread(x[i],y[i]);
						save(items[i],1,i);
					}
						break;
					case 2:
					{
						items[i]=new Bomb(x[i],y[i]);
						save(items[i],2,i);
					}
						break;
					case 3:
					{
						items[i]=new Apple(x[i],y[i]);
						save(items[i],0,i);
					}
						break;
					case 4:
					{
						items[i]=new Bread(x[i],y[i]);
						save(items[i],1,i);
					}
						break;
					case 5:
					{
						items[i]=new Crown(x[i],y[i]);
						save(items[i],3,i);
						Timer timer=new Timer(false);
						timer.schedule(new Mytask(p,(Crown)items[i],i), 3000);
					}
					break;
					}
					p.store("2");
				}
			}
	}

}