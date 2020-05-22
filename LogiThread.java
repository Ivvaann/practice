package thread3;

import java.io.IOException;
import java.io.OutputStream;

public class LogiThread implements Runnable {

	private Message msg;
	private OutputStream output;

	public LogiThread(Message message, OutputStream output) {
		this.msg = message;
		this.output = output;
	}

	@Override
	public void run() {

		synchronized (this) {
			try {
				wait(1000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			
			if (msg.isLogiFlag()) {
				try {
					output.write("CATUCOS\r\n".getBytes());
					output.flush();

					System.out.println("=========== send logi request ===========\n");
					output.write((msg.getLogiHatsHeaderReq() + msg.getLogiTransactionMessageReq() + "\r\n").getBytes());
					output.flush();

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
