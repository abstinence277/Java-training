import jdk.swing.interop.SwingInterOpUtils;

import javax.swing.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class FileInfo implements Serializable {
    HashMap<String , ArrayList<String>> SubDirectory = new HashMap<>();
    HashMap<String , String > Parent = new HashMap<>();
    ArrayList<String > names = new ArrayList<>();
    HashMap<String, ArrayList<String> > personalFile = new HashMap<>();

    boolean DownloadFree = true;
    boolean UploadFree = true;

    public FileInfo(String root){
        SubDirectory.put(root,new ArrayList<>());
    }


    public void addFile(String parent,String fileName){
        if(names.contains(parent)){
            SubDirectory.get(parent).add(fileName);
            Parent.put(fileName,parent);
        }else{
            names.add(parent);
            SubDirectory.put(parent,new ArrayList<>());
            SubDirectory.get(parent).add(fileName);
            Parent.put(fileName,parent);
        }
    }

    public void deleteFile(String fileName,String c){
        SubDirectory.get(Parent.get(fileName)).remove(fileName);
        Parent.remove(fileName);
        personalFile.get(c).remove(fileName);
    }

    public void reName(String oldName, String newName,String c){
        SubDirectory.get(Parent.get(oldName)).remove(oldName);
        SubDirectory.get(Parent.get(oldName)).add(newName);
        String tem = Parent.get(oldName);
        Parent.remove(oldName);
        Parent.put(newName,tem);
        personalFile.get(c).remove(oldName);
        personalFile.get(c).add(newName);
    }

    public HashMap<String, ArrayList<String>> getPersonalFile() {
        return personalFile;
    }

    public String getpath(String name){
        if(name.equals("root"))
            return "root";
        String parent = Parent.get(name),child = name;
        String path = "";
        while(!parent.equals("root")){
            child = parent;
            parent = Parent.get(child);
            path = "/"+ child + path;
        }
        path = "root"+path;
        return path;
    }

    public boolean isDownloadFree() {
        return DownloadFree;
    }

    public boolean isUploadFree() {
        return UploadFree;
    }

    public void setDownloadFree(boolean downloadFree) {
        DownloadFree = downloadFree;
    }

    public void setUploadFree(boolean uploadFree) {
        UploadFree = uploadFree;
    }

    public static void main(String[] args) {
        JFrame j = new JFrame("dd");
        JButton j1 = new JButton("she");
        JButton j2 = new JButton("ss");
        Box box = Box.createHorizontalBox();
        box.add(j1,0);
        box.add(j2,1);
        j.add(box);
        j.pack();
        j.setVisible(true);
        box.remove(0);
        box.remove(0);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        j.validate();
    }
}
