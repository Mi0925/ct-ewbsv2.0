package cn.comtom.linkage.main.dispatch.service;

import java.util.List;
import java.util.Map;

/**
 * 字幕插播
 * @author liuhy
 *
 */
public interface ITextInsertionService {
	
	/**
	 * 获取频道列表
	 * @return
	 */
	public List<Map> getChannelList();
	
	/**
	 * 获取字体列表
	 * @return
	 */
	public List<Map> getFontList();
	
	/**
	 * 添加应急广播字幕信息
	 * @param param
	 * @return
	 */
	public boolean issueEBMessage(String reqUrl, Map<String,Object> param);
	
	/**
	 * 读取应急广播字幕信息播出记录
	 * @return
	 */
	public List<Map> getEBHistory(Map<String,Object> param);
	
	/**
	 * 读取应急广播信息
	 * @return
	 */
	public List<Map> getEBMessage();
	
}
