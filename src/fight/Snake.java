package fight;

import gameitem.*;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.util.Random;
import java.util.Vector;
import java.awt.image.ImageObserver;

public class Snake {
	
	int sum;  //记录蛇体节点总数
	SnakePoint head;    //蛇头
	Vector<SnakePoint> body=new Vector<SnakePoint>();    //蛇身
	SnakePoint tail[]=new SnakePoint[2];    //蛇尾
	boolean isAte=false;    //是否吃到物品
	Point trash=new Point();    //记录擦除信息坐标
	boolean haveTrash=false;    //是否有擦除点
	boolean istwice=false;    //用于碰撞判断，有了它，才可在边界处实现转向
	private int death=0;  //记录死因
	private GameItem aim=null;  //人机对战时机器的目标食物
	private int path=10000;   //设定一初始大值，用于人机对战存储目标路径
	private Play pane;
	private int snakenum;   //记录蛇编号
	
	
	public Snake()
	{
		
	}
	
	public Snake(int snakenum,Play pane)
	{
		this.sum=4;
		this.pane=pane;
		this.snakenum=snakenum;
		Random r1,r2,r3;
		int x,y,z,i;
		Loop:while(true)
		{
			r1=new Random(System.currentTimeMillis());
			r2=new Random(System.currentTimeMillis());
			x=Math.abs(r1.nextInt())%14+4;
			y=Math.abs(r2.nextInt())%34+4;
			if(pane.map.getmap(x, y)!=1)
				continue Loop;
			if(snakenum==2)
			{
				Point p=new Point(x,y);
				if(pane.snake[0].isEqual(p))
					continue Loop;
			}
			r3=new Random(System.currentTimeMillis());
			z=Math.abs(r3.nextInt())%4;	
			switch(z)
			{
				case 0:
				{
					for(i=1;i<4;i++)
						if(pane.map.getmap(x+i, y)!=1)
							continue Loop;
					if(snakenum==2)
						for(i=1;i<4;i++)
							if(pane.snake[0].isEqual(new Point(x+i,y)))
								continue Loop;
					head=new SnakePoint(1,x,y,0,snakenum);
					pane.map.setmap(x, y, snakenum+2);
					body.addElement(new SnakePoint(2,x+1,y,0,snakenum));
					pane.map.setmap(x+1, y, snakenum+2);
					tail[0]=new SnakePoint(3,x+2,y,0,snakenum);
					pane.map.setmap(x+2, y, snakenum+2);
					tail[1]=new SnakePoint(4,x+3,y,0,snakenum);
					pane.map.setmap(x+3, y, snakenum+2);
					break Loop;
				}
				case 1:
				{
					for(i=1;i<4;i++)
						if(pane.map.getmap(x-i, y)!=1)
							continue Loop;
					if(snakenum==2)
						for(i=1;i<4;i++)
							if(pane.snake[0].isEqual(new Point(x+i,y)))
								continue Loop;
					head=new SnakePoint(1,x,y,1,snakenum);
					pane.map.setmap(x, y, snakenum+2);
					body.addElement(new SnakePoint(2,x-1,y,1,snakenum));
					pane.map.setmap(x-1, y, snakenum+2);
					tail[0]=new SnakePoint(3,x-2,y,1,snakenum);
					pane.map.setmap(x-2, y, snakenum+2);
					tail[1]=new SnakePoint(4,x-3,y,1,snakenum);
					pane.map.setmap(x-3, y, snakenum+2);
					break Loop;
				}
				case 2:
				{
					for(i=1;i<4;i++)
						if(pane.map.getmap(x, y+i)!=1)
							continue Loop;
					if(snakenum==2)
						for(i=1;i<4;i++)
							if(pane.snake[0].isEqual(new Point(x+i,y)))
								continue Loop;
					head=new SnakePoint(1,x,y,2,snakenum);
					pane.map.setmap(x, y, snakenum+2);
					body.addElement(new SnakePoint(2,x,y+1,2,snakenum));
					pane.map.setmap(x, y+1, snakenum+2);
					tail[0]=new SnakePoint(3,x,y+2,2,snakenum);
					pane.map.setmap(x, y+2, snakenum+2);
					tail[1]=new SnakePoint(4,x,y+3,2,snakenum);
					pane.map.setmap(x, y+3, snakenum+2);
					break Loop;
				}
				case 3:
				{
					for(i=1;i<4;i++)
						if(pane.map.getmap(x, y-i)!=1)
							continue Loop;
					if(snakenum==2)
						for(i=1;i<4;i++)
							if(pane.snake[0].isEqual(new Point(x+i,y)))
								continue Loop;
					head=new SnakePoint(1,x,y,3,snakenum);
					pane.map.setmap(x, y, snakenum+2);
					body.addElement(new SnakePoint(2,x,y-1,3,snakenum));
					pane.map.setmap(x, y-1, snakenum+2);
					tail[0]=new SnakePoint(3,x,y-2,3,snakenum);
					pane.map.setmap(x, y-2, snakenum+2);
					tail[1]=new SnakePoint(4,x,y-3,3,snakenum);
					pane.map.setmap(x, y-3, snakenum+2);
					break Loop;
				}
				
			}
		}

	}
	
