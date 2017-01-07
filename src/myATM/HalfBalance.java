package myATM;

import java.io.Serializable;

/**
 * @author wang
 * 半年期账户
 * 一个季度10秒
 * 每一个季度加息，1.0049629315732038是1.02开四次方
 * 实现可序列化接口是为了存进数据库
 * capital是本金，提前取出会按照活期利率计算
 */
public class HalfBalance implements Balance,Serializable {

	private static final long serialVersionUID = 1L;
	Double cash=0.0;//现在的金额
	int timeCount=0;//季度数
	public Double capital=0.0;//本金
	public boolean expire = false;//是否到期
	public HalfBalance(Double cash) {
		this.cash = cash;
		capital = cash;
	}
	//未到期提前取出，按照活期利率计算
	public Double getUnexpired(){
		return capital*Math.pow(1.0003747892468802, timeCount);
	}
	@Override
	public synchronized void run() {
		while (true) {
			if(timeCount>=2){
				expire=true;
				break;
			}
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
		cash*=1.0049629315732038;
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
