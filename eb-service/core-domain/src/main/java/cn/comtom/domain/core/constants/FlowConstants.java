package cn.comtom.domain.core.constants;


/**
 * 调度流程变量定义
 * 
 * @author zhucanhui
 *
 */
public class FlowConstants {

	/**
	 * 调度阶段定义
	 * 1-预警接入
	 * 2-预警响应
	 * 3-预警处理
	 * 4-广播播发
	 * 5-预警完成
	 * 6-预案匹配
	 */
	public static final String STAGE_STARTING = "1";
	public static final String STAGE_RESPONSE ="2";
	public static final String STAGE_PROCESS = "3";
	public static final String STAGE_PLAYING = "4";
	public static final String STAGE_COMPLETE = "5";
//	public static final String STAGE_CANCEL = "5";

	/**
	 * 调度状态定义
	 */
	// 预警触发 11:节目制作 12:节目审核 <br>
	public static final String STATE_INFO = "11";
	//public static final String STATE_INFO_AUDIT = "12";
	public static final String STATE_INFO_AUDIT = "12";
	/*public static final String STATE_INFO_AUDIT_NO = "14";*/

	// 预警响应
	/**
	 * 	方案生成
	 */
	public static final String STATE_SCHEME_CREATE = "21";
	/**
	 * 	方案优化
	 */
	public static final String STATE_SCHEME_OPTIMIZE = "22";
	/**
	 * 	方案调整
	 */
	public static final String STATE_SCHEME_ADJUST = "23";
	/**
	 * 方案审核
	 */
	public static final String STATE_SCHEME_AUDIT = "24";

	// 预警处理
	/**
	 * 消息生成
	 */
	public static final String STATE_MSG_CREATE = "31";

	/**
	 * 消息审核
	 */
	public static final String STATE_MSG_AUDIT = "32";
	/**
	 * 消息分发
	 */
	public static final String STATE_MSG_SEND = "33";
	/**
	 *
	 */
	public static final String STATE_MSG_RELEASE = "34";

	// 广播播发
	public static final String STATE_PLAYING_WAIT = "41";
	public static final String STATE_PLAYING = "42";

	// 预警完成
	public static final String STATE_COMPLETE_SUCCESS = "51";
	public static final String STATE_COMPLETE_FAILE = "52";
	public static final String STATE_COMPLETE_CANCEL = "53";

	/**
	 * 预案匹配，未匹配
	 */
	//public static final String PLAN_MATCH= "61";

	/**
	 * 事件级别
	 */
	public static final String SEVERITY_0 = "0";
	public static final String SEVERITY_1 = "1";
	public static final String SEVERITY_2 = "2";
	public static final String SEVERITY_3 = "3";
	public static final String SEVERITY_4 = "4";
	public static final String SEVERITY_10 = "10";
	public static final String SEVERITY_15 = "15";

	public static final String SCHEME_NAME_PREFIX = "调度方案-";


	public static final String FLOW_TYPE_1 = "1";
	public static final String FLOW_TYPE_2 = "2";


}
