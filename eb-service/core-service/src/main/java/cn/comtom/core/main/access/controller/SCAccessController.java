package cn.comtom.core.main.access.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;

import cn.comtom.core.fw.BaseController;
import cn.comtom.core.main.fegin.service.IResoFeginService;
import cn.comtom.core.main.fegin.service.ISystemFeginService;
import cn.comtom.core.main.sichuan.Ebm;
import cn.comtom.core.main.sichuan.service.ISCEbmService;
import cn.comtom.core.main.sichuan.service.SichuanEBMXML;
import cn.comtom.core.main.utils.SequenceGenerate;
import cn.comtom.domain.system.sysparam.info.SysParamsInfo;
import cn.comtom.tools.constants.Constants;

/**
 * WJ  信息接入入口
 */
@RestController
@RequestMapping(value = "access")
public class SCAccessController extends BaseController {

	@Autowired
    private ISCEbmService SCEbmService;
	
    @Autowired
    private IResoFeginService resoFeginService;
    
    @Autowired
    private ISystemFeginService systemFeginService;
    
    @Autowired
    private SequenceGenerate sequenceGenerate;
    
  	@RequestMapping(value = "/sichuanExchange.do"/* params = "method=exchange" */, produces = "text/html;charset=UTF-8")
  	public void exchange(HttpServletRequest request, HttpServletResponse response) throws Exception {
  		/*SichuanEBMXML sichuanEBMXML = new SichuanEBMXML();
        SysParamsInfo temp_file_path_ = systemFeginService.getByKey(Constants.TEMP_FILE_PATH);
        String filePath = temp_file_path_.getParamValue()+ "sichuan";
        MultipartHttpServletRequest mrequest = (MultipartHttpServletRequest) request;
        MultipartFile t_multipartFile = null;
        String orginalFileName = null;
        Collection<MultipartFile> partFiles = mrequest.getFileMap().values();
        for (MultipartFile multipartFile : partFiles) {
            orginalFileName = multipartFile.getOriginalFilename();
            if (orginalFileName.endsWith(".xml")) {
                t_multipartFile = multipartFile;
                break;
            }
         }
         // 接收的tar文件保存路径
         String tarFilePath = filePath;
         File tarDir = new File(tarFilePath);
         if (!tarDir.exists()) {
             tarDir.mkdirs();
         }
         // 设定所接收的文件的名称
         File file = new File(tarDir.getAbsolutePath() + SymbolConstants.FILE_SPLIT + orginalFileName);
         try {
             t_multipartFile.transferTo(file);
         } catch (IOException e) {
             e.printStackTrace();
         }
         */
  		
		SichuanEBMXML sichuanEBMXML = new SichuanEBMXML();
		
		
		ServletInputStream inputStream = request.getInputStream();
		BufferedInputStream bis=new BufferedInputStream(inputStream);
		
		SysParamsInfo temp_file_path_ = systemFeginService.getByKey(Constants.TEMP_FILE_PATH);
        String filePath = temp_file_path_.getParamValue();
		
		byte[] buffer=new byte[2048];
		int size=0;
		byte[] xml=null;
		DateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
		String time = sdf.format(new Date());
		File file = new File(filePath,time);
		file.mkdirs();
		String path=file.getAbsolutePath();
		File f=new File(path,String.format("EBM_%s.xml",time));
		
		BufferedOutputStream bos=new BufferedOutputStream(new FileOutputStream(f));
		while((size=bis.read(buffer))!=-1) {
			byte[] c=Arrays.copyOf(buffer, size);
			bos.write(c);
			if(xml==null) {
				xml=c;
			}else {
				byte[] _xml=xml;
				xml=new byte[_xml.length+size];
				System.arraycopy(_xml, 0, xml, 0, _xml.length);
				System.arraycopy(c, 0, xml, _xml.length, size);
			}
			
		}
		bos.flush();
		bos.close();
  		
  		//如果是以tar包形式下发，此函数用途为解压tar并返回解压文件的存储目录
  		//解析xml文件成Ebm对象
        /*DateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
		String time = sdf.format(new Date());
		File file = new File(filePath,time);
		file.mkdirs();
		String path=file.getAbsolutePath();
		File f=new File(path,String.format("EBM_%s.xml",time));
		
		byte[] buffer = new byte[64*1024];
		InputStream in = request.getInputStream();
		int length = in.read(buffer);
		String encode = request.getCharacterEncoding();

		byte[] data = new byte[length];
		System.arraycopy(buffer, 0, data, 0, length);
		
		String context = new String(data, "utf-8");
		System.out.println(context);*/
		String xx = file2String(f,"utf-8");
  		xx = HtmlUtils.htmlUnescape(xx);
  		xx = xx.replaceAll( "<\\?" + "(.+?)\\?>", "");
  		String yy = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"+xx;
  		boolean string2File = string2File(yy,f.getAbsolutePath());
  		System.out.println(f.getAbsolutePath());
  		Ebm ebm = sichuanEBMXML.analyze(f);
  		ebm.setGeocode(ebm.getGeocode().substring(0,ebm.getGeocode().length()-6));
  		// 获得解压文件的来源（被何单位发送）
  		String sender = null != ebm ? ebm.getSender() : "";
  		
//  		System.out.println("Analyzing the EBM.xml successful from the higher-up Platform !");
//  		System.out.println("the file was sent by the company of "+ sender);
//  		
//  		
//  		DealBusinessData DBD = new DealBusinessData(ebm);
  		SCEbmService.createEbms(ebm);
////  		//处理业务数据，播发信息
//  		DBD.Service(tFileinfoService, taskService, communicationService, settingService);
  		response.getWriter().write(",ok");
  	}
  	public static void main(String[] args) {
  		String htmlEscape = HtmlUtils.htmlUnescape("&lt;/presentation&gt;&lt;/dispatch&gt;&lt;/EBM&gt;</Emergency_Info>");
  		File file = new File("C:\\Users\\Administrator\\Desktop\\EBM_201906202026.xml");
  		String xx = file2String(file,"utf-8");
  		xx = HtmlUtils.htmlUnescape(xx);
  		boolean string2File = string2File(xx,file.getAbsolutePath());
  		System.out.print(xx);
  		System.out.println(string2File);
  	}
  	
