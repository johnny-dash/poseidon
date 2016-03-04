//package cn.poseidon.util;
//
//import java.security.MessageDigest;
//import java.security.NoSuchAlgorithmException;
//
//public class MD5Encoder {
//
//	public static String encode(String pwd) {
//		try {
//			MessageDigest  digest = MessageDigest.getInstance("MD5");
//			byte[]  bytes = digest.digest(pwd.getBytes());
//			StringBuffer sb = new  StringBuffer();
//			for(int i = 0;i<bytes.length;i++){
//				String s = Integer.toHexString(0xff&bytes[i]);
//				
//				if(s.length()==1){
//					sb.append("0"+s);
//				}else{
//					sb.append(s);
//				}
//			}
//			
//			return sb.toString();
//		} catch (NoSuchAlgorithmException e) {
//			e.printStackTrace();
//			throw new RuntimeException("MD5Encoder wrong");
//		}
//	}
//}
package cn.poseidon.util;

import java.security.MessageDigest;

public class MD5Encoder {

	public static String getData(String str){
		try {
			MessageDigest digest = MessageDigest.getInstance("md5");
			byte[] data = digest.digest(str.getBytes());
			StringBuilder sb = new StringBuilder();
			for(int i = 0;i< data.length;i++){
				//与255与，取出数据的低八位
				String result = Integer.toHexString(data[i]&0xff);
				String temp = null;
				if(result.length() == 1){
					temp = "0" + result;
				}else{
					temp = result;
				}
				sb.append(temp);
			}
			return sb.toString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
