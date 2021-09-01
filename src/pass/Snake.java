package pass;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.Random;
import java.util.Vector;
import java.awt.image.ImageObserver;

public class Snake {
	
    protected int pointNum=0;
    protected SnakePoint head=new SnakePoint();
    protected Vector<SnakePoint> body=new Vector<SnakePoint>();
	protected SnakePoint tail[]=new SnakePoint[2];
	private int i=0;
	protected boolean isAte=false;
	protected Point trash=new Point();
	protected boolean haveTrash=false;
	protected boolean istwice=false;    //������ײ�жϣ����������ſ��ڱ߽紦ʵ��ת��
	protected int minlength;
	private Vector<String> snakeSave=new Vector<String>(); //�洢��
	private Play play;

	protected Snake()
	{
		
	}
	
	protected Snake(Play p)
	{
		this.minlength=p.minlength;
		this.pointNum=4;
		this.play=p;
		Random r1=new Random(System.currentTimeMillis());
		Random r2=new Random(System.currentTimeMillis());
		Random r3=new Random(System.currentTimeMillis());
		int x=(Math.abs(r1.nextInt())%(p.maxmap-11)+3)*p.minlength+90;
		int y=(Math.abs(r2.nextInt())%(p.maxmap-11)+3)*p.minlength+43;
		int z=Math.abs(r3.nextInt())%4;
		head=new SnakePoint(1,x,y,z);
		switch(z)
		{
			case 0:
			{
				this.body.addElement(new SnakePoint(2,x,y+p.minlength,0));
				this.tail[0]=new SnakePoint(3,x,y+p.minlength*2,0);
				this.tail[1]=new SnakePoint(4,x,y+p.minlength*3,0);
			}break;
			case 1:
			{
				this.body.addElement(new SnakePoint(2,x,y-p.minlength,1));
				this.tail[0]=new SnakePoint(3,x,y-p.minlength*2,1);
				this.tail[1]=new SnakePoint(4,x,y-p.minlength*3,1);				
			}break;
			case 2:
			{

				this.body.addElement(new SnakePoint(2,x+p.minlength,y,2));
				this.tail[0]=new SnakePoint(3,x+p.minlength*2,y,2);
				this.tail[1]=new SnakePoint(4,x+p.minlength*3,y,2);
				
			}break;
			case 3:
			{
				this.body.addElement(new SnakePoint(2,x-p.minlength,y,3));
				this.tail[0]=new SnakePoint(3,x-p.minlength*2,y,3);
				this.tail[1]=new SnakePoint(4,x-p.minlength*3,y,3);				
			}break;
		}
		
		save(head,1);
		p.store("1");
		save(body.elementAt(0),2);
		p.store("1");
		save(tail[0],3);
		p.store("1");
		save(tail[1],4);
		p.store("1");
	}
	
	private void save(SnakePoint s,int i)
	{
		snakeSave.addElement(i+"-"+s.getX()+"-"+s.getY()+"-"+s.getD());
	}
	
	private void saveReduce()   //ɾ���߽ڵ�ʱ�������
	{
		snakeSave.addElement("reduce");
	}
	
	
	void saveEnd(String s)
	{
		snakeSave.addElement(s);
	}
	
     Vector<String> getSave()   //�õ���Ʒ�洢
	{
		return snakeSave;
	}
	
     protected void drawSnake(Graphics g,ImageObserver im,int m)      //������
	{
		if(this.haveTrash)
		{
	       	Color color=new Color(71,70,71);
        	g.setColor(color);
        	g.fillRect(trash.x+1, trash.y+1, m-2, m-2);
        	this.haveTrash=false;
		} 
		this.head.drawPoint(g,im,m);
		for(i=0;i<body.size();i++)
		{
			this.body.elementAt(i).drawPoint(g, im,m);
			
		}
		for(i=0;i<this.tail.length;i++)
			this.tail[i].drawPoint(g, im,m);	

	}
	