  	 public static boolean string2File(String res, String filePath) { 
         boolean flag = true; 
         BufferedReader bufferedReader = null; 
         BufferedWriter bufferedWriter = null; 
         try { 
                 File distFile = new File(filePath); 
                 if (!distFile.getParentFile().exists()) distFile.getParentFile().mkdirs(); 
                 bufferedReader = new BufferedReader(new StringReader(res)); 
                 bufferedWriter = new BufferedWriter(new FileWriter(distFile)); 
                 char buf[] = new char[1024];         //字符缓冲区 
                 int len; 
                 while ((len = bufferedReader.read(buf)) != -1) { 
                         bufferedWriter.write(buf, 0, len); 
                 } 
                 bufferedWriter.flush(); 
                 bufferedReader.close(); 
                 bufferedWriter.close(); 
         } catch (IOException e) { 
                 e.printStackTrace(); 
                 flag = false; 
                 return flag; 
         } finally { 
                 if (bufferedReader != null) { 
                         try { 
                                 bufferedReader.close(); 
                         } catch (IOException e) { 
                                 e.printStackTrace(); 
                         } 
                 } 
         } 
         return flag; 
 }
  	 
  	public static String file2String(File file, String encoding) { 
        InputStreamReader reader = null; 
        StringWriter writer = new StringWriter(); 
        try { 
                if (encoding == null || "".equals(encoding.trim())) { 
                        reader = new InputStreamReader(new FileInputStream(file), encoding); 
                } else { 
                        reader = new InputStreamReader(new FileInputStream(file)); 
                } 
                //将输入流写入输出流 
                char[] buffer = new char[1024]; 
                int n = 0; 
                while (-1 != (n = reader.read(buffer))) { 
                        writer.write(buffer, 0, n); 
                } 
        } catch (Exception e) { 
                e.printStackTrace(); 
                return null; 
        } finally { 
                if (reader != null) 
                        try { 
                                reader.close(); 
                        } catch (IOException e) { 
                                e.printStackTrace(); 
                        } 
        } 
        //返回转换结果 
        if (writer != null) 
                return writer.toString(); 
        else return null; 
}

}
