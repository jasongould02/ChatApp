package jcg.java.chat.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created on July 25 2018
 * Based on ClientWorker.java found in github.com/runiclp/HttpWebServer
 * 
 * */


public class Worker implements Runnable {
	
	private Thread thread;
	private String remoteIP;
	private boolean running = false;
	
	private Socket clientSocket;
	
	public Worker(Socket cs) {
		this.clientSocket = cs;
		remoteIP = clientSocket.getRemoteSocketAddress().toString().replaceAll("/", " ").trim();
	}
	
	@Override
	public void run() {
		String input;
		PrintWriter o;
		BufferedReader i;
		try {
			o = new PrintWriter(clientSocket.getOutputStream());
			i = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

			// Once this loop ends the entire socket gets closed
			while((input = i.readLine()) != null) { // After reading the first line, read the rest of the input
				// Check for message type (like checking if HTTP GET or whatever
				
				if(input.startsWith("MESSAGE:")) {
				}
				
				if(input.startsWith("CLOSESOCKET")) {
					break;
				}
			}
			
			o.close();
			i.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		
		try {
			// Close socket
			clientSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private void response(PrintWriter out, String input) throws IOException {
		//String[] sep = input.split(" ");
		
		// Parse the input and respond
		
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
