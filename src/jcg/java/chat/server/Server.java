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
	
	private void runServer() throws IOException {
		while (true) {
			System.out.println("Waiting for connection...");
			Worker worker = new Worker(serverSocket.accept());
			worker.start();
			userList.add(worker);
			System.out.println("Connection made.");

			removeClosedSockets();
		}
	}
	
	private void sendMessage(String message) throws IOException {
		for(Worker w : userList) {
			if(w.o == null) {
				w.o = new PrintWriter(w.clientSocket.getOutputStream());
			}
			System.out.println("Relaying Message:" + message);
			w.o.write(message);
			w.o.flush();
		}
	}
	
	
	private void removeClosedSockets() {
		ArrayList<Worker> removeQueue = new ArrayList<Worker>();
		for(Worker w : userList) {
			if(w.clientSocket.isClosed()) {
				removeQueue.add(w);
			}
		}
		userList.remove(removeQueue);
		removeQueue = null;
	}
	
	/*private ArrayList<Worker> getWorkerList() {
		return userList;
	}*/
	
	public static void main(String[] args) {
		try {
			Server server = new Server(80);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	
	class Worker implements Runnable {

		private Thread thread;
		private String remoteIP;
		private boolean running = false;

		private Socket clientSocket;
		
		private PrintWriter o;
		private BufferedReader i;

		public Worker(Socket cs) {
			this.clientSocket = cs;
			remoteIP = clientSocket.getRemoteSocketAddress().toString().replaceAll("/", " ").trim();
			
			try {
				o = new PrintWriter(clientSocket.getOutputStream());
				i = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}

		@Override
		public void run() {
			System.out.println("Worker started.");
			String input;
			
			try {
				System.out.println("while");
				while ((input = i.readLine()) != null) { // Once this loop ends the entire socket gets closed
					System.out.println("message:" + input);
					sendMessage(input + "\n");
					o.flush();
				}
				System.out.println("closing worker stream");

				o.close();
				i.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

			try {
				clientSocket.close(); // Close socket
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
	
}
