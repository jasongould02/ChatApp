package jcg.java.chat.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

public class Client implements Runnable {

	// Thread stuff
	private Thread thread = null;
	private boolean running = true;
	
	// Socket stuff
	private Socket socket;
	private PrintWriter o = null;
	private BufferedReader i = null;
	
	// GUI stuff
	private JFrame window = null;
	private SyntaxStyledPane pane = null;
	private JTextField inputField = null; //  				/CONNECT 'IP address of server':'port'/'channel'
	private JMenuBar menuBar = null;
	
	private JMenu fileMenu = null;
	private JMenuItem file_connectServer = null;
	private JMenuItem file_disconnectServer = null;
	//private JMenuItem 
	
	private boolean windowClosing = false;

	// User stuff
	private String nickname = null;
	
	public boolean isConnected() {
		if(socket == null || socket.isClosed()) {
			return false;
		} else {
			return socket.isConnected();
		}
	}
	
	public String getServerIP() {
		return socket.getInetAddress().getHostAddress();
	}
	
	public String getNickname() {
		return nickname;
	}
	
	private JMenuBar createMenuBar() {
		menuBar = new JMenuBar();
		
		fileMenu = new JMenu("File");
		file_connectServer = new JMenuItem("Connect");
		file_connectServer.setAction(actionConnectServerDialog);
		file_connectServer.setText(file_connectServer.getText());
		fileMenu.add(file_connectServer);
		
		file_disconnectServer = new JMenuItem("Disconnect");
		file_disconnectServer.setAction(actionDisconnectServer);
		file_disconnectServer.setText(file_disconnectServer.getText());
		fileMenu.add(file_disconnectServer);
		
		menuBar.add(fileMenu);
		return menuBar;
	}
	
	public Client() {
		socket = new Socket();
		this.start();
		initWindow();
		//running = true;
	}
	
