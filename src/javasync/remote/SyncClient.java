package remote;

import java.util.HashSet;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;
import javasync.Data;
import javasync.FileInfo;

public class SyncClient implements ClientIntf{

    private String folderName1;
    private int TCPport;
    private String ip;
    
    @Override
    public void getFiles(HashSet<FileInfo> folderInfo2) throws RemoteException {
        Thread host = new Thread(new HostSocket(ip, TCPport, folderInfo2, folderName1, false));
        host.start();
    }
    
    public void start(String ip, int port,int TCPport, String folderName1, String folderName2, String login, String pass){
        SyncClient client = this;
        this.folderName1 = folderName1;
        this.TCPport = TCPport;
        this.ip = ip;
        try {
            Registry registry = LocateRegistry.getRegistry(ip, port);
            ServerIntf server = (ServerIntf)registry.lookup("javaSync");
            ClientIntf stub = (ClientIntf)UnicastRemoteObject.exportObject(client, 0);
            HashSet<FileInfo> folderInfo1 = Data.getFolderInfo(folderName1);
            server.getFiles(stub, folderInfo1, folderName1, folderName2, login, pass);
        } catch (Exception ex) {
            Logger.getLogger(ClientSocket.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
