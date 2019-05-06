package jcg.java.chat.test;

import java.io.IOException;
import java.net.ServerSocket;

import jcg.java.chat.commons.connection.Connection;

public class ClientTest {

	public static void main(String[] args) {
		Connection a = new Connection("127.0.0.1", "2000");

		while(true) {
			if(a.getSocket().isConnected() == false) {
				a.connect("127.0.0.1", "2000");
				break;
			}
		}
		
		a.sendMessage("Shit".getBytes());
		
		try {
			a.getSocket().close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
}
