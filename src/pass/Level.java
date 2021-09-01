package pass;

import javax.swing.*;

import java.awt.*;

import java.awt.event.*;

import java.util.*;

import java.io.*;

public class Level {
	
	JFrame jf;
	int grade;
	Play pane;
	TimeScreen time;
	ScorePane scorePane;
	static int state=0;   //0��ʾδ��ʼ��1��ʾ��ʼ��2��ʾ��ͣ
	 JButton b1=new JButton("��ʼ");
	 JButton b2=new JButton("��ͣ");
	 JButton b3=new JButton("����");
	 JButton b4=new JButton("�ط�");
	static String label[]={"����","ѧʿ","����","����"};
	static Image img=Toolkit.getDefaultToolkit().getImage("img/money.gif");
	private JLabel money=new JLabel("�� "+Select.crystal,new ImageIcon(img),JLabel.CENTER);

	
	 Level(int grade)
	{
		 state=0;
		this.grade=grade;
		jf=new JFrame();
		jf.setTitle("̰���ߡ����ؿ�ģʽ��"+grade+"��");
		jf.getContentPane().setLayout(null);
		jf.setSize(1040,720);
		int width=Toolkit.getDefaultToolkit().getScreenSize().width;
		jf.setLocation((width-1040)/2, 0);
		jf.setResizable(false);
		time=new TimeScreen();
		jf.getContentPane().add(time);
		money.setBackground(new Color(194,211,223));
		money.setBounds(820, 170, 150, 50);
		money.setOpaque(true);
		money.setVisible(true);
		money.setFont(time.getFont());
		jf.getContentPane().add(money);
		b1.setBounds(865,275,80,30);
		b2.setBounds(865,325,80,30);
		b3.setBounds(865,375,80,30);
		b4.setBounds(865,425,80,30);
		jf.getContentPane().add(b1);
		jf.getContentPane().add(b2);
		jf.getContentPane().add(b3);
		jf.getContentPane().add(b4);
		jf.setFocusable(true);
		b1.addActionListener(new Action());
		b2.addActionListener(new Action());
		b3.addActionListener(new Action());
		b4.addActionListener(new Action());
		pane=new Play(null,true,grade);
		jf.getContentPane().add(pane);
		scorePane=new ScorePane(pane);
		jf.getContentPane().add(scorePane);
		jf.setVisible(true);
	}
	
	class Action implements ActionListener     //��ť�¼���
	{
		public void actionPerformed(ActionEvent e)
		{
			if(e.getSource()==b1)
			{
				time.TimeScreenStart();
				pane.init();
				scorePane.ScorePaneStart();
			}
			if(e.getSource()==b2)
			{
				if(Level.state==1)
				{
					Level.state=2;
					time.isStop=true;
					b2.setText("����");
				}
				else 
					if(Level.state==2)
					{
						Level.state=1;
						time.isStop=false;
						b2.setText("��ͣ");
					}

			}
			if(e.getSource()==b3)
			{
				if(state>0)
				{
					pane.isOver=true;
					regame();
					time.TimeScreenStart();
					pane.init();
					scorePane.ScorePaneStart();
				}
				
			}
			if(e.getSource()==b4)
			{
				RecordPlay r=new RecordPlay();
				if(r.restart)
					r=null;
			}
		}
	}
	
	void isSave()
	{
		pane.isOver=true;
		state=2;
		Object option[]={"�õ�","����"};
		int a=JOptionPane.showOptionDialog(jf,"�Ƿ���Ҫ�����¼","Snake say:", JOptionPane.OK_OPTION, JOptionPane.QUESTION_MESSAGE,null , option, option[0]);
		if(a==0)
		{
			SaveFile s=new SaveFile();
			if(s.restart)
				s=null;
		}
		
	}
	
	class SaveFile 
	{
		String filename=new String("save\\");
		int i;
		boolean restart=false;
	     SaveFile() 
		{
	    	 do
	    	 {
	    		 filename=filename+(String)JOptionPane.showInputDialog(jf,"������ı��ļ�������������׺��","Snake say:",JOptionPane.INFORMATION_MESSAGE)+".txt";
	    		 try{
	    			 File file=new File(filename);
	    			 if(file.exists())
	    			 {
	    				 restart=true;
	    				 JOptionPane.showMessageDialog(jf, "��������ļ��Ѵ��ڣ������¼���","Snake say:",JOptionPane.WARNING_MESSAGE);
	    			 }
	    			 else
	    			 {
	    				 restart=false;
	    				 BufferedWriter bw=new BufferedWriter(new FileWriter(filename));
	    				 String line;
	    				 pane.store("A");
	    				 for(i=0;i<pane.getStore().size();i++)
	    				 {
	    					 line=pane.getStore().elementAt(i);
	    					 bw.write(line);
	    					 bw.newLine();
	    				 }
	    				 pane.snake.saveEnd("B");
	    				 for(i=0;i<pane.snake.getSave().size();i++)
	    				 {
	    					 line=pane.snake.getSave().elementAt(i);
	    					 bw.write(line);
	    					 bw.newLine();
	    				 }
	    				 for(i=0;i<pane.items.getSave().size();i++)
	    				 {
	    					 line=pane.items.getSave().elementAt(i);
	    					 bw.write(line);
	    					 bw.newLine();
	    				 }
	    				 bw.close();
	    				 JOptionPane.showMessageDialog(jf, "��¼�ѳɹ�����");
	    			 }

	    		 }catch(Exception e)
	    		 {
	    			 restart=true;
	    			 JOptionPane.showMessageDialog(jf, "��¼����δ�ɹ�","Snake say:",JOptionPane.ERROR_MESSAGE);
	    		 }
	    	 }while(restart);
		}
	}
	
	
    void regame()  //��������
	{
		
		Level.state=0;
		time.isStop=false;
		time.setText("00:00:00");
		scorePane.setText("score:   0");
		pane.setScore(0);
		pane.isCrown=false;
		pane.repaint();
	
	}
	
    /*public static void main(String args[])
    {
    	Level l=new Level(1);
    	l.jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }*/

}
