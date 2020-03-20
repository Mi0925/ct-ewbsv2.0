package cn.comtom.core.main.sichuan.dao;

import java.io.File;

public interface SichuanXMLDao {

	/**
	 * 对XML文件进行解析
	 * @param filePath 需要进行解析的XML文件的存放路径
	 * 返回解析后的EBD对象
	 */
	public Object analyze(File filePath);
	
	
}
