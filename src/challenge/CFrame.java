package challenge;

import javax.swing.*;
import main.*;
import java.awt.*;
import java.awt.event.*;

public class CFrame extends JFrame implements ActionListener {
	
	static int px[]={15,30};
	static Point hole[]=new Point[3];
	static int stage=1;
	Image bgimg;
	static BackgroundPane bg;
	CPlay pane;
	static ShowGM message;
	JButton startB;
	JButton sleepB;
	JButton backB;
	static JPanel messagePane=null;
	private Control menu=null;
	
	public CFrame(Control menu)
	{
		setTitle("贪吃蛇——挑战模式");
		this.menu=menu;
		message=null;
		messagePane=null;
		bg=null;
		stage=1;
		hole[0]=new Point(0,0);
		hole[1]=new Point(0,0);
		hole[2]=new Point(0,0);
		this.setBounds(100, 0, 1200, 650);
		this.setLayout(null);
		bg=new BackgroundPane();
		this.getContentPane().add(bg);
		startB=new JButton();
		startB.setBounds(150, 500, 70, 70);
		startB.setIcon(new ImageIcon("cimg/start.gif"));
		startB.setRolloverIcon(new ImageIcon("cimg/start1.gif"));
		startB.setContentAreaFilled(false);
		startB.setFocusable(false);
		startB.setBorderPainted(false);
		startB.addActionListener(this);
		sleepB=new JButton();
		sleepB.setBounds(270, 500, 70, 70);
		sleepB.setIcon(new ImageIcon("cimg/sleep.gif"));
		sleepB.setRolloverIcon(new ImageIcon("cimg/sleep1.gif"));
		sleepB.setContentAreaFilled(false);
		sleepB.setFocusable(false);
		sleepB.setBorderPainted(false);
		sleepB.addActionListener(this);
		backB=new JButton();
		backB.setBounds(390, 500, 70, 70);
		backB.setIcon(new ImageIcon("cimg/back.gif"));
		backB.setRolloverIcon(new ImageIcon("cimg/back1.gif"));
		backB.setContentAreaFilled(false);
		backB.setFocusable(false);
		backB.setBorderPainted(false);
		backB.addActionListener(this);
		bg.add(startB);
		bg.add(sleepB);
		bg.add(backB);
		this.setFocusable(true);
		pane=new CPlay(this);
		pane.setBounds(130, 100, 600, 300);
		this.getLayeredPane().add(pane,JLayeredPane.PALETTE_LAYER);
		message=new ShowGM();
		message.setLocation(760, 5);
		this.getLayeredPane().add(message, JLayeredPane.MODAL_LAYER);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);            //need to update
		messagePane=(JPanel)getGlassPane();
		messagePane.setLayout(null);
		this.setVisible(true);
		this.addWindowListener(new WindowAdapter()
					{
						public void windowClosing(WindowEvent e)
						{
							back();
						}
					});
	}
	
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource()==startB)
		{
			if(CPlay.state==0)
			{
				message.time.TimeScreenStart();
				pane.init();
			}
		}
		else if(e.getSource()==sleepB)
		{
			if(CPlay.state==1)
			{
				CPlay.state=2;
				message.time.isStop=true;
				sleepB.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("cimg/continue.gif")));
				sleepB.setRolloverIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("cimg/continue1.gif")));
			}
			else if(CPlay.state==2)
			{
				CPlay.state=1;
				message.time.isStop=false;
				sleepB.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("cimg/sleep.gif")));
				sleepB.setRolloverIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("cimg/sleep1.gif")));
			}
		}
		else if(e.getSource()==backB)
		{
			back();
		}
	}
	
	 class BackgroundPane extends JPanel     //背景面板类
	{
		Image bgimg=Toolkit.getDefaultToolkit().getImage("cimg/stage"+CFrame.stage+".jpg");
		int stage;
		 
		BackgroundPane()
		{
			this.setOpaque(false);
			this.setLayout(null);
			this.setBounds(0,0,1200,650);
			this.stage=CFrame.stage;
		}
		
		public void paintComponent(Graphics g)
		{
			super.paintComponent(g);
			MediaTracker tracker=new MediaTracker(this);
			if(stage!=CFrame.stage)
			{
				bgimg=Toolkit.getDefaultToolkit().getImage("cimg/stage"+CFrame.stage+".jpg");
				stage=CFrame.stage;
			}
			tracker.addImage(bgimg, 0, 1200, 650);
			tracker.checkAll(true);
			g.drawImage(bgimg, 0, 0,1200,650, this);
		}
	}
	 
	 public void back()
	 {
		 this.setVisible(false);
		 menu.back();
	 }
	 
	 public void replay()
	 {
		 CFrame.stage=1;
		 CPlay.state=0;
		 message.time.isStop=false;
		 message.time.setText("00:00:00");
		 message.dps.setSum(0);
		 message.dps.change=0;
		 this.bg.repaint();
		 message.dps.repaint();
		 CPlay.isStop=false;
		 pane.map=new DrawMap();
		 pane.repaint();
	 }
	
	/*public static void main(String args[])
	{
		CFrame fr=new CFrame();
		//fr.pane.showHoling();
		//fr.pane.init();
		//fr.message.dps.reduce();
		//fr.message.dps.add();
		
	}*/
}
