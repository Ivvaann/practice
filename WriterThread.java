package thread3;

import java.io.IOException;
import java.io.OutputStream;
import example.StringTool;

public class WriterThread implements Runnable {
	private Message message;
	private OutputStream output;
	private boolean LOGIN_STATUS, CRIN_HEADER_STATUS, CRIN_STATUS;

	public WriterThread(Message message, OutputStream output) {
		this.message = message;
		this.output = output;
	}

	@Override
	public void run() {
		synchronized(message){
			
		
		try {
			while (!Thread.interrupted()) { // && (ch = System.in.read()) != -1
				try { // wait for serivce ready, and send CATUCOS
					
					if (message.isLogiFlag() && !LOGIN_STATUS) {
						output.write("CATUCOS\r\n".getBytes());
						output.flush();
//							Thread.sleep(1000);
						
						System.out.println("=========== send logi request ===========\n");
						System.out.println("To Host:(LOGI request)\n" + message.getLogiHatsHeaderReq()
						+ message.getLogiTransactionMessageReq());
						output.write(
								(message.getLogiHatsHeaderReq() + message.getLogiTransactionMessageReq()).getBytes());
						output.flush();
						Thread.sleep(500);
						LOGIN_STATUS = true;
						message.notify();
					}
					
					if (message.isCrinHeaderFlag() && !CRIN_HEADER_STATUS) {
						System.out.println("=========== send crin header request ===========\n");
						System.out.println("To Host:(CRIN HEADER)\n" + message.getCrinHatsHeaderReq());
						output.write(message.getCrinHatsHeaderReq().getBytes());
						output.flush();
						Thread.sleep(1000);
						CRIN_HEADER_STATUS = true;
						message.notify();
					}
					
					/**
					 * 
					 * ) 收CRIN_Transaction Message_Request 
					 * ) 送CRIN_Transaction Message_Request
					 */
					if (message.isCrinFlag() && !CRIN_STATUS) {
						System.out.println("=========== send crin request ===========\n");
						
						String crin01 = message.getCrinTransactionMessageReq(message.getMessage());
						System.out.println("To Host:(CrinTxnReq)\n" + crin01);
						
//							output.write(crin01.substring(1).getBytes());
						output.write(crin01.replace(String.valueOf((char) Integer.parseInt("03", 16)), "").getBytes());
						output.flush();
						Thread.sleep(2000);
						CRIN_STATUS = true;
						message.notify();
					}
					
					if (message.isLogoFlag() && CRIN_STATUS) {
						System.out.println("=========== send logo request ===========\n");
						System.out.println("To Host:(LOGO request)\n" + message.getLogoHatsHeaderReq());
						output.write((message.getLogoHatsHeaderReq()).getBytes());
						output.flush();
						break;
					}
					
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			// System.out.println("finish!!");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	}

	/**
	 * 
	 * @param trans
	 *            password
	 * @param nowDate
	 * @param nowTime
	 * @return String
	 */
	public static String convScrect(String plainText, String v1, String v2) {
		int[] date_buff = new int[3];
		int[] time_buff = new int[3];
		int[] num_buff = new int[8];
		int[] presult_buff = new int[8];
		char[] pwd_buff = new char[8];

		for (int i = 0; i < plainText.length(); i++) {
			pwd_buff[i] = plainText.charAt(i);
		}

		for (int i = plainText.length(); i <= plainText.length(); i++) {
			pwd_buff[i] = 32;
		}

		int j = 1;
		for (int i = 0; i <= 2; i++) {
			date_buff[i] = Integer.valueOf((v1.substring(j, j + 1))) + 1;
			time_buff[i] = Integer.valueOf((v2.substring(j, j + 1))) + 1;
			j = j + 2;
		}
		num_buff[0] = time_buff[2] * 3;
		num_buff[1] = date_buff[2] * 5;
		num_buff[2] = time_buff[1] * 4;
		num_buff[3] = time_buff[2] * 6;
		num_buff[4] = date_buff[1] * 5;
		num_buff[5] = time_buff[0] * 2;
		num_buff[6] = date_buff[0] * 3;
		num_buff[7] = time_buff[2] * 7;

		for (int i = 0; i <= 7; i++) {
			presult_buff[i] = (int) pwd_buff[i] + num_buff[i];
			if (presult_buff[i] > 127) {
				presult_buff[i] = presult_buff[i] - 96;
			}
		}
		StringBuilder encry_pwd = new StringBuilder();
		for (int i = 0; i <= 7; i++) {
			encry_pwd.append((char) presult_buff[i]);
		}
		return encry_pwd.toString();
	}

	/**
	 * 
	 * @param id
	 *            (ex: A123456789)
	 * @return String
	 */
	public static String transCode(String id) {
		StringBuffer sb = new StringBuffer();
		int firstChar = id.charAt(0);
		if (firstChar < 65 || firstChar > 90) {
			throw new RuntimeException("ERROR: id first char wrong.");
		}

		if (firstChar == 73) {// I
			firstChar = 34;
		} else if (firstChar == 79) {// O
			firstChar = 35;
		} else if (firstChar == 88 || firstChar == 89) {// X、Y
			firstChar -= 58;
		} else if ((firstChar >= 65 && firstChar <= 72) || firstChar == 87) {// A~H、W
			firstChar -= 55;
		} else if (firstChar >= 74 && firstChar <= 78) {// J~N
			firstChar -= 56;
		} else {
			firstChar -= 57;
		}

		return sb.append("000").append(String.valueOf(firstChar)).append(id.substring(1)).append("00").toString();
	}
}
