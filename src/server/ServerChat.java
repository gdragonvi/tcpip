package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class ServerChat {
	ServerSocket serverSocket;
	Scanner scanner;
	ArrayList<DataOutputStream> list;
	Socket socket;
	
	String userip;
	public ServerChat() {
	}

	public ServerChat(int port) {
		try {
			serverSocket = new ServerSocket(port);
			System.out.println("Ready Server ...");
			list = new ArrayList<>();
			start();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				socket.close();
			}catch(IOException e) {
				e.printStackTrace();
			}
		}

	}

	private void start() throws IOException {
		while (true) {
			System.out.println("Server Ready ....");
			Socket socket = serverSocket.accept();
			System.out.println(socket.getInetAddress()+" Connected..");
			Receiver receiver = new Receiver(socket);
			receiver.start();
		}
	}

	private void sendAllMsg(String msg) throws IOException {
		Sender sender = new Sender();
		Thread t = new Thread(sender);
		sender.setSendMsg(msg);
		t.start();
	}
	
	
	
	// Message Sender .....................................
	class Sender implements Runnable {
		
		String msg;

		public Sender() {
		}

		public void setSendMsg(String msg) {
			this.msg = msg;
		}

		@Override
		public void run() {
			try {
				if(list.size() != 0) {
					for(DataOutputStream dout:list) {
						dout.writeUTF(msg);
					}
				}
			} catch (IOException e) {
				System.out.println("Not Available");
			}
		}

	}

	// Message Receiver .....................................
	class Receiver extends Thread {
		InputStream in;
		DataInputStream din;
		OutputStream out;
		DataOutputStream dout;
		
		Socket socket;
		public Receiver(Socket socket) throws IOException {
			this.socket = socket;
			in = socket.getInputStream();
			din = new DataInputStream(in);
			out = socket.getOutputStream();
			dout = new DataOutputStream(out);
			list.add(dout);		
			System.out.println("立加磊 荐:"+list.size());
		}

		public void close() throws IOException {
			in.close();
			din.close();
		}

		@Override
		public void run() {
			while (true) {
				String msg = null;
				try {
					msg = din.readUTF();
					System.out.println("client:"+msg);
					sendAllMsg(msg);
				} catch (IOException e) {
					int temp = list.size()-1;
					System.out.println("Exit Client User ...");
					System.out.println("立加磊 荐:"+temp);
					break;				
				}

			}
		}
	}

}






