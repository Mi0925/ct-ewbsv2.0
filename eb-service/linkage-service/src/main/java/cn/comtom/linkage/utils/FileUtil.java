package cn.comtom.linkage.utils;

import cn.comtom.linkage.commons.EBDRespResultEnum;
import cn.comtom.linkage.commons.EbmException;
import cn.comtom.linkage.main.access.model.ebd.EBD;
import cn.comtom.linkage.main.access.model.signature.Signature;
import cn.comtom.tools.constants.SymbolConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import java.io.*;

/**
 * @author nobody 文件工具类
 */
@Slf4j
public class FileUtil {

	/**
	 * EBD将对象转换为文件
	 * 
	 * @param filePath
	 * @param ebd
	 * @return
	 */
	public static File converFile(String filePath, EBD ebd) {
		if (ebd == null) {
			throw new EbmException(EBDRespResultEnum.notprocessed, "ebd对象为空");
		}
		File tarDir = new File(filePath);
		if (!tarDir.exists()) {
			tarDir.mkdirs();
		}
		String xml = XmlUtil.toXml(ebd);
		xml = XmlUtil.xmlPre + System.getProperty(("line.separator")) + xml;
		String fileName = FileNameUtil.createEBDBName(ebd.getEBDID());
		return converFile(filePath,fileName,xml);
	}
	public static void main(String[] args) {
		EBD ebd = new EBD();
		String xml = XmlUtil.toXml(ebd);
	}
	public static File converFile(String filePath,EBD ebd, String fileName) {

		if (ebd == null) {
			throw new EbmException(EBDRespResultEnum.notprocessed, "ebd对象为空");
		}

		File tarDir = new File(filePath);
		if (!tarDir.exists()) {
			tarDir.mkdirs();
		}
		String xml = XmlUtil.toXml(ebd);
		xml = XmlUtil.xmlPre + System.getProperty(("line.separator")) + xml;
		return converFile(filePath,fileName,xml);
	}

	/**
	 * Signature将对象转换为文件
	 * 
	 * @param filePath
	 * @param signature
	 * @return
	 */
	public static File converFile(String filePath, Signature signature) {
		if (signature == null) {
			throw new EbmException(EBDRespResultEnum.notprocessed, "signature对象为空");
		}
		File tarDir = new File(filePath);
		if (!tarDir.exists()) {
			tarDir.mkdirs();
		}
		String xml = XmlUtil.toSignXml(signature);
		xml = XmlUtil.xmlPre + System.getProperty(("line.separator")) + xml;
		String fileName = FileNameUtil.createEBDSName(signature.getRelatedEBD().getEBDID());
		return converFile(filePath,fileName,xml);
	}

	/**
	 * 将字符串保存文件
	 * 
	 * @param filePath
	 * @param fileName
	 * @param fileInfo
	 * @return
	 */
	public static File converFile(String filePath, String fileName, String fileInfo) {
		File tarDir = new File(filePath);
		if (!tarDir.exists()) {
			tarDir.mkdirs();
		}
		File file = new File(filePath + SymbolConstants.FILE_SPLIT + fileName);
		FileOutputStream out = null;
		try {
			file.createNewFile();
			out = new FileOutputStream(file);
			IOUtils.write(fileInfo, out, "utf-8");
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(out);
		}
		return file;
	}

	/**
	 * 将输入流保存
	 * 
	 * @param filePath
	 * @param fileName
	 * @param inputStream
	 * @return
	 */
	public static File converFile(String filePath, String fileName, InputStream inputStream) {
		File tarDir = new File(filePath);
		if (!tarDir.exists()) {
			tarDir.mkdirs();
		}
		File flie = new File(filePath + SymbolConstants.FILE_SPLIT + fileName);
		FileOutputStream out = null;
		if (!flie.exists()) {
			try {
				flie.createNewFile();
				out = new FileOutputStream(flie);
				BufferedInputStream bin = new BufferedInputStream(inputStream);
				int size = 0;
				byte[] buf = new byte[1024];
				while ((size = bin.read(buf)) != -1) {
					out.write(buf, 0, size);
				}
				out.flush();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				IOUtils.closeQuietly(out);
			}
		}
		return flie;
	}

	/**
	 * 文件对象转字节数组
	 * @param file
	 * @return
	 */
	public static byte[] file2bytes(File file){
		byte[] buffer = null;
		FileInputStream fis = null;
		ByteArrayOutputStream bos = null;
		try {
			fis = new FileInputStream(file);
			bos = new ByteArrayOutputStream(1000);
			byte[] b = new byte[1000];
			int n;
			while ((n = fis.read(b)) != -1) {
				bos.write(b, 0, n);
			}
			buffer = bos.toByteArray();
		} catch (Exception e) {
			log.error(e.getMessage());
		} finally{
			try {
				fis.close();
				bos.close();
			} catch (IOException e) {
				log.error(e.getMessage());
			}

		}
		return buffer;
	}
	
	/**
	 * 删除目录
	 * @param file
	 */
	public static void deleteFile(File file){
        //判断文件不为null或文件目录存在
        if (file == null || !file.exists()){
            return;
        }
        //取得这个目录下的所有子文件对象
        File[] files = file.listFiles();
        //遍历该目录下的文件对象
        for (File f: files){
            //打印文件名
            String name = file.getName();
            //判断子目录是否存在子目录,如果是文件则删除
            if (f.isDirectory()){
                deleteFile(f);
            }else {
                f.delete();
            }
        }
        //删除空文件夹  for循环已经把上一层节点的目录清空。
        file.delete();
    }

}
