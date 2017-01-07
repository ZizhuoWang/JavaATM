package myATM;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.JTextArea;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.DecimalFormat;

public class UI {

	private JFrame frame;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
//					UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
					UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
					UI window = new UI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public UI() {
		initialize();
	}


	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		Client client = new Client();
		frame = new JFrame("ATM");
		frame.setBounds(100, 100, 450, 300);
		frame.setSize(450, 300);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		
		JTextArea textArea = new JTextArea();
		textArea.setBounds(87, 23, 268, 87);
		textArea.setEditable(false);
		textArea.setBackground(Color.WHITE);
		frame.getContentPane().add(textArea);
		
		JLabel lblNewLabel = new JLabel("用户名");
		lblNewLabel.setBounds(0, 170, 39, 15);
		frame.getContentPane().add(lblNewLabel);
		
		textField = new JTextField();
		textField.setBounds(44, 168, 210, 27);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		
		JLabel label = new JLabel("密码");
		label.setBounds(13, 201, 26, 15);
		frame.getContentPane().add(label);
		
		textField_1 = new JTextField();
		textField_1.setBounds(44, 199, 210, 27);
		frame.getContentPane().add(textField_1);
		textField_1.setColumns(10);
		
		textField_2 = new JTextField();
		textField_2.setBounds(44, 137, 122, 25);
		frame.getContentPane().add(textField_2);
		textField_2.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				int keyChar=e.getKeyChar();
				if (keyChar>=KeyEvent.VK_0 && keyChar<=KeyEvent.VK_9||e.getKeyChar()=='.') {
					
				} else {
					e.consume();  
				}
			}
			@Override
			public void keyPressed(KeyEvent e) {}
			@Override
			public void keyReleased(KeyEvent e) {}
		});
		textField_2.setColumns(10);
		textField_2.setEditable(false);
		
		textField_3 = new JTextField();
		textField_3.setBounds(233, 137, 122, 25);
		frame.getContentPane().add(textField_3);
		textField_3.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				int keyChar=e.getKeyChar();
				if (keyChar>=KeyEvent.VK_0 && keyChar<=KeyEvent.VK_9) {
					
				} else {
					e.consume();  
				}
			}
			@Override
			public void keyPressed(KeyEvent e) {}
			@Override
			public void keyReleased(KeyEvent e) {}
		});
		textField_3.setColumns(10);
		textField_3.setEditable(false);
		
		JLabel label_1 = new JLabel("金额");
		label_1.setBounds(13, 139, 26, 15);
		frame.getContentPane().add(label_1);
		
		JLabel label_2 = new JLabel("编号");
		label_2.setBounds(199, 139, 26, 15);
		frame.getContentPane().add(label_2);
		
		JButton button_2 = new JButton("存活期");
		button_2.setBounds(0, 24, 86, 25);
		frame.getContentPane().add(button_2);
		
		JButton button_7 = new JButton("取活期");
		button_7.setBounds(0, 47, 86, 25);
		frame.getContentPane().add(button_7);
		
		JButton button_3 = new JButton("存半年期");
		button_3.setBounds(0, 73, 86, 25);
		frame.getContentPane().add(button_3);
		
		JButton button_8 = new JButton("取半年期");
		button_8.setBounds(0, 100, 86, 25);
		frame.getContentPane().add(button_8);
		
		JButton button_4 = new JButton("存一年期");
		button_4.setBounds(362, 24, 86, 25);
		frame.getContentPane().add(button_4);
		
		JButton button_9 = new JButton("取一年期");
		button_9.setBounds(362, 47, 86, 25);
		frame.getContentPane().add(button_9);
		
		JButton button_5 = new JButton("存五年期");
		button_5.setBounds(362, 73, 86, 25);
		frame.getContentPane().add(button_5);
		
		JButton button_10 = new JButton("取五年期");
		button_10.setBounds(362, 100, 86, 25);
		frame.getContentPane().add(button_10);
		
		JButton button_6 = new JButton("偿还贷款");
		button_6.setBounds(87, 112, 86, 25);
		frame.getContentPane().add(button_6);
		
		JButton button_11 = new JButton("申请贷款");
		button_11.setBounds(269, 112, 86, 25);
		frame.getContentPane().add(button_11);
		
		JButton button = new JButton("注册");
		button.setBounds(266, 201, 60, 25);
		frame.getContentPane().add(button);
		
		JButton button_1 = new JButton("登录");
		button_1.setBounds(331, 201, 60, 25);
		frame.getContentPane().add(button_1);
		
		JButton button_12 = new JButton("查询");
		button_12.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!client.currentUser.getName().equals("")) {
					String output = client.query();
					JOptionPane.showMessageDialog(null, output, "账户信息", JOptionPane.INFORMATION_MESSAGE);
				}else {
					JOptionPane.showMessageDialog(null, "请先登录","错误",JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		button_12.setBounds(170, 111, 102, 27);
		frame.getContentPane().add(button_12);
		
		button_2.setEnabled(false);
		button_3.setEnabled(false);
		button_4.setEnabled(false);
		button_5.setEnabled(false);
		button_6.setEnabled(false);
		button_7.setEnabled(false);
		button_8.setEnabled(false);
		button_9.setEnabled(false);
		button_10.setEnabled(false);
		button_11.setEnabled(false);
		button_12.setEnabled(false);
		
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!textField.getText().isEmpty()) {
					boolean successful = client.login(textField.getText(), textField_1.getText());
					if (successful) {
						textArea.setText("登录成功！");
						textField_2.setEditable(true);
						textField_3.setEditable(true);
						textField.setEditable(false);
						textField_1.setEditable(false);
						
						button.setEnabled(false);
						button_1.setEnabled(false);
						button_2.setEnabled(true);
						button_3.setEnabled(true);
						button_4.setEnabled(true);
						button_5.setEnabled(true);
						button_6.setEnabled(true);
						button_7.setEnabled(true);
						button_8.setEnabled(true);
						button_9.setEnabled(true);
						button_10.setEnabled(true);
						button_11.setEnabled(true);
						button_12.setEnabled(true);
					} else {
						textArea.setText("用户名或密码错误！");
					} 
				}
			}
		});
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!textField.getText().isEmpty()) {
					boolean successful = client.newUser(textField.getText(), textField_1.getText());
					if (successful) {
						textArea.setText("注册成功！");
					} else {
						textArea.setText("用户已存在！");
					} 
				}
			}
		});
		button_11.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DecimalFormat format = new DecimalFormat("0.00");
