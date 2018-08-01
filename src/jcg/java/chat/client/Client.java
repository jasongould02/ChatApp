package jcg.java.chat.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import jcg.java.chat.core.ui.Keyword;
import jcg.java.chat.core.ui.SyntaxStyledPane;

public class Client implements Runnable {

	String username;
	
	Socket socket;
	Thread thread = null;
	private boolean running = true;
	
	JFrame window = null;
	SyntaxStyledPane pane = null;
	JTextField inputField = null; //  				/CONNECT 'IP address of server':'port'/'channel'
	
	PrintWriter o = null;
	BufferedReader i = null;
	
	public Client() {
		initWindow();
		socket = new Socket();
		//running = true;
	}
	
	@Override
	public void run() {
		String input;
 		try {
			socket.connect(new InetSocketAddress("127.0.0.1", 80));
			o = new PrintWriter(socket.getOutputStream());
			i = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		//while(running) {
			try {
				//System.out.println(running);
				while((input = i.readLine()) != null) { 	// Once this loop ends the entire socket gets closed
					//System.out.println("received:" + input);
					try {
						pane.insertString(input);
					} catch (BadLocationException e) {
						e.printStackTrace();
					}
					/*if(input.contains("MESSAGE:")) {
						String message = input.substring(8);
						
						if(!message.endsWith("\n")) message += "\n";
						
						//o.println(socket.getInetAddress().getHostAddress() + ":" + message);
						//System.out.println(socket.getInetAddress().getHostAddress() + ":" + message);
						
						// Append new content to editor pane
						try {
							pane.insertString(message);
						} catch (BadLocationException e) {
							e.printStackTrace();
						}
						
					}*/
					
				}
				System.out.println("closing");
				
				//o.close();
				i.close();
			} catch(IOException e) {
				e.printStackTrace();
			}
			
			
			try {
				// Close socket
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		//}
		
	}
	
	private void commandType(String message) {
		try {
			if(message.trim().startsWith("/connect")) {
				if(socket == null) {
					socket = new Socket();
				} else if(socket != null && socket.isClosed()) {
					socket.close();
					socket = null;
					socket = new Socket();
				}
				
				String comm = message.trim().substring(8).trim();
				System.out.println("COMM" + comm);
				String[] split = comm.split(":");
				socket.connect(new InetSocketAddress(split[0], Integer.parseInt(split[1])));
			}
			
			if(message.trim().equals("/dc")) {
				if(socket == null) {
					System.out.println("cant disconnect... not connected to a server");
				} else {
					socket.close();
					socket = null;
				}
				
			}
			
			if(message.trim().startsWith("/username")) {
				username = (message.substring(9)).trim();
				System.out.println("username:" + username);
			}
				
		}catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Returns null if fails
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
				if(username != null) { 
					o.println(username.trim() + ": " + message.trim());
				}
				o.flush();
				inputField.setText("");
			}
			
			
			
			return message;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private void initWindow() {
		window = new JFrame();
		window.setSize(400, 600);
		window.setTitle("ChatApp Client");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		
		window.setLocationRelativeTo(null);
		
		pane = new SyntaxStyledPane();
		pane.getJTextPane().setName("Messages");
		//pane.getJTextPane().setEditable(false);
		
		SimpleAttributeSet keyWord = new SimpleAttributeSet();
		StyleConstants.setForeground(keyWord, Color.RED);
		StyleConstants.setBackground(keyWord, Color.YELLOW);
		StyleConstants.setBold(keyWord, true);
		
		Keyword hello = new Keyword("hello", keyWord);
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
					sendMessage();
					inputField.setText("");
				}
			}
			@Override
			public void keyReleased(KeyEvent e) {	}
			@Override
			public void keyTyped(KeyEvent e) {	}
		});
		inputField.setVisible(true);
		window.add(inputField, BorderLayout.SOUTH);
		
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
		Client c = new Client();
		c.start();
		
	}
	
}
