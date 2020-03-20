package cn.comtom.core.main.sichuan;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;

public class Videodecode {

		/**  
	    * 解码  
	    * @param str  
	    * @return string  
	    */    
	   public static byte[] decode(String str){    
	   byte[] bt = null;    
	   try {    
	       sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();
	       bt = decoder.decodeBuffer( str );    
	       bt=Base64.decodeBase64(str);
	   } catch (IOException e) {    
	       e.printStackTrace();    
	   }    
	       return bt;    
	   }
	   
	   /**  
	    * 生成wav文件，并返回该文件  
	    * @param str  
	    * @return string  
	    */ 
	   public File analysis64coder(String content,String filesub){
		   
		   byte[] buffer = decode(content);
		   
//		   String newpath = PropertiesReader.getInstance().getProperties("downPath")
//					+ File.separator + "sichuan_"
//					+ UUID.randomUUID().toString().toLowerCase() + "." + filesub;
		   String newpath = "/var/ftp/workTemp/"+UUID.randomUUID().toString().toLowerCase() + "." + filesub;
		   File fileInst = new File(newpath);
        	
            try { 
            	OutputStream os=new FileOutputStream(fileInst);
            	BufferedOutputStream bos=new BufferedOutputStream(os);
            	bos.write(buffer);
            	bos.flush();
            	IOUtils.closeQuietly(bos);
            	IOUtils.closeQuietly(os);
            } catch (IOException e) {  
                e.printStackTrace();  
            }    
	       
		   return fileInst;
	   }
}