//				client.query();
				if (!textField_2.getText().isEmpty()){
					String amount = format.format(Double.parseDouble(textField_2.getText()));
					boolean successful = client.getLoan(Double.parseDouble(amount));
					if(successful){
						client.query();
						JOptionPane.showMessageDialog(null, "贷款" +
								amount + "元成功！"
								+ "\n" + client.query());
					}else {
						JOptionPane.showMessageDialog(null, "贷款失败！", "贷款失败！", JOptionPane.ERROR_MESSAGE);
					}
				}else {
					JOptionPane.showMessageDialog(null, "请输入贷款款额");
				}
			}
		});
		button_6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				client.query();
				Double amount = client.currentUser.getLoan().getloan();
				int option;
				if (amount!=0.0) {
					option = JOptionPane.showConfirmDialog(null, "是否偿还贷款" + amount + "元", "还款",
							JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
					if (option == 0 && client.currentUser.getLoan().getloan() != 0.0) {
						boolean successful = client.payLoan();
						if (successful) {
							client.query();
							JOptionPane.showMessageDialog(null, "还款" + amount + "元成功！" + "\n" + client.query());
						} else {
							JOptionPane.showMessageDialog(null, "还款失败！", "还款失败！", JOptionPane.ERROR_MESSAGE);
						}
					} 
				}else {
					JOptionPane.showMessageDialog(null, "今年的款已经还过了！", "还款失败！", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		button_10.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DecimalFormat format = new DecimalFormat("0.00");
				client.query();
				if (!textField_3.getText().isEmpty()) {
					int which = Integer.parseInt(textField_3.getText()) - 1;
					String amount = "0.00";
					if (client.currentUser.getFiveBalance().size()>=which+1) {
						if (client.currentUser.getFiveBalance().get(which).expire) {
							amount = format.format(client.currentUser.getFiveBalance().get(which).getCash());
						}else {
							amount = format.format(client.currentUser.getFiveBalance().get(which).getUnexpired());
						}
					}
					boolean successful = client.withFive(which);
					if (successful) {
						client.query();
						JOptionPane.showMessageDialog(null, "取五年期存款" +
								amount + "成功！"
								+ "\n" + client.query());
					} else {
						JOptionPane.showMessageDialog(null, "取款失败！", "取款失败！", JOptionPane.ERROR_MESSAGE);
					} 
				}else {
					JOptionPane.showMessageDialog(null, "请输入有效的存款编号");
				}
			}
		});
		button_5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DecimalFormat format = new DecimalFormat("0.00");
				if (!textField_2.getText().isEmpty()) {
					boolean successful = client.depoFive(Double.parseDouble(textField_2.getText()));
					if (successful) {
						client.query();
						JOptionPane.showMessageDialog(null, "五年期存款" +
								format.format(Double.parseDouble(textField_2.getText())) + "成功！"
								+ "\n" + client.query());
					} else {
						JOptionPane.showMessageDialog(null, "存款失败！", "存款失败！", JOptionPane.ERROR_MESSAGE);
					} 
				}else {
					JOptionPane.showMessageDialog(null, "请输入存款金额");
				}
			}
		});
		button_9.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DecimalFormat format = new DecimalFormat("0.00");
				client.query();
				if (!textField_3.getText().isEmpty()) {
					int which = Integer.parseInt(textField_3.getText()) - 1;
					String amount = "0.00";
					if (client.currentUser.getOneBalance().size()>=which+1) {
						if (client.currentUser.getOneBalance().get(which).expire) {
							amount = format.format(client.currentUser.getOneBalance().get(which).getCash());
						}else {
							amount = format.format(client.currentUser.getOneBalance().get(which).getUnexpired());
						}
					}
					boolean successful = client.withOne(which);
					if (successful) {
						client.query();
						JOptionPane.showMessageDialog(null, "取一年期存款" +
								amount+ "成功！"
								+ "\n" + client.query());
					} else {
						JOptionPane.showMessageDialog(null, "取款失败！", "取款失败！", JOptionPane.ERROR_MESSAGE);
					} 
				}else {
					JOptionPane.showMessageDialog(null, "请输入有效的存款编号");
				}
			}
		});
		button_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DecimalFormat format = new DecimalFormat("0.00");
				if (!textField_2.getText().isEmpty()) {
					boolean successful = client.depoOne(Double.parseDouble(textField_2.getText()));
					if (successful) {
						client.query();
						JOptionPane.showMessageDialog(null, "一年期存款" + 
								format.format(Double.parseDouble(textField_2.getText())) + "成功！"
								+ "\n" + client.query());
					} else {
						JOptionPane.showMessageDialog(null, "存款失败！", "存款失败！", JOptionPane.ERROR_MESSAGE);
					} 
				}else {
					JOptionPane.showMessageDialog(null, "请输入存款金额");
				}
			}
		});
		button_8.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DecimalFormat format = new DecimalFormat("0.00");
				client.query();
				if (!textField_3.getText().isEmpty()) {
					int which = Integer.parseInt(textField_3.getText()) - 1;
					String amount = "0.00";
					if (client.currentUser.getHalfBalance().size()>=which+1) {
						if (client.currentUser.getHalfBalance().get(which).expire) {
							amount = format.format(client.currentUser.getHalfBalance().get(which).getCash());
						}else {
							amount = format.format(client.currentUser.getHalfBalance().get(which).getUnexpired());
						}
					}
					boolean successful = client.withHalf(which);
					if (successful) {
						client.query();
						JOptionPane.showMessageDialog(null, "取半年期存款" +
								amount
						+ "成功！"+ "\n" + client.query());
					} else {
						JOptionPane.showMessageDialog(null, "取款失败！", "取款失败！", JOptionPane.ERROR_MESSAGE);
					} 
				}else {
					JOptionPane.showMessageDialog(null, "请输入有效的存款编号");
				}
			}
		});
		button_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DecimalFormat format = new DecimalFormat("0.00");
				if (!textField_2.getText().isEmpty()) {
					boolean successful = client.depoHalf(Double.parseDouble(textField_2.getText()));
					if (successful) {
						client.query();
						JOptionPane.showMessageDialog(null, "半年期存款"+
						format.format(Double.parseDouble(textField_2.getText()))+
								"成功！"+ "\n" + client.query());
					}else {
						JOptionPane.showMessageDialog(null, "存款失败！", "存款失败！", JOptionPane.ERROR_MESSAGE);
					}
				}else {
					JOptionPane.showMessageDialog(null, "请输入存款金额");
				}
			}
		});
		button_7.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DecimalFormat format = new DecimalFormat("0.00");
				if (!textField_2.getText().isEmpty()) {
					boolean successful = client.withCheck(Double.parseDouble(textField_2.getText()));
					if (successful) {
						client.query();
						JOptionPane.showMessageDialog(null, "取活期存款" +
								format.format(Double.parseDouble(textField_2.getText())) + "成功！"
								+ "\n" + client.query());
					} else {
						JOptionPane.showMessageDialog(null, "取款失败！", "取款失败！", JOptionPane.ERROR_MESSAGE);
					} 
				}else {
					JOptionPane.showMessageDialog(null, "请输入取款金额");
				}
			}
		});
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DecimalFormat format = new DecimalFormat("0.00");
				if (!textField_2.getText().isEmpty()) {
					boolean successful = client.depoCheck(Double.parseDouble(textField_2.getText()));
					if (successful) {
						client.query();
						JOptionPane.showMessageDialog(null, "活期存款" + 
						format.format(Double.parseDouble(textField_2.getText())) + "成功！"
								+ "\n" + client.query());
					} else {
						JOptionPane.showMessageDialog(null, "存款失败！", "存款失败！", JOptionPane.ERROR_MESSAGE);
					}
				}else{
					JOptionPane.showMessageDialog(null, "请输入存款金额");
				}
			}
		});
	}
}
