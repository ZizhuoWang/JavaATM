package myATM;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author wang
 *	用户类
 */
public class User implements Serializable{
	private static final long serialVersionUID = 1L;
	private CheckBalance checkBalance;//活期账户只有一个
	private ArrayList<HalfBalance> halfBalances = new ArrayList<HalfBalance>();
	private ArrayList<OneBalance> oneBalances = new ArrayList<OneBalance>();
	private ArrayList<FiveBalance> fiveBalances = new ArrayList<FiveBalance>();
	private Double totalBalance;
	private Loan loan = new Loan(this);
	private boolean isFreezed=false;
	private String name="";
	private String password="";
	public User() {
		this.checkBalance = new CheckBalance();
	}
	/**
	 * @return 存款总额
	 */
	public Double calculateTotalBalance(){
		Double result=0.0;
		result+=checkBalance.getCash();
		for (Iterator iterator = halfBalances.iterator(); iterator.hasNext();){
			HalfBalance halfBalance = (HalfBalance) iterator.next();
			result+=halfBalance.getCash();
		}
		for (Iterator iterator = oneBalances.iterator(); iterator.hasNext();){
			OneBalance oneBalance = (OneBalance) iterator.next();
			result+=oneBalance.getCash();
		}
		for (Iterator iterator = fiveBalances.iterator(); iterator.hasNext();){
			FiveBalance fiveBalance = (FiveBalance) iterator.next();
			result+=fiveBalance.getCash();
		}
		return result;
	}
	/**
	 * 貌似没什么用了。。。
	 */
	public void runThemAll(){
		try {
			Thread thread = new Thread(checkBalance);
			thread.start();
			for (Iterator iterator = halfBalances.iterator(); iterator.hasNext();) {
				HalfBalance halfBalance = (HalfBalance) iterator.next();
				Thread thread1 = new Thread(halfBalance);
				thread1.start();
			}
			for (Iterator iterator = oneBalances.iterator(); iterator.hasNext();) {
				OneBalance oneBalance = (OneBalance) iterator.next();
				Thread thread2 = new Thread(oneBalance);
				thread2.start();
			}
			for (Iterator iterator = fiveBalances.iterator(); iterator.hasNext();) {
				FiveBalance fiveBalance = (FiveBalance) iterator.next();
				Thread thread3 = new Thread(fiveBalance);
				thread3.start();
			}
		} catch (Exception e) {
//			e.printStackTrace();
		}
	}
	public CheckBalance getCheckBalance() {
		return checkBalance;
	}
	public void setCheckBalance(CheckBalance checkBalance) {
		this.checkBalance = checkBalance;
	}
	public ArrayList<HalfBalance> getHalfBalance() {
		return halfBalances;
	}
	public void setHalfBalance(ArrayList<HalfBalance> halfBalances) {
		this.halfBalances = halfBalances;
	}
	public ArrayList<OneBalance> getOneBalance() {
		return oneBalances;
	}
	public void setOneBalance(ArrayList<OneBalance> oneBalances) {
		this.oneBalances = oneBalances;
	}
	public ArrayList<FiveBalance> getFiveBalance() {
		return fiveBalances;
	}
	public void setFiveBalance(ArrayList<FiveBalance> fiveBalances) {
		this.fiveBalances = fiveBalances;
	}
	public Loan getLoan() {
		return loan;
	}
	public void setLoan(Loan loan) {
		
		this.loan = loan;
	}
	public boolean isFreezed() {
		return isFreezed;
	}
	public void setFreezed(boolean isFreezed) {
		this.isFreezed = isFreezed;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Double getTotalBalance() {
		this.totalBalance = calculateTotalBalance();
		return totalBalance;
	}
	public void setTotalBalance(Double totalBalance) {
		this.totalBalance = totalBalance;
	}
}