	private void disconnect() {
		try {
			socket.close();
			socket = null;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private boolean connectToServer(String ipAddress, String portNumber) {
		if(socket == null) {
			socket = new Socket();
		} else if(socket.isClosed()) {
			socket = null;
			socket = new Socket();
		}
		
		try {
			socket.connect(new InetSocketAddress(ipAddress, Integer.parseInt(portNumber)));
		} catch (NumberFormatException | IOException e) {
			e.printStackTrace();
		}
		
		if(socket.isConnected()) {
			return true;
		} else {
			return false;
		}
	}
	
	@Deprecated
	private boolean makeConnection() throws NumberFormatException, IOException {
		if (inputField != null) {
			if (inputField.getText().trim().startsWith("/connect")) {
				if (socket == null) {
					socket = new Socket();
				} else if (socket != null && socket.isClosed()) {
					socket = null;
					socket = new Socket();
				}
				String comm = inputField.getText().trim().substring(8).trim();
				if(socket.isConnected()) {
					System.out.println("Connected to server located at:" + comm);
				} else {
					System.out.println("Failed to connect to server, either the server at the given address is offline, or does not exist.");
				}
				String[] split = comm.split(":");

				if(split.length == 2) {
					socket.connect(new InetSocketAddress(split[0], Integer.parseInt(split[1])));
				}
			}
		}
		if (socket.isConnected()) {
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public void run() {
 		/*try {
 			String input;
 			while (socket.isConnected() == false) {} // Awaiting connection before allowing the run statement to commence
			o = new PrintWriter(socket.getOutputStream());
			i = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	
			while ((input = i.readLine()) != null) { // Once this loop ends the entire socket gets closed
				System.out.println("Data received:" + input);
				try {
					pane.println(input);
				} catch (BadLocationException e) {
					e.printStackTrace();
				}					
			}
			o.close();
			i.close();
			socket.close(); // Closing socket
		} catch(IOException e) {
			e.printStackTrace();
		}*/
		String input;
		System.out.println("windowClosing == " + windowClosing);
		while(windowClosing == false) { 
			System.out.println("windowClosing == " + windowClosing);
			//System.out.println("not closing");
			while (socket.isConnected() == false) {if(socket.isConnected()) {break;}} // Awaiting connection before allowing the run statement to commence
			
	 		try {
				//socket.connect(new InetSocketAddress("127.0.0.1", 80)); // attempt auto connect as soon as client starts
	 			if(!socket.isClosed()) {
	 				o = new PrintWriter(socket.getOutputStream());
					i = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	 			}
				
				while ((input = i.readLine()) != null || socket.isClosed()) { // Once this loop ends the entire socket gets closed
					System.out.println("Data received:" + input);
					try {
						pane.println(input);
					} catch (BadLocationException e) {
						e.printStackTrace();
					}					
				}
				
				disconnect();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private void commandType(String message) {
		if(message == null) {
			// TODO: Should this throw an exception instead hmmm
			return;
		}
		if(message.trim().equals("/disconnect") || message.trim().equals("/dc")) {
			if(socket == null) {
				System.out.println("Not currently connected to a server.");
			} else {
				disconnect();
				/*socket.close();	socket = null;*/
			}
		}
		
		if(message.trim().startsWith("/nickname")) {
			nickname = (message.substring(9)).trim();
			//o.write("/nickname " + nickname.trim());
			System.out.println("Setting nickname:" + nickname);
		}
	}
	
	private void updateNickname(String newNickname) {
		nickname = newNickname;
		System.out.println("Setting nickname: " + nickname);
	}

	/**
	 * Wraps the {@link SyntaxStyledPane#println(String)}
	 * This is for writing messages to the client privately
	 * 
	 * Useful for having commands like '/help'
	 * 
	 * @returns the string being printed or null if it fails
	 */
	private String printMessage(String text) {
		try {
			pane.println(text);
			return text;
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Sends message to server which will be received then to be sent 
	 * 
	 * @returns null if fails
	 */
	private String sendMessage() {
		try {
			String message = inputField.getText();
			if(message.trim().startsWith("/")) { // Is command
				commandType(message);
			} else {
				//message = "MESSAGE:" + message;
				//System.out.println("MESSAGE:" + message);
				if(o == null) {
					o = new PrintWriter(socket.getOutputStream());
				}
				if(nickname != null) {
					o.println(nickname.trim() + ": " + message.trim());
				} else {
					pane.println("You have not set your nickname yet.\n");
				}
				o.flush();
				inputField.setText("");
			}
			return message;
		} catch (IOException | BadLocationException e) {
			e.printStackTrace();
		}
		System.out.println("Failed to send message.");
		return null;
	}
	
	
	private void closeSocket(Socket socket) {
		try {
			if(!socket.isClosed()) {
				socket.getInputStream().close();
				socket.getOutputStream().close();
				socket.close();
			}
			socket = null;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void initWindow() {
		window = new JFrame();
		window.setSize(400, 600);
		window.setTitle("ChatApp Client");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		
		window.addWindowListener(new WindowListener() {
			@Override
			public void windowActivated(WindowEvent e) {}
			@Override
			public void windowClosed(WindowEvent e) {
				windowClosing = true;
				closeSocket(socket);
			}
			@Override
			public void windowClosing(WindowEvent e) {
				windowClosing = true;
				closeSocket(socket);
			}
			@Override
			public void windowDeactivated(WindowEvent e) {}
			@Override
			public void windowDeiconified(WindowEvent e) {}
			@Override
			public void windowIconified(WindowEvent e) {}
			@Override
			public void windowOpened(WindowEvent e) {}
		});
		
		window.setLocationRelativeTo(null);
		
		pane = new SyntaxStyledPane();
		pane.getJTextPane().setName("Messages");
		//pane.getJTextPane().setEditable(false);
		
		SimpleAttributeSet keyWord = new SimpleAttributeSet();
		StyleConstants.setForeground(keyWord, Color.RED);
		StyleConstants.setBackground(keyWord, Color.YELLOW);
		StyleConstants.setBold(keyWord, true);
		
		Keyword hello = new Keyword(("hello").trim(), keyWord);
		pane.addKeyword(hello);
		
		//pane.getJTextPane().setVisible(true);
		window.add(pane.getJTextPane(), BorderLayout.CENTER);
		/*messages = new JTextArea();
		messages.setEditable(false);
		messages.setVisible(true);
		window.add(messages, BorderLayout.CENTER);*/
		
		inputField = new JTextField(140);
		inputField.setEditable(true);
		inputField.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					//System.out.println("enter pressed");
					try {
						
						if(inputField.getText().length() > 0 && inputField.getText() != " ") {
							makeConnection();
							//connectToServer(ipAddress, portNumber)ct
							
							// if msg is not command send
							// else send coded message to server
							// and server send coded message back
							sendMessage();
							inputField.setText("");
						}
					} catch (NumberFormatException | IOException e1) {
						e1.printStackTrace();
					}
				}
			}
			@Override
			public void keyReleased(KeyEvent e) {	}
			@Override
			public void keyTyped(KeyEvent e) {	}
		});
		inputField.setVisible(true);
		window.add(inputField, BorderLayout.SOUTH);
		
		window.setJMenuBar(createMenuBar());
		
		
		window.setVisible(true);
	}
	
	public synchronized void start() {
		running = true;
		thread = new Thread(this, "Client");
		thread.setPriority(Thread.NORM_PRIORITY);
		thread.start();
	}
	
	public synchronized void stop() {
		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		Client c = new Client();
		//c.start();
	}
	
	private String showConnectDialog() {
		String IP = JOptionPane.showInputDialog("Enter address:");
		return IP;
	}
	
	public Action actionConnectServerDialog = new AbstractAction("Connect") {
		@Override
		public void actionPerformed(ActionEvent e) {
			String input = showConnectDialog();
			if(input != null) {
				String[] ip = input.split(":");
				connectToServer(ip[0].trim(), ip[1].trim());
			}
			//dialog = new ConnectionDialog(window, actionAcceptConnectionDialog, actionCancelConnectionDialog);
			//connectToServer(socket, "127.0.0.1", "80");
		}
	};
	
	public Action actionDisconnectServer = new AbstractAction("Disconnect") {
		@Override
		public void actionPerformed(ActionEvent e) {
			disconnect();
		}
	};

}
