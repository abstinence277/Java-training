package Events;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class Event extends JFrame implements ActionListener
{
	private JButton a = new JButton("一技能");
	private JButton b = new JButton("二技能");
	private JButton c = new JButton("三技能");
	/*设置事件源：三个按钮*/
	private JLabel l=new JLabel();
	
	private class WindowCloser extends WindowAdapter{
		public void windowClosing(WindowEvent we)
		{System.exit(0);}
	}
	Event()
	{
		super("选择攻击");
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
		l.setFont(new Font("宋体",1,15));
		l.setHorizontalAlignment(SwingConstants.CENTER);
		setVisible(true);
		setDefaultCloseOperation(3);
		
		validate();
		a.addActionListener(this);
		b.addActionListener(this);
		c.addActionListener(this);
		addWindowListener(new WindowCloser());
		/*事件监听器*/
	}
	
      public void actionPerformed(ActionEvent e) {
          Object o = e.getSource();
          JButton s = (JButton) o;
          if (o==a)
          {
             l.setText("您使用了：" + s.getText()+","+"对方速度减缓");
          }
          if (o==b)
          {
             l.setText("您使用了：" + s.getText()+","+"对方眩晕");
          }
          if (o==c)
          {
             l.setText("您使用了：" + s.getText()+","+"对方受到10000点伤害");
          }
      }
      /*事件，点击按钮后触发文字提示*/
      
   public static void main(String[] args)
   {
      new Event();
   }
}


