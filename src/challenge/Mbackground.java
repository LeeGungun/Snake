package challenge;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Mbackground extends JPanel{
	
	Image img;
	boolean close=false;
	
	Mbackground(int num)
	{
		this.setBounds(130, 100, 600, 300);
		this.setLayout(null);
		this.setFocusable(true);
		this.addMouseListener(new MouseAction());
		if(CFrame.stage!=3)
			img=Toolkit.getDefaultToolkit().getImage("cimg/wound"+CFrame.stage+"-"+num+".png");
		
	}
	
	Mbackground()
	{
		this.setBounds(130, 100, 600, 300);
		this.setLayout(null);
		this.setFocusable(true);
		this.addMouseListener(new MouseAction());
		img=Toolkit.getDefaultToolkit().getImage("cimg/wound3.png");
	}
	
	final class MouseAction extends MouseAdapter
	{
		public void mousePressed(final MouseEvent e)
		{
			close=true;
		}
	}
	
	public void paintComponent(Graphics g)
	{
		//super.paintComponent(g);
		g.drawImage(img, 0, 0, 600, 300, this);
	}

}
