package pass;

import java.util.*;
import java.text.*;
import java.awt.*;
import javax.swing.*;

public class TimeScreen extends JLabel{
	
	public boolean isStop=false;    //过关窗口弹出或重玩
	Date before,stop,now;
	java.util.Timer t;
	Task task;
	public static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm:ss");
	
	public TimeScreen()
	{
		this.setBackground(new Color(194,211,223));
		this.setBounds(820, 50, 150, 50);
		this.setHorizontalAlignment(CENTER);
		this.setForeground(Color.darkGray);
		Font f=new Font("迷你简胖头鱼",Font.PLAIN,16);
		this.setFont(f);
		String s="00:00:00";
		this.setText(s);
		this.setOpaque(true);
		this.setVisible(true);
		t=new java.util.Timer();
	}
	
    class Task extends TimerTask
    {
    	public void run()
    	{
    		if(Level.state>0)
    		{
    			if(!isStop)
    			{
    				before=now;
    				now=new Date(before.getTime()+1000);
    				stop=now;
    				write();
    			}
    			else
    			{
    				before=stop;
    				now=new Date(before.getTime()+1000);
    				write();  			
    			}
    		}
    		else
    			this.cancel();
    	}
    }
	public void TimeScreenStart()
	{
		before=new Date();
		before.setHours(0);
		before.setMinutes(0);
		before.setSeconds(0);
		now=before;
		stop=before;
		task=new Task();
		t.schedule(task, 1000,1000);
	}

	
	private void write()
	{
		if(!isStop)
		{
			
			String s=TIME_FORMAT.format(now);
			this.setText(s);
		}
		else
		{
			String s=TIME_FORMAT.format(stop);
			this.setText(s);
		}
	
	}
	

	
}