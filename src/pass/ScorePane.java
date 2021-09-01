package pass;

import java.awt.Color;
import java.awt.Font;

import java.util.*;

import javax.swing.*;

public class ScorePane extends JLabel{
	
	int score=0;
	java.util.Timer timer;
	NewPane task;
	Play p;
	
	public ScorePane(Play p)
	{
		this.p=p;
		this.setBackground(new Color(194,211,223));
		this.setBounds(820, 110, 150, 50);
		this.setHorizontalAlignment(CENTER);
		this.setForeground(Color.darkGray);
		Font f=new Font("ÃÔÄã¼òÅÖÍ·Óã",Font.PLAIN,16);
		this.setFont(f);
		String s="score:   0";
		this.setText(s);
		this.setOpaque(true);
		this.setVisible(true);
		timer=new java.util.Timer();
	}
	
	class NewPane extends TimerTask
	{
		public void run()
		{
			if(Level.state>0)
			{
				if(score!=p.getScore())
					setText("score: "+p.getScore());
			}
			else
				this.cancel();
			
		}
	}
	
	public void ScorePaneStart()
	{
		
		task=new NewPane();
		timer.schedule(task, Play.speed2, Play.speed2);
	}

}
