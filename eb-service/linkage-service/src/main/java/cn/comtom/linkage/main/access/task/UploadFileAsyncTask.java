/*
package cn.comtom.linkage.main.access.task;

import cn.comtom.linkage.main.mqListener.MQMessageProducer;
import cn.comtom.tools.constants.MqQueueConstant;
import cn.comtom.tools.utils.FtpUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

*/
/**
 * Create By wujiang on 2018/11/16
 *//*

@Component
@Slf4j
public class UploadFileAsyncTask {

    //@Value("${file.ftp.hostname}")
    private  String hostname ;

    //@Value("${file.ftp.port}")
    private  Integer port  ;

    //@Value("${file.ftp.username}")
    private  String username ;

    //@Value("${file.ftp.password}")
    private  String password ;

    @Autowired
    private MQMessageProducer mqMessageProducer;

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Async("myTaskAsyncPool")  //myTaskAsynPool即配置线程池的方法名，此处如果不写自定义线程池的方法名，会使用默认的线程池
    public void doTask(String ftpReceiveFilePath,String fileName,String curFilePath) throws InterruptedException{
        if(log.isDebugEnabled()){
            log.info("文件：[{}] 开始上传.",fileName);
        }
        boolean b = new FtpUtil(hostname,port,username,password).uploadFile(ftpReceiveFilePath, fileName, curFilePath);
        if(b){
            if(log.isDebugEnabled()){
                log.info("文件：[{}] 上传完毕.",fileName);
            }
            File file = new File(curFilePath);
            if(file.exists()){
                file.delete();
            }
        }

    }

    @Async("myTaskAsyncPool")  //myTaskAsynPool即配置线程池的方法名，此处如果不写自定义线程池的方法名，会使用默认的线程池
    public void doTask(String ftpReceiveFilePath,String fileName,File file) {
        if(log.isDebugEnabled()){
            log.info("文件：[{}] 开始上传.",fileName);
        }
        Boolean b = false;
        try {
            b = new FtpUtil(hostname,port,username,password).uploadFile(ftpReceiveFilePath, fileName, new FileInputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if(b){
            if(log.isDebugEnabled()){
                log.info("文件：[{}] 上传完毕.",fileName);
            }
            Boolean d = file.delete();
            if(!d){
                if(log.isDebugEnabled()){
                    log.info("文件：[{}] 删除失败.",file.getName());
                }
            }
        }
    }

    @Async("myTaskAsyncPool")
    public void doTaskAndSendMsg(String ftpReceiveFilePath, String fileName, String curFilePath,String ebdId) throws Exception{
        if(log.isDebugEnabled()){
            log.info("文件：[{}] 开始上传.",fileName);
        }
        boolean b = new FtpUtil(hostname,port,username,password).uploadFile(ftpReceiveFilePath, fileName, curFilePath);
        if(b){
            if(log.isDebugEnabled()){
                log.info("文件：[{}] 上传完毕.",fileName);
                log.info("创建执行消息分发的MQ消息：ebdId=[{}].",ebdId);
            }
            log.info("EBMBeforeDispatch】~发送ebdId到MQ,ebdId:{}",ebdId);
            mqMessageProducer.sendData(MqQueueConstant.EBM_DISPATCH_QUEUE,ebdId);

            File file = new File(curFilePath);
            if(file.exists()){
                file.delete();
            }
        }
    }
}
*/
