package challenge;

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
	SnakePoint tail;    //蛇尾
	//boolean isAte=false;    //是否吃到物品
	TrashPoint trash=null;    //记录擦除信息坐标
	boolean haveTrash=false;    //是否有擦除点
	boolean istwice=false;    //用于碰撞判断，有了它，才可在边界处实现转向
	int death=-1;  //记录死因
	private CPlay pane=null;
	
	class TrashPoint
	{
		int direction,x,y,turn;
		
		TrashPoint(SnakePoint s)
		{
			this.direction=s.getD();
			this.x=s.getX();
			this.y=s.getY();
			this.turn=s.getT();
		}
	}
	
	public Snake()
	{
		
	}
	
	public Snake(CPlay pane)
	{
		this.sum=4;
		this.pane=pane;
		Random r1,r2,r3;
		int x,y,z,i;
		Loop:while(true)
		{
			r1=new Random(System.currentTimeMillis());
			r2=new Random(System.currentTimeMillis());
			x=Math.abs(r1.nextInt())%12+4;
			y=Math.abs(r2.nextInt())%32+4;
			switch(CFrame.stage)
			{
			case 1:
			case 2:
				{
					if(pane.map.getmap(x, y)!=1)
						continue Loop;
				}
			case 3:
				{
					if(!(pane.map.getmap(x, y)==1||pane.map.getmap(x, y)==3))
						continue Loop;
				}
			}
			r3=new Random(System.currentTimeMillis());
			z=Math.abs(r3.nextInt())%4;	
			switch(z)
			{
				case 0:
				{
					for(i=1;i<4;i++)
						switch(CFrame.stage)
						{
						case 1:
						case 2:
							{
								if(pane.map.getmap(x+i, y)!=1)
									continue Loop;
							}
						case 3:
							{
								if(!(pane.map.getmap(x+i, y)==1||pane.map.getmap(x+i, y)==3))
									continue Loop;
							}
						}
					head=new SnakePoint(1,x,y,0);
					body.addElement(new SnakePoint(2,x+1,y,0));
					body.addElement(new SnakePoint(2,x+2,y,0));
					tail=new SnakePoint(3,x+3,y,0);
					break Loop;
				}
				case 1:
				{
					for(i=1;i<4;i++)
						switch(CFrame.stage)
						{
						case 1:
						case 2:
							{
								if(pane.map.getmap(x-i, y)!=1)
									continue Loop;
							}
						case 3:
							{
								if(!(pane.map.getmap(x-i, y)==1||pane.map.getmap(x-i, y)==3))
									continue Loop;
							}
						}
					head=new SnakePoint(1,x,y,1);
					body.addElement(new SnakePoint(2,x-1,y,1));
					body.addElement(new SnakePoint(2,x-2,y,1));
					tail=new SnakePoint(3,x-3,y,1);
					break Loop;
				}
				case 2:
				{
					for(i=1;i<4;i++)
						switch(CFrame.stage)
						{
						case 1:
						case 2:
							{
								if(pane.map.getmap(x, y+i)!=1)
									continue Loop;
							}
						case 3:
							{
								if(!(pane.map.getmap(x, y+i)==1||pane.map.getmap(x, y+i)==3))
									continue Loop;
							}
						}
					head=new SnakePoint(1,x,y,2);
					body.addElement(new SnakePoint(2,x,y+1,2));
					body.addElement(new SnakePoint(2,x,y+2,2));
					tail=new SnakePoint(3,x,y+3,2);
					break Loop;
				}
				case 3:
				{
					for(i=1;i<4;i++)
						switch(CFrame.stage)
						{
						case 1:
						case 2:
							{
								if(pane.map.getmap(x, y-i)!=1)
									continue Loop;
							}
						case 3:
							{
								if(!(pane.map.getmap(x, y-i)==1||pane.map.getmap(x, y-i)==3))
									continue Loop;
							}
						}
					head=new SnakePoint(1,x,y,3);
					body.addElement(new SnakePoint(2,x,y-1,3));
					body.addElement(new SnakePoint(2,x,y-2,3));
					tail=new SnakePoint(3,x,y-3,3);
					break Loop;
				}
				
			}
		}

	}
	
	boolean isEqual(Point p)   //检测整个蛇是否含有节点p
	{
		if(head.getX()==p.x&&head.getY()==p.y)
			return true;
		if(tail.getX()==p.x&&tail.getY()==p.y)
			return true;
		for(int i=0;i<body.size();i++)
		{
			if(body.elementAt(i).getX()==p.x&&body.elementAt(i).getY()==p.y)
			{
				return true;
			}
		}
		return false;
	}
	
	void eraser(SnakePoint s)
	{
		trash=new  TrashPoint(s);
		this.haveTrash=true;
	}
	
	void addPoint()     //增加结点
	{
		if(!pane.holing)
		{
			body.addElement(new SnakePoint(body.elementAt(sum-3)));
			sum++;
			body.elementAt(sum-3).change(tail);    //回退结点
			if(trash!=null)
			{
				tail.setX(trash.x);
				tail.setY(trash.y);
				tail.setD(trash.direction);
				tail.setT(trash.turn);
			}
		}
		else 
			sum++;
	}
	
	void reducePoint()
	{
		if(!pane.holing)
		{
			SnakePoint a=this.body.elementAt(sum-3);
			this.body.removeElementAt(sum-4);
			this.eraser(tail);
			this.tail.change(a); 
			this.sum--;
			a=null;
		}
		else
			sum--;
	}
	
	void snakeToHole()
	{
		switch(CFrame.stage)
		{
			case 1:
				{
					if(head==null)
						head=new SnakePoint(1,CFrame.hole[0].x,CFrame.hole[0].y,2);
					else if(pane.show<sum-1)
						body.addElement(new SnakePoint(2,CFrame.hole[0].x,CFrame.hole[0].y,2));
					else if(pane.show==sum-1)
						tail=new SnakePoint(3,CFrame.hole[0].x,CFrame.hole[0].y,2);
				}break;
			case 2:
			{
				if(head==null)
					head=new SnakePoint(1,CFrame.hole[1].x,CFrame.hole[1].y,1);
				else if(pane.show<sum-1)
					body.addElement(new SnakePoint(2,CFrame.hole[1].x,CFrame.hole[1].y,1));
				else if(pane.show==sum-1)
					tail=new SnakePoint(3,CFrame.hole[1].x,CFrame.hole[1].y,1);
			}break;
			case 3:
			{
				if(head==null)
					head=new SnakePoint(1,CFrame.hole[2].x,CFrame.hole[2].y,3);
				else if(pane.show<sum-1)
					body.addElement(new SnakePoint(2,CFrame.hole[2].x,CFrame.hole[2].y,3));
				else if(pane.show==sum-1)
					tail=new SnakePoint(3,CFrame.hole[2].x,CFrame.hole[2].y,3);
			}break;
		}
		pane.show++;
		if(pane.show==sum)
		{
			pane.holing=false;
		}
	}
	
	void move()        //同方向前进
   	{
		int a=head.getX(),b=head.getY(),d=head.getD(),p;
		if(!pane.holing)
		{
			p=sum-2; 
		}
		else
		{
			p=pane.show-1;
		}
   		switch(d)
   		{
   			case 0:
   			{
   				int s=pane.map.getmap(a-1, b);
   				if(!(s==1||s==5||(s==2&&CFrame.stage==3)))
   				{
   					istwice=true;
   		   			return;
   				}
   			}break;
   			case 1:
   			{
   				int s=pane.map.getmap(a+1,b);
   				if(!(s==1||s==5||(s==2&&CFrame.stage==3)))
   				{
   					istwice=true;
   		   			return;
   				}
   			}break;
   			case 2:
   			{
   				int s=pane.map.getmap(a, b-1);
   				if(!(s==1||s==5||(s==2&&CFrame.stage==3)))
   				{
   					istwice=true;
   		   			return;
   				}
   			}break;
   			case 3:
   			{
   				int s=pane.map.getmap(a,b+1);
   				if(!(s==1||s==5||(s==2&&CFrame.stage==3)))
   				{
   					istwice=true;
   		   			return;
   				}
   			}break;
   		}
   		if(!pane.holing)
   		{
   			this.eraser(tail);   //擦除尾部
   	   		tail.changeM(body.elementAt(p-1));
   		}
   		else
   		{
   			this.snakeToHole();	
   		}
   		for(int i=p-1;i>0;i--)
   		{
   			body.elementAt(i).changeM(body.elementAt(i-1));
   		}
   		body.elementAt(0).changeM(head);
   		if(pane.show==sum)
   		{
			tail.turn(body.elementAt(sum-3));
			pane.show=0;
   		}
		else if(pane.holing&&pane.show>=3)
			body.elementAt(pane.show-2).turn(body.elementAt(pane.show-3));
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
	
	
	
	void drawSnake(Graphics g,ImageObserver im)
	{
		if(this.haveTrash)
		{
			if((trash.x>=DrawMap.x1&&trash.x<=DrawMap.x2))
				if((trash.y>=DrawMap.y1&&trash.y<=DrawMap.y2))
				{
					int i=Math.abs(trash.y-DrawMap.y1),j=Math.abs(trash.x-DrawMap.x1),num=pane.map.getmap(trash.x, trash.y);
					g.drawImage(pane.map.img, i*CFrame.px[1], j*CFrame.px[1], (i+1)*CFrame.px[1], (j+1)*CFrame.px[1], 30*num-30, 0, 30*num, 30, im);
				}
		}
		if(head!=null)
			this.head.drawPoint(g,im);
		for(int i=0;i<body.size();i++)
		{
			this.body.elementAt(i).drawPoint(g, im);

		}
		if(tail!=null)
			this.tail.drawPoint(g, im);
	}
	
	boolean isCollision()
	{
		int a=head.getX(),b=head.getY();
    	switch(head.getD())
   		{
   			case 0:
   				if(CFrame.stage==1||CFrame.stage==2)
   				{
   					int r=pane.map.getmap(a-1, b);
   					if(!(r==1||r==5)&&istwice)
   					{
   						death=r%4;
   						if(death!=0)
   							death--;
   						return true;
   					}
   				}
   				else if(CFrame.stage==3)
   				{
   					int r=pane.map.getmap(a-1, b);
   					if((r==2||r==4)&&istwice)
   					{
   						death=r%4;
   						if(death!=0)
   							death--;
   						return true;
   					}
   				}
   				break;
   			case 1:
   				if(CFrame.stage==1||CFrame.stage==2)
   				{
   					int r=pane.map.getmap(a+1, b);
   					if(!(r==1||r==5)&&istwice)
   					{
   						death=r%4;
   						if(death!=0)
   							death--;
   						return true;
   					}
   				}
   				else if(CFrame.stage==3)
   				{
   					int r=pane.map.getmap(a+1, b);
   					if((r==2||r==4)&&istwice)
   					{
   						death=r%4;
   						if(death!=0)
   							death--;
   						return true;
   					}		
   				}
   				break;
   			case 2:
   				if(CFrame.stage==1||CFrame.stage==2)
   				{
   					int r=pane.map.getmap(a, b-1);
   					if(!(r==1||r==5)&&istwice)
   					{
   						death=r%4;
   						if(death!=0)
   							death--;
   						return true;
   					}
   				}
   				else if(CFrame.stage==3)
   				{
   					int r=pane.map.getmap(a, b-1);
   					if((r==2||r==4)&&istwice)
   					{
   						death=r%4;
   						if(death!=0)
   							death--;
   						return true;
   					}
   						
   				}
   				break;
   			case 3:
   				if(CFrame.stage==1||CFrame.stage==2)
   				{
   					int r=pane.map.getmap(a, b+1);
   					if(!(r==1||r==5)&&istwice)
   					{
   						death=r%4;
   						if(death!=0)
   							death--;
   						return true;
   					}
   						
   				}
   				else if(CFrame.stage==3)
   				{
   					int r=pane.map.getmap(a, b+1);
   					if((r==2||r==4)&&istwice)
   					{
   						death=r%4;
   						if(death!=0)
   							death--;
   						return true;
   					}
   						
   				}
   				break;
   		}
  		Point p=new Point(head.getX(),head.getY());
  		if(this.sum>4)
  		{
  			if(tail!=null&&p.equals(new Point(tail.getX(),tail.getY())))
  			{
  				if(CFrame.stage==3)
  					death=2;
  				else
  					death=3;
  				return true;
  			}
  			if(this.sum>6)
  			{
  				for(int i=3;i<body.size();i++)
  					if(p.equals(new Point(body.elementAt(i).getX(),body.elementAt(i).getY())))
  					{
  						if(CFrame.stage==3)
  		  					death=2;
  		  				else
  		  					death=3;
  						return true;
  					}
  			}
  		}
  		return false;
	}
	
	void turn(int direction)
	{
		head.setD(direction);
	}

}
