package jcg.java.chat.core.connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

import jcg.java.chat.core.log.Log;

public class Connection {

	//private String connectionName = "";
	private Socket socket = null;
	
	private PrintWriter writer = null;
	private BufferedReader reader = null;

	/**
	 * Creates a connection object, however nothing is initialized.
	 */
	public Connection() {
		socket = new Socket();
	}
	
	public Connection(String ip, String port) {
		socket = new Socket();
		if(connect(ip, port)) {
			Log.log("Connection made to: ["+ip+":"+port+"]");
			try {
				writer = new PrintWriter(socket.getOutputStream());
				reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public boolean closeConnection() {
		try {
			if(socket != null) {
				if(writer != null) {
					writer.flush();
					writer.close();
					writer = null;		//Log.log("PrintWriter closed successfully.");
				}
				if(reader != null) {
					reader.close();
					reader = null;		//Log.log("BufferedReader closed successfully.");
				}
				socket.close();
				if(!socket.isClosed()) {
					Log.error("Socket was unable to close. Setting it to null."); // Sett
				} 
				socket = null;
				Log.log("Connection closed successfully. Socket and streams are closed/null.");
				return true;
			} else {
				Log.log("Connection already closed.");
				return true;
			}
		} catch (IOException e) {
			Log.error("Failed to close connection.");
			socket = null;
			writer = null;
			reader = null;
			e.printStackTrace();
			return false;
		} 
	}
	
	public boolean connect(String ip, String port) {
		if(socket == null) {
			socket = new Socket();
		} else if(socket.isClosed()) {
			socket = null;
			socket = new Socket();
		} else {
			if(!socket.isClosed()) {
				Log.log("Socket not yet closed. Disconnecting before making new connection");
				closeConnection();
			}
			try {
				socket.connect(new InetSocketAddress(ip, Integer.parseInt(port)));
			} catch (NumberFormatException | IOException e) {
				e.printStackTrace();
				System.out.println("Failed to connect.");
			}
		}
		
		if(socket.isConnected()) {
			try {
				writer = new PrintWriter(socket.getOutputStream());
				reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			} catch (IOException e) {
				e.printStackTrace();
			}
			return true;
		} else {
			return false;
		}
	}

	public void sendMessage(byte[] data) {
		try {
			socket.getOutputStream().write(data);
			socket.getOutputStream().flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sendMessage(String data) {
		writer.print(data);
		writer.flush();
	}
	
	public Socket getSocket() {
		return socket;
	}
	
	public String getServerIP() {
		if(getSocket() != null) {
			if(getSocket().isConnected()) {
				return socket.getInetAddress().getHostAddress();
			} 
		}
		Log.warning("No connection made to a server, returning null.");
		return null;
	}
	
	public boolean isConnected() {
		if(getSocket() != null) {
			return getSocket().isConnected();
		} else {
			Log.warning("Socket is null. Connection#isConnected() returning false."); 
			return false;
		}
	}
	
	/*private PrintWriter getPrintWriter() {
		return writer;
	}
	
	private BufferedReader getBufferedReader() {
		return reader;
	}
	
	private InputStream getInputStream() throws IOException {
		return socket.getInputStream();
	}
	
	private OutputStream getOutputStream() throws IOException {
		return socket.getOutputStream();
	}*/
}
