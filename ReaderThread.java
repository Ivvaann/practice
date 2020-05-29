package thread3;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ReaderThread implements Runnable {
	private Message message;
	private InputStream input;
	private BufferedInputStream bis;

	public ReaderThread(Message message, InputStream input) {
		this.message = message;
		this.input = input;
		this.bis = new BufferedInputStream(this.input);
	}

	@Override
	public void run() {
		synchronized(message){
			
		
		try {
			StringBuffer sb = new StringBuffer();
			char ch = (char) bis.read();

			while (true) {
				sb.append(ch);

				ch = (char) bis.read();
				if (bis.available() == 0) {
					setFlagStatus(sb);
					System.out.println("From Host:\n" + sb.toString());
					message.setMessage(sb.toString());
					
					if (sb.toString().contains("HEADERLOGO")) {
						sb.delete(0, sb.length());
						break;
					} else {
						sb.delete(0, sb.length());
					}
					try {
						message.wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
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
	}

	public void setFlagStatus(StringBuffer sb) {
		if (sb.toString().contains("Choice")) {
			message.setLogiFlag(true);
			
		} else if (sb.toString().contains("HEADERLOGI")) {
			message.setCrinHeaderFlag(true);
			
		} else if (sb.toString().contains("HEADERCRIN") && sb.toString().contains("Approved")) {
			message.setCrinFlag(true);

		} else if (sb.toString().contains("HEADERCRIN") && sb.toString().contains("successful")) {
			message.setLogoFlag(true);

			// } else if (sb.toString().contains("HEADERLOGO")) {
			// message.setLogoFlag(true);
		} else {
			message.setLogoFlag(true);
		}
	}

}
