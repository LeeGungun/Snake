package gameitem;

import java.awt.Toolkit;

public class Water extends GameItem{
	
	public Water(int x,int y)
	{
		super(x,y);
		img=Toolkit.getDefaultToolkit().getImage("img/river.gif");
	}

}
