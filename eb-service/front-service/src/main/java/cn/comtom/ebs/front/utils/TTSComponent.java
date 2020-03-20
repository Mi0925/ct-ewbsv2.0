package cn.comtom.ebs.front.utils;

import cn.comtom.domain.system.sysparam.info.SysParamsInfo;
import cn.comtom.ebs.front.fegin.service.impl.SystemFeginServiceImpl;
import cn.comtom.tools.constants.Constants;
import cn.comtom.tools.utils.FileUtils;
import cn.comtom.tools.utils.UUIDGenerator;
import com.baidu.aip.speech.AipSpeech;
import com.baidu.aip.speech.TtsResponse;
import com.baidu.aip.util.Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

/**
 * 调用百度sdk，实现语音合成功能（文转语）
 * @author:WJ
 * @date: 2019/4/9 0009
 * @time: 下午 5:45
 */
@Slf4j
@Component
public class TTSComponent {


    @Autowired
    private SystemFeginServiceImpl systemFeginService;

    @Value("${baidu.tts.appid}")
    private  String APP_ID = "";

    @Value("${baidu.tts.apikey}")
    private  String API_KEY = "";

    @Value("${baidu.tts.secretkey}")
    private  String SECRET_KEY = "";

    private  AipSpeech client=null;

    public  AipSpeech getInstance(){
        if(client==null){
            client=new AipSpeech(APP_ID, API_KEY, SECRET_KEY);
            // 可选：设置网络连接参数
            client.setConnectionTimeoutInMillis(2000);
            client.setSocketTimeoutInMillis(60000);
            // 可选：设置代理服务器地址, http和socket二选一，或者均不设置
    //        client.setHttpProxy("proxy_host", proxy_port);  // 设置http代理
    //        client.setSocketProxy("proxy_host", proxy_port);  // 设置socket代理
                // 可选：设置log4j日志输出格式，若不设置，则使用默认配置
                // 也可以直接通过jvm启动参数设置此环境变量
    //        System.setProperty("aip.log4j.conf", "path/to/your/log4j.properties");
        }
        return client;
    }

    public  boolean synthesis(String text,String output)
    {
        // 设置可选参数
        HashMap<String, Object> options = new HashMap<String, Object>();
        options.put("spd", "5");
        options.put("pit", "5");
        options.put("per", "0");
        TtsResponse res = client.synthesis(text, "zh", 1, options);
        //服务器返回的内容，合成成功时为null,失败时包含error_no等信息
        org.json.JSONObject result = res.getResult();
        if(result==null){
            byte[] data = res.getData();
            if (data != null) {
                try {
                    Util.writeBytesToFileSystem(data, output);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return true;
            }
        }else{
            log.error("文转语失败！！！！"+result.toString(2));
        }
        log.info("文转语结束-----------");
        return false;
    }


    /**
     * 文转语获取时长
     * @param content
     * @return
     */
    public Integer getProgramContentSpeechLength(String content) {
        try {
            //文转语音，计算播放需要的时长
            this.getInstance();
            SysParamsInfo temp_file_path_ = systemFeginService.getByKey(Constants.TEMP_FILE_PATH);
            String dirPath=temp_file_path_.getParamValue()+"/tts/";
            File dir=new File(dirPath);
            if(!dir.exists())dir.mkdir();
            String filePath=dirPath+UUIDGenerator.getUUID()+".mp3";
            boolean flag = this.synthesis(content, filePath);
            if(flag){
                File file=new File(filePath);
                Long duration = FileUtils.getDuration(file);
                return duration.intValue();
            }
        }catch (Exception e){
        }
        return 0;
    }

    public String getTextToAudioFilePath(String content) {
        try {
            //文转语音，计算播放需要的时长
            this.getInstance();
            SysParamsInfo temp_file_path_ = systemFeginService.getByKey(Constants.TEMP_FILE_PATH);
            String dirPath=temp_file_path_.getParamValue()+"/audio/";
            File dir=new File(dirPath);
            if(!dir.exists())dir.mkdir();
            String filePath=dirPath+UUIDGenerator.getUUID()+".mp3";
            boolean flag = this.synthesis(content, filePath);
            if(flag){
                File file=new File(filePath);
                return file.getAbsolutePath();
            }
        }catch (Exception e){
        	e.printStackTrace();
        }
        return null;
    }

    public File getTextToAudioFile(String content) {
        try {
            //文转语音，计算播放需要的时长
            this.getInstance();
            SysParamsInfo temp_file_path_ = systemFeginService.getByKey(Constants.TEMP_FILE_PATH);
            String dirPath=temp_file_path_.getParamValue()+"/audio/";
            File dir=new File(dirPath);
            if(!dir.exists())dir.mkdir();
            String filePath=dirPath+UUIDGenerator.getUUID()+".mp3";
            boolean flag = this.synthesis(content, filePath);
            if(flag){
                File file=new File(filePath);
                return file;
            }
        }catch (Exception e){
        	e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
       // TTSUtil.getInstance();
        //TTSUtil.synthesis("百度你好，百度你好；","E:\\workTemp\\output.mp3");
    }


}
