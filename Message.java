package thread3;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import example.StringTool;

public class Message {

	private String reqMessage;
	private String resMessage;

	private boolean logiFlag;
	private boolean crinFlag;
	private boolean logoFlag;

	SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMdd");
	SimpleDateFormat timeFormat = new SimpleDateFormat("HHmmss");
	SimpleDateFormat dealTimeFormat = new SimpleDateFormat("HHmmssss");
	String separate = String.valueOf((char) Integer.parseInt("1C", 16));
	String nowDate = dateFormat.format(Calendar.getInstance().getTime());
	String nowTime = timeFormat.format(Calendar.getInstance().getTime());
	String dealTime = dealTimeFormat.format(Calendar.getInstance().getTime());

	private String logiHatsHeaderReq = separate + "HEADERLOGI00000000TR 0000000000 AFFFFFF";

	private String logiTransactionMessageReq = separate + "LOGI00" + StringTool.getFixStr("ECRD1", 8, " ")
			+ convScrect("1111111", nowDate, nowTime) + "0001" + StringTool.getFixStr("", 16, " ") + nowDate + nowTime
			+ "1.2 " + StringTool.getFixStr("MWARE", 16, " ");

	private String crinHatsHeaderReq = separate + "HEADERCRIN99980001ER 0000000000 BFFFFFF" + separate + "CRIN00"
			+ StringTool.getFixStr("4579528700019808", 19, " ") + dealTime + StringTool.getFixStr("", 26, " ")
			+ String.valueOf((char) Integer.parseInt("0D", 16));

	private String crinTransactionMessageReq = separate + "CRIN01" + StringTool.getFixStr("4579528700019808", 19, " ")
			+ StringTool.getFixStr("REPUBLIC OF CHINA", 26, " ") + "+0000115100000"
			+ "20200603+0000021668000+0000021668000T+000001510000020200630"
			+ StringTool.getFixStr(transCode("A122437806"), 20, " ")
			+ "+000011510000020200603+0000021668000+0000021668000+000001510000020200630"
			+ StringTool.getFixStr("Approved", 40, " ") + String.valueOf((char) Integer.parseInt("0D", 16));

	private String logoHatsHeaderReq = separate + "HEADERLOGO99980001" + StringTool.getFixStr("", 14, " ") + "AFFFFFF"
			+ String.valueOf((char) Integer.parseInt("0D", 16));

	public Message() {

	}

	public String getReqMessage() {
		return reqMessage;
	}

	public void setReqMessage(String reqMessage) {
		this.reqMessage = reqMessage;
	}

	public String getResMessage() {
		return resMessage;
	}

	public void setResMessage(String resMessage) {
		this.resMessage = resMessage;
	}

	public boolean isLogiFlag() {
		return logiFlag;
	}

	public void setLogiFlag(boolean logiFlag) {
		this.logiFlag = logiFlag;
	}

	public boolean isCrinFlag() {
		return crinFlag;
	}

	public void setCrinFlag(boolean crinFlag) {
		this.crinFlag = crinFlag;
	}

	public boolean isLogoFlag() {
		return logoFlag;
	}

	public void setLogoFlag(boolean logoFlag) {
		this.logoFlag = logoFlag;
	}

	public String getLogiHatsHeaderReq() {
		return logiHatsHeaderReq;
	}

	public void setLogiHatsHeaderReq(String logiHatsHeaderReq) {
		this.logiHatsHeaderReq = logiHatsHeaderReq;
	}

	public String getLogiTransactionMessageReq() {
		return logiTransactionMessageReq;
	}

	public void setLogiTransactionMessageReq(String logiTransactionMessageReq) {
		this.logiTransactionMessageReq = logiTransactionMessageReq;
	}

	public String getCrinHatsHeaderReq() {
		return crinHatsHeaderReq;
	}

	public void setCrinHatsHeaderReq(String crinHatsHeaderReq) {
		this.crinHatsHeaderReq = crinHatsHeaderReq;
	}

	public String getCrinTransactionMessageReq() {
		return crinTransactionMessageReq;
	}

	public void setCrinTransactionMessageReq(String crinTransactionMessageReq) {
		this.crinTransactionMessageReq = crinTransactionMessageReq;
	}

	public String getLogoHatsHeaderReq() {
		return logoHatsHeaderReq;
	}

	public void setLogoHatsHeaderReq(String logoHatsHeaderReq) {
		this.logoHatsHeaderReq = logoHatsHeaderReq;
	}

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
