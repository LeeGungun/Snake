package fight;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class FightFrame extends JFrame implements ActionListener {    //判断当前用户机的玩家编号
	
	protected Container container;
	protected BackgroundPane bgp;
	Play pane=null;
	static int crystal[]={0,0};    //玩家水晶数
	protected static JButton button=new JButton("返回");
	protected boolean isBack=false;   //当为真时回到菜单页
	protected Progress player1,player2;
	private static Help text=new Help();
	protected static JLabel title1;
	protected static JLabel title2;
	private int model;
	protected String name1=null;
	protected String name2=null;
	private Select select=null;
	
	public FightFrame()
	{
		
	}
	
	public FightFrame(int Acrystal,int Bcrystal,int model,String name1,String name2,Select select)
	{
		crystal[0]=Acrystal;
		crystal[1]=Bcrystal;
		this.model=model;
		this.name1=name1;
		this.name2=name2;
		this.select=select;
		switch(model)
		{
			case 0:this.setTitle("贪吃蛇——人机对战模式");break;
			case 1:this.setTitle("贪吃蛇——双人对战模式");break;
		}
		this.setSize(1050,700);
		int width=Toolkit.getDefaultToolkit().getScreenSize().width;
		this.setLocation((width-1050)/2, 0);
		this.setResizable(false);
		container=this.getContentPane();
		container.setLayout(null);
		bgp=new BackgroundPane();
		bgp.setBounds(0,0,1050,700);
		container.add(bgp);
		button.setBounds(950,600,70,40);
		button.addActionListener(this);
		bgp.setLayout(null);
		bgp.add(button);
		bgp.add(text);
		this.setFocusable(true);
		this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		pane=new Play(null,true,this);  
		this.getLayeredPane().add(pane,JLayeredPane.MODAL_LAYER);
		player1=new Progress(null,true,1,this,name1);
		player2=new Progress(null,true,2,this,name2);
		player1.setLocation(30,0);
		player2.setLocation(600, 0);
		this.getLayeredPane().add(player1,JLayeredPane.PALETTE_LAYER);
		this.getLayeredPane().add(player2,JLayeredPane.PALETTE_LAYER);
		title1=new JLabel(new ImageIcon(Toolkit.getDefaultToolkit().getImage("img/title1.gif")));
		title1.setBounds(5, 150, 80, 160);
		this.getLayeredPane().add(title1,JLayeredPane.PALETTE_LAYER);
		title2=new JLabel(new ImageIcon(Toolkit.getDefaultToolkit().getImage("img/title2.gif")));
		title2.setBounds(935, 150, 80, 160);
		this.getLayeredPane().add(title2,JLayeredPane.PALETTE_LAYER);
		title1.setVisible(false);
		title2.setVisible(false);
		this.setVisible(true);
		this.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e)
			{
				isBack=true;
				back();
			}
		});
		
	}
	
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource()==button)
		{
			isBack=true;
			back();
		}
	}
	
	int getM()   //获取对战模式
	{
		return model;
	}
	
	 void back()    //返回选择模式
	{
		if(isBack)
		{
	        
			select.update();
			//将money的信息变化写回数据库
		}
	}
	
	 void regame()    //重新游戏
	{
		//游戏模块
		pane.isOver=false;
		pane.state=0;
		pane.lose=0;
		pane.score[0]=0;
		pane.score[1]=0;
		pane.initmap();
		pane.repaint();
		//计时模块
		
	}
	
	
	
	 static class Help extends JLabel
	{
		Help()
		{
			super();
			this.setOpaque(false);
			this.setBounds(90,600,845,80);
		}
		
		public void paintComponent(Graphics g)
		{
			super.paintComponent(g);
			Graphics2D g2=(Graphics2D)g;
			Stroke stroke=new BasicStroke(5.0f,BasicStroke.CAP_BUTT,BasicStroke.JOIN_BEVEL,0,new float[]{10,5},0);
			g2.setStroke(stroke);
			g2.setColor(Color.ORANGE);
			g2.drawRect(10, 10, 830, 50);
			g.setColor(Color.BLACK);
			g.setFont(new Font("迷你简方隶",Font.BOLD,17));
			g.drawString("Enter开始/继续；空格键暂停；使用水晶可延迟对方动作5秒；玩家一用shift键，玩家二用Q键；", 35, 30);
		    g.drawString("玩家一方向键为键盘方向键，玩家二方向键为W/A/S/D键；绿蛇为玩家一，蓝蛇为玩家二", 35, 45);
		}
	}
	
	protected class BackgroundPane extends JPanel     //背景面板类
	{
		Image bgimg=Toolkit.getDefaultToolkit().getImage("img/fightback.jpg");
		
		public void paintComponent(Graphics g)
		{
			super.paintComponent(g);
			g.drawImage(bgimg, 0, 0,1050,700, this);
		}
	}
	
	/*public static void main(String args[])
	{
		new FightFrame(1,0,0,"ha","电脑");
	}*/

}
