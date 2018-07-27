package jcg.java.chat.client;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Client implements Runnable {

	Socket socket = null;
	Thread thread;
	private boolean running = false;
	
	JFrame window;
	JTextArea messages;
	JTextField inputField; //  				/CONNECT 'IP address of server':'port'/'channel'
	
	public Client() {
		initWindow();
	}
	
	@Override
	public void run() {
	
		while(running) {
			
		}
		
	}
	
	private void initWindow() {
		window = new JFrame();
		window.setSize(400, 600);
		window.setTitle("ChatApp Client");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		
		window.setLocationRelativeTo(null);
		
		messages = new JTextArea();
		messages.setEditable(false);
		messages.setVisible(true);
		window.add(messages, BorderLayout.CENTER);
		
		inputField = new JTextField(140);
		inputField.setEditable(true);
		inputField.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					//System.out.println("enter pressed");
					getInputMessage();
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

	/**
	 * Returns null if fails
	 * 
	 */
	private String getInputMessage() {
		
		try {
			String message = inputField.getText();
			PrintWriter o;
			if(message.trim().startsWith("/")) {
				// IS COMMAND
				
				if(message.trim().startsWith("/connect")) {
					if(socket == null) {
						socket = new Socket();
					} else if(socket != null || socket.isClosed()) {
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
				
				
			} else {
				message = "MESSAGE:" + message;
				System.out.println(message);
				o = new PrintWriter(socket.getOutputStream());
				o.println(message);
				inputField.setText("");
			}
			return message;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public synchronized void start() {
		running = true;
		thread = new Thread("Client");
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
	}
	
}
