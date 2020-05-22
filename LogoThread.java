package thread3;

import java.io.IOException;
import java.io.OutputStream;

public class LogoThread implements Runnable {

	private Message msg;
	private OutputStream output;

	public LogoThread(Message message, OutputStream output) {
		this.msg = message;
		this.output = output;
	}

	@Override
	public void run() {
		long startTime = System.currentTimeMillis();
		int t = 0;
		int timeOut = 10000;
		int timeStep = 100;

		synchronized (this) {
			while (t <= timeOut) {
				try {
					Thread.sleep(timeStep);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				t += timeStep;
			}
			if (t > timeStep) {
				System.out.println("time: " + (System.currentTimeMillis() - startTime));
				System.out.println("=========== send logo request ===========\n");
				try {
					output.write((msg.getLogoHatsHeaderReq() + "\r\n").getBytes());
					output.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}
	}
}
