package cn.comtom.tools.utils;

import java.io.*;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sound.midi.Soundbank;

public class FtpUtil {

    protected static Logger log = LoggerFactory.getLogger(FtpUtil.class);

    //ftp服务器地址
    private static String hostname;
    //ftp服务器端口号
    private static Integer port;
    //ftp登录账号
    private static String username;
    //ftp登录密码
    private static String password;

    public FtpUtil(String hostname, Integer port, String username, String password) {
        this.hostname = hostname;
        this.port = port;
        this.username = username;
        this.password = password;
    }

    /**
     * 初始化ftp服务器
     */
    public static FTPClient initFtpClient() {
        try {
            FTPClient ftpClient = new FTPClient();
            log.info("connecting...ftp服务器:" + hostname + ":" + port);
            ftpClient.connect(hostname, port); //连接ftp服务器
            ftpClient.login(username, password); //登录ftp服务器
            int replyCode = ftpClient.getReplyCode(); //是否成功登录服务器
            if (!FTPReply.isPositiveCompletion(replyCode)) {
                log.error("connect failed...ftp服务器:" + hostname + ":" + port);
                return null;
            }
            String localChar = "GBK";
            if (FTPReply.isPositiveCompletion(ftpClient.sendCommand("OPTS UTF8", "ON"))) {
                localChar = "UTF-8";
            }
            ftpClient.setControlEncoding(localChar);
            log.info("connect successfu...ftp服务器:" + hostname + ":" + port);
            return ftpClient;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 上传文件
     *
     * @param pathname       ftp服务保存地址
     * @param fileName       上传到ftp的文件名
     * @param originfilename 待上传文件的名称（绝对地址） *
     * @return
     */
    public boolean uploadFile(String pathname, String fileName, String originfilename) {
        boolean flag = false;
        InputStream inputStream = null;
        FTPClient ftpClient = initFtpClient();
        try {
            log.info("开始上传文件");
            inputStream = new FileInputStream(new File(originfilename));
            ftpClient.setFileType(ftpClient.BINARY_FILE_TYPE);
            if (!CreateDirecroty(pathname, ftpClient)) {
                log.info("上传文件失败：文件目录：{}创建失败", pathname);
            }
            ftpClient.enterLocalPassiveMode();
            flag = ftpClient.storeFile(charsetISO(fileName), inputStream);
            if (flag) {
                log.info("上传文件成功");
            } else {
                flag = false;
                log.info("上传文件失败");
            }
        } catch (Exception e) {
            log.error("上传文件失败", e);
            return false;
        } finally {
            try {
                if (ftpClient.isConnected()) {
                    ftpClient.disconnect();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                log.error("FTPUtil回收资源出错", e);
            }
        }
        return flag;
    }

    /**
     * 上传文件
     *
     * @param pathname    ftp服务保存地址
     * @param fileName    上传到ftp的文件名
     * @param inputStream 输入文件流
     * @return
     */
    public boolean uploadFile(String pathname, String fileName, InputStream inputStream) {
        boolean flag = false;
        FTPClient ftpClient = initFtpClient();
        try {
            log.info("开始上传文件");
            ftpClient.setFileType(ftpClient.BINARY_FILE_TYPE);
            if (!CreateDirecroty(pathname, ftpClient)) {
                log.info("上传文件失败：文件目录：{}创建失败", pathname);
            }
            ftpClient.enterLocalPassiveMode();
            flag = ftpClient.storeFile(charsetISO(fileName), inputStream);
            inputStream.close();
            if (flag) {
                log.info("上传文件成功");
            } else {
                log.info("上传文件失败");
                return false;
            }
        } catch (Exception e) {
            log.error("上传文件失败", e);
        } finally {
            try {
                if (ftpClient.isConnected()) {
                    ftpClient.disconnect();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                log.error("FTPUtil回收资源出错", e);
            }
        }
        return true;
    }

    //改变目录路径
    public static boolean changeWorkingDirectory(String directory, FTPClient ftpClient) {
        boolean flag = false;
        try {
            flag = ftpClient.changeWorkingDirectory(charsetISO(directory));
            if (flag) {
                log.info("进入文件夹" + directory + " 成功！");
            } else {
                log.info("进入文件夹" + directory + " 失败！开始创建文件夹");
            }
        } catch (IOException ioe) {
            log.error(ioe.getMessage(), ioe);
        }
        return flag;
    }

    //创建多层目录文件，如果有ftp服务器已存在该文件，则不创建，如果无，则创建
    public static boolean CreateDirecroty(String remote, FTPClient ftpClient) throws IOException {
        String directory = remote + "/";
        // 如果远程目录不存在，则递归创建远程服务器目录
        if (!directory.equalsIgnoreCase("/") && !changeWorkingDirectory(directory, ftpClient)) {
            int start = 0;
            int end = 0;
            if (directory.startsWith("/")) {
                start = 1;
            } else {
                start = 0;
            }
            end = directory.indexOf("/", start);
            String path = "";
            String paths = "";
            while (true) {
                String subDirectory = remote.substring(start, end);
                path = path + "/" + subDirectory;
                //切换到指定目录，如果切换失败证明目录不存在需要创建
                if (!changeWorkingDirectory(path, ftpClient)) {
                    if (makeDirectory(subDirectory, ftpClient)) {
                        changeWorkingDirectory(subDirectory, ftpClient);
                    } else {
                        log.info("创建目录:[{}]失败", path);
                        return false;
                    }
                }

                paths = paths + "/" + subDirectory;
                start = end + 1;
                end = directory.indexOf("/", start);
                // 检查所有目录是否创建完毕
                if (end <= start) {
                    break;
                }
            }
        }
        return true;
    }

    //判断ftp服务器文件是否存在
    public static boolean existFile(String path, FTPClient ftpClient) throws IOException {
        boolean flag = false;
        FTPFile[] ftpFileArr = ftpClient.listFiles(charsetISO(path));
        if (ftpFileArr.length > 0) {
            flag = true;
        }
        return flag;
    }

    //创建目录
    public static boolean makeDirectory(String dir, FTPClient ftpClient) {
        boolean flag = false;
        try {
            flag = ftpClient.makeDirectory(charsetISO(dir));
            if (flag) {
                log.info("创建文件夹:{}成功！", dir);
            } else {
                log.info("创建文件夹:{}失败！", dir);
            }
        } catch (Exception e) {
            log.error("创建文件夹:{}错误！", dir, e);
        }
        return flag;
    }

    /**
     * 下载文件 *
     *
     * @param pathname  FTP服务器文件目录 *
     * @param filename  文件名称 *
     * @param localpath 下载后的文件路径 *
     * @return
     */
    public List<File> downloadFile(String pathname, String filename, String localpath) {
        OutputStream os = null;
        List<File> fileList = new ArrayList<>();
        FTPClient ftpClient = initFtpClient();
        try {
            log.info("FTP开始下载文件filename:{},path:{}", filename, pathname);
            initFtpClient();
            //切换FTP目录，切换失败时报错
            if(!ftpClient.changeWorkingDirectory(charsetISO(pathname))) {
                log.info("FTP下载文件错误，无法切换到此路径：{}", pathname);
                return null;
            }
            ftpClient.enterLocalPassiveMode();
            FTPFile[] ftpFiles = ftpClient.listFiles();
            for (FTPFile file : ftpFiles) {
                if (filename.equalsIgnoreCase(file.getName())) {
                    File localFile = new File(localpath + "/" + file.getName());
                    os = new FileOutputStream(localFile);
                    if (!ftpClient.retrieveFile(charsetISO(file.getName()), os)) {
                        log.info("FTP下载文件：{}不存在", filename);
                        return null;
                    }
                    fileList.add(localFile);
                    os.close();
                }
            }
            log.info("FTP下载文件成功filename:{},path:{}", filename, pathname);
        } catch (Exception e) {
            log.error("FTP下载文件失败filename:{},path:{}", filename, pathname, e);
        } finally {
            try {
                if (ftpClient.isConnected()) {
                    ftpClient.disconnect();
                }
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                log.error("FTPUtil下载文件回收资源出错", e);
            }
        }
        return fileList;
    }

    /**
     * 下载文件 *
     *
     * @param ftpPathname  FTP服务器文件目录 *
     * @param ftpFilename  文件名称 *
     * @param localpath 下载后的文件路径 *
     * @return
     */
    public List<File> downloadFile(String ftpPathname, String ftpFilename, String localpath, String localFileName) {
        OutputStream os = null;
        List<File> fileList = new ArrayList<>();
        FTPClient ftpClient = initFtpClient();
        try {
            log.info("FTP开始下载文件filename:{},path:{},localpath:{},localFileName:{}", ftpFilename, ftpPathname, localpath, localFileName);
            initFtpClient();
            //切换FTP目录，切换目录失败报错
            if (!ftpClient.changeWorkingDirectory(charsetISO((ftpPathname)))) {
                log.info("FTP下载文件错误，无法切换到此路径：{}", ftpPathname);
                return null;
            }
            ftpClient.enterLocalPassiveMode();
            FTPFile[] ftpFiles = ftpClient.listFiles();
            for (FTPFile file : ftpFiles) {
                if (ftpFilename.equalsIgnoreCase(file.getName())) {
                    File localFile = new File(localpath + "/" + localFileName);
                    os = new FileOutputStream(localFile);
                    log.info("FTP保存本地文件：localpath：{}，localFileName:{}", localFile.getPath(), localFile.getName());
                    if (!ftpClient.retrieveFile(charsetISO(file.getName()), os)) {
                        log.info("文件：{}不存在", ftpFilename);
                        return null;
                    }
                    fileList.add(localFile);
                    os.close();
                }
            }
            log.info("FTP下载文件成功filename:{},path:{}", ftpFilename, ftpPathname);
        } catch (Exception e) {
            log.error("FTP下载文件失败filename:{},path:{}", ftpFilename, ftpPathname, e);
        } finally {
            try {
                if (ftpClient.isConnected()) {
                    ftpClient.disconnect();
                }
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                log.error("FTPUtil下载文件回收资源出错", e);
            }
        }
        return fileList;
    }

    /**
     * 下载文件 *
     *
     * @param pathname FTP服务器文件目录 *
     * @param filename 文件名称 *
     * @param os       输出流 *
     * @return
     */
    public List<File> downloadFileToOutputStream(OutputStream os, String pathname, String filename) {
        List<File> fileList = new ArrayList<>();
        FTPClient ftpClient = initFtpClient();
        try {
            log.info("开始下载文件");
            initFtpClient();
            //切换FTP目录
            if (!ftpClient.changeWorkingDirectory(charsetISO(pathname))) {
                log.info("FTP下载文件错误，无法切换到此路径：{}", pathname);
                return null;
            }
            ftpClient.enterLocalPassiveMode();
            FTPFile[] ftpFiles = ftpClient.listFiles();
            for (FTPFile file : ftpFiles) {
                if (filename.equalsIgnoreCase(file.getName())) {
                    if (!ftpClient.retrieveFile(charsetISO(file.getName()), os)) {
                        log.info("文件：{}不存在", filename);
                        return null;
                    }
                    os.flush();
                    os.close();
                }
            }
            log.info("下载文件成功");
        } catch (Exception e) {
            log.error("下载文件失败", e);
        } finally {
            try {
                if (ftpClient.isConnected()) {
                    ftpClient.disconnect();
                }
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                log.error("FTPUtil下载文件回收资源出错", e);
            }
        }
        return fileList;
    }


    /**
     * 删除文件 *
     *
     * @param pathname FTP服务器保存目录 *
     * @param filename 要删除的文件名称 *
     * @return
     */
    public boolean deleteFile(String pathname, String filename) {
        boolean flag = false;
        FTPClient ftpClient = initFtpClient();
        try {
            log.info("开始删除文件");
            //切换FTP目录
            if (!ftpClient.changeWorkingDirectory(charsetISO(pathname))) {
                log.info("FTP删除文件错误，无法切换到此路径：{}", pathname);
                return false;
            }
            ftpClient.dele(charsetISO(filename));
            flag = true;
            log.info("删除文件成功");
        } catch (Exception e) {
            log.error("删除文件失败", e);
        } finally {
            if (ftpClient.isConnected()) {
                try {
                    ftpClient.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return flag;
    }

    /**
     * 将字符串转为FTP服务器编码格式
     * @param str
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String charsetISO(String str) throws UnsupportedEncodingException {
        return new String(str.getBytes(), "ISO-8859-1");
    }

    public static void main(String[] args) throws IOException {
        FtpUtil ftp = new FtpUtil("192.168.111.100", 21, "ctctct", "123");
        FTPClient ftpClient = FtpUtil.initFtpClient();
        ArrayList<String> a = new ArrayList<>();
        a.add(null);
        System.out.println(a.size());
//        System.out.println(ftpClient.makeDirectory(new String("/var/ftp/file/节目制作上传文件夹".getBytes(), "ISO-8859-1")));
//        ftp.downloadFile("/var/ftp/pub/dist/receive", "EBDB_100101430000000000010000000000001702.tar", "F:/doc/download/123/");
        //ftp.downloadFile("/var/ftp/pub", "123.mp3", "F:/doc/download/");
        //  ftp.deleteFile("/var/ftp/pub", "123.mp3");
        System.out.println("ok");
      /*  StringBuffer str = new StringBuffer("0");
        addOne(str);
        System.out.println(str.toString());*/
    }

    public static void addOne(StringBuffer obj) {
        BigDecimal bigDecimal = new BigDecimal(obj.toString());
        bigDecimal = bigDecimal.add(new BigDecimal(1));
        obj = new StringBuffer(bigDecimal.toString());
        obj.append("1");
    }
}
