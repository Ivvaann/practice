package thread3;

import java.io.IOException;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.net.telnet.TelnetClient;

import example.StringTool;

public class TxnService {

	public TelnetClient telnet;
	public Message message;

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

	/*
	 * public void login(OutputStream output) { try {
	 * output.write("CATUCOS\r\n".getBytes()); output.flush();
	 * 
	 * System.out.println("=========== send logi request ===========\n");
	 * output.write((logiMsg.getReqMessage() + "\r\n").getBytes());
	 * output.flush(); } catch (IOException e) { e.printStackTrace(); } }
	 */

	public void sendTxn(TelnetClient socket) {
		ExecutorService service = Executors.newCachedThreadPool();
		service.execute(new ReaderThread(message, socket.getInputStream()));
		service.execute(new WriterThread(message, socket.getOutputStream()));
		
//		Thread reader = new Thread(new ReaderThread(message, socket.getInputStream()));
//		Thread writer = new Thread(new WriterThread(message, socket.getOutputStream()));
//		reader.start();
//		writer.start();
	}

	public static void main(String[] args) {
		TxnService tx = new TxnService(new Message());
		tx.doTxn();

//		while(!Thread.interrupted()){
//			if(tx.message.isLogoFlag()){
//				try {
//					tx.telnet.disconnect();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//		}
	}
}
