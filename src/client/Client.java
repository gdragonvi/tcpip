package client;

import java.net.Socket;

public class Client {
	
	public static void main(String[] args) {
		String ip = "70.12.111.142";
		int port = 8888;
		ClientChat chat = null;
		chat = new ClientChat(ip,port);
		
}
}