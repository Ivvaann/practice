package thread3;

import java.io.IOException;
import java.net.SocketException;

import org.apache.commons.net.telnet.TelnetClient;
import thread3.Message;

public class TxnService {

	private TelnetClient telnet;
	private Message message;

	public TxnService(Message message) {
		this.message = message;
	}

	public void doTxn() {
		// step1 get socket connect
		TelnetClient socket = getConnect();

		// step2 crin
		sendTxn(socket);
		// step3 check timpout and logo
	}

	public TelnetClient getConnect() {
		if (null == telnet) {
			telnet = new TelnetClient();
			try {
				telnet.connect("192.168.110.93", 23);

			} catch (SocketException e) {
				System.out.println("connect fail");
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return telnet;
	}

	public void sendTxn(TelnetClient socket) {

		Thread reader = new Thread(new ReadThread(message, socket.getInputStream()));
		Thread logiThread = new Thread(new LogiThread(message, socket.getOutputStream()));
		Thread crinThread = new Thread(new CrinThread(message, socket.getOutputStream()));
		Thread crinThread2 = new Thread(new CrinThread(message, socket.getOutputStream()));
		Thread crinThread3 = new Thread(new CrinThread(message, socket.getOutputStream()));
		Thread logoThread = new Thread(new LogoThread(message, socket.getOutputStream()));

		reader.start();
		logiThread.start();
		crinThread.start();
		crinThread2.start();
		crinThread3.start();
		logoThread.start();

		if (message.isLogoFlag()) {
			try {
				socket.disconnect();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		TxnService tx = new TxnService(new Message());
		tx.doTxn();
	}

}
