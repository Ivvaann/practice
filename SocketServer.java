package com.uitc.common.test;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer extends Thread {
	private Socket socket = null;
	protected static int port = 6989;

	public SocketServer(Socket skt) {
		this.socket = skt;
	}

	public void run() {
		OutputStream ost = null;
		DataInputStream dis = null;
		PrintStream ps = null;
		try {
			while (true) {
				ost = socket.getOutputStream();
				dis = new DataInputStream(socket.getInputStream());
				
				// server input
				//======= option[1]: for PrintStream =======
//				BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//				String readLine = br.readLine();
				
				//======= option[2]: for DataOutputStream but lock at line 42 =======
//				BufferedInputStream in = new BufferedInputStream(dis);
//				String readLine = "";
//				byte[] b = new byte[1024];
//				int len;
//				while ((len = in.read(b)) > 0) {
//					readLine += new String(b, 0, len);
//				}
//				in.close();
//				in = null;
				
				//======= option[3] for DataOutputStream but lock at line 50 =======
//				BufferedReader br = new BufferedReader(new InputStreamReader(dis));
//				String readLine = br.readLine();
				
				String readLine = dis.readUTF();
				System.out.println("Message by Client: ");
				System.out.println(readLine);
			
				// server output
				ps = new PrintStream(ost);
				ps.println("Append start " + readLine + " Append end");
				
				if ("END".equals(readLine.trim())) {
					socket.shutdownInput();
					socket.shutdownOutput();
					ost.close();
					dis.close();
					ps.close();
					socket.close();
					socket = null;
					break;
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			System.out.println("Server side END !!");
		}

	}

	public static void main(String args[]) {
		ServerSocket server = null;
		Socket skt = null;
		try {
			server = new ServerSocket(port);
			System.out.println("Server side START !!");

			while (true) {
				skt = server.accept();
				SocketServer thread = new SocketServer(skt);
				thread.start();

				System.out.println("Server get connect from: " + skt.getInetAddress());
				System.out.println("Server start listening ~~~");
			}
		} catch (IOException e) {
			e.printStackTrace();				
		}
	}

}
