package TestCS;

import java.io.*;
import java.net.*;

public class TestClient {
	public static void main(String[] args) {
		Socket socket=null;
		String s=null;
		DataInputStream in=null;
		DataOutputStream out=null;
		/*�������ݳ�Ա*/
		
		try {
			socket=new Socket("127.0.0.1",8888);//�ͻ��˴���Socket����������
			
			in=new DataInputStream(socket.getInputStream());
			out=new DataOutputStream(socket.getOutputStream());
			/*�������������������������*/
			
			while(true) {
				s=in.readUTF();
				if(s!=null) break;
			}
			/*ͨ��������������ȡ������������·����Ϣ*/
			
			out.writeUTF("��ã����ǿͻ���!");//�����˷�����Ϣ
			socket.close();//�ر�Socket
		}
		catch(IOException e1) {s="�޷�����";}
		System.out.println("�ͻ����յ�:"+s);
		System.exit(0);
	}

}
