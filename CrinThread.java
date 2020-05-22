package thread3;

import java.io.IOException;
import java.io.OutputStream;

public class CrinThread implements Runnable {

	private Message msg;
	private OutputStream output;

	public CrinThread(Message message, OutputStream output) {
		this.msg = message;
		this.output = output;
	}

	@Override
	public void run() {

		synchronized (this) {
			try {
				wait(2000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			if (msg.isCrinFlag()) {
				System.out.println("=========== send crin request ===========\n");
				try {
					output.write((msg.getCrinHatsHeaderReq() + "\r\n").getBytes());
					output.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
