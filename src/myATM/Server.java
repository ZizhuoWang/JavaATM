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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;

import myATM.ServerDaemon;

/**
 * @author wang
 *	服务器线程，由服务器守护进程动态创建
 */
public class Server extends Thread {
	Command send=new Command(),receive = new Command();//收发的命令
	User currentUser = new User();//当前用户
	Socket socket;
	ObjectOutputStream out;
	ObjectInputStream in;
	ArrayList<String> usernames = new ArrayList<String>();//因为HashMap在添加数据时会乱序
	ArrayList<User> users = new ArrayList<User>();//于是建了两个下标对应的ArrayList

	/**
	 * @return 与数据库的连接
	 */
	public static Connection getConnection(){
		try {
			Class.forName("com.mysql.jdbc.Driver");
			String dbURL = "jdbc:mysql://localhost:3306?characterEncoding=utf-8"
					+ "&useSSL=false&user=root&password=***";
			Connection connection = DriverManager.getConnection(dbURL);
			return connection;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	@Override
	public void run() {
		loadDB();//读出数据库中的用户信息
		//让线程们跑起来，来增加利息
		for(Iterator iterator = users.iterator();iterator.hasNext();){
			runThemAll((User)iterator.next());
		}
		//新建一个线程随时存档
		Thread update = new Thread(){
			public void run(){
				while(true){
					try {
						Thread.currentThread().sleep(1000);
					} catch (Exception e) {
						e.printStackTrace();
					}
					saveDB();
				}
			}
		};
		update.start();
		while(true){
			try {
				Thread.sleep(100);
				Object temp=null;Object temp2;
				if ((temp2=in.readObject())!=null) {//非空就把temp2赋给temp
					temp = temp2;
				}
				if(temp instanceof User){//如果传来的对象是User类的实例
					boolean succeeded=login((User) temp);//登录
					send.setTof(succeeded);//设置登录是否成功
					out.writeObject(send);
					out.flush();//告诉非图形化客户端
					send = new Command();
				}else if (temp instanceof Command) {//如果传来的对象是Command类的实例
					receive = (Command) temp;//接收到的命令就是temp
					String alter = receive.getCommand();//看看是啥命令
					if (alter.hashCode()=="newUser".hashCode()) {
						currentUser.setName(receive.getUser());
						currentUser.setPassword(receive.getPassword());
						boolean succeeded=newUser(currentUser);//注册新用户
						send.setTof(succeeded);
						out.writeObject(send);
						out.flush();//告诉非图形化客户端
						send = new Command();//新建以防发送已有缓存，后面同理，就不一一写了
					}
					if (alter.hashCode()=="depoCheck".hashCode()) {
						if (!users.get(usernames.indexOf(currentUser.getName())).isFreezed()) {
							boolean succeeded = depoCheck(receive.getAmount());
							send.setTof(succeeded);
							out.writeObject(send);
							out.flush();
							send = new Command();
						}else {
							send.setTof(false);
							out.writeObject(send);
							out.flush();
							send = new Command();
						}
					}
					if (alter.hashCode()=="depoHalf".hashCode()) {
						if (!users.get(usernames.indexOf(currentUser.getName())).isFreezed()) {
							boolean succeeded = depoHalf(receive.getAmount());
							send.setTof(succeeded);
							out.writeObject(send);
							out.flush();
							send = new Command();
						}else {
							send.setTof(false);
							out.writeObject(send);
							out.flush();
							send = new Command();
						}
					}
					if (alter.hashCode()=="depoOne".hashCode()) {
						if (!users.get(usernames.indexOf(currentUser.getName())).isFreezed()) {
							boolean succeeded = depoOne(receive.getAmount());
							send.setTof(succeeded);
							out.writeObject(send);
							out.flush();
							send = new Command();
						}else {
							send.setTof(false);
							out.writeObject(send);
							out.flush();
							send = new Command();
						}
					}
					if (alter.hashCode()=="depoFive".hashCode()) {
						if (!users.get(usernames.indexOf(currentUser.getName())).isFreezed()) {
							boolean succeeded = depoFive(receive.getAmount());
							send.setTof(succeeded);
							out.writeObject(send);
							out.flush();
							send = new Command();
						}else {
							send.setTof(false);
							out.writeObject(send);
							out.flush();
							send = new Command();
						}
					}
					if (alter.hashCode()=="withCheck".hashCode()) {
						if (!users.get(usernames.indexOf(currentUser.getName())).isFreezed()) {
							boolean succeeded = withCheck(receive.getAmount());
							send.setTof(succeeded);
							out.writeObject(send);
							out.flush();
							send = new Command();
						}else {
							send.setTof(false);
							out.writeObject(send);
							out.flush();
							send = new Command();
						}
					}
					if (alter.hashCode()=="withHalf".hashCode()) {
						if (!users.get(usernames.indexOf(currentUser.getName())).isFreezed()) {
							boolean succeeded = withHalf(receive.getWhich());
							send.setTof(succeeded);
							out.writeObject(send);
							out.flush();
							send = new Command();
						}else {
							send.setTof(false);
							out.writeObject(send);
							out.flush();
							send = new Command();
						}
					}
					if (alter.hashCode()=="withOne".hashCode()) {
						if (!users.get(usernames.indexOf(currentUser.getName())).isFreezed()) {
							boolean succeeded = withOne(receive.getWhich());
							send.setTof(succeeded);
							out.writeObject(send);
							out.flush();
							send = new Command();
						}else {
							send.setTof(false);
							out.writeObject(send);
							out.flush();
							send = new Command();
						}
					}
					if (alter.hashCode()=="withFive".hashCode()) {
						if (!users.get(usernames.indexOf(currentUser.getName())).isFreezed()) {
							boolean succeeded = withFive(receive.getWhich());
							send.setTof(succeeded);
							out.writeObject(send);
							out.flush();
							send = new Command();
						}else {
							send.setTof(false);
							out.writeObject(send);
							out.flush();
							send = new Command();
						}
					}
					if (alter.hashCode()=="getLoan".hashCode()) {
						boolean succeeded=getLoan(receive.getAmount());
						send.setTof(succeeded);
						out.writeObject(send);
						out.flush();
						send = new Command();
					}
					if (alter.hashCode()=="payLoan".hashCode()) {
						boolean succeeded=payLoan();
						send.setTof(succeeded);
						out.writeObject(send);
						out.flush();
						send = new Command();
					}
					if (alter.hashCode()=="freeze".hashCode()) {
						boolean succeeded=freeze();
						send.setTof(succeeded);
						out.writeObject(send);
						out.flush();
						send = new Command();
					}
					if (alter.hashCode()=="update".hashCode()) {
						update();
					}
				}
			} catch (Exception e) {//由于之前有非空检验，所以排除了EOF异常
				//一般只有当输入输出流断了(也就是客户端关闭)之后才会跳到这里
				ServerDaemon.CURRENT_THREADS--;//在线人数减一
				break;//打破while循环
			}
		}
	}
	public void update(){
		saveDB();//先保存
		load();//再从数据库中读取当前用户
		if (load()) {//如果读取成功
			try {
				out.writeObject(currentUser);//只有这样发出的才不是缓存
				out.flush();//users.get(usernames.indexOf(currentUser.getName()))就是在users这个ArrayList中的当前用户
				users.get(usernames.indexOf(currentUser.getName())).getLoan().query();//当前查询次数加一次
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}
	}
	public Server(Socket s) {
		socket = s;
		ServerDaemon.CURRENT_THREADS++;//在线人数加一
		try {
			out = new ObjectOutputStream(socket.getOutputStream());//与客户端建立输入输出流
			in= new ObjectInputStream(socket.getInputStream());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 新建数据库
	 * 无则改之有则加勉2333
	 * 异常？请你沉默...
	 */
	public static void createDB(){
		try {
			Connection connection = getConnection();
			Statement statement = connection.createStatement();
			statement.execute("create database atm;");
			statement.execute("CREATE TABLE `atm`.`user` "
					+ "( `name` VARCHAR(200) NOT NULL , `object` LONGBLOB NOT NULL , "
					+ "UNIQUE (`name`))"
					+ " ENGINE = InnoDB;");
			connection.close();
		} catch (Exception e) {
		}
	}
	/**
	 * @param user
	 * 都起来干活咯！
	 */
	public void runThemAll(User user){
		Thread check = new Thread(user.getCheckBalance());
		check.start();
		for(Iterator iterator = user.getHalfBalance().iterator();iterator.hasNext();){
			Thread half = new Thread((Runnable)iterator.next());
			half.start();
		}
		for(Iterator iterator = user.getOneBalance().iterator();iterator.hasNext();){
			Thread one = new Thread((Runnable)iterator.next());
			one.start();
		}
		for(Iterator iterator = user.getFiveBalance().iterator();iterator.hasNext();){
			Thread five = new Thread((Runnable)iterator.next());
			five.start();
		}
		Thread loan = new Thread(user.getLoan());
		user.getLoan().setUser(user);
		loan.start();
	}
	/**
	 * 把用户名和对象都从数据库中拿出来
	 */
	public void loadDB(){
		try {
			Connection connection = getConnection();
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT * FROM atm.user");//全都读出来
			while(resultSet.next()){
				String temp = resultSet.getString(1);
				usernames.add(temp);//用户名
				byte[] data = resultSet.getBytes(2);
				ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
				ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
				User tempUser = (User) objectInputStream.readObject();
				users.add(tempUser);//对象
			}
			connection.close();//断开与数据库的连接
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * @return 是否保存成功
	 * 很奇怪，我只是用了sql语句竟然也可以...
	 */
	public boolean saveDB() {
		try {
			Connection connection = getConnection();
			PreparedStatement preparedStatement = 
					connection.prepareStatement("UPDATE atm.user SET object=? WHERE name=?");
			Iterator iteratorUser = users.iterator();
			for(Iterator iterator = usernames.iterator();iterator.hasNext();){//添加批处理
				String tempName = (String)iterator.next();
				User tempUser = (User) iteratorUser.next();
				preparedStatement.setObject(1, tempUser);
				preparedStatement.setString(2, tempName);
				preparedStatement.addBatch();
				preparedStatement.clearParameters();
			}
			preparedStatement.executeBatch();//执行
			preparedStatement.clearBatch();//清空
			connection.close();//断连
			return true;
		} catch (Exception e) {//有异常就不成功
			e.printStackTrace();
			return false;
		}
	}
	public boolean newUser(User user){
		try {
			Connection connection = getConnection();
			String query = "INSERT INTO atm.user (name,object) VALUES (?,?)";
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, user.getName());
			//新建字节输出流，之后让对象输出流通过字节流
			ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(arrayOutputStream);
			objectOutputStream.writeObject(user);
			objectOutputStream.flush();
			byte[] data=arrayOutputStream.toByteArray();//将user对象转化成字节流
			preparedStatement.setObject(2, data);
			preparedStatement.execute();
			preparedStatement.clearParameters();
			connection.close();
			usernames.add(user.getName());
			users.add(user);
			return saveDB();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	/**
	 * @param user
	 * @return 登录是否成功
	 * 取出对应的对象，比较一下密码
	 * 一样就对，不一样就不对
	 */
	public boolean login(User user) {
		try{
			Connection connection = getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement("SELECT * "
					+ "FROM atm.user WHERE name=?");
			preparedStatement.setString(1, user.getName());
			ResultSet resultSet = preparedStatement.executeQuery();
			resultSet.next();
			byte[] data = resultSet.getBytes(2);
			connection.close();
			if (data.length!=0) {
				ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
				ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
				User tempUser = (User) objectInputStream.readObject();
				if (user.getPassword().hashCode()==tempUser.getPassword().hashCode()) {
					currentUser=tempUser;
					return true;
				}else{
					return false;
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean load(){
		try{
			Connection connection = getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement("SELECT * "
					+ "FROM atm.user WHERE name=?");
			preparedStatement.setString(1, currentUser.getName());
			ResultSet resultSet = preparedStatement.executeQuery();
			resultSet.next();
			byte[] data = resultSet.getBytes(2);
			connection.close();
			if (data.length!=0) {
				ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
				ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
				User tempUser = (User) objectInputStream.readObject();
				currentUser=tempUser;//指向一块新的地址，不会一味输出缓存
				return true;
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	public boolean depoCheck(Double amount) {
		CheckBalance checkBalance = new CheckBalance();//新建一个活期存款
		checkBalance.setCash(//设置款额
				users.get(usernames.indexOf(currentUser.getName())).getCheckBalance().getCash()+amount);
		checkBalance.setTimeCount(//设置季度数
				users.get(usernames.indexOf(currentUser.getName())).getCheckBalance().getTimeCount());
		Thread thread = new Thread(checkBalance);
		thread.start();//跑起来！
		users.get(usernames.indexOf(currentUser.getName())).setCheckBalance(checkBalance);//设置成新建的
		users.get(usernames.indexOf(currentUser.getName())).getLoan().deposit();//存款次数加一
		return saveDB();
	}


	public boolean depoHalf(Double amount) {
		HalfBalance halfBalance = new HalfBalance(amount);
		Thread thread =new Thread(halfBalance);
		thread.start();
		users.get(usernames.indexOf(currentUser.getName())).getHalfBalance().add(halfBalance);
		users.get(usernames.indexOf(currentUser.getName())).getLoan().deposit();//存款次数加一
		return saveDB();
	}


	public boolean depoOne(Double amount) {
		OneBalance oneBalance = new OneBalance(amount);
		Thread thread = new Thread(oneBalance);
		thread.start();
		users.get(usernames.indexOf(currentUser.getName())).getOneBalance().add(oneBalance);
		users.get(usernames.indexOf(currentUser.getName())).getLoan().deposit();//存款次数加一
		return saveDB();
	}


	public boolean depoFive(Double amount) {
		FiveBalance fiveBalance = new FiveBalance(amount);
		Thread thread = new Thread(fiveBalance);
		thread.start();
		users.get(usernames.indexOf(currentUser.getName())).getFiveBalance().add(fiveBalance);
		users.get(usernames.indexOf(currentUser.getName())).getLoan().deposit();//存款次数加一
		return saveDB();
	}


	public boolean withCheck(Double amount) {
		CheckBalance checkBalance = new CheckBalance();
		if (users.get(usernames.indexOf(currentUser.getName())).getCheckBalance().getCash()-amount>=0.0) {
			checkBalance.setCash(
					users.get(usernames.indexOf(currentUser.getName())).getCheckBalance().getCash()-amount);
		}else{
			return false;
		}
		Thread thread = new Thread(checkBalance);
		thread.start();
		users.get(usernames.indexOf(currentUser.getName())).setCheckBalance(checkBalance);
		users.get(usernames.indexOf(currentUser.getName())).getLoan().withdrawl();//存款次数加一
		return saveDB();
	}


	/**
	 * @param which
	 * @return 是否取款成功
	 * 当存款编号存在并且存款已经到期就按照定期存款各自的利率计算并且把钱取到活期账户中
	 * 当存款编号存在并且存款没有到期就按照活期存款的利率计算并且把钱取到活期账户中
	 * 当存款编号不存在时，返回取出失败
	 */
	public boolean withHalf(int which) {
		if (users.get(usernames.indexOf(currentUser.getName())).getHalfBalance().size()>=which+1) {
			if (users.get(usernames.indexOf(currentUser.getName())).getHalfBalance().get(which).expire) {
				users.get(usernames.indexOf(currentUser.getName())).getCheckBalance()
						.setCash(users.get(usernames.indexOf(currentUser.getName())).getCheckBalance().getCash() + 
								users.get(usernames.indexOf(currentUser.getName())).getHalfBalance().get(which).getCash());
				users.get(usernames.indexOf(currentUser.getName())).getHalfBalance().remove(which);
			}else {
				users.get(usernames.indexOf(currentUser.getName())).getCheckBalance()
				.setCash(users.get(usernames.indexOf(currentUser.getName())).getCheckBalance().getCash() + 
						users.get(usernames.indexOf(currentUser.getName())).getHalfBalance().get(which).getUnexpired());
				users.get(usernames.indexOf(currentUser.getName())).getHalfBalance().remove(which);
			}
			users.get(usernames.indexOf(currentUser.getName())).getLoan().withdrawl();
			return saveDB();
		}else{
			return false;
		}
	}

	/**
	 * @param which
	 * @return 是否取款成功
	 * 当存款编号存在并且存款已经到期就按照定期存款各自的利率计算并且把钱取到活期账户中
	 * 当存款编号存在并且存款没有到期就按照活期存款的利率计算并且把钱取到活期账户中
	 * 当存款编号不存在时，返回取出失败
	 */
	public boolean withOne(int which) {
		if (users.get(usernames.indexOf(currentUser.getName())).getOneBalance().size()>=which+1) {
			if (users.get(usernames.indexOf(currentUser.getName())).getOneBalance().get(which).expire) {
				users.get(usernames.indexOf(currentUser.getName())).getCheckBalance()
						.setCash(users.get(usernames.indexOf(currentUser.getName())).getCheckBalance().getCash() + 
								users.get(usernames.indexOf(currentUser.getName())).getOneBalance().get(which).getCash());
				users.get(usernames.indexOf(currentUser.getName())).getOneBalance().remove(which);
			}else {
				users.get(usernames.indexOf(currentUser.getName())).getCheckBalance()
				.setCash(users.get(usernames.indexOf(currentUser.getName())).getCheckBalance().getCash() + 
						users.get(usernames.indexOf(currentUser.getName())).getOneBalance().get(which).getUnexpired());
				users.get(usernames.indexOf(currentUser.getName())).getOneBalance().remove(which);
			}
			users.get(usernames.indexOf(currentUser.getName())).getLoan().withdrawl();
			return saveDB();
		}else {
			return false;
		}
	}

	/**
	 * @param which
	 * @return 是否取款成功
	 * 当存款编号存在并且存款已经到期就按照定期存款各自的利率计算并且把钱取到活期账户中
	 * 当存款编号存在并且存款没有到期就按照活期存款的利率计算并且把钱取到活期账户中
	 * 当存款编号不存在时，返回取出失败
	 */
	public boolean withFive(int which) {
		if (users.get(usernames.indexOf(currentUser.getName())).getFiveBalance().size()>=which+1) {
			if (users.get(usernames.indexOf(currentUser.getName())).getFiveBalance().get(which).expire) {
				users.get(usernames.indexOf(currentUser.getName())).getCheckBalance()
						.setCash(users.get(usernames.indexOf(currentUser.getName())).getCheckBalance().getCash() + 
								users.get(usernames.indexOf(currentUser.getName())).getFiveBalance().get(which).getCash());
				users.get(usernames.indexOf(currentUser.getName())).getFiveBalance().remove(which);
			}else {
				users.get(usernames.indexOf(currentUser.getName())).getCheckBalance()
				.setCash(users.get(usernames.indexOf(currentUser.getName())).getCheckBalance().getCash() + 
						users.get(usernames.indexOf(currentUser.getName())).getFiveBalance().get(which).getUnexpired());
				users.get(usernames.indexOf(currentUser.getName())).getFiveBalance().remove(which);
			}
			users.get(usernames.indexOf(currentUser.getName())).getLoan().withdrawl();
			return saveDB();
		}else {
			return false;
		}
	}


	/**
	 * @param amount
	 * @return 是否贷款成功
	 * 如果存款大于贷款额，当前没有贷款，并且满足一年内存款、取款、查询各至少一次，就可以贷款
	 */
	public boolean getLoan(Double amount) {
		if(users.get(usernames.indexOf(currentUser.getName())).calculateTotalBalance()>=amount
				&&users.get(usernames.indexOf(currentUser.getName())).getLoan().getCash().equals(0.0)
				&&users.get(usernames.indexOf(currentUser.getName())).getLoan().ableToLoan()){
			Loan l = new Loan(users.get(usernames.indexOf(currentUser.getName())));
			l.setCash(amount);
			users.get(usernames.indexOf(currentUser.getName())).setLoan(l);
			users.get(usernames.indexOf(currentUser.getName())).getLoan().setUser(users.get(usernames.indexOf(currentUser.getName())));
			Thread loan = new Thread(users.get(usernames.indexOf(currentUser.getName())).getLoan());
			loan.start();
			return saveDB();
		}
		return false;
	}


	/**
	 * @return 是否付清当年的款额
	 */
	public boolean payLoan() {
		users.get(usernames.indexOf(currentUser.getName())).getLoan().pay();
		return saveDB();
	}


	/**
	 * @return
	 * 没用上
	 */
	public boolean freeze() {
		if (users.get(usernames.indexOf(currentUser.getName())).isFreezed()) {
			users.get(usernames.indexOf(currentUser.getName())).setFreezed(false);
		}else {
			users.get(usernames.indexOf(currentUser.getName())).setFreezed(true);
		}
		return true;
	}

}
