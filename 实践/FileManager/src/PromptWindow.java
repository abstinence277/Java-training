import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PromptWindow extends JFrame {

    PromptWindow(String s){
    	JButton b = new JButton("确认");
    	JPanel a = new JPanel();
    	JPanel aa = new JPanel();
        this.setTitle("提示");
        this.setBounds(550,250,200,100);
        this.add(a);
        this.add(aa,BorderLayout.SOUTH);
        a.setBackground(new Color(173,216,230));
        aa.setBackground(new Color(173,216,230));
        JLabel j = new JLabel(s);
        a.add(j);
        b.setBackground(Color.white);
        aa.add(b,BorderLayout.SOUTH);
        this.setDefaultCloseOperation(1);
        setVisible(true);
        this.getRootPane().setDefaultButton(b);
        b.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });
    }
}
