import javax.sound.midi.Soundbank;
import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Client extends JFrame {

    private String userCount = "";
    private File fp = null;
    private Socket s = null;
    private DataOutputStream os = null;

    private FileInfo FileList = new FileInfo("root");

    private boolean isRunning = true;
    JTextArea files=new JTextArea();
    boolean DownloadFree;
    boolean UploadFree;


    public Client(String count,Socket s){
    	JButton renovate = new JButton("刷新");
    	JButton show= new JButton("目录");
    	JPanel a=new JPanel();

        File fp = new File("Download");
        if(!fp.exists())
            fp.mkdir();
        //
        try {
            ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
            FileList = (FileInfo)ois.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        this.DownloadFree = FileList.isDownloadFree();
        this.UploadFree = FileList.isUploadFree();
        this.userCount = count;
        this.s = s;
        this.setDefaultCloseOperation(3);
        this.setTitle(count);
        this.getContentPane().setBackground(new Color(173,216,230));
        a.add(renovate);
        a.add(show);
        this.add(a,BorderLayout.SOUTH);
        a.setBackground(new Color(173,216,230));
        files.setEditable(false);
        this.add(files,BorderLayout.CENTER);
        this.setVisible(true);
        this.setDefaultCloseOperation(3);//改成隐藏
        setBounds(100,100,400,400);
        this.setVisible(true);

        show.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new FolderWindow("root");
            }
        });

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    DataOutputStream os = new DataOutputStream(s.getOutputStream());
                    os.writeUTF("offline#"+userCount);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                System.exit(0);
            }
        });

        renovate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new PromptWindow("请重新打开目录");
                System.out.println(FileList.SubDirectory);
                System.out.println(FileList.Parent);
            }
        });

        if(FileList.getPersonalFile().containsKey(userCount))
            for(String c : FileList.getPersonalFile().get(userCount)){
                files.append(c+"\n");
            }
        new Thread(new getMessage()).start();
    }

    public void offline() throws IOException {
        DataOutputStream os = new DataOutputStream(s.getOutputStream());
        os.writeUTF("offline"+userCount);
    }

    //多线程接收消息
    class getMessage implements Runnable {

        @Override
        public void run() {
            try {
                while (isRunning) {
                    DataInputStream in = new DataInputStream(s.getInputStream());
                    String st = in.readUTF();
                    if (st.charAt(0) == '#') {  //一般消息
                        new PromptWindow(st.substring(1)).setLocationRelativeTo(Client.this); //设置位置
                    } else if (st.charAt(0) == '~') { //下载文件
                        String[] m = st.substring(1).split("#");
                        String name = m[0];
                        int size = Integer.valueOf(m[1]);
                        File f = new File("Download/"+name);
                        InputStream is = s.getInputStream();
                        FileOutputStream fos = new FileOutputStream(f.getPath());
                        byte[] buff = new byte[2048];
                        int lenth = 0,tot = 0;
                        while(tot != size){
                            lenth = is.read(buff);
                            tot += lenth;
                            fos.write(buff,0,lenth);
                        }
                        fos.flush();
                        new PromptWindow("下载成功");
                    } else if (st.charAt(0) == '!') {    //上传文件
                        String[] m = st.substring(1).split("#");
                        String parent = m[0];
                        String name = m[1];
                        String count = m[2];
                        FileList.addFile(parent,name);
                        if(FileList.getPersonalFile().containsKey(count)){
                            FileList.getPersonalFile().get(count).add(name);
                        }else{
                            FileList.getPersonalFile().put(count,new ArrayList<>());
                            FileList.getPersonalFile().get(count).add(name);
                        }
                    } else if(st.charAt(0) == '@'){ //上传文件夹
                        String[] m = st.substring(1).split("#");
                        FileList.addFile(m[0],m[1]);
                    }else if(st.charAt(0) == '$'){  // 重命名
                        String[] ss = st.substring(1).split("#");
                        FileList.reName(ss[0], ss[1],ss[2]);
                    }else if(st.charAt(0) == '%'){ //删除
                    	String[] t = st.substring(1).split("#");
                    	if(FileList.Parent.keySet().contains(t[0])){
                         FileList.deleteFile(t[0],t[1]);
                    	}
                    } else if (st.charAt(0) == '1') { //下载权限
                        DownloadFree = !DownloadFree;
                    } else if (st.charAt(0) == '2') {  // 权限上传
                        UploadFree = !UploadFree;
                    }
                }
            } catch (SocketException q) {
                q.printStackTrace();
            } catch (IOException e) {
                System.out.println("服务器关闭");
                e.printStackTrace();
            }
        }
    }

    class FolderWindow extends JFrame{
        Box box;
        int bIndex = 0;
        private HashMap<JButton,Integer> index = new HashMap<>();
        FolderWindow(String folderName){
            this.setTitle(folderName);
            this.setBounds(150,100,400,400);
            this.getContentPane().setBackground(Color.white);
            box = Box.createVerticalBox();
            this.add(box);
            this.setDefaultCloseOperation(1);
            this.setVisible(true);
            //给每个文件夹 添加一个上传,新建文件夹按钮
            JButton upload = new JButton("上传");
            box.add(upload);
            upload.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(!UploadFree){
                        new PromptWindow("服务器禁止上传");
                        return;
                    }
                    //选择上传文件
                    JFileChooser f = new JFileChooser(); // 查找文件
                    f.showOpenDialog(getThis());
                    File fp = f.getSelectedFile();
                    Upload(fp,folderName);
                    //动态刷新
                    JButton j = new JButton(fp.getName());
                    box.add(j,bIndex);
                    index.put(j,bIndex++);
                    j.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            new optionWindow(fp.getName(),j,getThis()).setLocation(getThis().getX()+j.getX()+j.getWidth(),getThis().getY()+j.getY());
                        }
                    });
                    validate();
                    files.setText("");
                    if(FileList.getPersonalFile().containsKey(userCount))
                        for(String c : FileList.getPersonalFile().get(userCount)){
                            files.append(c+"\n");
                        }
                }
            });
            //新建文件夹
            JButton newFolder = new JButton("新建文件夹");
            box.add(newFolder);
            newFolder.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    new FolderNameWindow(getThis()).setLocation(getThis().getX()+newFolder.getX()+newFolder.getWidth(),getThis().getY()+newFolder.getY());
                }
            });
            //所有文件选项
            if(FileList.SubDirectory.containsKey(folderName))
                for(String s : FileList.SubDirectory.get(folderName)){
                    JButton j = new JButton(s);
                    box.add(j,bIndex);
                    index.put(j,bIndex++);
                    j.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if(!isFile(s)){
                                new FolderWindow(s);
                            }else{
                                //选择窗口 删除，重命名，下载
                                new optionWindow(s,j,getThis()).setLocation(getThis().getX()+j.getX()+j.getWidth(),getThis().getY()+j.getY());
                            }

                        }
                    });
                }


        }

        public FolderWindow getThis(){
            return this;
        }
    }

    public boolean isFile(String s){
       return  s.indexOf('.') != -1;
    }


    class optionWindow extends JFrame{
        String name = null;
        Box box = Box.createHorizontalBox();

        optionWindow(String s,JButton j,FolderWindow f){
        	JButton download  = new JButton("下载");
            JButton rename = new JButton("重命名");
            JButton delete = new JButton("删除");
            name = s;
            this.setTitle(s);
            this.setDefaultCloseOperation(1);
            this.setVisible(true);
            box.add(download);
            download.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(!DownloadFree){
                        new PromptWindow("服务器禁止下载");
                        return;
                    }
                    //下载文件
                    Download(name);
                }
            });
            if(FileList.getPersonalFile().containsKey(userCount)&& FileList.getPersonalFile().get(userCount).contains(s)){
                box.add(rename);
                box.add(delete);
                rename.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        //重命名
                        new renameFile(name,j,f).setLocation(f.getX()+j.getX()+rename.getWidth(),f.getY()+j.getY());
                    }
                });
                delete.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        //删除文件
                        new deleteFile(name,j,f).setLocation(j.getX()+f.getX()+j.getWidth(),j.getY()+f.getY());
                        dispose();
                    }
                });
            }
            this.add(box);
            pack();

        }
    }

    class FolderNameWindow extends JFrame{

        FolderNameWindow(FolderWindow f){
        	JTextField jf = new JTextField(10);
            JButton j = new JButton("确认");
            this.getRootPane().setDefaultButton(j);
            this.setDefaultCloseOperation(2);
            this.add(jf, BorderLayout.CENTER);
            this.add(j,BorderLayout.SOUTH);
            pack();
            this.setVisible(true);
            j.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String newName = jf.getText() ;
                    //通知服务器
                    try {
                        DataOutputStream os = new DataOutputStream(s.getOutputStream());
                        String path = null;
                        path = FileList.getpath(f.getTitle());
                        path += "/"+newName;
                        os.writeUTF("newfolder#"+path);
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                    //加在自己的FileList
                    //FileList.addFile(f.getTitle(),newName);
                    JButton t = new JButton(newName);
                    t.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            new FolderWindow(newName);
                        }
                    });
                    f.box.add(t);
                    f.validate();
                    setVisible(false);
                }
            });
        }
    }

    class renameFile extends JFrame{

        renameFile(String name,JButton j,FolderWindow f){
        	JLabel jl = new JLabel("输入新名称");
            JTextField jf = new JTextField(10);
            JButton confirm = new JButton("确认");
            this.getRootPane().setDefaultButton(confirm);
            Box box = Box.createHorizontalBox();
            box.add(jl);
            box.add(jf);
            this.add(box,BorderLayout.CENTER);
            this.add(confirm,BorderLayout.SOUTH);
            pack();
            this.setDefaultCloseOperation(2);
            this.setVisible(true);
            confirm.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String newName = jf.getText();
                    String fileType = name.substring(name.lastIndexOf('.'));
                    newName += fileType;
                    Rename(name,newName);
                    //动态刷新
                    j.setText(newName);
                    f.validate();
                    setVisible(false);
                }
            });
        }
    }

    class deleteFile extends JFrame{
        deleteFile(String name,JButton j,FolderWindow f){
        	JButton confirm = new JButton("确认");
            JButton cancel = new JButton("取消");
            JLabel jl = new JLabel("是否确认删除");
            Box box = Box.createHorizontalBox();
            box.add(confirm);
            box.add(cancel);
            this.add(box,BorderLayout.SOUTH);
            this.add(jl,BorderLayout.CENTER);
            this.setDefaultCloseOperation(1);
            this.getRootPane().setDefaultButton(confirm);
            this.setVisible(true);
            pack();
            confirm.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Delete(name);
                   //删除按钮
                    f.box.remove(f.index.get(j));
                    f.bIndex--;
                    f.box.validate();
                    f.validate();
                    dispose();
                    files.setText("");
                    if(FileList.getPersonalFile().containsKey(userCount))
                        for(String c : FileList.getPersonalFile().get(userCount)){
                            files.append(c+"\n");
                        }
                }
            });
            cancel.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dispose();
                }
            });
        }
    }


    public void Rename(String oldName,String newName){
        DataOutputStream os = null;
        try {
            os = new DataOutputStream(s.getOutputStream());
            os.writeUTF("rename#"+oldName+"#"+ newName);
            FileList.reName(oldName,newName,userCount);
            files.setText("");
            if(FileList.getPersonalFile().containsKey(userCount))
                for(String c : FileList.getPersonalFile().get(userCount)){
                    files.append(c+"\n");
                }
//            DataInputStream is = new DataInputStream(s.getInputStream());
//            String ret = is.readUTF();
//            switch (ret){
//                case "修改成功" ->{
//                    new  PromptWindow("修改成功");
//                    FileList.reName(oldName,newName);
//
//                }
//                case "修改失败" ->{
//                    new PromptWindow("修改失败");
//                }
//            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void Delete(String name ){
        try {
            DataOutputStream os = new DataOutputStream(s.getOutputStream());
            os.writeUTF("delete#"+name);
//            DataInputStream is = new DataInputStream(s.getInputStream());
//            String ret = is.readUTF();
//            switch (ret){
//                case "删除成功" ->{
//                    new PromptWindow("删除成功");
//                }
//                case "删除失败" ->{
//                    new PromptWindow("删除失败");
//                }
//            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void Download(String name ){ //先发送下载信息，然后开始接收下载文件
        try {
            DataOutputStream os = new DataOutputStream(s.getOutputStream());
            os.writeUTF("download#"+name);
//            FileInputStream fis = new FileInputStream(f.getPath());
//            byte[] buff = new byte[1024];
//            int lenth = 0;
//            while((lenth = is.read(buff)) != -1){
//                System.out.println("下载循环");
//                fis.read(buff,0,lenth);
//            }
//            System.out.println("下载成功");
//            //文件接收完成
//            new PromptWindow("下载成功");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void Upload(File f ,String path){  //先发送上传信息，停100ms后开始上传文件

        try {
            DataOutputStream os = new DataOutputStream(s.getOutputStream());
            os.writeUTF("upload#"+path+"/"+f.getName()+"#"+f.length()+"#"+userCount);//写名字
            Thread.sleep(100);
            byte[] buff = new byte[1024];
            int lenth = 0;
            FileInputStream fis  = new FileInputStream(f.getPath());
            OutputStream oss = s.getOutputStream();
            while((lenth = fis.read(buff)) != -1){;
                oss.write(buff,0,lenth);
            }
            oss.flush();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }



    public void setFileList(FileInfo fileList) {
        FileList = fileList;
    }

    public FileInfo getFileList() {
        return FileList;
    }

    public void t(){
        new FolderWindow("root");
    }

    public static void main(String[] args) {

    }

}