	public Snake(SnakePoint head,PlayOnline pane,int snakenum)
	{
		this.snakenum=4;
		this.pane=pane;
		this.snakenum=snakenum;
		switch(head.getD())
		{
		case 0:
		{
			this.head=new SnakePoint(1,head.getX(),head.getY(),0,snakenum);
			pane.map.setmap(head.getX(), head.getY(), snakenum+2);
			this.body.addElement(new SnakePoint(2,head.getX()+1,head.getY(),0,snakenum));
			pane.map.setmap(head.getX()+1, head.getY(), snakenum+2);
			this.tail[0]=new SnakePoint(3,head.getX()+2,head.getY(),0,snakenum);
			pane.map.setmap(head.getX()+2, head.getY(), snakenum+2);
			this.tail[1]=new SnakePoint(4,head.getX()+3,head.getY(),0,snakenum);
			pane.map.setmap(head.getX()+3, head.getY(), snakenum+2);
		}break;
		case 1:
		{
			this.head=new SnakePoint(1,head.getX(),head.getY(),1,snakenum);
			pane.map.setmap(head.getX(), head.getY(), snakenum+2);
			this.body.addElement(new SnakePoint(2,head.getX()-1,head.getY(),1,snakenum));
			pane.map.setmap(head.getX()-1, head.getY(), snakenum+2);
			this.tail[0]=new SnakePoint(3,head.getX()-2,head.getY(),1,snakenum);
			pane.map.setmap(head.getX()-2, head.getY(), snakenum+2);
			this.tail[1]=new SnakePoint(4,head.getX()-3,head.getY(),1,snakenum);
			pane.map.setmap(head.getX()-3, head.getY(), snakenum+2);
		}break;
		case 2:
		{
			this.head=new SnakePoint(1,head.getX(),head.getY(),2,snakenum);
			pane.map.setmap(head.getX(), head.getY(), snakenum+2);
			this.body.addElement(new SnakePoint(2,head.getX(),head.getY()+1,2,snakenum));
			pane.map.setmap(head.getX(), head.getY()+1, snakenum+2);
			this.tail[0]=new SnakePoint(3,head.getX(),head.getY()+2,2,snakenum);
			pane.map.setmap(head.getX(), head.getY()+2, snakenum+2);
			this.tail[1]=new SnakePoint(4,head.getX(),head.getY()+3,2,snakenum);
			pane.map.setmap(head.getX(), head.getY()+3, snakenum+2);
		}break;
		case 3:
		{
			this.head=new SnakePoint(1,head.getX(),head.getY(),3,snakenum);
			pane.map.setmap(head.getX(), head.getY(), snakenum+2);
			this.body.addElement(new SnakePoint(2,head.getX(),head.getY()-1,3,snakenum));
			pane.map.setmap(head.getX(), head.getY()-1, snakenum+2);
			this.tail[0]=new SnakePoint(3,head.getX(),head.getY()-2,3,snakenum);
			pane.map.setmap(head.getX(), head.getY()-2, snakenum+2);
			this.tail[1]=new SnakePoint(4,head.getX(),head.getY()-3,3,snakenum);
			pane.map.setmap(head.getX(), head.getY()-3, snakenum+2);
		}break;
		}
	}
	
