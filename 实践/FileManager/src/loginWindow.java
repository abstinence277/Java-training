import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class loginWindow extends JFrame {
    private static String CSTR = "127.0.0.1";
    private static final int PORT = 8888;

    loginWindow() {

    	//账号
        JPanel c = new JPanel();
        JTextField countText = new JTextField(10);
       JLabel countLabel = new JLabel("请输入账号");
        //密码
        JPanel p = new JPanel();
        JPasswordField passwordText = new JPasswordField(10);
        JLabel passwordLabel = new JLabel("请输入密码");
        char defaultChar = passwordText.getEchoChar();
        //
        JRadioButton showPassword = new JRadioButton("显示密码");
        JPanel sp = new JPanel();
        //修改密码
        JButton  cPassword = new JButton("修改密码");
        //
        JPanel btnTool = new JPanel();
        JButton logIn = new JButton("登陆");
        JButton register = new JButton("注册");
        this.setTitle("文件管理");
        this.setBounds(500,250,400,300);
        //
        c.add(countLabel);
        c.add(countText);
        //
        p.add(logIn);
        p.add(register);
        p.add(cPassword);
        //
        btnTool.add(passwordLabel);
        btnTool.add(passwordText);
        //
        sp.add(showPassword);
      
        //
        this.setLayout(new GridLayout(4,1));
        this.add(c);
        this.add(btnTool);
        this.add(sp);
        this.add(p);
        logIn.setBackground(new Color(173,216,230));
		register.setBackground(new Color(173,216,230));
		cPassword.setBackground(new Color(173,216,230));
        c.setBackground(new Color(255,255,153));
		p.setBackground(new Color(255,255,153));
		sp.setBackground(new Color(255,255,153));
		btnTool.setBackground(new Color(255,255,153));
		showPassword.setBackground(new Color(255,255,153));
        passwordText.requestFocus();
        this.setVisible(true);
        this.setDefaultCloseOperation(3);
        this.getRootPane().setDefaultButton(logIn);

        //登陆按钮
        logIn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String n = countText.getText();
                String p = String.valueOf(passwordText.getPassword());
                if(n.equals("")){}
                else {
                    try {
                        Socket s = new Socket(CSTR,PORT);
                        DataOutputStream o = new DataOutputStream(s.getOutputStream());
                        o.writeUTF("&"+n+"#"+p);
                        DataInputStream i = new DataInputStream(s.getInputStream());
                        String ret = i.readUTF();
                        switch (ret) {
                            case "count nonexistent!" -> new PromptWindow("用户名不存在").setLocationRelativeTo(getthis());
                            case "wrong password!" -> new PromptWindow("密码错误").setLocationRelativeTo(getthis());
                            case "client is already online!" -> new PromptWindow("用户已在线").setLocationRelativeTo(getthis());
                            default -> {
                                Client c = new Client(n,s);
                                getInvisiable();
                            }
                        }
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
            }
        });


        //注册
        register.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerWindow n = new registerWindow();
                getthis().setLocationRelativeTo(n);
            }
        });
        //显示密码
        showPassword.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (showPassword.isSelected()){
                    passwordText.setEchoChar('\0');
                }else{
                    passwordText.setEchoChar(defaultChar);
                }
            }
        });
        //修改密码
        cPassword.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changePasswordWindow n = new changePasswordWindow();
                getthis().setLocationRelativeTo(n);
            }
        });
    }

    public void getVisiable(){
        this.setVisible(true);
    }

    public void getInvisiable(){
        this.setVisible(false);
    }

    public loginWindow getthis(){
        return this;
    }
}