package challenge;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.*;



public class Test {
	Mbackground mbg;
	
	synchronized void message()
	{
		mbg=new Mbackground();
		CFrame.messagePane.add(mbg);
		
		//mbg.repaint();
		new Thread(new Runnable(){
			public void run()
			{
				while(true)
				{
					if(!mbg.close)
						CFrame.messagePane.setVisible(true);
					else
					{
						CFrame.messagePane.setVisible(false);
						mbg=null;
						break;
					}
					try
					{
						Thread.sleep(500);
					}catch(InterruptedException e){
						e.printStackTrace();
					}
				}
			}
		}).start();
	}
	class Mbackground extends JPanel
	{
		Image img=Toolkit.getDefaultToolkit().getImage("cimg/wound2-3.png");
		Image wimg=Toolkit.getDefaultToolkit().getImage("cimg/toxicfog.gif");
		boolean close=false;
		JLabel label1,label;
		
		Mbackground()
		{
			this.setBounds(130, 100, 600, 300);
			this.setLayout(null);
			this.setFocusable(true);
			this.addMouseListener(new MouseAction());
			
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
			//super.paintComponents(g);
			g.drawImage(img, 0, 0, 600, 300, this);
			//g.drawImage(wimg,260,50,80,100,this);
		}
	}

}