	void drawSnake(Graphics g,ImageObserver im) //画蛇体
	{
		int i;
		if(this.haveTrash)
		{
			g.drawImage(Toolkit.getDefaultToolkit().getImage("img/floor.gif"),trash.y*20, trash.x*20,20,20,im);
			this.haveTrash=false;
		} 
		this.head.drawPoint(g,im);
		for(i=0;i<body.size();i++)
		{
			this.body.elementAt(i).drawPoint(g, im);

		}
		for(i=0;i<this.tail.length;i++)
			this.tail[i].drawPoint(g, im);	

	}
	
    boolean isEqual(Point p)   //检测整个蛇是否含有节点p
	{
    	if(pane.map.getmap(p.x, p.y)==snakenum+2)
    		return true;
    	else
    		return false;
	}
    
    void eraser(SnakePoint s)     //擦除蛇尾,蛇移动时用
 	{
 		trash.x=s.getX();
 		trash.y=s.getY();
 		this.haveTrash=true;
 		pane.map.setmap(trash.x, trash.y, 1);
 	}
	
    void change(SnakePoint s1,SnakePoint s2)    //依s1改变s2,蛇移动设定图片时调用
	{
		s2.change(s1);
	}
    
    protected void addSnake()    //吃到食物时蛇身增加
   	{
   		this.body.addElement(new SnakePoint(this.body.elementAt(sum-4)));
   		this.sum++;
   	}
    
    protected void reduceSnake()     //吃到炸弹变短
  	{
  		SnakePoint a=this.body.elementAt(this.sum-4);
  		this.body.removeElementAt(this.sum-4);
  		this.eraser(tail[1]);
  		this.change(this.tail[0], this.tail[1]);
  		this.change(a, this.tail[0]);
  		this.sum--;
  		a=null;
  	}
    
    void move()        //同方向前进
   	{
   		int p=sum-3,a=head.getX(),b=head.getY(),d=head.getD();
   		switch(d)
   		{
   			case 0:
   				if(pane.map.getmap(a-1, b)==2)
   				{
   					istwice=true;
   		   			return;
   				}break;
   			case 1:
   				if(pane.map.getmap(a+1, b)==2)
   				{
   					istwice=true;
   		   			return;
   				}break;
   			case 2:
   				if(pane.map.getmap(a, b-1)==2)
   				{
   					istwice=true;
   		   			return;
   				}break;
   			case 3:
   				if(pane.map.getmap(a, b+1)==2)
   				{
   					istwice=true;
   		   			return;
   				}break;
   		}
   		if(!this.isAte)     //吃到食物则最后三点不变
   		{
   			this.eraser(tail[1]);   //擦除尾部
   			change(tail[0], tail[1]);
   			if(p-1>=0)
   				change(body.elementAt(p-1), tail[0]);
   		}
   		else
   		{
   			this.isAte=false;
   			p--;
   		}
   		for(int i=p-1;i>0;i--)
   		{
   			change(body.elementAt(i-1), body.elementAt(i));
   		}
   		change(head,body.elementAt(0));
   		switch(head.getD())
   		{
   			case 0:
   			{
   				head.setX(a-1);		
   			}break;
   			case 1:
   			{
   				head.setX(a+1);		
   			}break;
   			case 2:
   			{
   				head.setY(b-1);
   			}break;
   			case 3:
   			{
   				head.setY(b+1);
   			}break;
   		}
   		
   	}
   	
    void movetoturn(int direction)     //转向
   	{
   		head.setD(direction);
   		head.setImg(-1, direction);      //头节点不需要与其他节点判断是否转向，这里为了方便直接设为-1
   	}
    
