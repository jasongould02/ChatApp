package jcg.java.chat.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {

	private ServerSocket serverSocket;
	private ArrayList<Worker> userList;
	
	public Server(int portNumber) throws IOException {
		serverSocket = new ServerSocket(portNumber, 100);
		userList = new ArrayList<Worker>();
		runServer();
	}
	
	private void runServer() {
		BufferedReader br;
		try {
			while(true) {
				Worker worker = new Worker(serverSocket.accept());
				worker.start();
				userList.add(worker);
				System.out.println("Connection made.");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void sendMessage(String message) {
		
	}
	
	public ArrayList<Worker> getWorkerList() {
		return userList;
	}
	
	class Worker implements Runnable {

		private Thread thread;
		private String remoteIP;
		private boolean running = false;

		private Socket clientSocket;
		
		PrintWriter o;
		BufferedReader i;

		public Worker(Socket cs) {
			this.clientSocket = cs;
			remoteIP = clientSocket.getRemoteSocketAddress().toString().replaceAll("/", " ").trim();
		}

		@Override
		public void run() {
			String input;
			
			try {
				o = new PrintWriter(clientSocket.getOutputStream());
				i = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				// Once this loop ends the entire socket gets closed
				while ((input = i.readLine()) != null) {

					if (input.contains("MESSAGE:")) {
						String message = input.substring(8);

						if (!message.endsWith("\n"))
							message += "\n";

						o.println(clientSocket.getInetAddress().getHostAddress() + ":" + message);
						System.out.println(clientSocket.getInetAddress().getHostAddress() + ":" + message);
					}

				}

				o.close();
				i.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

			try {
				// Close socket
				clientSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		public synchronized void start() {
			running = true;
			thread = new Thread(this, "Worker/" + remoteIP);
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
	}
	
	public static void main(String[] args) {
		try {
			Server server = new Server(80);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
