package myATM;
/**
 *                              _ooOoo_
 *                            o8888888o
 *                              88" . "88
 *                               (| -_- |)
 *                              O\  =  /O
 *                         ____/`---'\____
 *                         .'  \\|     |//  `.
 *                      /  \\|||  :  |||//  \
 *                   /  _||||| -:- |||||-  \
 *                      |   | \\\  -  /// |   |
 *                     | \_|  ''\---/''  |   |
 *                     \  .-\__  `-`  ___/-. /
 *                   ___`. .'  /--.--\  `. . __
 *            ."" '<  `.___\_<|>_/___.'  >'"".
 *            | | :  `- \`.;`\ _ /`;.`/ - ` : | |
 *              \  \ `-.   \_ __\ /__ _/   .-` /  /
 *=====`-.____`-.___\_____/___.-`____.-'======
 *                              `=---='
 *^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
 *                     佛祖保佑        永无BUG
 */
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.util.Iterator;

import myATM.Command;
import myATM.ServerDaemon;

/**
 * @author wang
 * 非图形化的客户端，与服务器的守护进程连接，由守护进程分配给它一个服务器线程
 * 使用对象输入输出流和服务器交互
 * 图形化的客户端的使用的对象
 */
public class Client{
	User currentUser=new User();//当前用户的信息存在对象里
	Command send=new Command();//发送的信息
	Command receive=new Command();//接受的信息
	Socket client;
	ObjectOutputStream out;
	ObjectInputStream in;
	
