package Events;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class Event extends JFrame implements ActionListener
{
	private JButton a = new JButton("һ����");
	private JButton b = new JButton("������");
	private JButton c = new JButton("������");
	/*�����¼�Դ��������ť*/
	private JLabel l=new JLabel();
	
	private class WindowCloser extends WindowAdapter{
		public void windowClosing(WindowEvent we)
		{System.exit(0);}
	}
	Event()
	{
		super("ѡ�񹥻�");
		setSize(300,300);
		getContentPane().setBackground(Color.yellow);
		Panel p=new Panel();
		p.setLayout(new FlowLayout());
		setLayout(new BorderLayout());
		add(p,BorderLayout.SOUTH);
		p.add(a);
		p.add(b);
		p.add(c);
		add(l,BorderLayout.CENTER);
		l.setFont(new Font("����",1,15));
		l.setHorizontalAlignment(SwingConstants.CENTER);
		setVisible(true);
		setDefaultCloseOperation(3);
		
		validate();
		a.addActionListener(this);
		b.addActionListener(this);
		c.addActionListener(this);
		addWindowListener(new WindowCloser());
		/*�¼�������*/
	}
	
      public void actionPerformed(ActionEvent e) {
          Object o = e.getSource();
          JButton s = (JButton) o;
          if (o==a)
          {
             l.setText("��ʹ���ˣ�" + s.getText()+","+"�Է��ٶȼ���");
          }
          if (o==b)
          {
             l.setText("��ʹ���ˣ�" + s.getText()+","+"�Է�ѣ��");
          }
          if (o==c)
          {
             l.setText("��ʹ���ˣ�" + s.getText()+","+"�Է��ܵ�10000���˺�");
          }
      }
      /*�¼��������ť�󴥷�������ʾ*/
      
   public static void main(String[] args)
   {
      new Event();
   }
}


