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
		/*�������ݳ�Ա*/
		
		try {ssocket1=new ServerSocket(8888);}//�������˴���ServeSocket�����ý������ӵĶ˿ں�
		catch(IOException e) {System.out.println("�������ӳ���");}
		/*�����쳣�¼�*/
		
		try {
			socket1=ssocket1.accept();//�ȴ��ͻ��˴��������ź�
			
			in=new DataInputStream(socket1.getInputStream());
			out=new DataOutputStream(socket1.getOutputStream());
			/*�������������������������*/
			
			out.writeUTF("��ã����Ƿ�����!");//ͨ���������ͻ���������Ϣ
			
			while(true) {
				s=in.readUTF();
				if(s!=null) break;
			}
			/*��ȡ�ͻ������͵���Ϣ*/
			
			System.out.println("�������յ�:"+s);//��ʾ���յ�����Ϣ
			socket1.close();//�ر�ServeSocket
		}
		catch(IOException e1) {System.out.println("��·��д����");}
	}
}
