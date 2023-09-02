package TestCS;

import java.io.*;
import java.net.*;

public class TestServer {
	public static void main(String[] args) {
		ServerSocket ssocket1=null;
		Socket socket1=null;
		String s=null;
		DataInputStream in=null;
		DataOutputStream out=null;
		/*定义数据成员*/
		
		try {ssocket1=new ServerSocket(8888);}//服务器端创建ServeSocket，设置建立连接的端口号
		catch(IOException e) {System.out.println("建立连接出错");}
		/*处理异常事件*/
		
		try {
			socket1=ssocket1.accept();//等待客户端传来连接信号
			
			in=new DataInputStream(socket1.getInputStream());
			out=new DataOutputStream(socket1.getOutputStream());
			/*建立数据输入流和数据输出流*/
			
			out.writeUTF("你好，我是服务器!");//通过输出流向客户机发送信息
			
			while(true) {
				s=in.readUTF();
				if(s!=null) break;
			}
			/*读取客户机传送的信息*/
			
			System.out.println("服务器收到:"+s);//显示接收到的信息
			socket1.close();//关闭ServeSocket
		}
		catch(IOException e1) {System.out.println("线路读写错误");}
	}
}
