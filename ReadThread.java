package thread3;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ReadThread implements Runnable {

	private Message msg;
	private InputStream input;
	private BufferedInputStream bis;

	public ReadThread(Message message, InputStream input) {
		this.msg = message;
		this.input = input;
		bis = new BufferedInputStream(this.input);
	}

	@Override
	public void run() {
		try {
			StringBuffer sb = new StringBuffer();
			char ch = (char) bis.read();

			while (true) {
				synchronized (this) {
					sb.append(ch);
					ch = (char) bis.read();
					if (bis.available() == 0) {
						setFlagStatus(sb);
						// message.setMessage(sb.toString());
						System.out.println(sb.toString());
						if (sb.toString().contains("HEADERLOGO")) {
							sb.delete(0, sb.length());
							break;
						} else {
							sb.delete(0, sb.length());
						}
						notifyAll();
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		} finally {
			try {
				bis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void setFlagStatus(StringBuffer sb) {
		if (sb.toString().contains("Choice")) {
			msg.setLogiFlag(true);
		} else if (sb.toString().contains("HEADERLOGI")) {
			msg.setCrinFlag(true);

		} else if (sb.toString().contains("HEADERCRIN")) {
			msg.setLogoFlag(true);

		} else if (sb.toString().contains("HEADERLOGO")) {
			// message.setLogoFlag(true);
		}
	}
}
