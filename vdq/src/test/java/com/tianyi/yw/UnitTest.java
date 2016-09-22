
package com.tianyi.yw; 

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONObject;

import org.junit.Test; 
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.exception.InvalidConfigurationException;
import org.mybatis.generator.exception.XMLParserException;
import org.mybatis.generator.internal.DefaultShellCallback;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import com.tianyi.yw.common.utils.Constants;
import com.tianyi.yw.common.utils.StringUtil;
 

public class UnitTest {
	

	@Autowired
	RabbitTemplate rabbitTemplate;
	
	@Test
	public  void main()  {
		test();
    }

	private void test() {
		// TODO Auto-generated method stub
		try{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			Date date = new Date();
			String dateStr = sdf.format(date);
			String fileName = dateStr+"_exception.log";
			String filePath = "c:\\usr\\"+fileName;
			File file = new File(filePath);
			if(!file.exists()){
				file.createNewFile();
			}
			String fileData = "2016-09-01 12:54:23---009031----sssssssssssssssssssss";
			byte contents[] = fileData.getBytes() ;
			OutputStream out = null ;
			try{
				out = new FileOutputStream(file) ;
			}
			catch (FileNotFoundException e) 
			{
				e.printStackTrace();
			}
			try{ 
				// 将byte数组写入到文件之中 
			    out.write(contents) ; 
			}  
			catch (IOException e1) 
			{
			    e1.printStackTrace();
			}
			try{
				out.close() ;
			} 
			catch (IOException e2) 
			{
				e2.printStackTrace();
			} 
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

	private void mqtest() {
		
		 
			// TODO Auto-generated method stub 
			JSONObject json = new JSONObject();
			json.element("cameraId", "010123");
			json.element("cameraName", "010123");
			json.element("deviceIp", "192.0.22.46");
			json.element("faultCode", Constants.ROUTEDATA_YWALARM_VIDEO_CODE);
			json.element("faultContent", "视频诊断结果异常，请注意查收");
			json.element("faultType", Constants.ROUTEDATA_YWALARM_VIDEO_TYPE);
			try {
				rabbitTemplate.convertAndSend(Constants.ROUTEDATA_YWALARM_VIDEO,json.toString());
			} catch (AmqpException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
	}
	
	public void genCfg(){
		List<String> warnings = new ArrayList<String>();
        boolean overwrite = true;
        String genCfg = "src/main/resources/mybatis/generatorConfig.xml";
        File configFile = new File(genCfg);
        ConfigurationParser cp = new ConfigurationParser(warnings);
        Configuration config = null;
        try {
            config = cp.parseConfiguration(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XMLParserException e) {
            e.printStackTrace();
        }
        DefaultShellCallback callback = new DefaultShellCallback(overwrite);
        MyBatisGenerator myBatisGenerator = null;
        try {
            myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        }
        try {
            myBatisGenerator.generate(null);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
	}
}
