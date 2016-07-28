package com.tianyi.yw.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Properties;

public class MediaCenterConfig {
	
	
	  //流媒体中心server配置文件路径
    private static final String ConfigPath = "jdbc.properties";  
    private static MediaCenterConfig instance=null;
	private String ip = null;
	private String port = null;
	private String connectionKey = null;
	private String connectionSession = null;
	public String getConnectionKey() {
		return connectionKey;
	}
	public void setConnectionKey(String connectionKey) {
		this.connectionKey = connectionKey;
	}
	public String getConnectionSession() {
		return connectionSession;
	}
	public void setConnectionSession(String connectionSession) {
		this.connectionSession = connectionSession;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public static MediaCenterConfig getInstance(){
        if(instance==null){
            instance= new MediaCenterConfig().getNewCenterConfig();
        }
        return instance;
    }
	private MediaCenterConfig getNewCenterConfig() {
		// TODO Auto-generated method stub
		MediaCenterConfig mc=new MediaCenterConfig();
        Properties prop = new Properties();  
        String path=null;
        FileInputStream fis=null;
        
        try {
            path = MediaCenterConfig.class.getClassLoader().getResource("").toURI().getPath();
            fis = new FileInputStream(new File(path + ConfigPath));
            prop.load(fis);
            mc.ip=prop.getProperty("centerIp"); 
            mc.port=prop.getProperty("centerPort"); 
            mc.connectionKey=prop.getProperty("connectionKey");
            mc.connectionSession=prop.getProperty("connectionSession");   
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }  
        
        return mc;
	}
}
