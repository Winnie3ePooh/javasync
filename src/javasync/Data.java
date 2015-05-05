package javasync;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.attribute.FileTime;
import java.io.ObjectOutputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Paths;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;

public class Data {
    
    static public String infoFileName; 
    
    static public HashSet<FileInfo> getFolderInfo(String folderPath) throws IOException, ClassNotFoundException, NoSuchAlgorithmException{
        HashSet<FileInfo> infoSet = getFolderInfo(Paths.get(folderPath), Paths.get(folderPath), new HashSet<>());
        HashSet<String> filesNames;
        try{
            filesNames = loadFolderInfo(folderPath);
        } catch(IOException ex){
            return infoSet;
        }
        for(String name: filesNames){
            boolean deleted = true;
            for(FileInfo file: infoSet)
                if(file.name.equals(name)){
                    deleted = false;
                    break;
                }
            if(deleted)
                infoSet.add(new FileInfo(name, folderPath, null, deleted, null));
        }
        return infoSet;
    }
    
    static private HashSet<FileInfo> getFolderInfo(Path folderPath, Path currentPath, HashSet<FileInfo> set) throws IOException, NoSuchAlgorithmException{
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(currentPath)) {
            for (Path entry : stream) {
                if (Files.isDirectory(entry)) {
                    getFolderInfo(folderPath, entry, set);
                } else {
                    String name = folderPath.relativize(entry).toString();
                    if(!name.equals(infoFileName)){
                        FileTime modifyTime = Files.getLastModifiedTime(entry);
                        MessageDigest md = MessageDigest.getInstance("MD5");
                        try (FileInputStream fileInput = new FileInputStream(entry.toFile());) {                 
                            byte[] dataBytes = new byte[1024];
                            int bytesRead = 0;
                            while ((bytesRead = fileInput.read(dataBytes)) != -1) {
                                md.update(dataBytes, 0, bytesRead);
                            }
                        }
                        set.add(new FileInfo(name, folderPath.toString()+"/", modifyTime, false, md.digest()));
                    }
                }
            }
            return set;
        }
    }
    
    static public void saveFolderInfo(String folderPath) throws IOException, NoSuchAlgorithmException{
        HashSet<FileInfo> infoSet = getFolderInfo(Paths.get(folderPath), Paths.get(folderPath), new HashSet<>());
        try(ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(folderPath+infoFileName))){
            HashSet<String> filesNames = new HashSet<>();
            for(FileInfo file: infoSet)
                filesNames.add(file.name);
            out.writeObject(filesNames);
        }
    }
    
    static public HashSet<String> loadFolderInfo(String folderPath) throws IOException, ClassNotFoundException{
        try(ObjectInputStream in = new ObjectInputStream(new FileInputStream(folderPath+infoFileName))){
            return (HashSet<String>) in.readObject();
        }
    }
}
