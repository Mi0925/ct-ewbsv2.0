package cn.comtom.core.main.sichuan.ftp;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

public class FtpUtil {
	private static FTPClient ftp;
	
	/**
	 * 获取ftp连接
	 * @param f
	 * @return
	 * @throws Exception
	 */
	public static boolean connectFtp(FtpConfig f) throws Exception{
	    ftp=new FTPClient();
	    
	    ftp.setDataTimeout(60000);
	    ftp.setControlEncoding("GBK");
	    ftp.setDefaultTimeout(60000);
	    
	    boolean flag=false;
	    if (f.getFtpPort()==null) {
	        ftp.connect(f.getFtpHost(),21);
	    }else{
	        ftp.connect(f.getFtpHost(),f.getFtpPort());
	    }
	    ftp.login(f.getFtpUser(), f.getFtpPassword());
	    int reply = ftp.getReplyCode();      
	    if (!FTPReply.isPositiveCompletion(reply)) {      
	          ftp.disconnect();      
	          return flag;      
	    }      
	    ftp.changeWorkingDirectory(f.getFtpPath());      
	    flag = true;      
	    return flag;
	}
	
	/**
	 * 关闭ftp连接
	 */
	public static void closeFtp(){
	  try {
	      if (ftp!=null && ftp.isConnected()) {
	            ftp.logout();
	            ftp.disconnect();
	      }
	  }catch (IOException e){
	    e.printStackTrace();
	  }   
	}
	
	/**
	 * ftp上传文件
	 * @param f
	 * @throws Exception
	 */
	public static void upload(File f) throws Exception{
	    if (f.isDirectory()) {
	        ftp.makeDirectory(f.getName());
	        ftp.changeWorkingDirectory(f.getName());
	        String[] files=f.list();
	        for(String fstr : files){
	            File file1=new File(f.getPath()+File.separator+fstr);
	            if (file1.isDirectory()) {
	                upload(file1);
	                ftp.changeToParentDirectory();
	            }else{
	                File file2=new File(f.getPath()+File.separator+fstr);
	                FileInputStream input=new FileInputStream(file2);
	                ftp.storeFile(file2.getName(),input);
	                input.close();
	            }
	        }
	    }else{
	        File file2=new File(f.getPath());
	        FileInputStream input=new FileInputStream(file2);
	        ftp.storeFile(file2.getName(),input);
	        input.close();
	    }
	}
	
	/*
	 * FTP协议下载文件
	 */
	public static String DownFileByFTP(String localFilePath,String remoteFilePath){
		int success = 0;
		//remoteFilePath 如： /auxiliary/20150819/test.mp3
		String FilePath[] = remoteFilePath.split("/");
		//获取文件名
		String fileName = FilePath[FilePath.length-1];
		try { 
				
				ftp.setFileType(FTP.BINARY_FILE_TYPE);
				ftp.enterLocalPassiveMode();
				OutputStream os=new FileOutputStream(localFilePath+fileName);
				ftp.retrieveFile(remoteFilePath, os);
				
				/*InputStream in =null;
				in = ftp.retrieveFileStream(remoteFilePath);
				FileOutputStream outputStream = new FileOutputStream(localFilePath + fileName); 
				BufferedOutputStream bos = new BufferedOutputStream(outputStream);
			 
				byte[] buffer = new byte[1024];  
		        int len = -1;  
		        while((len = in.read(buffer)) != -1){  
		        	 bos.write(buffer,0,len);
		        } 	        
	        	bos.flush();
	        	bos.close();
	        	in.close();*/
				
	        	os.close();
	        	success=1;
		    } catch (Exception e) { 
		      System.out.println("Download File by FTP falied");
		      e.printStackTrace();
		    }
		if(success==0){
			fileName="";
		}
		return fileName;
	}
	
	
	public static FtpConfig getURLDetail(String url){
		
		FtpConfig FtpConfig = new FtpConfig();
		
		 // ftp://127.0.0.1:8999/auxiliary/20150819/test.mp3
		
		String pattern = "ftp://([\\d\\.]*)(:?\\d*)(/.*)";
		 
	      // 创建 Pattern 对象
	      Pattern r = Pattern.compile(pattern);
	 
	      // 现在创建 matcher 对象
	      Matcher m = r.matcher(url);
	      if(m.find()) {
	    	  String ip = m.group(1);
	  		int port = 21;
	  		String sPort=m.group(2);
	  		if(StringUtils.isNotBlank(sPort)) {
	  			port=Integer.parseInt(sPort);
	  		}
	  		String remotePath = m.group(3);

	  		
	  		
	  		FtpConfig.setFtpHost(ip);
	  		FtpConfig.setFtpPort(port);
	  		FtpConfig.setFtpPath(remotePath);
	      }
		
		
		
//		FtpConfig.setFtpHost("192.168.107.133");
//		FtpConfig.setFtpPort(Integer.parseInt("21"));
//		FtpConfig.setFtpPath("/video/asong.mp3");
		
		return FtpConfig;
	}
	
}
