package fight;

import java.net.*;
import java.util.*;
import java.io.*;

public class AllIP {
	
	private ArrayList<String> ping=new ArrayList<String>(260);    //ping后的结果集
	private int threadCount=0;
	
	ArrayList<String> getPing()   //返回ping后结果集
	{
		return ping;
	}
	
	void Ping(String ip)
	{
		try{
			while(threadCount>30)
				Thread.sleep(50);
			threadCount++;
			PingIP thread=new PingIP(ip);
			thread.start();
		}catch(Exception e){
			System.out.println(e.toString());
		}
		
	}
	
	void PingAll()
	{
		try{
			InetAddress host = InetAddress.getLocalHost();
			String hostAddress = host.getHostAddress();
			int k=0; 
			k=hostAddress.lastIndexOf("."); 
			String beforesub = hostAddress.substring(0,k+1); 
			for(int i=1;i <=255;i++){  //对所有局域网Ip 
				String ip=beforesub+Integer.toString(i);
				if(ip.equals(hostAddress))
					continue;
				Ping(ip); 
			}
			while(threadCount>0)
				Thread.sleep(50);
		}catch(Exception e){
			System.out.println(e.toString());
		}
	}
	
	class PingIP extends Thread
	{
		String ip=null;
		PingIP(String ip)
		{
			this.ip=ip;
		}
		public void run()
		{
			try{
				Process startping=Runtime.getRuntime().exec("ping "+ip+" -w 280 -n 1");
				InputStream in=startping.getInputStream();
				BufferedReader read=new BufferedReader(new InputStreamReader(in));
				String line=read.readLine();
				while(line!=null)
				{
					if(line!=null&&!line.equals(""))
					{
						if(line.substring(0, 2).equals("来自")||(line.length()>10&&line.substring(0, 10).equals("Reply from")))
							ping.add(ip);
					}
					line=read.readLine();
				}
				read.close();

				
			}catch(IOException e){
				System.out.println(e.toString());
			}
		}
	}
		
	

}
