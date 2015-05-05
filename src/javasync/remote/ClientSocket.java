package remote;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javasync.FileInfo;

public class ClientSocket implements Runnable{

    private final String hostIP;
    private final int port;
    private final HashSet<FileInfo> files;

    public ClientSocket(String hostIP, int port, HashSet<FileInfo> files){
        this.hostIP = hostIP;
        this.port = port;
        this.files = files;
    }
    
    public void sendFiles(Socket socket) throws IOException{
        try(DataOutputStream dos = new DataOutputStream(socket.getOutputStream())){
            byte[] buffer = new byte[8192];
            for (FileInfo fileInfo: files) {
                File file = fileInfo.getFile();
                FileInputStream fis = new FileInputStream(file);
                dos.writeUTF(fileInfo.name);
                dos.writeLong(file.length());
                int read = 0;
                while ((read = fis.read(buffer)) > 0) {
                    dos.write(buffer, 0, read);
                }
            }
        }
    }
    
    @Override
    public void run() {
        try(Socket socket = new Socket(hostIP, port)){
            sendFiles(socket);
        } catch (IOException ex) {
            Logger.getLogger(ClientSocket.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}
