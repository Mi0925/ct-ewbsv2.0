package cn.comtom.linkage.main.access.model.ebd.details.other;

import cn.comtom.linkage.main.access.model.ebd.commom.AccessRecord;
import lombok.Data;

/**
 * @author nobody
 * 数据请求结果反馈
 */
@Data
public class EBDResponse {

	/**
	 * 执行结果代码：
	 *  0: 收到数据未处理
	 *  1：接收解析及数据校验成功
	 *	2：接收解析失败
	 *  3：数据内容缺失
	 *  4: 签名验证失败
	 *  5：其他错误
	 *  对于执行结果代码为2-5的情况下,不会再进一步处理及发送相应的业务数据。
	 */
	private Integer resultCode;

	/**
	 * 执行结果描述
	 */
	private String resultDesc;

	private AccessRecord accessRecord;
}