    int isCollision(Snake s,Play pane)      //碰撞检测
  	{
    	int a=head.getX(),b=head.getY();
    	switch(head.getD())
   		{
   			case 0:
   				if(pane.map.getmap(a-1, b)==2&&istwice)
   					return 1;
   				break;
   			case 1:
   				if(pane.map.getmap(a+1, b)==2&&istwice)
   					return 1;
   				break;
   			case 2:
   				if(pane.map.getmap(a, b-1)==2&&istwice)
   					return 1;
   				break;
   			case 3:
   				if(pane.map.getmap(a, b+1)==2&&istwice)
   					return 1;
   				break;
   		}
  		Point p=new Point(head.getX(),head.getY());
  		if(this.sum>4)
  		{
  			for(int i=0;i<tail.length;i++)
				if(p.equals(new Point(tail[i].getX(),tail[i].getY())))
					return 2;
			if(this.sum>6)
				for(int i=3;i<sum-3;i++)
					if(p.equals(new Point(body.elementAt(i).getX(),body.elementAt(i).getY())))
						return 2;
  		}
  		if(s.isEqual(p))      //判断该蛇是否撞上另一条蛇
					return 3;
  		return 0;
  	}
	
    boolean dealDrop()       //判断蛇是否掉进河里并作出相应处理
    {
    	int j;
    	Point p=new Point(head.getX(),head.getY());
    	if(pane.map.getmap(p.x, p.y)==6)
    	{
    		head.clearPoint();
			head.setX(body.elementAt(0).getX());
			head.setY(body.elementAt(0).getY());
			for(j=0;j<body.size();j++)
			{
				body.elementAt(j).clearPoint();	
				if(j!=0)
				{
					pane.map.setmap(body.elementAt(j).getX(), body.elementAt(j).getY(), 1);
					body.elementAt(j).setX(body.elementAt(0).getX());
					body.elementAt(j).setY(body.elementAt(0).getY());
				}
			}
			tail[0].clearPoint();
			pane.map.setmap(tail[0].getX(), tail[0].getY(), 1);
			tail[0].setX(body.elementAt(0).getX());
			tail[0].setY(body.elementAt(0).getY());
			tail[1].setD(head.getD());
			tail[1].setImg(-1, head.getD());
			pane.map.setmap(tail[1].getX(), tail[1].getY(), 1);
			tail[1].setX(body.elementAt(0).getX());
			tail[1].setY(body.elementAt(0).getY());
			return true;
    	}
    	else
    		return false;
    }
    
    void death(int reason)   //记录死因以便比较
    {
    	 death=reason;
    }
    
    int getDeath()     //返回死因
    {
    	return death;
    }
    
    void setAim(GameItem g)
    {
    	aim=g;
    }
    
    void setPath(int a)
    {
    	path=a;
    }
    
    private void checkItems(int x,int y)     //人机对战时机器检查食物路径
    {
    	//判断食物是否都为炸弹
    	int sum=0,i,j=0;
    	for(i=0;i<pane.items.items.length;i++)
    		if(pane.items.items[i]==null||pane.items.items[i] instanceof Bomb)
    			sum++;
    	if(sum<4)   //食物不全为炸弹，找到路径上最近的食物
    	{
    		sum=0;
    		for(i=0;i<pane.items.items.length;i++)
    			if(pane.items.items[i]==null||pane.items.items[i] instanceof Bomb)
    				continue;
    			else
    			{
    				if(sum==0||sum>(Math.abs(pane.items.items[i].getX()-x)+Math.abs(pane.items.items[i].getY()-y)))
    				{
    					sum=Math.abs(pane.items.items[i].getX()-x)+Math.abs(pane.items.items[i].getY()-y);
    					j=i;
    				}
    			}
    		if(aim!=null)
    		{
    			if(path>=sum)
    			{
    				aim=pane.items.items[j];
    				path=sum;
    			}
    		}
    		else
    		{
    			aim=pane.items.items[j];
				path=sum;
    		}
    		
    	}
    	else      //全为炸弹
    	{
    		if(pane.score[0]>=pane.score[1])     //当电脑分数低于玩家时，选择一搏
    		{
    			sum=0;
    			for(i=0;i<pane.items.items.length;i++)
    				if(pane.items.items[i]!=null&&(sum==0||sum>(Math.abs(pane.items.items[i].getX()-x)+Math.abs(pane.items.items[i].getY()-y))))
    				{
    					sum=Math.abs(pane.items.items[i].getX()-x)+Math.abs(pane.items.items[i].getY()-y);
    					j=i;
    				}
    			if(aim!=null)
        		{
        			if(path>=sum)
        			{
        				aim=pane.items.items[j];
        				path=sum;
        			}
        		}
        		else
        		{
        			aim=pane.items.items[j];
    				path=sum;
        		}
    		}
    	}
    }
    
