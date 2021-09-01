package fight;

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class Progress extends JPanel{
	
	private int player=0;   //记录当前玩家
	Time jindu;      //显示时间条
	JLabel showP;     //显示玩家
	JLabel showS;     //显示得分
	JLabel showC=null;     //显示水晶数
	private FightFrame fr=null;
	private OnlineFrame ofr=null;
	private java.util.Timer culculator;
	
	Progress(LayoutManager lm,boolean isDoubleBuffered,int player,FightFrame fr,String name )
	{
		super(lm,isDoubleBuffered);
		this.player=player;
		this.setSize(410, 150);
		this.setOpaque(false);
		this.fr=fr;
		showP=new JLabel("玩家"+player+":"+name);
		showP.setFont(new Font("迷你简琥珀",Font.BOLD,12));
		showP.setForeground(new Color(255,55,1));
		showP.setOpaque(false);
		showP.setBounds(320*(player-1),20,80,40);
		showS=new JLabel("score: 0");
		showS.setFont(new Font("迷你简琥珀",Font.BOLD,15));
		showS.setForeground(new Color(255,55,1));
		showS.setBounds(320*(player-1),60,80,30);
		showS.setOpaque(false);
		if(fr.getM()>0)
		{
			showC=new JLabel("×"+FightFrame.crystal[player-1],new ImageIcon(Toolkit.getDefaultToolkit().getImage("img/money.gif")),JLabel.CENTER);
			showC.setFont(new Font("迷你简琥珀",Font.BOLD,16));
			showC.setForeground(new Color(255,55,1));
			showC.setBounds(320*(player-1),90,80,40);
			showC.setOpaque(false);
			this.add(showC);
		}
		this.add(showP);
		this.add(showS);
		jindu=new Time(null,true);
		jindu.setOpaque(false);
		jindu.setBounds(90*(2-player),20,320,110);
		this.add(jindu);
		this.setVisible(true);	
	}
	
	Progress(LayoutManager lm,boolean isDoubleBuffered,int player,OnlineFrame fr,String name )
	{
		super(lm,isDoubleBuffered);
		this.player=player;
		this.setSize(410, 150);
		this.setOpaque(false);
		this.ofr=fr;
		showP=new JLabel("玩家"+player+":"+name);
		showP.setFont(new Font("迷你简琥珀",Font.BOLD,12));
		showP.setForeground(new Color(255,55,1));
		showP.setOpaque(false);
		showP.setBounds(320*(player-1),20,80,40);
		showS=new JLabel("score: 0");
		showS.setFont(new Font("迷你简琥珀",Font.BOLD,15));
		showS.setForeground(new Color(255,55,1));
		showS.setBounds(320*(player-1),60,80,30);
		showS.setOpaque(false);
		showC=new JLabel("×"+fr.crystal[player-1],new ImageIcon(Toolkit.getDefaultToolkit().getImage("img/money.gif")),JLabel.CENTER);
		showC.setFont(new Font("迷你简琥珀",Font.BOLD,16));
		showC.setForeground(new Color(255,55,1));
		showC.setBounds(320*(player-1),90,80,40);
		showC.setOpaque(false);
		this.add(showC);
		this.add(showP);
		this.add(showS);
		jindu=new Time(null,true);
		jindu.setOpaque(false);
		jindu.setBounds(90*(2-player),20,320,110);
		this.add(jindu);
		this.setVisible(true);	
	}
	
	void update(String name)
	{
		showP.setText("玩家"+player+":"+name);
		showC.setText("×"+ofr.crystal[player-1]);
	}
	
	void reset(String name,int crystal)
	{
		showP.setText("玩家"+player+":"+name);
		showC.setText("×"+crystal);
	}
	
	void initP()     //开始游戏计时
	{
		culculator=new java.util.Timer();
		jindu.init();
	}
	
	class Time extends JPanel
	{
		private float finishLine=252;
		private int i=1;
		private Image img;
		
		Time(LayoutManager lm,boolean isDoubleBuffered)
		{
			super(lm,isDoubleBuffered);
			img=Toolkit.getDefaultToolkit().getImage("img/star.gif");
		}
		
		void init()
		{
			culculator.schedule(new GameControl(), 1000, 1000);
		}
		
		class GameControl extends TimerTask
		{
			Date start,stop;
			int time=180000;   //计时三分钟
			
			GameControl()
			{
				start=new Date(System.currentTimeMillis());
				stop=start;
			}
			
			public void run()
			{
				if(fr!=null)
				{
					if(fr.pane.state==1)
					{
						stop=new Date(stop.getTime()+1000);
						if(stop.getTime()-start.getTime()<time)    //时间不到
						{
							finishLine-=1.40;
							repaint();
							showS.setText("score:"+fr.pane.score[player-1]);
							if(fr.getM()>0)
								showC.setText("×"+FightFrame.crystal[player-1]);
						}
						else   //时间已到
						{
							finishLine=0;
							repaint();
							fr.pane.whichWin();

						}
					}
					else if(fr.pane.state==0)//&&stop.getTime()-start.getTime()>=time)
					{
						repaint();   //重玩
						finishLine=252;
						showS.setText("score: 0");
						culculator.cancel();
					}
			}

			if(ofr!=null)
			{
				if(ofr.pane.state==1)
				{
					stop=new Date(stop.getTime()+1000);
					if(stop.getTime()-start.getTime()<time)    //时间不到
					{
						finishLine-=1.40;
						repaint();
						showS.setText("score:"+ofr.pane.score[player-1]);
						showC.setText("×"+ofr.crystal[player-1]);
					}
					else   //时间已到
					{
						finishLine=0;
						repaint();
						ofr.pane.whichWin();

					}
				}
				else if(ofr.pane.state==0)//&&stop.getTime()-start.getTime()>=time)
				{
					repaint();   //重玩
					finishLine=252;
					showS.setText("score: 0");
					culculator.cancel();
				}
			}
		}
		}
		
		public void paint(Graphics g)
		{
			super.paint(g);
			Graphics2D g2=(Graphics2D)g;
			Stroke stroke=new BasicStroke(5.0f,BasicStroke.CAP_SQUARE,BasicStroke.JOIN_ROUND);
			g2.setColor(new Color(245,239,117));
			g2.setStroke(stroke);
			g2.drawRoundRect(15, 25, 290, 60, 50, 20);
			g.setColor(new Color(77,197,226));
			g.drawRoundRect(29, 39, 254, 30, 40, 15);
			Color color1=new Color(26,170,192);
			Color color2=new Color(165,247,244);
			if(fr!=null)
			{
				if(fr.pane.state==0)
				{
					g2.setPaint(new GradientPaint(30f,40f,color1,282f,68f,color2,true));
					g2.fillRoundRect(30, 40, 252, 28, 38, 13);
				}
				else if(fr.pane.state==1)
				{
					if(finishLine<84&&finishLine>0)
					{
						if(i==1)
						{
							g2.setPaint(new GradientPaint(30f,40f,new Color(255,54,0),finishLine+30,68f,new Color(246,131,116),true));
							g2.fillRoundRect(30, 40,(int)finishLine, 28, 38, 13);
							i--;
						}
						else
						{
							g2.setPaint(new GradientPaint(30f,40f,color1,finishLine+30,68f,color2,true));
							g2.fillRoundRect(30, 40, (int)finishLine, 28, 38, 13);
							i=1;
						}
						g.drawImage(img, (int)finishLine+30, 12, 28,56,this);

					}
					else if(finishLine>=84)
					{
						g2.setPaint(new GradientPaint(30f,40f,color1,finishLine+30,68f,color2,true));
						g2.fillRoundRect(30, 40, (int)finishLine, 28, 38, 13);
						g.drawImage(img, (int)finishLine+30, 12, 28,56,this);
					}

				}
				else if(fr.pane.state==2)
				{
					if(finishLine<84&&finishLine>0)
					{
						if(i==1)
						{
							g2.setPaint(new GradientPaint(30f,40f,new Color(255,54,0),finishLine+30,68f,new Color(246,131,116),true));
							g2.fillRoundRect(30, 40,(int)finishLine, 28, 38, 13);
							i--;
						}
						else
						{
							g2.setPaint(new GradientPaint(30f,40f,color1,finishLine+30,68f,color2,true));
							g2.fillRoundRect(30, 40, (int)finishLine, 28, 38, 13);
							i=1;
						}

					}
					else if(finishLine>=84)
					{
						g2.setPaint(new GradientPaint(30f,40f,color1,finishLine+30,68f,color2,true));
						g2.fillRoundRect(30, 40, (int)finishLine, 28, 38, 13);

					}
				}
			}
			if(ofr!=null)
			{
				if(ofr.pane.state==0)
				{
					g2.setPaint(new GradientPaint(30f,40f,color1,282f,68f,color2,true));
					g2.fillRoundRect(30, 40, 252, 28, 38, 13);
				}
				else if(ofr.pane.state==1)
				{
					if(finishLine<84&&finishLine>0)
					{
						if(i==1)
						{
							g2.setPaint(new GradientPaint(30f,40f,new Color(255,54,0),finishLine+30,68f,new Color(246,131,116),true));
							g2.fillRoundRect(30, 40,(int)finishLine, 28, 38, 13);
							i--;
						}
						else
						{
							g2.setPaint(new GradientPaint(30f,40f,color1,finishLine+30,68f,color2,true));
							g2.fillRoundRect(30, 40, (int)finishLine, 28, 38, 13);
							i=1;
						}
						g.drawImage(img, (int)finishLine+30, 12, 28,56,this);

					}
					else if(finishLine>=84)
					{
						g2.setPaint(new GradientPaint(30f,40f,color1,finishLine+30,68f,color2,true));
						g2.fillRoundRect(30, 40, (int)finishLine, 28, 38, 13);
						g.drawImage(img, (int)finishLine+30, 12, 28,56,this);
					}

				}
				else if(ofr.pane.state==2)
				{
					if(finishLine<84&&finishLine>0)
					{
						if(i==1)
						{
							g2.setPaint(new GradientPaint(30f,40f,new Color(255,54,0),finishLine+30,68f,new Color(246,131,116),true));
							g2.fillRoundRect(30, 40,(int)finishLine, 28, 38, 13);
							i--;
						}
						else
						{
							g2.setPaint(new GradientPaint(30f,40f,color1,finishLine+30,68f,color2,true));
							g2.fillRoundRect(30, 40, (int)finishLine, 28, 38, 13);
							i=1;
						}

					}
					else if(finishLine>=84)
					{
						g2.setPaint(new GradientPaint(30f,40f,color1,finishLine+30,68f,color2,true));
						g2.fillRoundRect(30, 40, (int)finishLine, 28, 38, 13);

					}
				}
			}
		}
	}
	
	
	

}
