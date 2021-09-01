package gameitem;

import java.awt.Toolkit;

public class Apple extends GameItem {
	
	public Apple(int x,int y)
	{
		super(x, y);
		img=Toolkit.getDefaultToolkit().getImage("img/apple.gif");

	}

}
