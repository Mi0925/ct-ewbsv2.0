package cn.comtom.linkage.main.access.untils;

import cn.comtom.linkage.main.access.model.ebd.EBD;
import cn.comtom.linkage.utils.FileNameUtil;
import cn.comtom.linkage.utils.FileUtil;
import cn.comtom.linkage.utils.TarFileUtil;
import cn.comtom.linkage.utils.XmlUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.UUID;

/**
 * @author nobody
 * 发送http请求
 */
public class HttpRequestUtil {

	private final static Logger logger = LoggerFactory.getLogger (HttpRequestUtil.class);

	private static final int READ_TIME_OUT = 30 *1000;
	private static final int CONNECT_TIME_OUT = 30 * 1000;
	private static final String CHARSET = "utf-8";
	private static final String PREFIX = "--";
	private static final String CONTENT_TYPE = "multipart/form-data";
	//private static final String LINE_END = System.getProperty("line.separator");
	private static final String LINE_END = "\r\n";

	public static void main(String[] args) {
////		List<File> fileList = new ArrayList<File>();
////		File signFile = new File("G:\\EBM__201709181030.xml");
////		fileList.add(signFile);
////
////		EBD ebd = new EBD();
////		ebd.setEBDID("ebm_21222121323213123213");
////		// 生成联动TAR包
////		File tarFile = TarFileUtil.compressorsTar(ebd, fileList, "F:\\var\\lib\\comtom\\send");
////
////	//	File file = new File("G:\\EBM__20170866666.tar");
////		String url = "http://192.168.107.116/ts/sichuanExchange.do";
////		String path = "/var/lib/client/receive";
////		HttpRequestUtil.sendFile(tarFile, url, path);
//
//
////		File file = new File("F:\\git-comtom\\EBDT_20170210135800009-7427.tar");
////		String url = "http://192.168.107.189:38081/ts/exchange.do";
////		String path = "F:\\var\\lib\\comtom\\response";
		File file = new File("G:\\EBDT_10234000000000001030101010000000006664194.tar");
		String url = "http://220.180.97.229:9002/linkage/access";
		String path = "E:\\workTemp\\receive";
		HttpRequestUtil.sendFile(file, url, path);
	}

	/**
	 * @param tarfile
	 * @param requestURL
	 * @param filePath(保存返回文件路径)
	 * @return
	 * @throws Throwable
	 */
	public static EBD sendFile(File tarfile, String requestURL, String filePath) {
		if (tarfile == null){
			throw new RuntimeException("参数对象为空.文件为空");
		}
		if (requestURL==null){
			throw new RuntimeException("参数对象为空.请求地址为空");
		}
		if (filePath==null){
			throw new RuntimeException("参数对象为空.文件路径");
		}
		HttpURLConnection conn=null;
		String BOUNDARY = UUID.randomUUID().toString();
		try {
			URL url = new URL(requestURL);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setReadTimeout(READ_TIME_OUT);
			conn.setConnectTimeout(CONNECT_TIME_OUT);
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setUseCaches(false);
			conn.setRequestProperty("Charset", CHARSET);
			conn.setRequestProperty("connection", "keep-alive");
			conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + BOUNDARY);
		} catch (Exception e) {
			logger.error("get connect to "+requestURL+" error:"+e.getMessage());
			return null;
		}
		StringBuffer sb = new StringBuffer();
		sb.append(PREFIX);
		sb.append(BOUNDARY);
		sb.append(LINE_END);

		sb.append("Content-Disposition: form-data; name=\"file\"; filename=\"" + tarfile.getName() + "\"" + LINE_END);
		sb.append("Content-Type: application/octet-stream; charset=" + CHARSET + LINE_END);
		sb.append(LINE_END);
		OutputStream outputSteam=null;
		InputStream is=null;
		byte[] bytes = new byte[1024];
		try {
			is=new FileInputStream(tarfile);
			outputSteam = conn.getOutputStream();
			DataOutputStream dos = new DataOutputStream(outputSteam);
			dos.write(sb.toString().getBytes());
			int len = 0;
			while ((len = is.read(bytes)) != -1)
			{
				dos.write(bytes, 0, len);
			}
			dos.write(LINE_END.getBytes());
			byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END).getBytes();
			dos.write(end_data);
			dos.flush();
			int res = conn.getResponseCode();
			if(HttpURLConnection.HTTP_OK==res){
				String fileName=getFileName(conn);
				File resonseFile=saveFile(conn,filePath,fileName);
				if(resonseFile==null){
					logger.error("saveOriginFile file error.");
					return null;
				}
				/*if(resonseFile!=null) {
					return new EBD();
				}*/
				List<File> files=TarFileUtil.decompressorsTar(resonseFile);
				File EBDBFile=null;
				for (File file2 : files) {
					String name=file2.getName();
					if(name.startsWith("EBDB")){
						EBDBFile=file2;
					}
				}
				//业务数据文件
				if(EBDBFile==null){
					return null;
				}
				//将文件转换xml
				String eBDxmlString=FileUtils.readFileToString(EBDBFile, "utf-8");
				//转换对象
				EBD eBD=XmlUtil.fromXml(eBDxmlString,EBD.class);
				return eBD;
			}
		} catch (Exception e) {
			logger.error("send file error."+e.getMessage());
			e.printStackTrace();
			return null;
		}finally{
			IOUtils.closeQuietly(is);
			IOUtils.closeQuietly(outputSteam);
			conn.disconnect();
		}
		return null;
	}

    /**
     * 获取文件名称
     * @param urlConn
     * @return
     * @throws IOException
     */
    private static String getFileName(HttpURLConnection urlConn){
    	String fileName=FileNameUtil.generateTarFileName();
    	String value=urlConn.getHeaderField("content-disposition");
    	if(value==null){
    		return fileName;
    	}
    	int index=value.indexOf("filename=");
    	if(index<0){
    		return fileName;
    	}
    	int end=value.indexOf(".tar");
    	if(end<0){
    		return fileName;
    	}
    	fileName=value.substring(index+9,end+4);
    	return fileName.replace("\"", "");
    }


    /**
     * 保存响应的文件
     * @param urlConn
     * @param filePath
     * @param fileName
     */
    private static File saveFile(HttpURLConnection urlConn,String filePath,String fileName){
    	InputStream input=null;
    	try {
    		input=urlConn.getInputStream();
		} catch (Exception e) {
			logger.error("get inputStream form urlConnection error:"+e.getMessage());
		}
    	if(input==null){
    		return null;
    	}
    	return FileUtil.converFile(filePath, fileName, input);
    }
}