    GameItem getAim()
    {
    	return aim;
    }
   
    
    void dealObstacle()    //当前方向前行将会撞墙
    {
    	int x=head.getX(),y=head.getY(),i,left=0,right=0,up=0,down=0;
    	switch(head.getD())
    	{
    	case 0:
    	{
    		for(i=y;i>=1;i--)
    			if(pane.map.getmap(x,i-1)!=1)
    				break;
    			else
    				left++;
    		for(i=y;i<=40;i++)
    			if(pane.map.getmap(x,i+1)!=1)
    				break;
    			else
    				right++;
    		if(left>=right)
    		{
    			
    			this.movetoturn(2);
    			if(pane.map.getmap(x, y-1)!=1)
    			{//有障碍
    				this.movetoturn(3);		
    			}
    		}
    		else
    		{
    			this.movetoturn(3);
    			if(pane.map.getmap(x, y+1)!=1)
    			{//有障碍
    				this.movetoturn(2);	
    			}
    		}
    		
    	}
    	break;
    	case 1:
    	{
    		for(i=y;i>=1;i--)
    			if(pane.map.getmap(x,i-1)!=1)
    				break;
    			else
    				left++;
    		for(i=y;i<=40;i++)
    			if(pane.map.getmap(x,i+1)!=1)
    				break;
    			else
    				right++;
    		if(left>=right)
    		{
    			this.movetoturn(2);
    			if(pane.map.getmap(x, y-1)!=1)
    			{//有障碍
    				this.movetoturn(3);		
    			}
    		}
    		else
    		{
    			this.movetoturn(3);
    			if(pane.map.getmap(x, y+1)!=1)
    			{//有障碍
    				this.movetoturn(2);	
    			}
    		}
    		
    	}
    	break;
    	case 2:
    	{
    		for(i=x;i>=1;i--)
    			if(pane.map.getmap(i-1,y)!=1)
    				break;
    			else
    				up++;
    		for(i=x;i<=40;i++)
    			if(pane.map.getmap(i+1,y)!=1)
    				break;
    			else
    				down++;
    		if(up>=down)
    		{
    			this.movetoturn(0);
    			if(pane.map.getmap(x-1, y)!=1)
    			{//有障碍
    				this.movetoturn(1);		
    			}
    		}
    		else
    		{
    			this.movetoturn(1);
    			if(pane.map.getmap(x+1, y)!=1)
    			{//有障碍
    				this.movetoturn(0);	
    			}
    		}
    		
    	}
    	break;
    	case 3:
    	{
    		for(i=x;i>=1;i--)
    			if(pane.map.getmap(i-1,y)!=1)
    				break;
    			else
    				up++;
    		for(i=x;i<=40;i++)
    			if(pane.map.getmap(i+1,y)!=1)
    				break;
    			else
    				down++;
    		if(up>=down)
    		{
    			this.movetoturn(0);
    			if(pane.map.getmap(x-1, y)!=1)
    			{//有障碍
    				this.movetoturn(1);		
    			}
    		}
    		else
    		{
    			this.movetoturn(1);
    			if(pane.map.getmap(x+1, y)!=1)
    			{//有障碍
    				this.movetoturn(0);	
    			}
    		}
    		
    	}
    	break;
    	}
    }
    
  
   
