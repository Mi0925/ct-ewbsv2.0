package cn.comtom.core.main.utils;

import cn.comtom.tools.constants.SymbolConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

/**
 * @author nobody 文件工具类
 */
@Slf4j
public class FileUtil {

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

	public static byte[] file2bytes(MultipartFile file){
		byte[] buffer = null;
		InputStream fis = null;
		ByteArrayOutputStream bos = null;
		try {
			fis = file.getInputStream();
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

}
