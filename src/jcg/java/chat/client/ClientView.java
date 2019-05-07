package jcg.java.chat.client;

import java.awt.GridLayout;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ClientView {

	Client client;
	
	JFrame frame = null;
	String title = null;
	int WIDTH, HEIGHT;
	
	
	/*	Status Bar	*/
	JLabel isConnectedLabel = null;
	JLabel serverIPLabel = null;
	JLabel nicknameLabel = null;
	
	public static void main(String args[]) {
		String t = " ";
		System.out.println(t.trim().length());
		ClientView view = new ClientView();
	}
	
	public ClientView() {
		WIDTH = 400;
		HEIGHT = 600;
		
		frame = new JFrame();
		frame.setTitle(title);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(WIDTH, HEIGHT);
		
		frame.setLocationRelativeTo(null);
		
		JPanel content = new JPanel();
		
		GridLayout layout = new GridLayout(0, 1);
		content.setLayout(layout);
		content.add(createMessageBoard());
		content.add(createMessageBox());
		content.add(createStatusBar());
		frame.add(content);
		frame.repaint();
		frame.revalidate();
		frame.pack();
		frame.setVisible(true);
		
		
		client = new Client();
	}
	
	private JPanel createMessageBoard() {
		JPanel board = new JPanel();
		SyntaxStyledPane pane = new SyntaxStyledPane();
		pane.getJTextPane().setName("Message Board");
		pane.getJTextPane().setBackground(null);
		pane.getJTextPane().setForeground(null);
		
		board.add(pane.getJTextPane());
		return board;
	}
	
	private JPanel createMessageBox() {
		JPanel panel = new JPanel();
		JTextField field = new JTextField(20);
		panel.add(field);
		return panel;
	}
	
	private JPanel createStatusBar() {
		JPanel bar = new JPanel();
		BoxLayout bl = new BoxLayout(bar, BoxLayout.X_AXIS);
		bar.setLayout(bl);
		
		isConnectedLabel = new JLabel();
		serverIPLabel = new JLabel();
		nicknameLabel = new JLabel();
		
		if(client != null) {
			isConnectedLabel.setText("Connected:" + client.isConnected());
			serverIPLabel.setText("Server IP:" + client.getServerIP());
			nicknameLabel.setText("Nickname:" + client.getNickname());
		} else {
			isConnectedLabel.setText("Connected:");
			serverIPLabel.setText("Server IP:");
			nicknameLabel.setText("Nickname:");
		}
		
		isConnectedLabel.setText("Connected:");
		serverIPLabel.setText("Server IP:");
		nicknameLabel.setText("Nickname:");
		
		bar.add(nicknameLabel);
		bar.add(serverIPLabel);
		bar.add(isConnectedLabel);
		
		return bar;
	}
	
	public void updateStatusBar() {
		isConnectedLabel.setText("Connected:"  + client.isConnected());
		serverIPLabel.setText("Server IP:" + client.getServerIP());
		nicknameLabel.setText("Nickname:" + client.getNickname());
	}
	
	public void displayMessage(String text) {
		//client.printMessage(text);
		//sendMessage();
	}
	
	
}
