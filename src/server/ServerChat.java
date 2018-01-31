package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class ServerChat {
	ServerSocket serverSocket;
	Socket socket;
	Scanner scanner;// 소켓이 연결이 되어야 만든다.

	public ServerChat() {
	}

	public ServerChat(int port) {
		try {
			serverSocket = new ServerSocket(port);
			System.out.println("아그야,,서버기다린다,,,");
			start();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void start() throws IOException {
		// 소켓을 만들어서 client에서 소켓이 들어올 수 있도록 해준다.
		socket = serverSocket.accept();
		scanner = new Scanner(System.in);

		Receiver receiver = new Receiver();
		receiver.start();

		while (true) {
			Sender sender = new Sender();
			Thread t = new Thread(sender);// throw해준다.
			// Sender,Thread->한번 만들고 데이터 전송할 때 마다 사용하라고 밖으로 꺼내줌
			System.out.println("서버 메세지 갖고온나,,,");
			String msg = scanner.nextLine();// msg -> 다시 client에게 전송 필요
			if (msg.equals("q")) {
				scanner.close();
				sender.close();
				break;
			}
			sender.setSendMsg(msg);
			t.start();// 이렇게 하면 자동으로 sender라는 함수안의 run이 동작한다.
		}
		System.out.println("끝났다,,,");
	}

	// Message Sender ...............................................
	class Sender implements Runnable {
		OutputStream out;
		DataOutputStream dout;
		String msg;

		public Sender() throws IOException {
			out = socket.getOutputStream();
			dout = new DataOutputStream(out);
		}

		public void setSendMsg(String msg) {
			this.msg = msg;
		}

		public void close() throws IOException {
			dout.close();
			out.close();
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				if (dout != null) {
					dout.writeUTF(msg);
				}
			} catch (IOException e) {
				System.out.println("아쉽게도 존재하지가 않네요,,,");
			}
		}

	}

	class Receiver extends Thread {
		InputStream in;
		DataInputStream din;

		public Receiver() throws IOException {
			in = socket.getInputStream();
			din = new DataInputStream(in);
		}

		public void close() throws IOException {
			in.close();
			din.close();
		}

		@Override
		public void run() {
			// ready구현
			while (true) {
				String msg=null;
				try {
					msg = din.readUTF();
					System.out.println("client:"+msg);
				} catch (IOException e) {
					System.out.println("서버 유저가 나갔습니다,,,");
					break;
					
				}

			}
		}
	}

}
