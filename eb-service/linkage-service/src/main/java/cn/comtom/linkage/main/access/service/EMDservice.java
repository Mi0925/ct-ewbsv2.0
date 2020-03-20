package cn.comtom.linkage.main.access.service;


import cn.comtom.linkage.main.access.model.ebd.EBD;

import java.io.File;
import java.util.List;

/**
 * @author nobody
 * 应急消息报服务接口
 */
public interface EMDservice {
	
	public String serviceType();
	
	public void service(EBD ebd, List<File> resourceFiles);

	public void preservice(EBD ebd, String ebdName, List<File> resourceFiles);

	public void afterservice(EBD ebd, String ebdName);
	
}
