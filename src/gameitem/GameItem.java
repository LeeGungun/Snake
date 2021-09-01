package gameitem;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.ImageObserver;

public class GameItem {
	
	protected Image img;
	protected int x,y;
	protected boolean isRepaint=false;
	protected Image fightimg=Toolkit.getDefaultToolkit().getImage("img/floor.gif");
	
	
	public GameItem()
	{
		
	}
	
	public GameItem(int x,int y)
	{
		this.x=x;
		this.y=y;
	}
	
	public GameItem(GameItem g)
	{
		this.x=g.x;
		this.y=g.y;
		this.img=g.img;
		this.isRepaint=g.isRepaint;
	}
	
    public int getX()
    {
   	return this.x;
    }
   
   public int getY()
   {
   	return this.y;
   }
   
   public Image getimg()
   {
	   return this.img;
   }
	
	public void cleanItem() {

		this.isRepaint=true;
		
	}
	
	public void drawFightItem(Graphics g,ImageObserver i)
	{
		if(!isRepaint)
    	{
    	
        	g.drawImage(img,y*20,x*20,20,20,i);
    	}
    	else
    	{
        	g.drawImage(fightimg, y*20, x*20, 20, 20, i);
    	}	
	}
	
    public void drawItem(Graphics g,ImageObserver i,int m)
    {
    	if(!isRepaint)
    	{
    	
        	g.drawImage(img,x+1,y+1,m,m,i);
    	}
    	else
    	{
        	Color color=new Color(71,70,71);
        	g.setColor(color);
        	g.fillRect(x+1, y+1, m, m);
    	}		
    }
}

