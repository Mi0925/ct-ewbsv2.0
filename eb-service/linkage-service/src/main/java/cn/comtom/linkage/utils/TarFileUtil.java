package cn.comtom.linkage.utils;

import cn.comtom.linkage.main.access.model.ebd.EBD;
import cn.comtom.tools.constants.SymbolConstants;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TarFileUtil {

	private final static Logger logger = LoggerFactory.getLogger(TarFileUtil.class);

	/**
	 * 将tar文件解压缩
	 * 
	 * @param tarFile
	 *            解压缩后的路径
	 * @return
	 */
	public static List<File> decompressorsTar(File tarFile) {
		List<File> result = new ArrayList<File>();
		FileInputStream in = null;
		ArchiveInputStream araIn = null;
		BufferedInputStream bufferedInputStream = null;
		try {
			String filePath = tarFile.getAbsolutePath();
			filePath = filePath.substring(0, filePath.length() - 4);
			File path = new File(filePath);
			if (!path.exists()) {
				path.mkdirs();
			}
			in = new FileInputStream(tarFile);
			araIn = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.TAR,
					in);
			bufferedInputStream = new BufferedInputStream(araIn);
			ArchiveEntry entry = araIn.getNextEntry();
			while (entry != null) {
				String entryFileName = entry.getName();
				File entryFile = new File(filePath + SymbolConstants.FILE_SPLIT + entryFileName);
				if (entryFileName.endsWith("/")) {
					if (!entryFile.exists()) {
						entryFile.mkdirs();
					}
				} else {
					if (!entryFile.exists()) {
						entryFile.createNewFile();
					}
				}
				BufferedOutputStream bufferedOutputStream = null;
				try {
					bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(entryFile));
					byte[] buffer = new byte[1024];
					int len = -1;
					while ((len = bufferedInputStream.read(buffer)) != -1) {
						bufferedOutputStream.write(buffer, 0, len);
					}
					bufferedOutputStream.flush();
					bufferedOutputStream.close();
					result.add(entryFile);
				} catch (Exception e) {
					logger.error(e.getStackTrace().toString());
					e.printStackTrace();
				} finally {
					IOUtils.closeQuietly(bufferedOutputStream);
				}
				entry = (TarArchiveEntry) araIn.getNextEntry();
			}
		} catch (Exception e) {
			logger.error(e.getStackTrace().toString());
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(in);
			IOUtils.closeQuietly(bufferedInputStream);
		}
		return result;
	}

	/**
	 * 将文件压缩成tar包
	 * 
	 * @param files
	 * @param filePath
	 * @return
	 */
	public static File compressorsTar(List<File> files, String filePath) {
		File path = new File(filePath);
		if (!path.exists()) {
			path.mkdirs();
		}
		String fileName = FileNameUtil.generateTarFileName();
		return compressorsTar(files,filePath,fileName);
	}

	/**
	 * 将文件压缩成tar包，文件名为前缀+EBDID
	 * 
	 * @param files
	 * @param filePath
	 * @return
	 */
	public  static File compressorsTar(EBD ebd, List<File> files, String filePath) {
		File path = new File(filePath);
		if (!path.exists()) {
			path.mkdirs();
		}
		String fileName = FileNameUtil.generateTarFileName(ebd.getEBDID());
		return compressorsTar(files,filePath,fileName);
	}

	/**
	 * 将文件压缩成tar包
	 *
	 * @param files
	 * @param filePath
	 * @return
	 */
	public static File compressorsTar(List<File> files, String filePath,String fileName) {
		File path = new File(filePath);
		if (!path.exists()) {
			path.mkdirs();
		}
		File tarFile = new File(filePath + SymbolConstants.FILE_SPLIT + fileName);
		if (!tarFile.exists()) {
			try {
				tarFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				logger.error("create new file error:" + e.getMessage());
			}
		}
		TarArchiveOutputStream tarArchiveOutputStream = null;
		try {
			tarArchiveOutputStream = new TarArchiveOutputStream(new FileOutputStream(tarFile));
			for (File file : files) {
				BufferedInputStream in = null;
				try {
					in = new BufferedInputStream(new FileInputStream(file));
					TarArchiveEntry entry = new TarArchiveEntry(file);
					entry.setSize(file.length());
					entry.setName(file.getName());
					tarArchiveOutputStream.putArchiveEntry(entry);
					IOUtils.copy(in, tarArchiveOutputStream);
					tarArchiveOutputStream.flush();
					tarArchiveOutputStream.closeArchiveEntry();
				} catch (Exception e) {
					logger.error(e.getStackTrace().toString());
					e.printStackTrace();
				} finally {
					IOUtils.closeQuietly(in);
				}
			}
		} catch (IOException e) {
			logger.error(e.getStackTrace().toString());
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(tarArchiveOutputStream);
		}
		return tarFile;
	}

/*
	public static void main(String[] args) {

		Date date = new Date();
		DateTimeUtil.dateToString(date, "yyyyMMddHHmm");

		String pathname = "D:\\temp\\EBDT_100101320000000000010000000000000001.tar";
		File file = new File(pathname);
		List<File> reslut = decompressorsTar(file);
		System.err.println(reslut);
		compressorsTar(reslut, "D:\\temp2");
	}*/

}
