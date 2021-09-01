package fight;

import java.awt.*;
import java.awt.image.ImageObserver;
import java.io.*;
import javax.swing.*;

public class DrawMap {        //0表示无东西，1表示为地板，2表示墙,3表示蛇1，4表示蛇2，5表示食物，6表示河流
	
	private String map[][]=new String[22][];
	private int mapint[][]=new int[22][42];
	private String filename;
	private Image img[]=new Image[2];
	
	public DrawMap(int random)
	{
		random++;
		filename=new String("fightmap\\map"+random+".txt");
		getImg();
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
				int i=0,j;
				while(line!=null)
				{
					map[i]=line.split("-");
					line=br.readLine();
					i++;
				}
				br.close();
				for(i=0;i<map.length;i++)     //转换String值为int值
					for(j=0;j<map[0].length;j++)
						mapint[i][j]=Integer.parseInt(map[i][j]);				
				JOptionPane.showMessageDialog(null, "地图信息读取完毕");
						
			}catch(Exception e){
				JOptionPane.showMessageDialog(null, "打开地图失败");
			}
		}	
	}
	
	private void getImg()
	{
		img[0]=Toolkit.getDefaultToolkit().getImage("img/floor.gif");
		img[1]=Toolkit.getDefaultToolkit().getImage("img/wall.gif");
	}
	
	int getmap(int i,int j)    //返回此格物件
	{
		return mapint[i][j];
	}
	
	int getRow()   //得到行数
	{
		return mapint.length;
	}
	
	int getColumn()    //得到列数
	{
		return mapint[0].length;
	}
	
	void setmap(int i,int j,int z)
	{
		mapint[i][j]=z;
	}
	
	void drawMap(Graphics g,ImageObserver im)    //画地图
	{
		for(int i=0;i<mapint.length;i++)
			for(int j=0;j<mapint[0].length;j++)
				switch(mapint[i][j])
				{
					case 0:break;
					case 1:g.drawImage(img[0], 20*j,20*i,20,20, im);break;
					case 2:g.drawImage(img[1], 20*j,20*i,20,20, im);break;
					case 3:
					case 4:
					case 5:
					case 6:g.drawImage(img[0], 20*j,20*i,20,20, im);break;
					
				}				
	}

}
