package jcg.java.chat.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {

	private ServerSocket serverSocket;
	private ArrayList<Socket> userList;
	private boolean running = false;
	
	public Server() throws IOException {
		serverSocket = new ServerSocket();
		userList = new ArrayList<Socket>();
		BufferedReader i;
		String input;
		
		while(running || !serverSocket.isClosed()) { // Either should indicate that the server should be closed OR should it be both required hmmmm
			userList.add(serverSocket.accept());

			
			for(Socket s : userList) {
				i = new BufferedReader(new InputStreamReader(s.getInputStream()));
				while((input = i.readLine()) != null) { // After reading the first line, read the rest of the input
				}
			}
			
		}
		
	}
	
	
	
	
	public static void main(String[] args) {
		
	}
	
}
