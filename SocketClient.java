package com.uitc.common.test;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class SocketClient extends Thread {
	protected static int port = 6989;

	public SocketClient() {
		System.out.println("Client side START !!");
	}

	public void run() {
		Socket client = null;
		String host = "127.0.0.1";
		BufferedReader brReader = null;
		PrintStream ps = null;
		OutputStream ost = null;
		DataOutputStream dos = null;

		try {
			client = new Socket(host, port);
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		try {
			while (true) {
				// client output
				System.out.println("Please key in: ");
				brReader = new BufferedReader(new InputStreamReader(System.in));
				ost = client.getOutputStream();
				String inputStr = brReader.readLine();
//				ps = new PrintStream(ost);
//				ps.println(inputStr);
				dos = new DataOutputStream(ost);
				dos.writeUTF(inputStr);
				dos.flush();

				
				// client input
				BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
				String readLine = br.readLine();
				System.out.println("Message by Server: ");
				System.out.println(readLine);
				if ("Append start END Append end".equals(readLine.trim())) {
					client.shutdownInput();
					client.shutdownOutput();
					dos.close();
					ost.close();
					client.close();
					client = null;
					break;
				}
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Socket Failed !!");
			e.printStackTrace();
		} finally {
			System.out.println("Client side END !!");
		}

	}

	public static void main(String args[]) {
		new SocketClient().start();
	}
}
