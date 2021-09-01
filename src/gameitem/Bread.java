package gameitem;

import java.awt.Toolkit;

public class Bread extends GameItem {
	
	public Bread(int x,int y)
	{
		super(x, y);
		img=Toolkit.getDefaultToolkit().getImage("img/bread.gif");

	}
}
