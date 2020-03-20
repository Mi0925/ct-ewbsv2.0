package cn.comtom.core.main.sichuan;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.comtom.core.main.sichuan.entity.EBM;


public class Ebm extends EBM{

	/**
	 * 媒体文件
	 */
	private List<File> files = new ArrayList<File>();

	public List<File> getFiles() {
		return files;
	}

	public void setFiles(List<File> files) {
		this.files = files;
	}
}
