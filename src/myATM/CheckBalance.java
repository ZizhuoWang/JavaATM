package myATM;

import java.io.Serializable;

/**
 * @author wang
 * 活期账户
 * 一个季度10秒
 * 每一个季度加息，1.0003747892468802是1.0015开四次方
 * 实现可序列化接口是为了存进数据库
 */
public class CheckBalance implements Balance,Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Double cash;//总钱数
	int timeCount=0;//季度数
	public CheckBalance() {
		this.cash = 0.0;
	}
	@Override
	public synchronized void run() {
		while (true) {
			try {
				Thread.currentThread().sleep(10000);
				increase();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void increase() {
		cash*=1.0003747892468802;
		timeCount++;
	}
	public Double getCash() {
		return cash;
	}
	
	public void setCash(Double cash) {
		this.cash = cash;
	}
	
	public int getTimeCount() {
		return timeCount;
	}
	
	public void setTimeCount(int timeCount) {
		this.timeCount = timeCount;
	}


}
