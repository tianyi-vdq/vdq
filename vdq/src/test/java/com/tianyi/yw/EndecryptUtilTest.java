//package com.tianyi.yw;
//
//import org.apache.shiro.crypto.SecureRandomNumberGenerator;
//import org.apache.shiro.crypto.hash.Md5Hash;
//import org.junit.Test;
//
//import com.google.common.base.Preconditions;
//import com.google.common.base.Strings;
//import com.tianyi.yw.common.utils.EndecryptUtils;
//import com.tianyi.yw.model.User;
//
//public class EndecryptUtilTest {
//
//	/**
//	 * @param args
//	 */
//	@Test
//	public void main() {
//		
//		Preconditions.checkArgument(!Strings.isNullOrEmpty("admin"),"username不能为空");
//        Preconditions.checkArgument(!Strings.isNullOrEmpty("111111"),"password不能为空");
//        SecureRandomNumberGenerator secureRandomNumberGenerator=new SecureRandomNumberGenerator();
//        String salt= secureRandomNumberGenerator.nextBytes().toHex();
//        //组合username,两次迭代，对密码进行加密
//        String password_cipherText= new Md5Hash("111111","admin"+salt,2).toHex();
//		
//		System.out.println("salt:"+salt);
//		System.out.println("password:"+password_cipherText);
//	}
//
//}
/*
package com.tianyi.yw;

import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.junit.Test;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.tianyi.yw.common.utils.EndecryptUtils;
import com.tianyi.yw.model.Users;

public void main(){
	Preconditions.checkArgument(!String.isNullOrEmpty("admin"),"username不能为空");
	Preconditions.checkArgument(!String.isNullOrEmpty("111111"),"password不能为空");
	SecureRandonNumberGenerator secureRandonNumberGenerator = new SecureRandonNumberGenerator();
	String salt = secureRandomNumberGenerator.nextBytes().toHex();
	String password_copherTest =new Md5Hash("111111","admin"+salt,2).toHex();
	
	System.out.println("salt:"+salt);
	System.out.println("password:"+password_cipherText);
}
*/


