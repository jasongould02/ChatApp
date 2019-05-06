package jcg.java.chat.test;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerTest {

	public static void main(String[] args) {
		ServerSocket p = null; 
		try {
			p = new ServerSocket(2000);
		} catch (IOException e) {
			// TODO Auto-generated catch block
		}
		Socket connection = null;
		try {
		
			 while(true) {
				 connection = p.accept();
				 if(connection.getInputStream().available() != 0) {
					 System.out.println("waiting");
					 break;
				 } else {
					 System.out.println(connection.getInputStream().readAllBytes());
				 }
			 }
			 
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		
		try {
			System.out.println(connection.getInputStream().readAllBytes().toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
}
