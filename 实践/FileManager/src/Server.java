import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Server extends JFrame {

    private FileInfo FileList = new FileInfo("root");

    private static String CSTR = "";
    static {
        try {
            CSTR = InetAddress.getLocalHost().toString().split("/")[1];
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    private static final int PORT = 8888;
    private ServerSocket ss = null;

    private String cp = "count.txt";
    private String file = "FileInfo.txt";
    private String personalFile = "personalFile.txt";


    //账号，用户
    private static HashMap<String , ClientInfo> ClientsInfo = new HashMap<>();
    //账号 文件
    private HashMap<String , ArrayList<String> > countToFile = new HashMap<>();
    //
    private HashMap<Socket,internalClient > ClientsList = new HashMap<>();
    private boolean isRunning = true;

    private JTextArea serverTa = new JTextArea();


    Server() throws IOException {
    	JPanel p=new JPanel();
    	JButton allowToDownload = new JButton("允许下载");
        JButton allowToUpload = new JButton("允许上传");
        JScrollPane scrollBar = new JScrollPane(serverTa);
        
    	this.setTitle("服务器");
        this.setBounds(1100,100,400,400);
        this.add(scrollBar, BorderLayout.CENTER);
        this.add(p,BorderLayout.SOUTH);
        p.setBackground(new Color(173,216,230));
        p.add(allowToDownload);
        p.add(allowToUpload);
        serverTa.setEditable(false);
        serverTa.append(CSTR+"\n");
        this.setVisible(true);

        //读入文件信息
        File f = new File(file);
        if(f.exists()){
            FileReader fr = new FileReader(f);
            BufferedReader fb = new BufferedReader(fr);
            String line = null;
            while((line = fb.readLine())!=null){
                String[] a = line.split("#");
                if(!FileList.SubDirectory.containsKey(a[0]))
                    FileList.SubDirectory.put(a[0],new ArrayList<>());
                for(int i =1;i<a.length;i++){
                    FileList.addFile(a[0],a[i]);
                    if(!FileList.names.contains(a[i])){
                        FileList.names.add(a[i]);
                    }
                }
            }
        }else {
            f.createNewFile();
        }
        //个人文件
        f = new File(personalFile);
        if(f.exists()){
            FileReader fr = new FileReader(f);
            BufferedReader fb = new BufferedReader(fr);
            String line = null;
            while((line = fb.readLine())!=null){
                String[] a = line.split("#");
                FileList.getPersonalFile().put(a[0],new ArrayList<>());
                for(int i = 1;i<a.length;i++){
                    FileList.getPersonalFile().get(a[0]).add(a[i]);
                }
            }
        }else {
            f.createNewFile();
        }
        //读入账号信息
        f = new File(cp);
        if(f.exists()){
            FileReader fr = new FileReader(f);
            BufferedReader fb = new BufferedReader(fr);
            String line = null;
            while((line = fb.readLine())!=null){
                String[] a =line.split("#");
                ClientsInfo.put(a[0],new ClientInfo(a[0],a[1]));
            }
        }else {
            f.createNewFile();
        }
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                //更新文件
                try {
                    upDateFile();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                System.exit(0);
            }
        });
        allowToDownload.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FileList.setDownloadFree(!FileList.isDownloadFree());
                if(allowToDownload.getText().equals("允许下载"))
                    allowToDownload.setText("禁止下载");
                else
                    allowToDownload.setText("允许下载");
                System.out.println(FileList.SubDirectory);
                System.out.println(FileList.Parent);
                for (Socket s : ClientsList.keySet()){
                    if(ClientsList.get(s).isOnline){
                        try {
                            DataOutputStream o = new DataOutputStream(s.getOutputStream());
                            o.writeUTF("1");
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                    }
                }
            }
        });
        allowToUpload.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FileList.setUploadFree(!FileList.isUploadFree());
                if(allowToUpload.getText().equals("允许上传"))
                    allowToUpload.setText("禁止上传");
                else
                    allowToUpload.setText("允许上传");
                for (Socket s : ClientsList.keySet()){
                    if(ClientsList.get(s).isOnline){
                        try {
                            DataOutputStream o = new DataOutputStream(s.getOutputStream());
                            o.writeUTF("2");
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                    }
                }
            }
        });
        System.out.println(CSTR);
        startServer();
    }

    class internalClient implements Runnable {
        Socket s = null;
        String count = "";
        boolean isOnline = true;

        public internalClient(Socket s, String count) {
            this.s = s;
            this.count = count;
            (new Thread(this)).start();
        }

        //多线程接收客户信息
        @Override
        public void run() {
            try {
                DataInputStream dis = new DataInputStream(s.getInputStream());
                while (isOnline) {    //客户端不关闭socket
                    String str = dis.readUTF();
                    if (str != "") {
                        dealMessage(str, s);
                    }
                }
            } catch (EOFException w) {
                //w.printStackTrace();
                //System.out.println(111);
            } catch (SocketException q) {
                q.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void startServer() {
        try {
            ss = new ServerSocket(PORT);
            System.out.println("服务器已启动");
            while (isRunning) {
                Socket s = ss.accept();
                String st = getInformation(s);
                char ch = st.charAt(0);
                DataOutputStream o = new DataOutputStream(s.getOutputStream());
                switch (ch) {

                    case '%' -> registerVerify(st.substring(1).split("#"), s, o);//注册

                    case '&' -> loginVerify(st.substring(1).split("#"), s, o);//登陆

                    case '!' -> changePasswordVerify(st.substring(1).split("#"), s, o);//修改密码

                }
            }
        } catch (SocketException q) {
            System.out.println("服务器停止或出现异常");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    //服务器接收用户名
    public String getInformation(Socket s) {
        try {
            DataInputStream dis = new DataInputStream(s.getInputStream());
            String str = dis.readUTF();
            return str;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }


    public synchronized void dealMessage(String s, Socket S) throws IOException {
        String[] order = s.split("#");
        DataOutputStream os = new DataOutputStream(S.getOutputStream());
        switch (order[0]) {
            case "newfolder":
                File f = new File(order[1]);
                if(! f.exists()){
                    f.mkdir();
                    os.writeUTF("#创建文件夹成功");
                    FileList.addFile(f.getParent(),f.getName());
                    serverTa.append(ClientsList.get(S).count+" 新建了文件夹 "+f.getParent()+"/"+f.getName()+"\n");
                    serverTa.setSelectionStart(serverTa.getText().length());
                    for (Socket q : ClientsList.keySet()){
                        if(ClientsList.get(q).isOnline){
                            try {
                                DataOutputStream o = new DataOutputStream(q.getOutputStream());
                                o.writeUTF("@"+f.getParent()+"#"+f.getName());
                            } catch (IOException ioException) {
                                ioException.printStackTrace();
                            }
                        }
                    }
                }
                break;
            case "upload":
                DataOutputStream o = new DataOutputStream(S.getOutputStream());
                String path = order[1];
                String[] mm = order[1].split("/");
                String name = mm[mm.length-1];
                String parent = mm[mm.length-2];
                InputStream is = S.getInputStream();
                byte[] buff = new byte[1024];
                FileOutputStream fos = new FileOutputStream(path);
                int size = Integer.valueOf(order[2]);
                int lenth = 0,tot = 0;
                while(tot != size){
                    lenth = is.read(buff);
                    tot += lenth;
                    fos.write(buff,0,lenth);
                }
                FileList.addFile(parent,name);
                System.out.println(ClientsList.keySet());
                for (Socket q : ClientsList.keySet()){
                    if(ClientsList.get(q).isOnline){
                        try {
                            DataOutputStream oo = new DataOutputStream(q.getOutputStream());
                            oo.writeUTF("!"+parent+"#"+name+"#"+order[3]);
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                    }
                }
                if(FileList.getPersonalFile().containsKey(order[3])){
                    FileList.getPersonalFile().get(order[3]).add(name);
                }else{
                    FileList.getPersonalFile().put(order[3],new ArrayList<>());
                    FileList.getPersonalFile().get(order[3]).add(name);
                }
                o.writeUTF("#上传成功");
                serverTa.append(ClientsList.get(S).count+" 上传了文件"+parent+"/"+name+"\n");
                serverTa.setSelectionStart(serverTa.getText().length());
                break;
            case "download":
                DataOutputStream oo = new DataOutputStream(S.getOutputStream());
                File t = new File(FileList.getpath(order[1])+"/"+order[1]);
                if(!t.exists()){
                    oo.writeUTF("#文件已被删除");
                    return;
                }
                os.writeUTF("~"+order[1]+"#"+t.length());
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                FileInputStream fis  = new FileInputStream(FileList.getpath(order[1])+"/"+order[1]);
                OutputStream oss = S.getOutputStream();
                byte[] bf = new byte[1024];
                int len = 0;
                while((len = fis.read(bf)) != -1){
                    oss.write(bf,0,len);
                }
                oss.flush();
                serverTa.append(ClientsList.get(S).count+" 下载了文件"+order[1]+"\n");
                serverTa.setSelectionStart(serverTa.getText().length());
                break;
            case "rename":
                File fi = new File(FileList.getpath(order[1])+"/"+order[1]);
                fi.renameTo(new File(FileList.getpath(order[1])+"/"+order[2]));
                FileList.reName(order[1], order[2],ClientsList.get(S).count);
                os.writeUTF("#重命名成功");
                serverTa.append(ClientsList.get(S).count+" 重名了文件"+order[1]+"-->"+order[2]+"\n");
                serverTa.setSelectionStart(serverTa.getText().length());
                for (Socket q : ClientsList.keySet()){
                    if(ClientsList.get(q).isOnline){
                        try {
                            new DataOutputStream(q.getOutputStream()).writeUTF("$"+order[1]+"#"+order[2]+"#"+ClientsList.get(S).count);
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                    }
                }
                break;
            case "delete":
                name = order[1];
                String p = FileList.getpath(name)+"/"+name;
                File file = new File(p);
                if(file.exists()&&file.isFile())
                    file.delete();
                String c = ClientsList.get(S).count;
                FileList.deleteFile(order[1],c);
                os.writeUTF("%"+order[1]+"#"+c);
                os.writeUTF("#删除成功");
                serverTa.append(ClientsList.get(S).count+" 删除了文件"+name+"\n");
                serverTa.setSelectionStart(serverTa.getText().length());
                break;
            case "offline":                           //下线消息
                ClientsInfo.get(order[1]).setOnline(false);
                break;
            case "renovate":
                break;
            default:
                break;
        }

    }

    //登录验证
    public void loginVerify(String[] a, Socket s, DataOutputStream o) throws IOException {
        if (!ClientsInfo.containsKey(a[0])) {
            o.writeUTF("count nonexistent!");
            s.close();
        } else if (!String.valueOf(ClientsInfo.get(a[0]).getPassword()).equals(String.valueOf(a[1]))) {
            o.writeUTF("wrong password!");
            s.close();
        } else if (ClientsInfo.get(a[0]).isOnline()) {
            o.writeUTF("client is already online!");
            s.close();
        } else {
            o.writeUTF("login successfully!");
            ClientsList.put(s,new internalClient(s,a[0]));
            ClientsInfo.get(a[0]).setOnline(true);
            System.out.println(s.toString());
            try {
                Thread.sleep(100);
                ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
                oos.writeObject(FileList);
                oos.flush();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    //注册验证
    public void registerVerify(String[] a, Socket s, DataOutputStream o) throws IOException {
        if (ClientsInfo.containsKey(a[0])) {
            o.writeUTF("count existed!");
        } else if (a[0].length() < 4 || a[0].length() > 10) {
            o.writeUTF("count illegal!");
        } else if (!(a[1].length() >= 8 && a[1].length() <= 10)) {
            o.writeUTF("password illegal!");
        } else {
            o.writeUTF("register completed!");
            ClientsInfo.put(a[0], new ClientInfo(a[0], a[1]));
        }
        s.close();
    }

    //修改密码验证
    public void changePasswordVerify(String[] a, Socket s, DataOutputStream o) throws IOException {
        if (!ClientsInfo.containsKey(a[0])) {
            o.writeUTF("count nonexistent!");
            s.close();
        } else if (!ClientsInfo.get(a[0]).getPassword().equals(a[1])) {
            o.writeUTF("wrong op!");
            s.close();
        } else if (ClientsInfo.get(a[0]).getPassword().equals(a[2])) {
            o.writeUTF("same ps!");
            s.close();
        } else if (!(a[2].length() >= 8 && a[2].length() <= 10)) {
            o.writeUTF("password illegal!");
            s.close();
        } else {
            ClientsInfo.get(a[0]).setPassword(a[2]);
            o.writeUTF("change successfully!");
            s.close();
        }
    }



    public synchronized void upDateFile() throws IOException {
        //账号密码
        File f1 = new File(cp);
        Writer fp1 = new FileWriter(cp,false);
        for(String q : ClientsInfo.keySet())
            fp1.write(q+"#"+ClientsInfo.get(q).getPassword()+"\n");
        fp1.close();
        //子目录
        File f2 = new File(file);
        Writer fp2 = new FileWriter(file,false);
        fp2.write("root");
        for (String q : FileList.SubDirectory.get("root")){
            fp2.write("#"+q);
        }
        fp2.write("\n");
        for(String s : FileList.SubDirectory.keySet()){
            if(!s.equals("root")){
                fp2.write(s);
                for(String q : FileList.SubDirectory.get(s)){
                    fp2.write("#"+q);
                }
                fp2.write("\n");
            }
        }
        fp2.close();
        //个人文件
        File f3 = new File(personalFile);
        Writer fp3 = new FileWriter(personalFile,false);
        for(String s : FileList.personalFile.keySet()){
            fp3.write(s);
            for(String q : FileList.personalFile.get(s)){
                fp3.write("#"+q);
            }
            fp3.write("\n");
        }
        fp3.close();
    }

    public static void main(String[] args) {
        try {
            new Server();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


//服务器下线更新文件
//客户端关闭，不关闭socket