package challenge;

import java.awt.*;
import java.awt.image.ImageObserver;
import java.io.*;
import javax.swing.*;

public class DrawMap {        //0表示无东西，1表示为地板，场景1、2中2、3均表示场内障碍，场景3中2表示霸王蛇（场内障碍），3表示其他地板，4表示墙，5表示洞
	
	private String map[][]=new String[20][];
	private int mapint[][]=new int[20][40];
	private String filename;
	Image img,img1;
	static int x1=0,y1=0,x2=19,y2=39;    //游戏时记录当前显示范围
	int px=15;
	//Draw drawmap1,drawmap2;
	
	public DrawMap()
	{
		img=Toolkit.getDefaultToolkit().getImage("cimg/stage"+CFrame.stage+".gif");
		img1=Toolkit.getDefaultToolkit().getImage("cimg/stage"+CFrame.stage+".png");
		filename=new String("challengemap\\stage"+CFrame.stage+".txt");
		read();
	}
	
	void read()
	{
		File file=new File(filename);
		if(file.exists())
		{
			try{
				BufferedReader br=new BufferedReader(new FileReader(filename));
				String line=br.readLine();
				int i=0,j=0;
				while(line!=null)
				{
					map[i]=line.split("-");
					line=br.readLine();
					i++;
				}
				br.close();
				for(i=0;i<map.length;i++)     //转换String值为int值
					for(j=0;j<map[0].length;j++)
					{
						mapint[i][j]=Integer.parseInt(map[i][j]);
						if(mapint[i][j]==5)
						{
							CFrame.hole[CFrame.stage-1].x=i;
							CFrame.hole[CFrame.stage-1].y=j;
						}
					}
						
			}catch(Exception e){
				JOptionPane.showMessageDialog(null, "打开地图失败");
			}
		}	
	}
	
	int getmap(int i,int j)    //返回此格物件
	{
		return mapint[i][j];
	}
	
	void setmap(int i,int j,int z)
	{
		mapint[i][j]=z;
	}
	
