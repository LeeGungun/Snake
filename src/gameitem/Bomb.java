package gameitem;

import java.awt.Toolkit;

public class Bomb extends GameItem {
	
	public Bomb(int x,int y)
	{
		super(x, y);
		img=Toolkit.getDefaultToolkit().getImage("img/death.gif");
	}

}
