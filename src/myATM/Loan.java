package myATM;

import java.io.Serializable;
import java.text.DecimalFormat;

/**
 * @author wang
 *	贷款类
 */
public class Loan implements Balance, Serializable {

	private static final long serialVersionUID = 1L;
	Double cash=0.0;//贷了多少款
	Double tenPercent;//贷款值的十分之一
	int timeCount=0;//年数
	Double[] loans = new Double[10];//每年该交多少钱
	User user;//对那个用户进行操作
	int deposit=0,withdrawl=0,query=0;//今年账户的活跃度
	//付清今年的款
	public void pay(){
		loans[timeCount] = 0.0;
		cash-=tenPercent;
		user.setFreezed(false);
	}
	//得到当月还款额
	public Double getloan(){
		return loans[timeCount];
	}
	//设定对那个用户进行操作
	public void setUser(User temp){
		this.user = temp;
	}
	//计算每年应缴纳的款额
	public void calLoan(){
		Double original = cash;
		tenPercent = cash*0.1;
		for(int i=0;i<10;i++){
			loans[i] = cash*0.15;
			cash-=tenPercent;
		}
		cash=original;
	}
	//判断用户是否有贷款能力
	public boolean ableToLoan(){
		return (deposit>0)&&(withdrawl>0)&&(query>0);
	}
	public void deposit(){
		deposit++;
	}
	public void withdrawl(){
		withdrawl++;
	}
	public void query(){
		query++;
	}
	public Double getCash() {
		return cash;
	}

	public void setCash(Double cash) {
		DecimalFormat format = new DecimalFormat("0.00");
		this.cash = Double.parseDouble(format.format(cash));
		calLoan();
	}

	public int getTimeCount() {
		return timeCount;
	}

	public void setTimeCount(int timeCount) {
		this.timeCount = timeCount;
	}

	public Loan(User u) {
		user=u;
	}
	@Override
	public void run() {
		if (cash<0.0) {
			cash=0.0;
		}
		//设置新的线程用于清空用户上一年的操作数
		Thread clear = new Thread(){
			public void run(){
				try {
					Thread.currentThread().sleep(40000);
					deposit = 0;
					withdrawl = 0;
					query = 0;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		clear.start();//启动这个线程
		while (timeCount<10&&cash>0.0) {
			try {
				Thread.currentThread().sleep(40000);
				increase();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (timeCount>=10) {
			cash=0.0;
		}
		
	}

	/* (non-Javadoc)
	 * @see myATM.Balance#increase()
	 */
	@Override
	public void increase() {
		if (user.calculateTotalBalance()>=loans[timeCount]&&loans[timeCount]!=0.0) {
			user.getCheckBalance().setCash(user.getCheckBalance().getCash() - loans[timeCount]);
			loans[timeCount] = 0.0;
			cash-=tenPercent;
		}
		if (user.calculateTotalBalance()<loans[timeCount]) {
			user.setFreezed(true);
		}
		timeCount++;
	}


}