   boolean isHaveObstacle()
   {
	   int d=head.getD(),x=head.getX(),y=head.getY();
	   switch(d)
	   {
	   case 0:
	   {
		   if(aim!=null)
			   if(x-1==aim.getX()&&y==aim.getY())
				   return false;
		   if(pane.map.getmap(x-1, y)!=1)
			   return true;
	   }
		   break;
	   case 1:
	   {
		   if(aim!=null)
			   if(x+1==aim.getX()&&y==aim.getY())
				   return false;
		   if(pane.map.getmap(x+1, y)!=1)
			   return true;
	   }
		   break;
	   case 2:
	   {
		   if(aim!=null)
			   if(x==aim.getX()&&y-1==aim.getY())
				   return false;
		   if(pane.map.getmap(x, y-1)!=1)
			   return true;
	   }
		   break;
	   case 3:
	   {
		   if(aim!=null)
			   if(x==aim.getX()&&y+1==aim.getY())
				   return false;
		   if(pane.map.getmap(x, y+1)!=1)
			   return true;
	   }
		   break;
	   }
	   return false;
   }
    
    void searchFood()      //电脑蛇自动搜索食物转向
    {
    	checkItems(head.getX(),head.getY());
    	int x=head.getX(),y=head.getY();
    	if(aim!=null)     //若为空，表示得分低于玩家且此时食物全为炸弹，只要在僵局中保持不死即可
    	{
    		//前方是否为食物
    		switch(head.getD())
    		{
    		case 0:
    		{
    			if(aim.getY()>y)
    			{
    				this.movetoturn(3);
    			}
    			else if(aim.getY()<y)
    			{
    				
    					this.movetoturn(2);
    			}
    		}break;
    		case 1:
    		{
    			if(aim.getY()>y)
    			{	
    					this.movetoturn(3);
    			}
    			else if(aim.getY()<y)
    			{
    					this.movetoturn(2);
    			}
    		}break;
    		case 2:
    		{

    			if(aim.getX()>x)
    			{
    					this.movetoturn(1);
    					
    				
    			}
    			else if(aim.getX()<x)
    			{
    			
    					this.movetoturn(0);
    				
 
    			}
    			
    		}break;
    		case 3:
    		{
    			if(aim.getX()>x)
    			{
    					this.movetoturn(1);
    
    			}
    			else if(aim.getX()<x)
    			{
    					this.movetoturn(0);
    				
    			}
    		
    		}break;

    		}

    	}
    	
    }

   
    
