package remote;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import javasync.FileInfo;

public interface ServerIntf extends Remote{
    public void getFiles(ClientIntf stub, HashSet<FileInfo> folderInfo2, String folderName2, String folderName1, String login, String pass) throws RemoteException, IOException, ClassNotFoundException, NoSuchAlgorithmException;
}
