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
			System.out.println("������ �����մϴ�,,");
			start();// ������ �Ǿ��� ������ ��Ų��.
		} catch (IOException e) {
			System.out.println("��Ʈ��ũ�� �Ҿ����ؼ� ������ �����Ͽ�����,,");
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// �׽�Ʈ ���ְ� ���� ��쿡 start()�� �� ���� �־��ݴϴ�.
	}

	public void start() throws IOException {
		scanner = new Scanner(System.in);// ��ĳ�ʸ� �о���δ�.

		Receiver receiver = new Receiver();
		receiver.start();// �̷��� ������ �������־�� �޴� ���� �غ�ȴ�.

		while (true) {
			Sender sender = new Sender();
			Thread t = new Thread(sender);// throw���ش�.

			System.out.println("���,,, �޼����� �Է����ֽʼ�,,,");
			String msg = scanner.nextLine();
			if (msg.equals("q")) {
				scanner.close();
				sender.close();
				break;// �� �̻� Ű�� ���� ���� �� ��ĳ��, while ������ �����Ų��.
			}

			sender.setSendMsg(msg);
			t.start();// �̷��� �ϸ� �ڵ����� sender��� �Լ����� run�� �����Ѵ�.
		}
		System.out.println("�ý����� �������� ���,,,");
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
					dout.writeUTF(msg);// OutputStream�� ���� ��
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
			// ready����
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
