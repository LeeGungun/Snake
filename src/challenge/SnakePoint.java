package challenge;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.ImageObserver;

public class SnakePoint {
	
    private int direction;   //0,1,2,3分别代表上、下、左、右
    private int x,y;
    private int imgNum;   //只有三类数组，编号分别为1，2，3
    private Image img[]=new Image[2];
    private int turn=-1;   //记录当前图片是否须转向
    
    public SnakePoint()
    {
    	
    }
    
    public SnakePoint(int imgNum,int x,int y,int direction)
    {
    	this.imgNum=imgNum;
    	this.x=x;
    	this.y=y;
    	this.direction=direction;
    	img[0]=Toolkit.getDefaultToolkit().getImage("cimg/snake1.gif");
    	if(imgNum==2)
    		img[1]=Toolkit.getDefaultToolkit().getImage("cimg/snake2.gif");
    	else
    		img[1]=null;
    }
    
    public SnakePoint(SnakePoint s)
    {
    	this.imgNum=s.imgNum;
    	this.x=s.x;
    	this.y=s.y;
    	this.direction=s.direction;
    	this.turn=s.turn;
    	img[0]=Toolkit.getDefaultToolkit().getImage("cimg/snake1.gif");
    	if(s.imgNum==2)
    		img[1]=Toolkit.getDefaultToolkit().getImage("cimg/snake2.gif");
    	else
    		img[1]=null;
    }
    
     public int getX()
    {
    	return this.x;
    }
    
    public int getY()
    {
    	return this.y;
    }
    
    
    public void setX(int x)
    {
    	this.x=x;
    }
    
    public void setY(int y)
    {
    	this.y=y;
    }
    
    public void setD(int direction)
    {
    	this.direction=direction;
    }
    
    public int getD()
    {
    	return this.direction;
    }
    
    public int getT()
    {
    	return this.turn;
    }
    
    public void setT(int turn)
    {
    	this.turn=turn;
    }
    
   /* private void showImg1(Graphics g,ImageObserver im,int p,int x,int y,int d) //画当前显示图片
    {
    	if(this.imgNum==2)
    	{
    		if(turn!=-1)
    		{
    			switch(d)
    			{
    				case 0:g.drawImage(img[1], p*y, p*x, p*(y+1), p*(x+1), turn*30,0 ,turn*30+30 , 30, im);break;
    				case 1:g.drawImage(img[1], p*y, p*x, p*(y+1), p*(x+1), turn*30+30 , 30,turn*30,0 , im);break;
    				case 2:g.drawImage(img[1], p*x, p*y, p*(x+1), p*(y+1), turn*30,30 ,turn*30+30 , 0, im);break;
    				case 3:g.drawImage(img[1], p*x, p*y, p*(x+1), p*(y+1), turn*30+30 , 0,turn*30,30 , im);break;
    			}
    			return;
    		}
    		
    	}
    	switch(d)
		{
			case 0:g.drawImage(img[0], p*y, p*x, p*(y+1), p*(x+1), direction*30,(imgNum-1)*30 ,direction*30+30 , imgNum*30, im);break;
			case 1:g.drawImage(img[0], p*y, p*x, p*(y+1), p*(x+1), direction*30+30 , imgNum*30, direction*30,(imgNum-1)*30 ,im);break;
			case 2:g.drawImage(img[0], p*x, p*y, p*(x+1), p*(y+1), direction*30,imgNum*30, direction*30+30 , (imgNum-1)*30 ,im);break;
			case 3:g.drawImage(img[0], p*x, p*y, p*(x+1), p*(y+1),direction*30+30 , (imgNum-1)*30 ,direction*30,imgNum*30, im);break;
		}
    }*/
    
    private void showImg(Graphics g,ImageObserver im,int p,int x,int y)
    {
    	if(this.imgNum==2)
    	{
    		if(turn!=-1)
    		{
    			g.drawImage(img[1], p*y, p*x, p*y+p, p*x+p, turn*30, 0, turn*30+30, 30, im);
    			return;
    		}
    	}
    	g.drawImage(img[0], p*y, p*x, p*y+p, p*x+p, direction*30, (imgNum-1)*30, direction*30+30, imgNum*30, im);
    }
    
    void turn(SnakePoint s)   //判断转向
    {
        int turn=-1;   //不转向
    	if(s.direction!=direction)
    	{
       		switch(direction)
    		{
    			case 0:
    			{
    				if(s.direction==2)
    					turn= 0;
    				else
    					turn= 1;
    			}break;
    			case 1:
    			{
    				if(s.direction==2)
    					turn= 2;
    				else
    					turn= 3;
    			}break;
    			case 2:
    			{
    				if(s.direction==0)
    					turn= 4;
    				else
    					turn= 5;
    			}break;
    			case 3:
    			{
    				if(s.direction==0)
    					turn= 6;
    				else
    					turn= 7;
    			}break;
    		}
    	}
    	this.turn=turn;
    }
    
    public void changeM(SnakePoint s)   //移动时不改变点性质前提下，改变点相关属性
    {
        turn(s);  
    	this.setX(s.getX());
    	this.setY(s.getY());
    	this.setD(s.getD());
    }
    
    public void change(SnakePoint s)   //在确保至少移动过一次的前提下使用不改变点性质前提下，改变点相关属性
    {
    	this.setX(s.getX());
    	this.setY(s.getY());
    	this.setD(s.getD());
    	this.setT(s.getT());
    }

    boolean draw()     //判断是否在当前视野
	{
		if((x>=DrawMap.x1&&x<=DrawMap.x2)||(x>=DrawMap.x2&&x<=DrawMap.x1))
			if((y>=DrawMap.y1&&y<=DrawMap.y2)||(y>=DrawMap.y2&&y<=DrawMap.y1))
				return true;
		return false;
	}
    
    public void drawPoint(Graphics g,ImageObserver im)    //画蛇体节点
    { 
    	if(CPlay.state==0||CPlay.state==3||CPlay.isStop)
    		this.showImg( g, im, CFrame.px[0],x,y);
    	else
    	{
    		if(draw())
    		{
    			int i=Math.abs(y-DrawMap.y1),j=Math.abs(x-DrawMap.x1);
    			this.showImg( g, im, CFrame.px[1],j,i);
    		}
    	}
    	
    }
    
}
