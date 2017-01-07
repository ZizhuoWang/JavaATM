package myATM;

import java.io.Serializable;

/**
 * @author wang
 * 客户端和服务器之间信息传递的载体
 */
public class Command implements Serializable {

	private static final long serialVersionUID = 1L;
	private String command;//命令是什么
	private Double amount;//存款金额是多少
	private int which;//对哪个款项进行操作
	private boolean tof = false;//操作是否顺利完成
	private String user="";//新建用户的时候用
	private String password;//新用户的密码
	public String getCommand() {
		return command;
	}
	public void setCommand(String command) {
		this.command = command;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public boolean isTof() {
		return tof;
	}
	public void setTof(boolean tof) {
		this.tof = tof;
	}
	public int getWhich() {
		return which;
	}
	public void setWhich(int which) {
		this.which = which;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}