	/**
	 * @return 当前用户的全部信息
	 */
	public String query(){
		String info="";//新建一个空的字符串
		//与服务器联系更新数据
		try {
			send = new Command();
			send.setCommand("update");
			out.writeObject(send);
			out.flush();
			while (true) {
				Object temp = in.readObject();
				if (temp instanceof User) {
					currentUser = null;
					currentUser = (User) temp;
					break;
				} 
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		//将用户的信息一个一个的加上去
		try {
			info+="当前用户："+URLDecoder.decode(currentUser.getName(), "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		DecimalFormat format = new DecimalFormat("0.00");
		info+="\n总存款"+format.format(currentUser.calculateTotalBalance())+"元";
		info+="\n活期存款："+format.format(currentUser.getCheckBalance().getCash())+"元";
		int halfCount=1,oneCount=1,fiveCount=1;
		for(Iterator iterator = currentUser.getHalfBalance().iterator();iterator.hasNext();){
			HalfBalance halfBalance = (HalfBalance) iterator.next();
			if (halfBalance.getCash()>=0.01) {
				info+="\n半年期存款" + halfCount + " 金额：" + format.format(halfBalance.getCash() )+ "元 "+
						"季度数："+ halfBalance.getTimeCount()+" 是否到期:"+halfBalance.expire;
				halfCount++;
			}
		}
		for(Iterator iterator = currentUser.getOneBalance().iterator();iterator.hasNext();){
			OneBalance oneBalance = (OneBalance) iterator.next();
			if (oneBalance.getCash()>0.0) {
				info+="\n一年期存款" + oneCount + " 金额：" + format.format(oneBalance.getCash()) + "元 "+
						"季度数："+ oneBalance.getTimeCount()+" 是否到期:"+oneBalance.expire;
				oneCount++;
			}
		}
		for(Iterator iterator = currentUser.getFiveBalance().iterator();iterator.hasNext();){
			FiveBalance fiveBalance = (FiveBalance) iterator.next();
			if (fiveBalance.getCash()>0.0) {
				info+="\n五年期存款" + fiveCount + " 金额：" + format.format(fiveBalance.getCash()) + "元 "+
						"季度数："+ fiveBalance.getTimeCount()+" 是否到期:"+fiveBalance.expire;
				fiveCount++;
			}
		}
		info+="\n贷款："+format.format(currentUser.getLoan().getCash())+"元";
		info+="\n今年查询次数："+currentUser.getLoan().query;
		info+="\n今年存款次数："+currentUser.getLoan().deposit;
		info+="\n今年取款次数："+currentUser.getLoan().withdrawl;
		
		return info;
	}
	public Client() {
		try {
			InetAddress address = InetAddress.getLocalHost();//得到localhost的地址
			client = new Socket(address, ServerDaemon.PORT);//连接本地主机的服务器守护进程
			//建立对象输入输出流
			out = new ObjectOutputStream(client.getOutputStream());
			in = new ObjectInputStream(client.getInputStream());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public boolean newUser(String name,String password) {
		send = new Command();//防止流发送缓存数据，于是新建之
		send.setCommand("newUser");
		send.setUser(name);
		send.setPassword(password);
		try {
			out.writeObject(send);
			out.flush();
			while(true){
				try {
					Thread.currentThread().sleep(100);
				} catch (Exception e) {
					e.printStackTrace();
				}
				Object temp = in.readObject();//读入数据临时存下
				if(temp instanceof Command){//如果读入的数据是Command类的实例的话
					receive =(Command) temp;//这就是接收到的数据
					if(receive.isTof()==true){//看看有没有顺利完成
						receive = new Command();//清除接收到的对象
						return true;//返回真，给图形化客户端的按钮点击事件
					}else{
						receive = new Command();
						return false;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean login(String name,String password) {
		currentUser.setName(name);
		currentUser.setPassword(password);
		try {
			out.writeObject(currentUser);
			out.flush();//这次发送一个User类对象
			while(true){
				try {
					Thread.currentThread().sleep(100);
				} catch (Exception e) {
					e.printStackTrace();
				}
				Object temp = in.readObject();
				if(temp instanceof Command){
					receive =(Command) temp;
					if(receive.isTof()==true){
						receive = new Command();
						query();//更新信息
						return true;
					}else{
						receive = new Command();
						currentUser = new User();
						return false;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	
	/**
	 * @param amount
	 * @return 是否成功存款
	 * 其实这些函数都是一个套路，只是发发消息，没什么好写的。。。所以。。。
	 */
	public boolean depoCheck(Double amount) {
		send = new Command();
		send.setCommand("depoCheck");
		send.setAmount(amount);
		try {
			out.writeObject(send);
			out.flush();
			while(true){
				try {
					Thread.currentThread().sleep(100);
				} catch (Exception e) {
					e.printStackTrace();
				}
				Object temp =in.readObject();
				if(temp instanceof Command){
					receive =(Command) temp;
					if(receive.isTof()==true){
						System.out.println("Check Account Deposit");
						receive = new Command();
						return true;
					}else{
						System.out.println("Failed");
						receive = new Command();
						return false;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	
	public boolean depoHalf(Double amount) {
		send = new Command();
		send.setCommand("depoHalf");
		send.setAmount(amount);
		try {
			out.writeObject(send);
			out.flush();
			while(true){
				try {
					Thread.currentThread().sleep(100);
				} catch (Exception e) {
					e.printStackTrace();
				}
				Object temp =in.readObject();
				if(temp instanceof Command){
					receive =(Command) temp;
					if(receive.isTof()==true){
						System.out.println("Successful");
						receive = new Command();
						return true;
					}else{
						System.out.println("Failed");
						receive = new Command();
						return false;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	
	public boolean depoOne(Double amount) {
		send = new Command();
		send.setCommand("depoOne");
		send.setAmount(amount);
		try {
			out.writeObject(send);
			out.flush();
			while(true){
				try {
					Thread.currentThread().sleep(100);
				} catch (Exception e) {
					e.printStackTrace();
				}
				Object temp = in.readObject();
				if(temp instanceof Command){
					receive =(Command) temp;
					if(receive.isTof()==true){
						System.out.println("Successful");
						receive = new Command();
						return true;
					}else{
						System.out.println("Failed");
						receive = new Command();
						return false;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	
	public boolean depoFive(Double amount) {
		send = new Command();
		send.setCommand("depoFive");
		send.setAmount(amount);
		try {
			out.writeObject(send);
			out.flush();
			while(true){
				try {
					Thread.currentThread().sleep(100);
				} catch (Exception e) {
					e.printStackTrace();
				}
				Object temp =  in.readObject();
				if(temp instanceof Command){
					receive =(Command) temp;
					if(receive.isTof()==true){
						System.out.println("Successful");
						receive = new Command();
						return true;
					}else{
						System.out.println("Failed");
						receive = new Command();
						return false;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	
	public boolean withCheck(Double amount) {
		send = new Command();
		send.setCommand("withCheck");
		send.setAmount(amount);
		try {
			out.writeObject(send);
			out.flush();
			while(true){
				try {
					Thread.currentThread().sleep(100);
				} catch (Exception e) {
					e.printStackTrace();
				}
				Object temp =  in.readObject();
				if(temp instanceof Command){
					receive =(Command) temp;
					if(receive.isTof()==true){
						System.out.println("Successful");
						receive = new Command();
						return true;
					}else{
						System.out.println("Failed");
						receive = new Command();
						return false;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	
	public boolean withHalf(int which) {
		send = new Command();
		send.setCommand("withHalf");
		send.setWhich(which);
		try {
			out.writeObject(send);
			out.flush();
			while(true){
				try {
					Thread.currentThread().sleep(100);
				} catch (Exception e) {
					e.printStackTrace();
				}
				Object temp =  in.readObject();
				if(temp instanceof Command){
					receive =(Command) temp;
					if(receive.isTof()==true){
						System.out.println("Successful");
						receive = new Command();
						return true;
					}else{
						System.out.println("Failed");
						receive = new Command();
						return false;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	
	public boolean withOne(int which) {
		send = new Command();
		send.setCommand("withOne");
		send.setWhich(which);
		try {
			out.writeObject(send);
			out.flush();
			while(true){
				try {
					Thread.currentThread().sleep(100);
				} catch (Exception e) {
					e.printStackTrace();
				}
				Object temp =  in.readObject();
				if(temp instanceof Command){
					receive =(Command) temp;
					if(receive.isTof()==true){
						System.out.println("Successful");
						receive = new Command();
						return true;
					}else{
						System.out.println("Failed");
						receive = new Command();
						return false;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	
	public boolean withFive(int which) {
		send = new Command();
		send.setCommand("withFive");
		send.setWhich(which);
		try {
			out.writeObject(send);
			out.flush();
			while(true){
				try {
					Thread.currentThread().sleep(100);
				} catch (Exception e) {
					e.printStackTrace();
				}
				Object temp =  in.readObject();
				if(temp instanceof Command){
					receive =(Command) temp;
					if(receive.isTof()==true){
						System.out.println("Successful");
						receive = new Command();
						return true;
					}else{
						System.out.println("Failed");
						receive = new Command();
						return false;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	
	public boolean getLoan(Double amount) {
		send = new Command();
		send.setCommand("getLoan");
		send.setAmount(amount);
		try {
			out.writeObject(send);
			out.flush();
			while(true){
				try {
					Thread.currentThread().sleep(100);
				} catch (Exception e) {
					e.printStackTrace();
				}
				Object temp =  in.readObject();
				if(temp instanceof Command){
					receive =(Command) temp;
					if(receive.isTof()==true){
						System.out.println("Successful");
						receive = new Command();
						return true;
					}else{
						System.out.println("Failed");
						receive = new Command();
						return false;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	
	public boolean payLoan() {
		send = new Command();
		send.setCommand("payLoan");
		try {
			out.writeObject(send);
			out.flush();
			while(true){
				try {
					Thread.currentThread().sleep(100);
				} catch (Exception e) {
					e.printStackTrace();
				}
				Object temp =in.readObject();
				if(temp instanceof Command){
					receive =(Command) temp;
					if(receive.isTof()==true){
						System.out.println("Successful");
						receive = new Command();
						return true;
					}else{
						System.out.println("Failed");
						receive = new Command();
						return false;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	
	/**
	 * @return
	 * 没用上
	 */
	public boolean freeze() {
		send = new Command();
		send.setCommand("freeze");
		try {
			out.writeObject(send);
			out.flush();
			while(true){
				try {
					Thread.currentThread().sleep(100);
				} catch (Exception e) {
					e.printStackTrace();
				}
				Object temp = in.readObject();
				if(temp instanceof Command){
					receive =(Command) temp;
					if(receive.isTof()==true){
						System.out.println("Successful");
						receive = new Command();
						return true;
					}else{
						System.out.println("Failed");
						receive = new Command();
						return false;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

}
