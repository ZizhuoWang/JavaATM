package myATM;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import myATM.Server;

public class ServerDaemon {
	public static final int PORT = 1234;
	public static int CURRENT_THREADS = 0;
	ServerSocket server;
	JTextArea area = new JTextArea();
	ArrayList<User> users = new ArrayList<User>();
	public ServerDaemon() {
		Server.createDB();
		try {
			server = new ServerSocket(PORT);
			JFrame frame = new JFrame("Server Daemon");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setSize(400, 400);
			frame.setVisible(true);
			frame.add(area);
			JScrollPane pane = new JScrollPane(area);
			frame.add(pane);
			DecimalFormat format = new DecimalFormat("0.00");
			Thread update = new Thread(){
				public void run() {
					while (true) {
						try {
							users.clear();
							Connection connection = Server.getConnection();
							Statement statement = connection.createStatement();
							ResultSet resultSet = statement.executeQuery("SELECT * FROM atm.user");
							while(resultSet.next()){
								byte[] data = resultSet.getBytes(2);
								ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
								ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
								User tempUser = (User) objectInputStream.readObject();
								users.add(tempUser);
							}
							connection.close();
						} catch (Exception e) {
							e.printStackTrace();
						}
						String show = "";
						show+="服务器已启动\n在线人数：" + CURRENT_THREADS+"\n";
						for (Iterator iterator = users.iterator();iterator.hasNext();) {
							User temp = (User)iterator.next();
							show+=temp.getName()+"的总存款金额："+
									format.format(temp.calculateTotalBalance())+"元\n";
						}
						area.setText(show);
						try {
							Thread.currentThread().sleep(10);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			};
			update.start();
			while(true){
				Server thread = new Server(server.accept());
				thread.start();
			}
		} catch (Exception e) {
//			e.printStackTrace();
		}finally {
			try {
//				server.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
		ServerDaemon serverDaemon = new ServerDaemon();
	}
}
