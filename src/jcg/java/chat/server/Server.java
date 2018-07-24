package jcg.java.chat.server;

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
	
	public Server(int portNumber) throws IOException {
		serverSocket = new ServerSocket(portNumber);
		userList = new ArrayList<Socket>();
		BufferedReader br;
	
		while(running) {
			for(Socket socket : userList) {
				if(socket.getInputStream().available() > 0) {
					String message = "";
					String line;
					
					// Add protocol reading like whos message from
					// time sent, and formatting etc
					
					br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					while((line = br.readLine()) != null) {
						message += line;
					}
					
					
					if(message.length() > 0) {
						sendMessage(message);
					}
					
				}
			}
		}
	}
	
	public void sendMessage(String message) {
		
	}
	
	public static void main(String[] args) {
		
	}

	
	
	
}
