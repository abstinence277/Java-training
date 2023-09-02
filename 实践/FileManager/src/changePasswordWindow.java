import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class changePasswordWindow extends JFrame {
    private static String CSTR = IP.ip;
    private static final int PORT = 8888;

    changePasswordWindow() {
    	String PASSWORD_REQUIREMENT = "8-10个字符";

        JLabel countLabel = new JLabel("请输入账号");
        JTextField countText = new JTextField(10);
        JPanel c = new JPanel();
        JLabel oldpasswordLabel = new JLabel("请输入原密码");
        JPasswordField oldpasswordText = new JPasswordField(10);
        JPanel op = new JPanel();
        JLabel newpasswordLabel = new JLabel("请输入新密码");
        JPasswordField newpasswordText = new JPasswordField(10);
        JPanel np = new JPanel();
        JButton m = new JButton("确认");
        JButton cancel = new JButton("取消");
        JPanel bt = new JPanel();
        this.setTitle("修改密码");
        this.setBounds(600,300,400,300);

        //账号
        c.add(countLabel, BorderLayout.WEST);
        c.add(countText,BorderLayout.EAST);
        //旧密码
        op.add(oldpasswordLabel,BorderLayout.WEST);
        op.add(oldpasswordText,BorderLayout.EAST);
        //新密码
        np.add(newpasswordLabel,BorderLayout.WEST);
        np.add(newpasswordText,BorderLayout.EAST);
        //按钮
        bt.add(m,BorderLayout.WEST);
        bt.add(cancel,BorderLayout.EAST);
        //
        this.setLayout(new GridLayout(4,1));
        bt.setBackground(new Color(173,216,230));
        c.setBackground(new Color(173,216,230));
        op.setBackground(new Color(173,216,230));
        np.setBackground(new Color(173,216,230));
        this.add(c);
        this.add(op);
        this.add(np);
        this.add(bt);
        c.requestFocus();
        this.setVisible(true);

        //确认
        m.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String Count = countText.getText();
                String oldPW = String.valueOf(oldpasswordText.getPassword());
                String newPW = String.valueOf(newpasswordText.getPassword());
                try {
                    Socket s = new Socket(CSTR,PORT);
                    DataOutputStream o = new DataOutputStream(s.getOutputStream());
                    o.writeUTF("!"+Count+"#"+oldPW+"#"+newPW);
                    DataInputStream i =new DataInputStream(s.getInputStream());
                    String ret = i.readUTF();
                    switch (ret) {
                        case "count nonexistent!" -> new PromptWindow("账号不存在");
                        case "wrong op!" -> new PromptWindow("原密码错误");
                        case "same ps!" -> new PromptWindow("新密码不能与原密码相同");
                        case "password illegal!" -> new PromptWindow("密码不合法");
                        case "change successfully!" -> {
                            new PromptWindow("修改成功");
                            setVisible(false);
                        }
                    }
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });

        //取消
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	setVisible(false);
            }
        });
        //关闭
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        newpasswordText.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                String p = new String(newpasswordText.getPassword()).trim();
                if(p.equals(PASSWORD_REQUIREMENT)){
                    newpasswordText.setText("");
                    newpasswordText.setEchoChar(newpasswordText.getEchoChar());
                    newpasswordText.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                String p =new String(newpasswordText.getPassword()).trim();
                if(p.equals("")){
                    newpasswordText.setEchoChar('\0');
                    newpasswordText.setText(PASSWORD_REQUIREMENT);
                    newpasswordText.setForeground(Color.LIGHT_GRAY);
                }
            }
        });

    }
}
