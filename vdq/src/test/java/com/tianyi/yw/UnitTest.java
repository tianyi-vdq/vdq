
package com.tianyi.yw; 

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.ArrayList;
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
		String str = "点位视频诊断异常,详细:数据流捕获异常;";
		if(!StringUtil.isEmpty(str)){
			if(str.contains("数据流捕获异常")){
				str = "数据流捕获异常";
			}else if(str.contains("画面冻结异常") && str.contains("画面亮度异常")){
				str = "前端点位无视频信号";
			}else if(str.contains("色彩丢失异常") && str.contains("画面亮度异常")){
				str = "前端点位无视频信号";
			}
		}
		System.out.printf(str);
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
