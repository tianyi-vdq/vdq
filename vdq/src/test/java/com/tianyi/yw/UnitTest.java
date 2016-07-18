
package com.tianyi.yw; 

import java.io.UnsupportedEncodingException;

import net.sf.json.JSONObject;

import org.junit.Test; 
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import com.tianyi.yw.common.utils.Constants;
 

public class UnitTest {
	

	@Autowired
	RabbitTemplate rabbitTemplate;
	
	@Test
	public  void main()  {
        mqtest();
    }

	private void mqtest() {
		
		int count= 5;
		
		while(count>0){
			count --;
			// TODO Auto-generated method stub 
			JSONObject json = new JSONObject();
			json.element("cameraId", "1");
			json.element("cameraName", "1111");
			json.element("deviceIp", "25.30.9.244");
			json.element("faultCode", Constants.ROUTEDATA_YWALARM_VIDEO_CODE);
			json.element("faultContent", "视频亮度异常");
			json.element("faultType", Constants.ROUTEDATA_YWALARM_VIDEO_TYPE);
			try {
				rabbitTemplate.convertAndSend(Constants.ROUTEDATA_YWALARM_VIDEO,json.toString());
			} catch (AmqpException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
//	public void genCfg(){
//		List<String> warnings = new ArrayList<String>();
//        boolean overwrite = true;
//        String genCfg = "src/main/resources/mybatis/generatorConfig.xml";
//        File configFile = new File(genCfg);
//        ConfigurationParser cp = new ConfigurationParser(warnings);
//        Configuration config = null;
//        try {
//            config = cp.parseConfiguration(configFile);
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (XMLParserException e) {
//            e.printStackTrace();
//        }
//        DefaultShellCallback callback = new DefaultShellCallback(overwrite);
//        MyBatisGenerator myBatisGenerator = null;
//        try {
//            myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
//        } catch (InvalidConfigurationException e) {
//            e.printStackTrace();
//        }
//        try {
//            myBatisGenerator.generate(null);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//	}
}