	private void drawBox(Graphics g,ImageObserver im,int xo,int yo,int xe,int ye,int px)    //具体实现画地图
	{
		x1=xo;
		x2=xe;
		y1=yo;
		y2=ye;
		this.px=px;
		g.drawImage(img1, 0, 0, 600, 300, yo*30, xo*30, (ye+1)*30,(xe+1)*30, im);
		
	}
	
/*	void drawMap(CPlay pane)
	{
		if(drawmap1!=null)
		{
			
			drawmap1.setVisible(false);
			pane.remove(drawmap1);
			drawmap1=null;
			drawmap2=new Draw(pane);
			pane.add(drawmap2);
		}
		else if(drawmap2!=null)
		{
			drawmap2.setVisible(false);
			pane.remove(drawmap2);
			drawmap2=null;
			drawmap1=new Draw(pane);
			pane.add(drawmap1);
		}
		else
		{
			drawmap1=new Draw(pane);
			pane.add(drawmap1);
		}
		
	}
	
	class Draw extends JPanel
	{
		CPlay pane;
		
		Draw(CPlay pane)
		{
			this.pane=pane;
			this.setOpaque(false);
			this.setBounds(0, 0, 600, 300);
			this.setLayout(null);
		}
		
		public void paintComponent(Graphics g)
		{
			super.paintComponent(g);
			if(CPlay.state==0||CPlay.state==3||CPlay.isStop)
			{
				for(int i=0;i<20;i++)
					for(int j=0;j<40;j++)
							if(mapint[i][j]!=0)
								g.drawImage(img, CFrame.px[0]*j, CFrame.px[0]*i, CFrame.px[0]*(j+1), CFrame.px[0]*(i+1), 30*mapint[i][j]-30, 0, 30*mapint[i][j], 30, this);

			}
			else
			{
				if(pane.snake!=null)
				{
					
					int d=pane.snake.head.getD(),x=pane.snake.head.getX(),y=pane.snake.head.getY();
					switch(d)
					{
					case 0:
					{
						if(x<9)
						{
							if(y<10)
								drawBox(g,this,0,0,9,19,CFrame.px[1]);
							else if(y>30)
								drawBox(g,this,0,20,9,39,CFrame.px[1]);
							else
								drawBox(g,this,0,y-10,9,y+9,CFrame.px[1]);
										
						}
						else 
						{
							if(y<10)
								drawBox(g,this,x-9,0,x,19,CFrame.px[1]);
							else if(y>30)
								drawBox(g,this,x-9,20,x,39,CFrame.px[1]);
							else
								drawBox(g,this,x-9,y-10,x,y+9,CFrame.px[1]);
						}
					}break;
					case 1:
					{
						if(x>10)
						{
							if(y<10)
								drawBox(g,this,10,0,19,19,CFrame.px[1]);
							else if(y>30)
								drawBox(g,this,10,20,10,39,CFrame.px[1]);
							else
								drawBox(g,this,10,y-10,19,y+9,CFrame.px[1]);
										
						}
						else 
						{
							if(y<10)
								drawBox(g,this,x,0,x+9,19,CFrame.px[1]);
							else if(y>30)
								drawBox(g,this,x,20,x+9,39,CFrame.px[1]);
							else
								drawBox(g,this,x,y-10,x+9,y+9,CFrame.px[1]);
						}
					}break;
					case 2:
					{
						if(y<19)
						{
							if(x<5)
								drawBox(g,this,0,0,9,19,CFrame.px[1]);
							else if(x>15)
								drawBox(g,this,10,0,19,19,CFrame.px[1]);
							else
								drawBox(g,this,x-5,0,x+4,19,CFrame.px[1]);
						}
						else
						{
							if(x<5)
								drawBox(g,this,0,y-19,9,y,CFrame.px[1]);
							else if(x>15)
								drawBox(g,this,10,y-19,19,y,CFrame.px[1]);
							else
								drawBox(g,this,x-5,y-19,x+4,y,CFrame.px[1]);
						}
					}break;
					case 3:
					{
						if(y>20)
						{
							if(x<5)
								drawBox(g,this,0,20,9,39,CFrame.px[1]);
							else if(x>15)
								drawBox(g,this,10,20,19,39,CFrame.px[1]);
							else
								drawBox(g,this,x-5,20,x+4,39,CFrame.px[1]);
						}
						else
						{
							if(x<5)
								drawBox(g,this,0,y,9,y+19,CFrame.px[1]);
							else if(x>15)
								drawBox(g,this,10,y,19,y+19,CFrame.px[1]);
							else
								drawBox(g,this,x-5,y,x+4,y+19,CFrame.px[1]);
						}
					}break;
					}
					
				}
			}
			if(CPlay.state>0)
			{
				pane.snake.drawSnake(g, this);
				switch(CFrame.stage)
				{
				case 1:if(pane.rabbit!=null)
					pane.rabbit.repaint();
				break;
				case 2:if(pane.box!=null)
					pane.box.repaint();
				break;
				case 3:if(pane.mouse!=null)
					pane.mouse.repaint();
				break;
				}
			}
		}
	}*/
	
	
	void drawMap(Graphics g,ImageObserver im,Snake snake)    //画地图
	{
		if(CPlay.state==0||CPlay.state==3||CPlay.isStop)
		{
			px=CFrame.px[0];
			g.drawImage(img1,0,0,600,300,im);
		}
		else
		{
			if(snake!=null)
			{

				g.drawImage(CFrame.bg.bgimg,0,0,600,300,130,100,730,400,im);
				int d=snake.head.getD(),x=snake.head.getX(),y=snake.head.getY();
				switch(d)
				{
				case 0:
				{
					if(x<9)
					{
						if(y<10)
							drawBox(g,im,0,0,9,19,CFrame.px[1]);
						else if(y>30)
							drawBox(g,im,0,20,9,39,CFrame.px[1]);
						else
							drawBox(g,im,0,y-10,9,y+9,CFrame.px[1]);
									
					}
					else 
					{
						if(y<10)
							drawBox(g,im,x-9,0,x,19,CFrame.px[1]);
						else if(y>30)
							drawBox(g,im,x-9,20,x,39,CFrame.px[1]);
						else
							drawBox(g,im,x-9,y-10,x,y+9,CFrame.px[1]);
					}
				}break;
				case 1:
				{
					if(x>10)
					{
						if(y<10)
							drawBox(g,im,10,0,19,19,CFrame.px[1]);
						else if(y>30)
							drawBox(g,im,10,20,10,39,CFrame.px[1]);
						else
							drawBox(g,im,10,y-10,19,y+9,CFrame.px[1]);
									
					}
					else 
					{
						if(y<10)
							drawBox(g,im,x,0,x+9,19,CFrame.px[1]);
						else if(y>30)
							drawBox(g,im,x,20,x+9,39,CFrame.px[1]);
						else
							drawBox(g,im,x,y-10,x+9,y+9,CFrame.px[1]);
					}
				}break;
				case 2:
				{
					if(y<19)
					{
						if(x<5)
							drawBox(g,im,0,0,9,19,CFrame.px[1]);
						else if(x>15)
							drawBox(g,im,10,0,19,19,CFrame.px[1]);
						else
							drawBox(g,im,x-5,0,x+4,19,CFrame.px[1]);
					}
					else
					{
						if(x<5)
							drawBox(g,im,0,y-19,9,y,CFrame.px[1]);
						else if(x>15)
							drawBox(g,im,10,y-19,19,y,CFrame.px[1]);
						else
							drawBox(g,im,x-5,y-19,x+4,y,CFrame.px[1]);
					}
				}break;
				case 3:
				{
					if(y>20)
					{
						if(x<5)
							drawBox(g,im,0,20,9,39,CFrame.px[1]);
						else if(x>15)
							drawBox(g,im,10,20,19,39,CFrame.px[1]);
						else
							drawBox(g,im,x-5,20,x+4,39,CFrame.px[1]);
					}
					else
					{
						if(x<5)
							drawBox(g,im,0,y,9,y+19,CFrame.px[1]);
						else if(x>15)
							drawBox(g,im,10,y,19,y+19,CFrame.px[1]);
						else
							drawBox(g,im,x-5,y,x+4,y+19,CFrame.px[1]);
					}
				}break;
				}
				
			}
		}
	}

}
