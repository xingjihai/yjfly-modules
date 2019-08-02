package cn.ejfy.web.tools.http;

import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Map;

/**
 * 签名接口.
 */
public class SHA1Sign {

	/**
	 * 用SHA1算法生成安全签名
	 *@author wyj
	 *@param 参数集合
	 */
	public static String getSHA1(Map<String,Object> map) {
		try {
		    String[] array = new String[map.size()] ;
            StringBuffer sb = new StringBuffer();
            // 字符串排序
            int a=0;
            for (String key : map.keySet()) {
                array[a]=key;
                a++;
            }
            Arrays.sort(array);
            for (int i = 0; i < map.size(); i++) {
                if(i==map.size()-1){
                    sb.append(array[i]+"="+map.get(array[i]));
                }else{
                    sb.append(array[i]+"="+map.get(array[i])+"&");
                }
            }
			String str = sb.toString();
			// SHA1签名生成
			// 注意这里参数名必须全部小写，且必须有序
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			md.update(str.getBytes());
			byte[] digest = md.digest();

			StringBuffer hexstr = new StringBuffer();
			String shaHex = "";
			for (int i = 0; i < digest.length; i++) {
				shaHex = Integer.toHexString(digest[i] & 0xFF);
				if (shaHex.length() < 2) {
					hexstr.append(0);
				}
				hexstr.append(shaHex);
			}
			return hexstr.toString();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("签名错误： " + e.getMessage());
		}
	}
}
