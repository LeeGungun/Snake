package fight;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.ImageObserver;

public class SnakePoint {
	
    private int direction;   //0,1,2,3�ֱ�����ϡ��¡�����
    private int x,y;   //�������꣬��������
    private int imgNum;   //ֻ���������飬��ŷֱ�Ϊ1��2��3��4
    private String images[][]=new String[2][];
    private Image img;  //��ǰͼƬ
    private int snakenum;   //�ߵı��
    private boolean isClear=false;      //�Ƿ����
    
    public SnakePoint()
    {
    	
    }
    
    public SnakePoint(int imgNum,int x,int y,int direction,int snakenum)
    {
    	this.imgNum=imgNum;
    	this.x=x;
    	this.y=y;
    	this.direction=direction;
    	this.snakenum=snakenum;
        switch(imgNum)
        {
        	case 1:{
            	images[0]=new String[4];
            	images[0][0]=new String("img/fightsnake/"+snakenum+"-1-2.gif");
            	images[0][1]=new String("img/fightsnake/"+snakenum+"-1-4.gif");
            	images[0][2]=new String("img/fightsnake/"+snakenum+"-1-3.gif");
            	images[0][3]=new String("img/fightsnake/"+snakenum+"-1-1.gif");
            	images[1]=new String[1];
            	images[1][0]=new String("");
            }break;
        	case 2:{
        		images[0]=new String[4];
        		images[0][0]=new String("img/fightsnake/"+snakenum+"-2-2.gif");
            	images[0][1]=new String("img/fightsnake/"+snakenum+"-2-2.gif");
            	images[0][2]=new String("img/fightsnake/"+snakenum+"-2-1.gif");
            	images[0][3]=new String("img/fightsnake/"+snakenum+"-2-1.gif");
            	images[1]=new String[8];
            	images[1][0]=new String("img/fightsnake/"+snakenum+"-5-2.gif");
            	images[1][1]=new String("img/fightsnake/"+snakenum+"-5-1.gif");
            	images[1][2]=new String("img/fightsnake/"+snakenum+"-5-3.gif");
            	images[1][3]=new String("img/fightsnake/"+snakenum+"-5-4.gif");
            	images[1][4]=new String("img/fightsnake/"+snakenum+"-5-4.gif");
            	images[1][5]=new String("img/fightsnake/"+snakenum+"-5-1.gif");
            	images[1][6]=new String("img/fightsnake/"+snakenum+"-5-3.gif");
            	images[1][7]=new String("img/fightsnake/"+snakenum+"-5-2.gif");
        	}break;
        	case 3:{
        		images[0]=new String[4];
        		images[0][0]=new String("img/fightsnake/"+snakenum+"-3-2.gif");
            	images[0][1]=new String("img/fightsnake/"+snakenum+"-3-4.gif");
            	images[0][2]=new String("img/fightsnake/"+snakenum+"-3-3.gif");
            	images[0][3]=new String("img/fightsnake/"+snakenum+"-3-1.gif");
            	images[1]=new String[8];
            	images[1][0]=new String("img/fightsnake/"+snakenum+"-6-8.gif");
            	images[1][1]=new String("img/fightsnake/"+snakenum+"-6-7.gif");
            	images[1][2]=new String("img/fightsnake/"+snakenum+"-6-3.gif");
            	images[1][3]=new String("img/fightsnake/"+snakenum+"-6-4.gif");
            	images[1][4]=new String("img/fightsnake/"+snakenum+"-6-2.gif");
            	images[1][5]=new String("img/fightsnake/"+snakenum+"-6-1.gif");
            	images[1][6]=new String("img/fightsnake/"+snakenum+"-6-5.gif");
            	images[1][7]=new String("img/fightsnake/"+snakenum+"-6-6.gif");
        	}break;
        	case 4:{
        		images[0]=new String[4];
            	images[0][0]=new String("img/fightsnake/"+snakenum+"-4-2.gif");
            	images[0][1]=new String("img/fightsnake/"+snakenum+"-4-4.gif");
            	images[0][2]=new String("img/fightsnake/"+snakenum+"-4-3.gif");
            	images[0][3]=new String("img/fightsnake/"+snakenum+"-4-1.gif");
            	images[1]=new String[1];
            	images[1][0]=new String("");
        	}break;   
        }
        img=Toolkit.getDefaultToolkit().getImage(images[0][direction]);
    }
    
