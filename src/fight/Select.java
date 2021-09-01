package fight;

import main.*;
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

public class Select extends JFrame implements ActionListener{
	
    JButton online=new JButton();
	JButton computer=new JButton();
	JButton two=new JButton();
	 JButton goback=new JButton();
	private Control menu=null;
	static Background bg=null;
	int model=-1;   //0为人机、1为双人，2为联机
	OnlineFrame ofr=null,ofr1=null;    //ofr1用于测试
	FightFrame ffr=null;
	
	public Select(Control menu)
	{
		this.menu=menu;
		this.setTitle("对战模式");
		this.setBounds(250, 75, 800, 600);
		this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		this.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e)
			{
				back();
			}
		});
		bg=new Background();
		this.getContentPane().add(bg);
		online.setIcon(new ImageIcon("img/online.png"));
		online.setRolloverIcon(new ImageIcon("img/online1.png"));
		online.setContentAreaFilled(false);
		online.setFocusable(false);
		online.setBorderPainted(false);
		online.addActionListener(this);
		online.setBounds(300, 150, 200, 60);
		bg.add(online);
		computer.setIcon(new ImageIcon("img/computer.png"));
		computer.setRolloverIcon(new ImageIcon("img/computer1.png"));
		computer.setContentAreaFilled(false);
		computer.setFocusable(false);
		computer.setBorderPainted(false);
		computer.addActionListener(this);
		computer.setBounds(300, 230, 200, 60);
		bg.add(computer);
		two.setIcon(new ImageIcon("img/two.png"));
		two.setRolloverIcon(new ImageIcon("img/two1.png"));
		two.setContentAreaFilled(false);
		two.setFocusable(false);
		two.setBorderPainted(false);
		two.addActionListener(this);
		two.setBounds(300, 310, 200, 60);
		bg.add(two);
		goback.setIcon(new ImageIcon("img/back.png"));
		goback.setRolloverIcon(new ImageIcon("img/back1.png"));
		goback.setContentAreaFilled(false);
		goback.setFocusable(false);
		goback.setBorderPainted(false);
		goback.addActionListener(this);
		goback.setBounds(300, 390, 200, 60);
		bg.add(goback);
		this.setVisible(true);
		System.out.println(model);
		
	}
	
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource()==online)
		{
			this.setVisible(false);
			model=2;
			ofr=new OnlineFrame(menu.usercrystal.elementAt(0),menu.user.elementAt(0),this);
			ofr1=new OnlineFrame(menu.usercrystal.elementAt(1),menu.user.elementAt(1),this);
		}
		else if(e.getSource()==computer)
		{
			if(model==-1)
			this.setVisible(false);
			model=0;
			System.out.println("测试");
			
			ffr=new FightFrame(menu.usercrystal.elementAt(0),0,0,menu.user.elementAt(0),"电脑",this);
		}
		else if(e.getSource()==two)
		{
			this.setVisible(false);
			if(menu.user.size()<2)
				JOptionPane.showMessageDialog(this, "登录用户只有一个，不能玩");
			else
			{
				ffr=new FightFrame(menu.usercrystal.elementAt(0),menu.usercrystal.elementAt(1),0,menu.user.elementAt(0),menu.user.elementAt(1),this);
				model=1;
			}
		}
		else if(e.getSource()==goback)
		{
			back();
		}
	}
	
	void update()
	{
		switch(model)
		{
		case 0:
		{
			ffr.dispose();
			ffr=null;
			this.setVisible(true);
		}break;
		case 1:
		{
			menu.usercrystal.setElementAt(new Integer(FightFrame.crystal[0]), 0);
			menu.usercrystal.setElementAt(new Integer(FightFrame.crystal[1]), 1);
			menu.update(FightFrame.crystal[0], menu.userlevel.elementAt(0), menu.user.elementAt(0));
			menu.update(FightFrame.crystal[1], menu.userlevel.elementAt(1), menu.user.elementAt(1));
			ffr.dispose();
			ffr=null;
			this.setVisible(true);
		}break;
		case 2:
		{
			menu.usercrystal.setElementAt(new Integer(ofr.localcrystal), 0);
			menu.update(ofr.localcrystal, menu.userlevel.elementAt(0), menu.user.elementAt(0));
			ofr.dispose();
			ofr=null;
			//用于测试
			menu.usercrystal.setElementAt(new Integer(ofr1.localcrystal), 1);
			menu.update(ofr1.localcrystal, menu.userlevel.elementAt(1), menu.user.elementAt(1));
			ofr1.dispose();
			ofr1=null;
			//用于测试
			this.setVisible(true);
		}break;
		}
	}
	
	public void back()
	{
		this.setVisible(false);
		menu.back();
	}
	
	class Background extends JPanel
	{
		private Image img=Toolkit.getDefaultToolkit().getImage("img/bg.jpg");
		
		Background()
		{
			this.setLayout(null);
			this.setBounds(0, 0, 800, 600);
		}
		
		public void paintComponent(Graphics g)
		{
			super.paintComponent(g);
			g.drawImage(img, 0, 0, 800, 600, this);
		}
	}

}
