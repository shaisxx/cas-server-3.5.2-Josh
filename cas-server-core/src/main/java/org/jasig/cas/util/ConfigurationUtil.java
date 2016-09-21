package org.jasig.cas.util;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

/**
 * 配置文件读取工具
 */
public class ConfigurationUtil {
	
    private Properties propertie;
    private InputStream in;
    private FileOutputStream out;
    
    public ConfigurationUtil() {
        propertie = new Properties();
    }
    
    public ConfigurationUtil(String filePath) {
        propertie = new Properties();
        try {
            in = getClass().getResourceAsStream(filePath);
            propertie.load(in);
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public String getValue(String key) {
        if(propertie.containsKey(key)) {
            String value = propertie.getProperty(key);
            if (value == null) {
                return null;
            }
            try {
                value = new String(value.getBytes("ISO8859-1"), "UTF-8");
                return value;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return null;
            }
        } else {
        	return "";
        }  
    }
    
    public String getValue(String fileName, String key) {
        try {
            String value = "";
            in = getClass().getResourceAsStream(fileName);
            propertie.load(in);
            in.close();
            if(propertie.containsKey(key)) {
                value = propertie.getProperty(key);
                return value;
            }else
                return value;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public void clear() {
        propertie.clear();
    }
    
    public void setValue(String key, String value) {
        propertie.setProperty(key, value);
    }
    
    public void saveFile(String fileName, String description) {
        try {
            out = new FileOutputStream(fileName);
            propertie.store(out, description);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
