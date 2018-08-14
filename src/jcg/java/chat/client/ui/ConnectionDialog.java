package jcg.java.chat.client.ui;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class ConnectionDialog {
	
	enum DialogType {
		CONNECTION,
		NICKNAME;
	};

	JDialog dialogBox = null;
	JLabel serverIPLabel = null;
	JLabel serverPortLabel = null;
	
	JTextField serverIP = null;
	JTextField serverPort = null;
	
	JButton connectButton = null;
	JButton cancelButton = null;
	
	public ConnectionDialog(JFrame frame) {
		
	}
	
	public void initWindow(JFrame frame, String title) {
		dialogBox = new JDialog(frame, title);
		
		dialogBox.setVisible(true);
	}
	
	
	
}
