package javasync;

import remote.SyncServer;
import remote.SyncClient;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

public class JavaSync {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Data.infoFileName = ".folder_info";
        String XMLsettingsFile = "settings.xml";
        
        try{
            Properties xml = XML.getLaunchInfo(XMLsettingsFile);
            int RMIport = Integer.parseInt((String)xml.get("RMI_port"));
            int TCPport = Integer.parseInt((String)xml.get("TCP_port"));
            String folder1 = (String)xml.get("folder_1");
            String folder2 = (String)xml.get("folder_2");
            String IP = (String)xml.get("host_IP");
            boolean remote = Boolean.parseBoolean((String)xml.get("remote"));
            String login = (String)xml.get("login");
            String pass = (String)xml.get("pass");
            
            if(args[0].equals("server")){
                SyncServer serv = new SyncServer();
                serv.start(RMIport, TCPport);
            } else if(args[0].equals("remote")){
                SyncClient client = new SyncClient();
                client.start(IP, RMIport, TCPport, folder1, folder2, login, pass);
            }
            else{
                HashSet<FileInfo> folderInfo1 = Data.getFolderInfo(folder1);
                HashSet<FileInfo> folderInfo2 = Data.getFolderInfo(folder2);
                Thread syncThread = new Thread(new Sync(folderInfo1, folderInfo2, folder1, folder2, true));
                syncThread.run();
                Data.saveFolderInfo(folder1);
                Data.saveFolderInfo(folder2);
            }
        } catch(IOException ioEx){
            System.out.println(ioEx);
        } catch (ClassNotFoundException | SAXException | ParserConfigurationException | NoSuchAlgorithmException ex) {
            Logger.getLogger(JavaSync.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
