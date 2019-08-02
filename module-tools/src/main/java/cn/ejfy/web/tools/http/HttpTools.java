package cn.ejfy.web.tools.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;

public class HttpTools {
	public static HttpClient getClient() {
		HttpClient httpClient = new HttpClient();
		List<Header> headers = new ArrayList<Header>();
		headers.add(new Header("Content-Type", "application/json;charset=UTF-8"));
		httpClient.getHostConfiguration().getParams().setParameter("http.default-headers", headers);
		return httpClient;
	}

	private static PostMethod getPostMethod(String url) {
		PostMethod postMethod = new PostMethod(url);
		return postMethod;
	}

	public static String excetePost(String url, String jsonStr) {
		HttpClient httpClient = getClient();
		PostMethod postMethod = getPostMethod(url);
		postMethod.setRequestEntity(new StringRequestEntity(jsonStr));
		InputStream responseBody = null;
		try {
			httpClient.executeMethod(postMethod);
			responseBody = postMethod.getResponseBodyAsStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
		BufferedReader reader = null;
		if (responseBody != null) {
			try {
				reader = new BufferedReader(new InputStreamReader(responseBody, "utf-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		String returnString = "";
		if (reader != null) {
			String line = null;
			try {
				line = reader.readLine();
			} catch (IOException e1) {
				line = null;
			}
			while (line != null) {
				returnString += line;
				try {
					line = reader.readLine();
				} catch (IOException e) {
					line = null;
				}
			}
		}
		return returnString;
	}

	public static String exceteGet(String url) {
		return exceteGet(url, "utf-8");
	}

	/**
	 * @param string
	 * @param string2
	 * @return
	 */
	public static String exceteGet(String url, String encoding) {

		HttpClient httpClient = getClient();
		GetMethod method = new GetMethod(url);
		InputStream responseBody = null;
		try {
			httpClient.executeMethod(method);
			responseBody = method.getResponseBodyAsStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
		BufferedReader reader = null;
		if (responseBody != null) {
			try {
				reader = new BufferedReader(new InputStreamReader(responseBody, encoding));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		String returnString = "";
		if (reader != null) {
			String line = null;
			try {
				line = reader.readLine();
			} catch (IOException e1) {
				line = null;
			}
			while (line != null) {
				returnString += line;
				try {
					line = reader.readLine();
				} catch (IOException e) {
					line = null;
				}
			}
		}
		return returnString;
	}
}
