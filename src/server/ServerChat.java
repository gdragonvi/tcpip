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
	Scanner scanner;// ������ ������ �Ǿ�� �����.

	public ServerChat() {
	}

	public ServerChat(int port) {
		try {
			serverSocket = new ServerSocket(port);
			System.out.println("�Ʊ׾�,,������ٸ���,,,");
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
		// ������ ���� client���� ������ ���� �� �ֵ��� ���ش�.
		socket = serverSocket.accept();
		scanner = new Scanner(System.in);

		Receiver receiver = new Receiver();
		receiver.start();

		while (true) {
			Sender sender = new Sender();
			Thread t = new Thread(sender);// throw���ش�.
			// Sender,Thread->�ѹ� ����� ������ ������ �� ���� ����϶�� ������ ������
			System.out.println("���� �޼��� ����³�,,,");
			String msg = scanner.nextLine();// msg -> �ٽ� client���� ���� �ʿ�
			if (msg.equals("q")) {
				scanner.close();
				sender.close();
				break;
			}
			sender.setSendMsg(msg);
			t.start();// �̷��� �ϸ� �ڵ����� sender��� �Լ����� run�� �����Ѵ�.
		}
		System.out.println("������,,,");
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
				System.out.println("�ƽ��Ե� ���������� �ʳ׿�,,,");
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
				String msg=null;
				try {
					msg = din.readUTF();
					System.out.println("client:"+msg);
				} catch (IOException e) {
					System.out.println("���� ������ �������ϴ�,,,");
					break;
					
				}

			}
		}
	}

}
