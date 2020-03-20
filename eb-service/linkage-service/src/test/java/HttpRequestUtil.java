import cn.comtom.linkage.main.access.model.ebd.EBD;
import cn.comtom.linkage.utils.FileNameUtil;
import cn.comtom.linkage.utils.FileUtil;
import cn.comtom.linkage.utils.TarFileUtil;
import cn.comtom.linkage.utils.XmlUtil;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
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
 * @author
 * 发送http请求
 * 临时测试工具类
 */
public class HttpRequestUtil {
	
	private final static Logger logger = LoggerFactory.getLogger (HttpRequestUtil.class);
	
	private static final int READ_TIME_OUT = 5* 60 *1000; 
	private static final int CONNECT_TIME_OUT = 60 * 1000; 
	private static final String CHARSET = "utf-8"; 
	private static final String PREFIX = "--"; 
	private static final String CONTENT_TYPE = "multipart/form-data"; 
	//private static final String LINE_END = System.getProperty("line.separator");
	private static final String LINE_END = "\r\n";
	
	public static void main(String[] args) {
		System.out.print("341523000000".substring(0,"341523000000".length()-6));
		//发送文件地址
		String sendFilePath="G:/EBDT_10234000000000001030101010000000006664194.tar";
		//发送目标地址
		//String url = "http://localhost:9003/access//sichuanExchange.do";
		String url = "http://60.175.205.105:9002/linkage/access";
		//反馈文件接收地址
		String path = "E:/workTemp/receive2";
		File file = new File(sendFilePath);
		HttpRequestUtil.sendFile(file, url, path);
	}
	public boolean deArchiveFile(File sourcefile,String destPath){
		TarArchiveInputStream tar = null;
		TarArchiveEntry entry = null;
		BufferedOutputStream bos = null;
		try {
			tar = new TarArchiveInputStream(new FileInputStream(sourcefile));
			File destFileDir = new File(destPath);
			if(!destFileDir.exists()){
				destFileDir.mkdir();
			}
			while((entry = tar.getNextTarEntry())!=null){
				if(entry.isDirectory()){

				}else{
					try{
						byte b[] = new byte[1024];
						bos = new BufferedOutputStream(new FileOutputStream(destPath+File.separator+entry.getName()),1024);
						int count = 0;
						while((count = tar.read(b, 0, 1024))!=-1){
							bos.write(b, 0, count);
						}
					}finally{
						bos.flush();
						bos.close();
					}
					
				}
			}
			return true;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			if(bos!=null){
				try {
					bos.flush();
					bos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return false;
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
				if(resonseFile!=null){
					return new EBD();
				}
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
