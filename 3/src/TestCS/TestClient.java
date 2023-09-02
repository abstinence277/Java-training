package TestCS;

import java.io.*;
import java.net.*;

public class TestClient {
	public static void main(String[] args) {
		Socket socket=null;
		String s=null;
		DataInputStream in=null;
		DataOutputStream out=null;
		/*定义数据成员*/
		
		try {
			socket=new Socket("127.0.0.1",8888);//客户端创建Socket，发起连接
			
			in=new DataInputStream(socket.getInputStream());
			out=new DataOutputStream(socket.getOutputStream());
			/*建立数据输入流和数据输出流*/
			
			while(true) {
				s=in.readUTF();
				if(s!=null) break;
			}
			/*通过数据输入流读取服务器放在线路的信息*/
			
			out.writeUTF("你好，我是客户机!");//向服务端发送信息
			socket.close();//关闭Socket
		}
		catch(IOException e1) {s="无法连接";}
		System.out.println("客户机收到:"+s);
		System.exit(0);
	}

}
