package cn.comtom.domain.core.flow.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Date;

@Data
public class DispatchFlowUpdateRequest implements Serializable {

	/**
	 * 流程（会话）编号
	 */
	@NotEmpty(message = "flowId 不能为空")
	private String flowId;

	/**
	 * 流程阶段（1:预警触发2:预警响应3:预警处理4:预警完成）
	 */
	private String flowStage;

	/**
	 * 流程状态<br>
	 * 预警触发 11:节目制作 12:节目审核 13:审核通过 14:审核不通过<br>
	 * 预警触发 11:接收信息 12:信息检验 13:校验通过 14:校验不通过<br> 
	 * 预警响应 21:方案生成 22:方案优化 23:方案调整 24:方案制作 25方案审核 26:审核通过 27:审核不通过<br>
	 * 预警处理 31:消息下发 32:广播发布 <br>
	 * 预警完成 41:预警完成<br>
	 */
	private String flowState;

	/**
	 * 更新时间
	 */
	private Date updateTime;



}