     protected boolean isEqual(Point p)   //����������Ƿ��нڵ�p
	{
		if(head.getX()==p.x&&head.getY()==p.y)
			return true;
		for(i=0;i<tail.length;i++)
		{
			if(tail[i].getX()==p.x&&tail[i].getY()==p.y)
				return true;
		}
		for(i=0;i<pointNum-3;i++)
		{
			if(body.elementAt(i).getX()==p.x&&body.elementAt(i).getY()==p.y)
			{
				return true;
			}
		}
		return false;
	}
	
     protected void eraser(SnakePoint s)     //������β
	{
		trash.x=s.getX();
		trash.y=s.getY();
		this.haveTrash=true;

	}
	
     protected void change(SnakePoint s1,SnakePoint s2)    //��s1�ı�s2
	{
		s2.change(s1);
	}
	
     protected void addSnake()    //�Ե�ʳ��ʱ��������
	{
		this.body.addElement(new SnakePoint(this.body.elementAt(pointNum-4)));
		this.pointNum++;
		save(body.elementAt(pointNum-4),2);
		play.store("1-"+head.getX()+"-"+head.getY());
	}
	
     protected void reduceSnake()     //�Ե�ը�����
	{
		SnakePoint a=this.body.elementAt(this.pointNum-4);
		this.body.removeElementAt(this.pointNum-4);
		saveReduce();
		play.store("1-"+head.getX()+"-"+head.getY());
		this.eraser(tail[1]);
		this.change(this.tail[0], this.tail[1]);
		this.change(a, this.tail[0]);
		this.pointNum--;
		a=null;

	}
	
     protected void move()        //ͬ����ǰ��
	{
		int p=pointNum-3,a=head.getX(),b=head.getY();
		if((head.getD()==2&&a==90)||(a==690-this.minlength&&head.getD()==3)||(b==43&&head.getD()==0)||(b==643-this.minlength&&head.getD()==1))
		{
			istwice=true;
			return;
		}
		if(!this.isAte)     //�Ե�ʳ����������㲻��
		{
			this.eraser(tail[1]);   //����β��
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
				head.setY(b-Play.speed1);		
			}break;
			case 1:
			{
				head.setY(b+Play.speed1);		
			}break;
			case 2:
			{
				head.setX(a-Play.speed1);
			}break;
			case 3:
			{
				head.setX(a+Play.speed1);
			}break;
		}
	}
	
     protected void movetoturn(int direction)
	{
		head.setD(direction);
		head.setImg(-1, direction);      //ͷ�ڵ㲻��Ҫ�������ڵ��ж��Ƿ�ת������Ϊ�˷���ֱ����Ϊ-1
	}
	
/*	public void movetoturn(int x,int y,int direction)     //ת��ʱǰ��
	{
		int a=head.getX(),b=head.getY(),p=pointNum-3;
		SnakePoint s=new SnakePoint(head);
		s.setD(direction);
		head.setX(a+x);
		head.setY(b+y);		
        head.setD(direction);
        head.setImg(-1, direction);
        if(!this.isAte)       //�Ե�ʳ����������㲻��
        {
        	this.eraser(tail[1]);   //����β��
        	change(tail[0], tail[1]);
        	if(p-1>=0)
        		change(body.elementAt(p-1), tail[0]);
        }
        else
        {
        	this.isAte=false;
        	p--;
        }
		for(i=p-1;i>0;i--)
		{
			change(body.elementAt(i-1), body.elementAt(i));
		}
		change(s,body.elementAt(0));	
		
	}*/
	
     protected boolean isCollision()      //��ײ���
	{
		if(istwice&&((head.getD()==2&&head.getX()==90)||(head.getX()==690-this.minlength&&head.getD()==3)||(head.getY()==43&&head.getD()==0)||(head.getY()==643-this.minlength&&head.getD()==1)))
			return true;
		Point p=new Point(head.getX(),head.getY());
		if(this.pointNum>4)
		{
			for(i=0;i<tail.length;i++)
				if(p.equals(new Point(tail[i].getX(),tail[i].getY())))
					return true;
			if(this.pointNum>6)
				for(i=3;i<pointNum-3;i++)
					if(p.equals(new Point(body.elementAt(i).getX(),body.elementAt(i).getY())))
						return true;
		}
		return false;
	}

	
}
