package cn.comtom.signature.test;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

public class TestUpload {

	public static void main(String[] args) throws IOException {
		String url = "http://192.168.111.112:9007/sign/getSignByFile";
		//String url = "http://192.168.111.112:9007/sign/getSignByStream";
		//String filePath = "C:\\Users\\Administrator\\Desktop\\ebd\\EBDT_10434152300000001030101010000000000839370.tar";

		RestTemplate rest = new RestTemplate();
		FileSystemResource resource = new FileSystemResource(
				new File("C:\\Users\\Administrator\\Desktop\\sys_dict.sql"));
		MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
		param.add("xmlFile", resource);
		param.add("signValue",
				"AA3j8gAAEQEAAZr/FdQyVOX5Lv9oIAk5MPlCa8c9+h9wYwqx6S6Iic963pLxUpMytPIg1Vr09gMI2cVQ0C+dIu5EduXPD8V2AqY=");
		HttpHeaders headers = new HttpHeaders();
		// 需求需要传参为form-data格式
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		List<MediaType> acceptableMediaTypes = new ArrayList<MediaType>();
		acceptableMediaTypes.add(MediaType.APPLICATION_JSON);
		headers.setAccept(acceptableMediaTypes);
		HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(param, headers);
		Map postForObject = rest.postForObject(url, httpEntity, Map.class);
		System.out.println(postForObject);

		
//		ByteArrayInputStream byteArrayInputStream = new
//		ByteArrayInputStream(FileUtils.readFileToByteArray(new File(
//		"C:\\Users\\Administrator\\Desktop\\数字签名\\EBDT_10434152300000001030101010000000000839370.tar"
//		))); String uploadFile = uploadFile("xmlFile", byteArrayInputStream, url);
//		System.out.println(uploadFile);
		//doPost(url, "0909");

	}

	 public static String doPost(String httpUrl, String param) {

	        HttpURLConnection connection = null;
	        InputStream is = null;
	        OutputStream os = null;
	        BufferedReader br = null;
	        String result = null;
	        try {
	            URL url = new URL(httpUrl);
	            // 通过远程url连接对象打开连接
	            connection = (HttpURLConnection) url.openConnection();
	            // 设置连接请求方式
	            connection.setRequestMethod("POST");
	            // 设置连接主机服务器超时时间：15000毫秒
	            connection.setConnectTimeout(15000);
	            // 设置读取主机服务器返回数据超时时间：60000毫秒
	            connection.setReadTimeout(60000);

	            // 默认值为：false，当向远程服务器传送数据/写数据时，需要设置为true
	            connection.setDoOutput(true);
	            // 默认值为：true，当前向远程服务读取数据时，设置为true，该参数可有可无
	            connection.setDoInput(true);
	            // 设置传入参数的格式:请求参数应该是 name1=value1&name2=value2 的形式。
	            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
	            // 设置鉴权信息：Authorization: Bearer da3efcbf-0845-4fe3-8aba-ee040be542c0
	            // 通过连接对象获取一个输出流
	            os = connection.getOutputStream();
	            // 通过输出流对象将参数写出去/传输出去,它是通过字节数组写出的
	            os.write(param.getBytes());
	            // 通过连接对象获取一个输入流，向远程读取
	            //if (connection.getResponseCode() == 200) {

	                is = connection.getInputStream();
	                // 对输入流对象进行包装:charset根据工作项目组的要求来设置
	                br = new BufferedReader(new InputStreamReader(is, "UTF-8"));

	                StringBuffer sbf = new StringBuffer();
	                String temp = null;
	                // 循环遍历一行一行读取数据
	                while ((temp = br.readLine()) != null) {
	                    sbf.append(temp);
	                    sbf.append("\r\n");
	                }
	                result = sbf.toString();
	            //}
	        } catch (MalformedURLException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        } finally {
	            // 关闭资源
	            if (null != br) {
	                try {
	                    br.close();
	                } catch (IOException e) {
	                    e.printStackTrace();
	                }
	            }
	            if (null != os) {
	                try {
	                    os.close();
	                } catch (IOException e) {
	                    e.printStackTrace();
	                }
	            }
	            if (null != is) {
	                try {
	                    is.close();
	                } catch (IOException e) {
	                    e.printStackTrace();
	                }
	            }
	            // 断开与远程地址url的连接
	            connection.disconnect();
	        }
	        return result;
	    }
	

	private static String uploadFile(String fileName, ByteArrayInputStream inStream, String urlStr) {
		try {
			// 换行符
			final String newLine = "\r\n";
			// 服务器的上传地址
			URL url = new URL(urlStr);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			// 设置为POST情
			conn.setRequestMethod("POST");
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			// 设置请求头参数
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("Charsert", "UTF-8");
			conn.setRequestProperty("Content-Type",
					"multipart/form-data; boundary=----WebKitFormBoundaryari0emH33oMihIU4");
			conn.setRequestProperty("User-Agent",
					"Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.146 Safari/537.36");

			OutputStream out = new DataOutputStream(conn.getOutputStream());

			// 上传文件
			StringBuilder sb = new StringBuilder();
			sb.append(newLine);
			// 文件参数
			sb.append("------WebKitFormBoundaryari0emH33oMihIU4\n"
					+ "Content-Disposition: form-data; name=\"xmlFile\"; filename=\"" + fileName + "\"");
			sb.append("Content-Type:application/octet-stream");
			// 参数头设置完以后需要两个换行，然后才是参数内容
			sb.append(newLine);
			sb.append(newLine);

			// 将参数头的数据写入到输出流中
			out.write(sb.toString().getBytes());

			// 数据输入流,用于读取文件数据
			DataInputStream in = new DataInputStream(inStream);
			byte[] bufferOut = new byte[2048];
			int bytes = 0;
			// 每次读2KB数据,并且将文件数据写入到输出流中
			while ((bytes = in.read(bufferOut)) != -1) {
				out.write(bufferOut, 0, bytes);
			}
			// 最后添加换行
			out.write(newLine.getBytes());
			in.close();
			// 定义最后数据分隔线，即--加上BOUNDARY再加上--。
			byte[] end_data = "------WebKitFormBoundaryari0emH33oMihIU4--".getBytes();
			// 写上结尾标识
			out.write(end_data);
			out.flush();
			out.close();

			// 定义BufferedReader输入流来读取URL的响应
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
			StringBuffer buffer = new StringBuffer();
			String line = null;
			while ((line = reader.readLine()) != null) {
				buffer.append(line);
			}
			return buffer.toString();
		} catch (Exception e) {
			System.out.println("发送POST请求出现异常！" + e);
			e.printStackTrace();
		}
		return "";

	}
}