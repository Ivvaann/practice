package thread3;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import example.StringTool;

public class Message {

	private String reqMsg;
	private String respMsg;

	private String message;
	private boolean logiFlag;
	private boolean crinHeaderFlag;
	private boolean crinFlag;
	private boolean logoFlag;

	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMdd");
	private SimpleDateFormat timeFormat = new SimpleDateFormat("HHmmss");
	private SimpleDateFormat dealTimeFormat = new SimpleDateFormat("HHmmssSSS");
	private String nowDate = dateFormat.format(Calendar.getInstance().getTime());
	private String nowTime = timeFormat.format(Calendar.getInstance().getTime());
	private String dealTime = dealTimeFormat.format(Calendar.getInstance().getTime());
	
	private String newCardLimit = "0000135000000";
	private String newCardLimitExpiryDate = "20200620";

	private String separateChar = String.valueOf((char) Integer.parseInt("1C", 16));
	private String endFlag = String.valueOf((char) Integer.parseInt("0D", 16));

	
	private String logiHatsHeaderReq = separateChar + "HEADERLOGI00000000TR 0000000000 AFFFFFF";
	private String logiTransactionMessageReq = separateChar + "LOGI00" + StringTool.getFixStr("ECRD1", 8, " ")
			+ convScrect("1111111", nowDate, nowTime) + "0001" + StringTool.getFixStr("", 16, " ") + nowDate + nowTime
			+ "1.2 " + StringTool.getFixStr("MWARE", 16, " ") + endFlag;

	private String crinHatsHeaderReq = separateChar + "HEADERCRIN99980001ER 0000000000 BFFFFFF" + separateChar
			+ "CRIN00" + StringTool.getFixStr("3560568200055202", 19, " ") + dealTime.substring(0, 8)
			+ StringTool.getFixStr("", 26, " ") + endFlag;


	private String logoHatsHeaderReq = separateChar + "HEADERLOGO99980001" + StringTool.getFixStr("", 14, " ")
			+ "AFFFFFF" + String.valueOf((char) Integer.parseInt("0D", 16));

	public String getReqMsg() { // for writer use
		return reqMsg;
	}

	// public String setReqMsg(String ) {
	//
	// }

	public void setRespMsg(String resp) {
		this.respMsg = resp;
	}

	public String getRespMsg() {
		return this.respMsg;
	}

	public String getMessage() {
//		while (null == message) {
//			try {
//				System.out.println("message null and wait ~~~~~");
//				wait();
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		}
		return this.message;
	}

	public void setMessage(String message) {
		this.message = message;
//		if (null != this.message) {
//			notify();
//		}
	}

	public synchronized boolean isLogiFlag() throws InterruptedException {
		if(!logiFlag){
			wait();
		}
		return logiFlag;
	}

	public synchronized void setLogiFlag(boolean logiFlag) {
		this.logiFlag = logiFlag;
		if (logiFlag)
			notify();
	}

	public synchronized boolean isCrinFlag() throws InterruptedException {
		if(!crinFlag){
			wait();
		}
		return crinFlag;
	}

	public synchronized void setCrinFlag(boolean crinFlag) {
		this.crinFlag = crinFlag;
		if (crinFlag) {
			notify();
		}
	}

	public synchronized boolean isCrinHeaderFlag() throws InterruptedException {
		if(!crinHeaderFlag){
			wait();
		}
		return crinHeaderFlag;
	}

	public synchronized void setCrinHeaderFlag(boolean crinHeaderFlag) {
		this.crinHeaderFlag = crinHeaderFlag;
		if(crinHeaderFlag)
			notify();
	}

	public boolean isLogoFlag() {
		return logoFlag;
	}

	public synchronized void setLogoFlag(boolean logoFlag) {
		this.logoFlag = logoFlag;
		if(logoFlag){
			notify();
		}
	}

	public String getLogiHatsHeaderReq() {
		return logiHatsHeaderReq;
	}

	public String getLogiTransactionMessageReq() {
		return logiTransactionMessageReq;
	}

	public String getCrinHatsHeaderReq() {
		return crinHatsHeaderReq;
	}


	public String getCrinTransactionMessageReq(String crinHeaderRes) {
		StringBuilder crin01 = new StringBuilder();
		crinHeaderRes = crinHeaderRes.replace("ER ", "EU ");
		crin01.append(crinHeaderRes.substring(0, crinHeaderRes.lastIndexOf("CRIN") - 35))
				.append(dealTimeFormat.format(Calendar.getInstance().getTime()).subSequence(0, 8))
				.append(StringTool.getFixStr("", 26, " ")).append(separateChar)
				.append(crinHeaderRes.substring(crinHeaderRes.lastIndexOf("CRIN"), crinHeaderRes.indexOf("T+") + 2))
				.append(newCardLimit).append(newCardLimitExpiryDate);
		crin01.append(crinHeaderRes.substring(crin01.length(), crinHeaderRes.indexOf("Approved") - 21))
				.append(newCardLimit).append(newCardLimitExpiryDate).append(StringTool.getFixStr("Approved", 40, " "))
				.append(String.valueOf((char) Integer.parseInt("0D", 16)));

		return crin01.toString();
	}


	public String getLogoHatsHeaderReq() {
		return logoHatsHeaderReq;
	}

	/**
	 * password encode
	 * @param plainText(password)
	 * @param v1
	 * @param v2
	 * @return
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
