package jcg.java.chat.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server{

	private ServerSocket serverSocket;
	private ArrayList<Worker> userList;
	
	public Server(int portNumber) throws IOException {
		serverSocket = new ServerSocket(portNumber, 100);
		userList = new ArrayList<Worker>();
		BufferedReader br;
		
		while(true) {
			
			Worker worker = new Worker(serverSocket.accept());
			worker.start();
			userList.add(worker);
			System.out.println("Connection made.");
			
			//userList.add(serverSocket.accept());
			/*for(Socket socket : userList) {
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
			}*/
		}
	}
	
	public void sendMessage(String message) {
		
	}
	
	public static void main(String[] args) {
		try {
			Server server = new Server(80);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	
	
}
