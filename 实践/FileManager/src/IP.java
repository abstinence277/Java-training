import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class IP  extends JFrame{
	
    public static String ip = "";
    IP(){
    	JLabel jl = new JLabel("输入服务器IP:");
        JTextField jf = new JTextField(5);
        JButton jb = new JButton("确认");
        JPanel jp = new JPanel();
        try {
        	jp.setBackground(new Color(173,216,230));
            jl.setBackground(new Color(255,255,153));
            jb.setBackground(new Color(255,255,153));
            String s = InetAddress.getLocalHost().toString().split("/")[1];
            String[] m = s.split("\\.");
            for (int i = 0; i < 3; i++) {
                ip += m[i];
                ip += ".";
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }



        this.setBounds(600,200,200,100);
        jp.add(jl);
        jp.add(jf);
        this.add(jp,BorderLayout.CENTER);
        this.add(jb,BorderLayout.SOUTH);
        this.setDefaultCloseOperation(3);
        this.setVisible(true);
        this.getRootPane().setDefaultButton(jb);
        jb.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String s = jf.getText();
                if(s.equals(""))return ;
                ip += s;
                setVisible(false);
                new loginWindow();
            }
        });

    }

    public static void main(String[] args) {
        new IP();
    }
}
