package cn.comtom.linkage.main.access.model.ebd.details.other;

import cn.comtom.linkage.main.access.model.ebd.ebm.Dispatch;
import cn.comtom.linkage.main.access.model.ebd.ebm.MsgBasicInfo;
import cn.comtom.linkage.main.access.model.ebd.ebm.MsgContent;
import cn.comtom.linkage.main.access.model.ebd.ebm.RelatedInfo;
import lombok.Data;

import java.util.List;

/**
 * @author nobody
 * 应急广播消息
 */
@Data
public class EBM {
	
	/**
	 * 应急广播消息协议版本号 目前取值1
	 */
	private String EBMVersion;
	
	/**
	 * 应急广播消息ID
	 * 30位数字码，通过应急广播消息ID区别其他的应急广播消息。
	 * 编码规则：应急广播平台ID(18位)+日期（8位）+顺序码（4位），
	 * 日期格式为YYYYMMDD，YYYY表示年，MM表示月，DD表示日
	 */
	private String EBMID;
	
	
	/**
	 * 关联信息
	 */
	private RelatedInfo relatedInfo;
	
	/**
	 * 消息基本消息(可选)
	 */
	private MsgBasicInfo msgBasicInfo;
	
	/**
	 * 应急广播消息内容
	 */
	private MsgContent msgContent;

	/**
	 * 调度数据(可选)
	 */
	private List<Dispatch> dispatchList;
	

}
