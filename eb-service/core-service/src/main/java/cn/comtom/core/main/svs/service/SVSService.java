package cn.comtom.core.main.svs.service;

/**
 * 大屏展示接口通知
 * @author lhy
 *
 */
public interface SVSService {

	/**
	 * 最新ebm消息通知推送
	 */
	void callEbmNotice();
	
	/**
	 * 最新资源状态更新通知推送
	 */
	void callEbrState();

	/**
	 * 字幕插播视频流通知
	 */
	void callDVBNotice();
}