   /* boolean isToWall(Play pane)
    {
    	switch(this.head.getD())
    	{
    		case 0:
    		{
    			if(pane.map.getmap(this.head.getX()-1, this.head.getY())==2)
    				return true;	
    		}break;
    		case 1:
    		{
    			if(pane.map.getmap(this.head.getX()-1, this.head.getY())==2)
    				return true;
    		}break;
    		case 2:
    		{
    			if(pane.map.getmap(this.head.getX(), this.head.getY()-1)==2)
    				return true;
    		}break;
    		case 3:
    		{
    			if(pane.map.getmap(this.head.getX(), this.head.getY()+1)==2)
    				return true;
    		}break;
    		
    	}
    	return false;
    }
    
    boolean isToRiver(Play pane)
    {
    	switch(this.head.getD())
    	{
    		case 0:
    			if(pane.items.getR().contains(new Water(this.head.getX()-1,this.head.getY())))
    				return true;
    			break;
    		case 1:
    			if(pane.items.getR().contains(new Water(this.head.getX()+1,this.head.getY())))	
    				return true;
    			break;
    		case 2:
    			if(pane.items.getR().contains(new Water(this.head.getX(),this.head.getY()-1)))
    				return true;
    			break;	
    		case 3:
    			if(pane.items.getR().contains(new Water(this.head.getX(),this.head.getY()+1)))	
    				return true;
    			break;	
    	}
    	return false;
    }
    
    boolean isToSnake(Play pane)
    {
    	switch(this.head.getD())
    	{
    		case 0:
    		{
    			if(pane.snake[0].isEqual(new Point(this.head.getX()-1, this.head.getY())))
    			{
    				return true;
    			}	
    		}break;
    		case 1:
    		{
    			if(pane.snake[0].isEqual(new Point(this.head.getX()+1, this.head.getY())))
    			{
    				return true;
    			}
    			
    		}break;
    		case 2:
    		{
    			if(pane.snake[0].isEqual(new Point(this.head.getX(), this.head.getY()-1)))
    			{
    				return true;
    			}
    			
    		}break;
    		case 3:
    		{
    			if(pane.snake[0].isEqual(new Point(this.head.getX(), this.head.getY()+1)))
    			{
    				return true;
    			}
    			
    		}break;
    		
    	}
    	return false;
    }
    
    boolean isToSelf()
    {
    	int d=head.getD();
    	switch(d)
    	{
    	case 0:
    	{
    		if(biteItself(this.head.getX()-1, this.head.getY()))
    		{
				return true;
			}
    	}break;
    	case 1:
    	{
    		if(biteItself(this.head.getX()+1, this.head.getY()))
			{
				return true;
			}
    	}break;
    	case 2:
    	{
    		if(biteItself(this.head.getX(), this.head.getY()-1))
			{
				return true;
			}
    	}break;
    	case 3:
    	{
    		if(biteItself(this.head.getX(), this.head.getY()+1))
			{
				return true;
			}
    	}break;
    	}
    	return false;
    }
   /* private void auto_moveLR(Play pane)    //机器蛇判断左右转
    {
    	checkItems(pane,this.head.getX(),this.head.getY()-1);
		int a=path;
		if(aim!=null)
		{
			checkItems(pane,this.head.getX(),this.head.getY()+1);
			if(a==path)     //是否保持不变来选择左右
				this.movetoturn(2);
			else
				this.movetoturn(3);
		}
		else
		{
			checkItems(pane,this.head.getX(),this.head.getY()+1);
			this.movetoturn(3);
		}	
    }
    
    private void auto_moveUD(Play pane)     ////机器蛇判断上下转
    {
    	checkItems(pane,this.head.getX()-1,this.head.getY());
		int a=path;
		if(aim!=null)
		{
			checkItems(pane,this.head.getX()+1,this.head.getY());
			if(a==path)     //是否保持不变来选择上下
				this.movetoturn(0);
			else
				this.movetoturn(1);
		}
		else
		{
			checkItems(pane,this.head.getX()+1,this.head.getY());
			this.movetoturn(1);
		}	
    }

    boolean checkRiver(Play pane)     //人机对战时检查前方是否是河水,若是则处理
    {
    	switch(this.head.getD())
    	{
    		case 0:
    			if(pane.items.getR().contains(new Water(this.head.getX()-1,this.head.getY())))
    			{
    				movetoturn(2);
    				return true;
    			}break;
    		case 1:
    			if(pane.items.getR().contains(new Water(this.head.getX()+1,this.head.getY())))
    			{
    				movetoturn(3);	
    				return true;
    			}break;
    		case 2:
    			if(pane.items.getR().contains(new Water(this.head.getX(),this.head.getY()-1)))
    			{
    				movetoturn(0);
    				return true;
    			}break;	
    		case 3:
    			if(pane.items.getR().contains(new Water(this.head.getX(),this.head.getY()+1)))
    			{
    				movetoturn(1);	
    				return true;
    			}break;	
    	}
    	return false;
    	
    }
    
    boolean checkWall(Play pane)     //人机对战时判断前方是否是墙,如是则处理
    {
    	switch(this.head.getD())
    	{
    		case 0:
    		{
    			if(pane.map.getmap(this.head.getX()-1, this.head.getY())==2)
    			{
    				movetoturn(2);
    				return true;
    			}
    		}break;
    		case 1:
    		{
    			if(pane.map.getmap(this.head.getX()-1, this.head.getY())==2)
    			{
    				movetoturn(3);
    				return true;
    			}
    		}break;
    		case 2:
    		{
    			if(pane.map.getmap(this.head.getX(), this.head.getY()-1)==2)
    			{
    				movetoturn(0);
    				return true;
    			}
    		}break;
    		case 3:
    		{
    			if(pane.map.getmap(this.head.getX(), this.head.getY()+1)==2)
    			{
    				movetoturn(1);
    				return true;
    			}
    		}break;
    		
    	}
    	return false;
    }
    
    private boolean biteItself(int x,int y)
    {
    	int i=0;
    	Point p=new Point(x,y);
  		if(this.sum>4)
  		{
  			for(i=0;i<tail.length;i++)
  				if(p.equals(new Point(tail[i].getX(),tail[i].getY())))
  					return true;
  			if(this.sum>6)
  				for(i=3;i<sum-3;i++)
  					if(p.equals(new Point(body.elementAt(i).getX(),body.elementAt(i).getY())))
  						return true;
  		}
  		return false;
    }
    
    boolean checkSnake(Play pane)   //机器检查是否撞上玩家或咬自己，如是则处理
    {
    	switch(this.head.getD())
    	{
    		case 0:
    		{
    			if(pane.snake[0].isEqual(new Point(this.head.getX()-1, this.head.getY())))
    			{
    				if(!pane.snake[0].isEqual(new Point(this.head.getX(), this.head.getY()-1)))
    					movetoturn(2);
    				else
    					movetoturn(3);
    				return true;
    			}
    			
    			
    		}break;
    		case 1:
    		{
    			if(pane.snake[0].isEqual(new Point(this.head.getX()+1, this.head.getY())))
    			{
    				if(!pane.snake[0].isEqual(new Point(this.head.getX(), this.head.getY()-1)))
    					movetoturn(2);
    				else
    					movetoturn(3);
    				return true;
    			}
    			
    		}break;
    		case 2:
    		{
    			if(pane.snake[0].isEqual(new Point(this.head.getX(), this.head.getY()-1)))
    			{
    				if(!pane.snake[0].isEqual(new Point(this.head.getX()-1, this.head.getY())))
    					movetoturn(0);
    				else
    					movetoturn(1);
    				return true;
    			}
    			
    		}break;
    		case 3:
    		{
    			if(pane.snake[0].isEqual(new Point(this.head.getX(), this.head.getY()+1)))
    			{
    				if(!pane.snake[0].isEqual(new Point(this.head.getX()-1, this.head.getY())))
    					movetoturn(0);
    				else
    					movetoturn(1);
    				return true;
    			}
    			
    		}break;
    		
    	}
    	return false;
    }
    
    boolean checkItself()      //人机对战时检测机器是否咬自己
    {
    	int d=head.getD();
    	switch(d)
    	{
    	case 0:
    	{
    		if(biteItself(this.head.getX()-1, this.head.getY()))
			{
				if(head.getY()>tail[0].getY())
					movetoturn(3);
				else if(head.getY()<tail[0].getY())
					movetoturn(2);
				else
					movetoturn(2);
				return true;
			}
    	}break;
    	case 1:
    	{
    		if(biteItself(this.head.getX()+1, this.head.getY()))
			{
    			if(head.getY()>tail[0].getY())
					movetoturn(3);
				else if(head.getY()<tail[0].getY())
					movetoturn(2);
				else
					movetoturn(3);
				return true;
			}
    	}break;
    	case 2:
    	{
    		if(biteItself(this.head.getX(), this.head.getY()-1))
			{
    			if(head.getX()>tail[0].getX())
					movetoturn(1);
				else if(head.getX()<tail[0].getX())
					movetoturn(0);
				else
					movetoturn(0);
				return true;
			}
    	}break;
    	case 3:
    	{
    		if(biteItself(this.head.getX(), this.head.getY()+1))
			{
    			if(head.getX()>tail[0].getX())
					movetoturn(1);
				else if(head.getX()<tail[0].getX())
					movetoturn(0);
				else
					movetoturn(1);
				return true;
			}
    	}break;
    	}
    	return false;
    }*/
    
   
}
