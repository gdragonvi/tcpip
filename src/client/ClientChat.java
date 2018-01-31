package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

public class ClientChat {
	String ip;
	int port;
	Socket socket;
	Scanner scanner;

	public ClientChat() {

	}

	public ClientChat(String ip, int port) {
		this.ip = ip;
		this.port = port;
		try {
			socket = new Socket(ip, port);
			System.out.println("서버를 연결합니더,,");
			start();// 연결이 되야지 진행을 시킨다.
		} catch (IOException e) {
			System.out.println("네트워크가 불안정해서 연결이 실패하였슴더,,");
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// 테스트 해주고 싶을 경우에 start()를 이 곳에 넣어줍니당.
	}

	public void start() throws IOException {
		scanner = new Scanner(System.in);// 스캐너를 읽어들인다.

		Receiver receiver = new Receiver();
		receiver.start();// 이렇게 두줄을 선언해주어야 받는 것이 준비된다.

		while (true) {
			Sender sender = new Sender();
			Thread t = new Thread(sender);// throw해준다.

			System.out.println("행님,,, 메세지를 입력해주십쇼,,,");
			String msg = scanner.nextLine();
			if (msg.equals("q")) {
				scanner.close();
				sender.close();
				break;// 더 이상 키인 받지 않을 때 스캐너, while 루프를 종료시킨다.
			}

			sender.setSendMsg(msg);
			t.start();// 이렇게 하면 자동으로 sender라는 함수안의 run이 동작한다.
		}
		System.out.println("시스템이 끝났습더 행님,,,");
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
					dout.writeUTF(msg);// OutputStream에 쏘라는 말
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
				String msg;
				try {
					msg = din.readUTF();
					System.out.println(msg);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}
	}
}
