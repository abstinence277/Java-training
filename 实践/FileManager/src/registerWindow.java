import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class registerWindow extends JFrame {
    private static String CSTR = "127.0.0.1";
    private static final int PORT = 8888;

    public registerWindow() {
    	String COUNT_REQUIREMENT = "4-10个字符";
        String PASSWORD_REQUIREMENT = "8-10个字符";

        //账号
        JPanel c = new JPanel();
        JLabel countLabel = new JLabel("  账号");
        JTextField countText = new JTextField(10);
        //密码
        JPanel p = new JPanel();
        JLabel passwordLabel = new JLabel("  密码");
        JPasswordField passwordText = new JPasswordField(10);
        char defaultChar = passwordText.getEchoChar();
        //确认密码
        JPanel cp = new JPanel();
        JLabel cpasswordLabel = new JLabel("确认密码");
        JPasswordField cpasswordText = new JPasswordField(10);
        //
        JPanel sp = new JPanel();
        JRadioButton showPassword = new JRadioButton("显示密码");
        //
        JPanel jp = new JPanel();
        JButton confirmed = new JButton("确定");
        JButton cancel = new JButton("取消");
        //

        this.setTitle("注册新用户");
        this.setBounds(600,300,400,300);
        this.setDefaultCloseOperation(HIDE_ON_CLOSE);
        //用户名
        c.add(countLabel,BorderLayout.WEST);
        c.add(countText,BorderLayout.EAST);
        //密码
        p.add(passwordLabel,BorderLayout.WEST);
        p.add(passwordText,BorderLayout.EAST);
        //确认密码
        cp.add(cpasswordLabel,BorderLayout.WEST);
        cp.add(cpasswordText,BorderLayout.EAST);
        //
        sp.add(showPassword);
        //按钮
        jp.add(confirmed,BorderLayout.WEST);
        jp.add(cancel,BorderLayout.EAST);
        //
        this.setLayout(new GridLayout(5,1));
        this.add(c);
        this.add(p);
        this.add(cp);
        this.add(sp);
        this.add(jp);
        sp.setBackground(new Color(173,216,230));
        jp.setBackground(new Color(173,216,230));
        showPassword.setBackground(new Color(173,216,230));
        c.setBackground(new Color(173,216,230));
        p.setBackground(new Color(173,216,230));
        cp.setBackground(new Color(173,216,230));
        countText.setText(COUNT_REQUIREMENT);
        countText.setForeground(Color.LIGHT_GRAY);
        passwordText.setText(PASSWORD_REQUIREMENT);
        passwordText.setForeground(Color.LIGHT_GRAY);
        passwordText.setEchoChar('\0');
        pack();
        //
        this.getRootPane().setDefaultButton(confirmed);
        getVisiable();
        countLabel.requestFocus();
        //确认键
        confirmed.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                /**检查用户名是否重合*/
                String count = countText.getText();
                String p = String.valueOf(passwordText.getPassword());
                String cp = String.valueOf(cpasswordText.getPassword());
                if (!p.equals(cp)){
                    new PromptWindow("两次密码不同");
                    return;
                }
                Socket s = null;
                if(count.equals(""))return ;
                try{
                    s = new Socket(CSTR,PORT);
                    DataOutputStream d = new DataOutputStream(s.getOutputStream());
                    d.writeUTF("%"+count+"#"+p);
                    DataInputStream i =new DataInputStream(s.getInputStream());
                    String ret = i.readUTF();
                    switch (ret) {
                        case "count illegal!" -> new PromptWindow("账号不合法").setLocationRelativeTo(getThis());
                        case "count existed!" -> new PromptWindow("账号已存在").setLocationRelativeTo(getThis());
                        case "password illegal!" -> new PromptWindow("密码不合法").setLocationRelativeTo(getThis());
                        case "register completed!" -> {
                            new PromptWindow("注册成功").setLocationRelativeTo(getThis());
                            getInvisiable();
                            s.close();
                        }
                    }
                }catch (IOException a){
                    a.printStackTrace();
                }
            }
        });

        //取消键
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getInvisiable();
            }
        });

        //账号聚焦
        countText.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                String s = countText.getText();
                if(s.equals(COUNT_REQUIREMENT)){
                    countText.setText("");
                    countText.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                String s = countText.getText();
                if(s.equals("")){
                    countText.setText(COUNT_REQUIREMENT);
                    countText.setForeground(Color.LIGHT_GRAY);
                }
            }
        });
        //密码聚焦
        passwordText.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                String p = new String(passwordText.getPassword()).trim();
                if(p.equals(PASSWORD_REQUIREMENT)){
                    passwordText.setText("");
                    passwordText.setEchoChar(defaultChar);
                    passwordText.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                String p =new String(passwordText.getPassword()).trim();
                if(p.equals("")){
                    passwordText.setEchoChar('\0');
                    passwordText.setText(PASSWORD_REQUIREMENT);
                    passwordText.setForeground(Color.LIGHT_GRAY);
                }
            }
        });
        //显示密码
        showPassword.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (showPassword.isSelected()){
                    passwordText.setEchoChar('\0');
                    cpasswordText.setEchoChar('\0');
                }else{
                    passwordText.setEchoChar(defaultChar);
                    cpasswordText.setEchoChar(defaultChar);
                }
            }
        });

    }

    public void getVisiable(){
        this.setVisible(true);
    }

    public void getInvisiable(){
        this.setVisible(false);
    }

    public registerWindow getThis(){
        return this;
    }

}