    public SnakePoint(SnakePoint s)
    {
    	this.imgNum=s.imgNum;
    	this.x=s.x;
    	this.y=s.y;
    	this.direction=s.direction;
    	this.snakenum=s.snakenum;
    	switch(imgNum)
        {
        	case 1:{
            	images[0]=new String[4];
            	images[0][0]=new String("img/fightsnake/"+snakenum+"-1-2.gif");
            	images[0][1]=new String("img/fightsnake/"+snakenum+"-1-4.gif");
            	images[0][2]=new String("img/fightsnake/"+snakenum+"-1-3.gif");
            	images[0][3]=new String("img/fightsnake/"+snakenum+"-1-1.gif");
            	images[1]=new String[1];
            	images[1][0]=new String("");
            }break;
        	case 2:{
        		images[0]=new String[4];
        		images[0][0]=new String("img/fightsnake/"+snakenum+"-2-2.gif");
            	images[0][1]=new String("img/fightsnake/"+snakenum+"-2-2.gif");
            	images[0][2]=new String("img/fightsnake/"+snakenum+"-2-1.gif");
            	images[0][3]=new String("img/fightsnake/"+snakenum+"-2-1.gif");
            	images[1]=new String[8];
            	images[1][0]=new String("img/fightsnake/"+snakenum+"-5-2.gif");
            	images[1][1]=new String("img/fightsnake/"+snakenum+"-5-1.gif");
            	images[1][2]=new String("img/fightsnake/"+snakenum+"-5-3.gif");
            	images[1][3]=new String("img/fightsnake/"+snakenum+"-5-4.gif");
            	images[1][4]=new String("img/fightsnake/"+snakenum+"-5-4.gif");
            	images[1][5]=new String("img/fightsnake/"+snakenum+"-5-1.gif");
            	images[1][6]=new String("img/fightsnake/"+snakenum+"-5-3.gif");
            	images[1][7]=new String("img/fightsnake/"+snakenum+"-5-2.gif");
        	}break;
        	case 3:{
        		images[0]=new String[4];
        		images[0][0]=new String("img/fightsnake/"+snakenum+"-3-2.gif");
            	images[0][1]=new String("img/fightsnake/"+snakenum+"-3-4.gif");
            	images[0][2]=new String("img/fightsnake/"+snakenum+"-3-3.gif");
            	images[0][3]=new String("img/fightsnake/"+snakenum+"-3-1.gif");
            	images[1]=new String[8];
            	images[1][0]=new String("img/fightsnake/"+snakenum+"-6-8.gif");
            	images[1][1]=new String("img/fightsnake/"+snakenum+"-6-7.gif");
            	images[1][2]=new String("img/fightsnake/"+snakenum+"-6-3.gif");
            	images[1][3]=new String("img/fightsnake/"+snakenum+"-6-4.gif");
            	images[1][4]=new String("img/fightsnake/"+snakenum+"-6-2.gif");
            	images[1][5]=new String("img/fightsnake/"+snakenum+"-6-1.gif");
            	images[1][6]=new String("img/fightsnake/"+snakenum+"-6-5.gif");
            	images[1][7]=new String("img/fightsnake/"+snakenum+"-6-6.gif");
        	}break;
        	case 4:{
        		images[0]=new String[4];
            	images[0][0]=new String("img/fightsnake/"+snakenum+"-4-2.gif");
            	images[0][1]=new String("img/fightsnake/"+snakenum+"-4-4.gif");
            	images[0][2]=new String("img/fightsnake/"+snakenum+"-4-3.gif");
            	images[0][3]=new String("img/fightsnake/"+snakenum+"-4-1.gif");
            	images[1]=new String[1];
            	images[1][0]=new String("");
        	}break;   
        }
    	img=s.img;
    
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
    
    public void setImg(int turn,int direction) //����ת��ͼƬ
    {
    	if(this.images[1].length>1)
    	{
    		if(turn==-1)
    			this.img=Toolkit.getDefaultToolkit().getImage(images[0][direction]);
    		else 
    			this.img=Toolkit.getDefaultToolkit().getImage(images[1][turn]);
    	}
    	else
    		this.img=Toolkit.getDefaultToolkit().getImage(images[0][direction]);
    }
    
    public int turn(SnakePoint s)   //�ж�ת��
    {
        int turn=-1;   //��ת��
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
    	return turn;
    }
    
    public void change(SnakePoint s)   //���ı������ǰ���£��ı���������
    {
    	int turn=this.turn(s);
    	this.setImg(turn, s.getD());
    	this.setX(s.getX());
    	this.setY(s.getY());
    	this.setD(s.getD());
    }
    
    public void clearPoint()
    {
    	this.isClear=true;
    }

    
    public void drawPoint(Graphics g,ImageObserver i)    //������ڵ�
    { 
    	if(!isClear)	
    		g.drawImage(img,y*20,x*20,20,20,i);
    	else
    		g.drawImage(Toolkit.getDefaultToolkit().getImage("img/floor.gif"), y*20, x*20, 20,20,i);
    }
    
}
