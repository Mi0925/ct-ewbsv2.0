package cn.comtom.linkage.main.access.model.ebd.validate;

import java.util.List;

import cn.comtom.linkage.utils.DateStyle;
import org.apache.commons.lang.StringUtils;

import cn.comtom.linkage.main.access.model.ebd.commom.EBRBS;
import cn.comtom.linkage.main.access.model.ebd.details.other.EBMStateResponse;
import cn.comtom.linkage.main.access.model.ebd.ebm.ResBrdItem;

public class EBMStateResponseValidator implements Validator {

	public String validateEntity(Object entity) {
        if(!EBMStateResponse.class.equals(entity.getClass())) {
        	return null;
        }
        
        EBMStateResponse res = (EBMStateResponse) entity;
        if(null == res.getRptTime()) {
        	return "数据操作（生成）时间不能为空";
        } else {
        	if(!ValidatorHelper.isDate(res.getRptTime(), DateStyle.YYYY_MM_DD_HH_MM_SS.getValue())) {
        		return "数据操作（生成）时间格式不符合要求 YYYY-MM-DD HH:MI:SS";
        	}
        }
        if(null == res.getEBM()) {
        	return "所播发的应急广播消息不能为空";
        } else {
        	if(StringUtils.isEmpty(res.getEBM().getEBMID())) {
        		return "所播发的应急广播消息ID不能为空";
        	}
        }
        if(null == res.getBrdStateCode()) {
        	return "播发状态代码不能为空";
        } else {
        	if(!ValidatorHelper.validBroadcastState(res.getBrdStateCode())) {
        		return "播发状态代码系统暂不支持";
        	}
        }
        if(StringUtils.isEmpty(res.getBrdStateDesc())) {
        	return "播发状态的详细描述不能为空";
        }


        if(null != res.getCoverage()) {
        	if(null == res.getCoverage().getCoverageRate()) {
        		return "实际覆盖率不能为空";
        	}
        	/*if(StringUtils.isEmpty(res.getCoverage().getAreaCode())) {
        		return "实际覆盖区域编码不能为空";
        	}*/
        	if(StringUtils.isEmpty(res.getCoverage().getResBrdStat())){
				return "实际调用资源响应统计不能为空";
			}

        }

		// TODO: 2018/12/18 0018 为了测试 暂时去除校验
/*        if(null == res.getResBrdInfo()) {
        	return "调用资源播出数据不能为空";
        } else {
        	if(null == res.getResBrdInfo().getDataList() || res.getResBrdInfo().getDataList().size() < 1) {
        		return "调用资源播出条目不能为空";
        	} else {
                String errorMsg = validResBrdItem(res.getResBrdInfo().getDataList());
                if(null != errorMsg) {
                	return errorMsg;
                }
        	}
        }*/
        
		return null;
	}
	
	private String validResBrdItem(List<ResBrdItem> dataList) {
		String resBrdItemErrMsg = null;
		for(ResBrdItem brdItm : dataList) {
			if(null != brdItm.getEBRST() && StringUtils.isEmpty(brdItm.getEBRST().getEBRID())) {
				resBrdItemErrMsg = "台站（前端）ID不能为空";
				break;
			}
			if(null == brdItm.getEBRAS()) {
				resBrdItemErrMsg = "消息接收设备不能为空";
				break;
			} else {
				if(StringUtils.isEmpty(brdItm.getEBRAS().getEBRID())) {
    				resBrdItemErrMsg = "消息接收设备ID不能为空";
    				break;
				}
			}
			if(null == brdItm.getDataList() || brdItm.getDataList().size() < 1) {
				resBrdItemErrMsg = "调用播出系统播出情况不能为空";
				break;
			} else {
				resBrdItemErrMsg = validEBRBS(brdItm.getDataList());
				if(null != resBrdItemErrMsg) {
					break;
				}
			}
		}
		
		return resBrdItemErrMsg;
	}
	
	private String validEBRBS(List<EBRBS> dataList) {
		String ebrbsErrMsg = null;
		for(EBRBS bs : dataList) {
			if(null == bs.getRptTime()) {
				ebrbsErrMsg = "当前数据的生成时间不能为空";
				break;
			} else {
				if(!ValidatorHelper.isDate(bs.getRptTime(), DateStyle.YYYY_MM_DD_HH_MM_SS.getValue())) {
					ebrbsErrMsg = "当前数据的生成时间格式不符合要求YYYY-MM-DD HH:MI:SS";
					break;
				}
			}
//			if(StringUtils.isEmpty(bs.getBrdSysType())) {
//				ebrbsErrMsg = "播出系统类型不能为空";
//				break;
//			}
			if(StringUtils.isEmpty(bs.getBrdSysInfo())) {
				ebrbsErrMsg = "播出系统信息不能为空";
				break;   						
			}
			if(null == bs.getStartTime()) {
				ebrbsErrMsg = "播发起始时间不能为空";
				break;					
			} else {
				if(!ValidatorHelper.isDate(bs.getStartTime(), DateStyle.YYYY_MM_DD_HH_MM_SS.getValue())) {
					ebrbsErrMsg = "播发起始时间格式不符合要求YYYY-MM-DD HH:MI:SS";
					break;
				}
			}
			if(null == bs.getEndTime()) {
				ebrbsErrMsg = "播发结束时间不能为空";
				break;					
			} else {
				if(!ValidatorHelper.isDate(bs.getEndTime(), DateStyle.YYYY_MM_DD_HH_MM_SS.getValue())) {
					ebrbsErrMsg = "播发结束时间格式不符合要求YYYY-MM-DD HH:MI:SS";
					break;
				}
			}
			if(StringUtils.isEmpty(bs.getFileURL())) {
				ebrbsErrMsg = "播发录音文件地址不能为空";
				break;
			}
			if(null == bs.getBrdStateCode()) {
				ebrbsErrMsg = "播发状态代码不能为空";
				break;
			} else {
				if(!ValidatorHelper.validBroadcastState(bs.getBrdStateCode())) {
					ebrbsErrMsg = "播发状态代码系统暂不支持";
					break;
				}
			}
			if(StringUtils.isEmpty(bs.getBrdStateDesc())) {
				ebrbsErrMsg = "播发状态描述不能为空";
				break;
			}
		}
		
		return ebrbsErrMsg;
	}

}
