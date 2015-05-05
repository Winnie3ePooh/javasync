package javasync;

import java.io.File;
import java.io.Serializable;
import java.nio.file.attribute.FileTime;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileInfo implements Serializable{
    
    final public String name;
    final public String baseFolder;
    final public long lastModifiedTime;
    final public boolean deleted;
    final public byte[] checksum;
    
    public FileInfo(String name, String baseFolder, FileTime lastModifiedTime, boolean deleted, byte[] checksum){
        this.name = name;
        this.baseFolder = baseFolder;
        this.lastModifiedTime = lastModifiedTime==null?0:lastModifiedTime.toMillis();
        this.deleted = deleted;
        this.checksum = checksum;
    }

    public Path getPath(){
        return Paths.get(baseFolder+name);
    }       
    
    public FileTime getLastModTime(){
        return FileTime.fromMillis(lastModifiedTime);
    }
    
    public File getFile(){
        return new File(baseFolder+name);
    }
}
