package pass;

import javax.swing.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.Toolkit;
import java.io.*;
import java.util.*;
import java.awt.*;
import gameitem.*;
import java.awt.event.*;

public class RecordPlay extends JFrame implements ActionListener{
	
	Vector<String> control=new Vector<String>();      //��ȡPlay���store��Ϣ
	Vector<String> v_snake=new Vector<String>();      //��ȡSnake���save��Ϣ
	Vector<String> v_items=new Vector<String>();      //��ȡDrawGameItem���save��Ϣ
	static JButton button=new JButton("��ʼ");
	boolean restart=false;       //�Ƿ��ش��ļ�
	private String v;
	int grade;
	private RecordPane pane;
	
	public RecordPlay()
	{
		read();
		if(!restart)
		{
			v=control.elementAt(0);
			grade=Integer.parseInt(v);
			this.setTitle("̰���ߡ����ؿ�ģʽ��"+grade+"�ػط�");
			this.setSize(900,720);
			int width=Toolkit.getDefaultToolkit().getScreenSize().width;
			this.setLocation((width-900)/2, 0);
			this.setResizable(false);
			this.getContentPane().setLayout(null);
			button.setBounds(800, 340, 80, 40);
			button.addActionListener(this);
			this.getContentPane().add(button);
			pane=new RecordPane(null,true,grade);
			this.getContentPane().add(pane);
			this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			setVisible(true);
		}
	}
	
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource()==button)
		{
			pane.init();
		}
	}
	
	private void read()    //��ȡ�ļ���Ϣ��Vector����
	{
		String filename=new String("save\\");
		filename=filename+(String)JOptionPane.showInputDialog(this,"������¼�ļ�������������׺","Snake say:",JOptionPane.INFORMATION_MESSAGE)+".txt";
		try{
			if(!new File(filename).exists())
			{
				restart=true;
				JOptionPane.showMessageDialog(this,"��������ļ��������ڣ������°��طż�����", "Snake say:",JOptionPane.WARNING_MESSAGE);
			}
			else
			{
				BufferedReader br=new BufferedReader(new FileReader(filename));
				String line=br.readLine();
				while(line!=null&&!line.equals("A"))
				{
					control.addElement(line);
					line=br.readLine();
				}
				line=br.readLine();
				while(line!=null&&!line.equals("B"))
				{
					v_snake.addElement(line);
					line=br.readLine();
				}
				line=br.readLine();
				while(line!=null)
				{
					v_items.addElement(line);
					line=br.readLine();
				}
				br.close();
				JOptionPane.showMessageDialog(this, "�ѳɹ���ȡ��Ϣ");
			}
			
		}catch(Exception e)
		{
			restart=true;
			JOptionPane.showMessageDialog(this, "��ȡ��¼�д���");
		}
	}
	
	class RecordPane extends Play {          //�ط��е�Play��
			
			private int sum=7;
			private RecordSnake snake;
			private RecordDrawItem items;
			private String str[];
			//private JPanel panel=new JPanel();
			private boolean isStart=false;
			
			RecordPane(LayoutManager lm,boolean isDoubleBuffered,int grade)
			{
				super(lm,isDoubleBuffered,grade);
			}
			
			protected void different()     //ʵ�ֹص���Ӧ��ʼ��
			{
				switch(grade)
				{
					case 1:
					{
						this.maxmap=20;
						this.minlength=30;
						Play.speed1=30;
						Play.speed2=150;
					}break;
					case 2:
					{
						this.maxmap=20;
						this.minlength=30;
						Play.speed1=30;
						Play.speed2=120;
					}break;
					case 3:
					{
						this.maxmap=30;
						this.minlength=20;
						Play.speed1=20;
						Play.speed2=80;
					}break;
				}
			}
			
			protected void init()
			{
				snake=new RecordSnake(this);
				items=new RecordDrawItem();
				isStart=true;
				repaint();
				mThread=new Thread(this);
				mThread.start();
			}
			
			
			
			public void run()    
			{
				while(true)
				{
					Loop:while(sum<control.size())
					{
						str=control.elementAt(sum).split("-");
					    switch(Integer.parseInt(str[0]))
						{
							case 1:    //�ߵı䶯
							{
								Point p1=new Point(snake.head.getX(),snake.head.getY());
								if(str[1].equals("dead")||str[1].equals("crown")||str[1].equals("score"))    //���������
								{
									Point p2=new Point(Integer.parseInt(str[2]),Integer.parseInt(str[3]));
									while(!p1.equals(p2))
									{
										snake.move();
										repaint();
										try{
											Thread.sleep(speed2);
										}catch(InterruptedException e){
											e.printStackTrace();
										}
										p1=new Point(snake.head.getX(),snake.head.getY());
									}
									if((snake.head.getD()==2&&snake.head.getX()==90)||(snake.head.getX()==690-this.minlength&&snake.head.getD()==3)||(snake.head.getY()==43&&snake.head.getD()==0)||(snake.head.getY()==643-this.minlength&&snake.head.getD()==1))
										JOptionPane.showMessageDialog(null, "ײǽ����");	
									else
									{
										if(str[1].equals("dead"))
											JOptionPane.showMessageDialog(null, "�ո�����ը������");
										if(str[1].equals("crown"))
											JOptionPane.showMessageDialog(null, "�Ե��ʹڹ���");
										if(str[1].equals("score"))
											JOptionPane.showMessageDialog(null, "�÷ִﵽ���ط���"+passScore[grade-1]);
									}
									
								}
								else     //����䶯
								{
									Point p2=new Point(Integer.parseInt(str[1]),Integer.parseInt(str[2]));
									while(!p1.equals(p2))
									{
										snake.move();
										repaint();
										try{
											Thread.sleep(speed2);
										}catch(InterruptedException e){
											e.printStackTrace();
										}
										p1=new Point(snake.head.getX(),snake.head.getY());
									}
									String a[]=v_snake.elementAt(snake.sum).split("-");
									if(a[0].equals("reduce"))
									{
										snake.reduceSnake();
									}	
									else
									{
										snake.body.addElement(snake.createPoint(v_snake.elementAt(snake.sum)));
									}
								}
							}break;
							case 2:     //��Ʒ�ı䶯
							{
								
								items.createItem(v_items.elementAt(items.sum));
								repaint();
							}break;
							case 3:     //����ı䶯
							{
								Point p1=new Point(snake.head.getX(),snake.head.getY());
								Point p2=new Point(Integer.parseInt(str[2]),Integer.parseInt(str[3]));
								while(!p1.equals(p2))
								{
									snake.move();
									repaint();
									try{
										Thread.sleep(speed2);
									}catch(InterruptedException e){
										e.printStackTrace();
									}
									p1=new Point(snake.head.getX(),snake.head.getY());
								}
								snake.movetoturn(Integer.parseInt(str[1]));
								repaint();
							}break;
						}
						repaint();
						
						sum++;   //���б䶯��¼����
						
					}
				}
			}
			
			public void paint(Graphics g)
			{
				//panel.paint(g);
				g.clearRect(0, 0, 780, 686);
				Color c1=new Color(136,188,13);
				g.setColor(c1);
				g.fillRoundRect(60, 13,660, 660, 10, 10);
				Color c2=new Color(0,0,0);
				g.setColor(c2);
				g.fillRoundRect(78, 31, 624, 624, 6, 6);
				Color c3=new Color(255,255,255);
				g.setColor(c3);
				g.fillRect(90, 43, 600, 600);
				Color c4=new Color(71,70,71);
				g.setColor(c4);
				for (int i=0;i<this.maxmap;i++)
					for(int j=0;j<this.maxmap;j++)
						g.fillRect(i*this.minlength+91, j*this.minlength+44, minlength-2, minlength-2);
				if(isStart)
				{
						items.drawAllItem(g, this,this.minlength-2);
						snake.drawSnake(g, this,this.minlength);

				}

			}
			
			class RecordSnake extends Snake {         //�ط��е�Snake��
				
				String temp[];
				int i,x,y,d;
				int sum=0;
				
				RecordSnake(RecordPane r)
				{
					this.minlength=r.minlength;
					head=createPoint(v_snake.elementAt(0));
					body.addElement(createPoint(v_snake.elementAt(1)));
					tail[0]=createPoint(v_snake.elementAt(2));
					tail[1]=createPoint(v_snake.elementAt(3));
					
				}
				
				SnakePoint createPoint(String s)
				{
					temp=s.split("-");
					i=Integer.parseInt(temp[0]);
					x=Integer.parseInt(temp[1]);
					y=Integer.parseInt(temp[2]);
					d=Integer.parseInt(temp[3]);
					SnakePoint sp=new SnakePoint(i,x,y,d);
					this.pointNum++;
					sum++;
					return sp;
				}
			
				
				protected void reduceSnake()
				{
					SnakePoint a=this.body.elementAt(this.pointNum-4);
					this.body.removeElementAt(this.pointNum-4);
					this.eraser(tail[1]);
					this.change(this.tail[0], this.tail[1]);
					this.change(a, this.tail[0]);
					this.pointNum--;
					sum++;
					a=null;
				}
				

			}
			
			class RecordDrawItem extends DrawGameItem {      // �ط��е�DrawGameItem��

				String temp[];
				int i,x,y,k;
			    int sum=0;
				
				RecordDrawItem()
				{
					createItem(v_items.elementAt(0));
					createItem(v_items.elementAt(1));
				}
				
				void createItem(String s)
				{
				
					temp=s.split("-");
					x=Integer.parseInt(temp[0]);
					y=Integer.parseInt(temp[1]);
					k=Integer.parseInt(temp[2]);
					i=Integer.parseInt(temp[3]);
					if(items[i]!=null)
					{
						items[i].cleanItem();
						repaint();
					}
					switch(k)
					{
						case 0:
							items[i]=new Apple(x,y);
							break;
						case 1:
							items[i]=new Bread(x,y);
							break;
						case 2:
							items[i]=new Bomb(x,y);
							break;
						case 3:
						{
							items[i]=new Crown(x,y);
						}break;
					}
					sum++;
				}
				
			}

		}

}
