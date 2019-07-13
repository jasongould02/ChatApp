package jcg.java.chat.core.server;

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
	
	/**
	 *	Worker thread designed to handle client socket
	 */
	class Worker implements Runnable {

		// TODO: Update to handle more than one client per thread
		
		private Thread thread;
		private boolean running = false;

		private Socket clientSocket;
		private PrintWriter o;
		private BufferedReader i;

		public Worker(Socket cs) {
			this.clientSocket = cs;
			try {
				o = new PrintWriter(clientSocket.getOutputStream());
				i = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
		/**
		 * Sends a message directly to the client
		 * 
		 * @param text - message to be sent
		 */
		private void sendClientMessage(String text) {
			try {
				o.write(text);
				o.flush();
			} catch(NullPointerException e) {
				e.printStackTrace();
			}
		}
		
		@Override
		public void run() {
			try {
				System.out.println("Worker started.");
				String input;
				if(i != null && clientSocket.isConnected()) {
				
					while ((input = i.readLine()) != null) { // Once this loop ends the entire socket gets closed
						//System.out.println("["+getAddress() + "] [Message] >>" + input+"<< [/Message]");
						
						// parse message,
						// if privacy is private
						System.out.println("sending message to all connected clients...");
						sendMessage(input + "\n"); // sends message received to the entire server
						o.flush();
					} //System.out.println("closing worker stream");
	
					o.close();
					i.close();
					clientSocket.close(); // Close socket
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		private String getAddress() {
			return clientSocket.getRemoteSocketAddress().toString().replaceAll("/", " ").trim();
		}

		public synchronized void start() {
			running = true;
			thread = new Thread(this, "Worker/" + getAddress());
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
