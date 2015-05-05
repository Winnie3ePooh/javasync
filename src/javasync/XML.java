package javasync;

import java.io.File; 
import java.io.FileInputStream;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import java.util.Properties;

public class XML {
    static public Properties getLaunchInfo(String fileName) throws IOException, SAXException, ParserConfigurationException{
        try(FileInputStream fos = new FileInputStream(new File(fileName))){
            Properties prop = new Properties();
            prop.loadFromXML(fos);
            return prop;
        }
    }
}
