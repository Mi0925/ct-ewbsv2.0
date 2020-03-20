package cn.comtom.linkage.main.access.model.ebd.validate;

import cn.comtom.linkage.commons.EBDType;
import cn.comtom.linkage.main.access.model.ebd.EBD;
import cn.comtom.linkage.utils.DateStyle;
import org.apache.commons.lang.StringUtils;


public class EBDValidator implements Validator {
	public String validateEntity(Object entity) {
        if(!EBD.class.equals(entity.getClass())) {
        	return null;
        }
        
        EBD ebdEnty = (EBD) entity;
        if(StringUtils.isEmpty(ebdEnty.getEBDVersion())) {
        	return "协议版本号EBDVersion不能为空";
        }
        if(StringUtils.isEmpty(ebdEnty.getEBDID())) {
        	return "业务数据包 ID不能为空";
        }
        if(StringUtils.isEmpty(ebdEnty.getEBDType())) {
        	return "业务数据类型EBDType不能为空";
        } else {
        	if(!cn.comtom.linkage.main.access.model.ebd.validate.ValidatorHelper.validEBDType(ebdEnty.getEBDType())) {
        		return "业务数据类型EBDType系统暂不支持";
        	}
        }
        if(null == ebdEnty.getSRC()) {
        	return "数据包来源对象不能为空";
        } else {
        	if(StringUtils.isEmpty(ebdEnty.getSRC().getEBRID())) {
        		return "数据包来源对象的资源 ID不能为空";
        	}
        }
        if(null != ebdEnty.getDEST()) {
        	if(StringUtils.isEmpty(ebdEnty.getDEST().getEBRID())) {
        		return "数据包目标对象的资源 ID不能为空";
        	}
        }
        if(StringUtils.isEmpty(ebdEnty.getEBDTime())) {
        	return "数据包生成时间不能为空";
        } else {
        	if(!cn.comtom.linkage.main.access.model.ebd.validate.ValidatorHelper.isDate(ebdEnty.getEBDTime(), DateStyle.YYYY_MM_DD_HH_MM_SS.getValue())) {
        		return "数据包生成时间格式不符合要求 YYYY-MM-DD HH:MI:SS";
        	}
        }
        if(null != ebdEnty.getRelatedEBD()) {
        	if(StringUtils.isEmpty(ebdEnty.getRelatedEBD().getEBDID())) {
        		return "关联业务数据包 ID不能为空";
        	}
        }
        
        String errMsg = null;
        if(EBDType.EBM.name().equals(ebdEnty.getEBDType())) {
        	if(null == ebdEnty.getEBM()) {
        		errMsg = "应急广播消息不能为空";
	        } else {
	        	errMsg = new cn.comtom.linkage.main.access.model.ebd.validate.EBMValidator().validateEntity(ebdEnty.getEBM());
	        }
        } else if(EBDType.EBMStateRequest.name().equals(ebdEnty.getEBDType())) {
            if(null == ebdEnty.getEBMStateRequest()) {
            	errMsg = "应急广播消息播发状态查询不能为空";
            } else {
        	    errMsg = new cn.comtom.linkage.main.access.model.ebd.validate.EBMStateRequestValidator().validateEntity(ebdEnty.getEBMStateRequest());
        	}
        } else if(EBDType.EBMStateResponse.name().equals(ebdEnty.getEBDType())) {
        	if(null == ebdEnty.getEBMStateResponse()) {
        		errMsg = "应急广播消息播发状态反馈不能为空";
        	} else {
         	    errMsg = new cn.comtom.linkage.main.access.model.ebd.validate.EBMStateResponseValidator().validateEntity(ebdEnty.getEBMStateResponse());
         	}
        } else if(EBDType.OMDRequest.name().equals(ebdEnty.getEBDType())) {
        	if(null == ebdEnty.getOMDRequest()) {
        		errMsg = "运维数据请求不能为空";
        	} else {
         	    errMsg = new cn.comtom.linkage.main.access.model.ebd.validate.OMDRequestValidator().validateEntity(ebdEnty.getOMDRequest());
         	}
        } else if(EBDType.EBRPSInfo.name().equals(ebdEnty.getEBDType())) {
        	if(null == ebdEnty.getEBRPSInfo()) {
        		errMsg = "应急广播平台信息不能为空";
        	} else {
         	    errMsg = new cn.comtom.linkage.main.access.model.ebd.validate.EBRPSInfoValidator().validateEntity(ebdEnty.getEBRPSInfo());
         	}
        } else if(EBDType.EBRSTInfo.name().equals(ebdEnty.getEBDType())) {
        	if(null == ebdEnty.getEBRSTInfo()) {
        		errMsg = "台站（前端）信息不能为空";
        	} else {
         	    errMsg = new cn.comtom.linkage.main.access.model.ebd.validate.EBRSTInfoValidator().validateEntity(ebdEnty.getEBRSTInfo());
         	}
        } else if(EBDType.EBRASInfo.name().equals(ebdEnty.getEBDType())) {
        	if(null == ebdEnty.getEBRASInfo()) {
        		errMsg = "消息接收设备信息不能为空";
        	} else {
         	    errMsg = new cn.comtom.linkage.main.access.model.ebd.validate.EBRASInfoValidator().validateEntity(ebdEnty.getEBRASInfo());
         	}
        } else if(EBDType.EBRBSInfo.name().equals(ebdEnty.getEBDType())) {
        	if(null == ebdEnty.getEBRBSInfo()) {
        		errMsg = "播出系统信息不能为空";
        	} else {
         	    errMsg = new cn.comtom.linkage.main.access.model.ebd.validate.EBRBSInfoValidator().validateEntity(ebdEnty.getEBRBSInfo());
         	}
        } else if(EBDType.EBRPSState.name().equals(ebdEnty.getEBDType())) {
        	if(null == ebdEnty.getEBRPSState()) {
        		errMsg = "应急广播平台状态不能为空";
        	} else {
         	    errMsg = new cn.comtom.linkage.main.access.model.ebd.validate.EBRPSStateValidator().validateEntity(ebdEnty.getEBRPSState());
         	}
        } else if(EBDType.EBRASState.name().equals(ebdEnty.getEBDType())) {
        	if(null == ebdEnty.getEBRASState()) {
        		errMsg = "消息接收设备状态不能为空";
        	} else {
         	    errMsg = new cn.comtom.linkage.main.access.model.ebd.validate.EBRASStateValidator().validateEntity(ebdEnty.getEBRASState());
         	}
        } else if(EBDType.EBRDTInfo.name().equals(ebdEnty.getEBDType())) {
        	if(null == ebdEnty.getEBRDTInfo()) {
        		errMsg = "平台设备及终端信息不能为空";
        	} else {
         	    errMsg = new cn.comtom.linkage.main.access.model.ebd.validate.EBRDTInfoValidator().validateEntity(ebdEnty.getEBRDTInfo());
         	}
        } else if(EBDType.EBMBrdLog.name().equals(ebdEnty.getEBDType())) {
        	if(null == ebdEnty.getEBMBrdLog()) {
        		errMsg = "播发记录不能为空";
        	} else {
         	    errMsg = new cn.comtom.linkage.main.access.model.ebd.validate.EBMBrdLogValidator().validateEntity(ebdEnty.getEBMBrdLog());
         	}
        } else if(EBDType.ConnectionCheck.name().equals(ebdEnty.getEBDType())) {
        	if(null == ebdEnty.getConnectionCheck()) {
        		errMsg = "心跳检测不能为空";
        	} else {
         	    errMsg = new ConnectionCheckValidator().validateEntity(ebdEnty.getConnectionCheck());
         	}
        } else if(EBDType.EBDResponse.name().equals(ebdEnty.getEBDType())) {
        	if(null == ebdEnty.getEBDResponse()) {
        		errMsg = "处理结果通用反馈不能为空";
        	} else {
         	    errMsg = new EBDResponseValidator().validateEntity(ebdEnty.getEBDResponse());
         	}
        } else if(EBDType.EBRBSState.name().equals(ebdEnty.getEBDType())) {
        	if(null == ebdEnty.getEBRBSState()) {
        		errMsg = "播出系统状态不能为空";
        	} else {
         	    errMsg = new cn.comtom.linkage.main.access.model.ebd.validate.EBRBSStateValidator().validateEntity(ebdEnty.getEBRBSState());
         	}
        } else if(EBDType.EBRDTState.name().equals(ebdEnty.getEBDType())) {
        	if(null == ebdEnty.getEBRDTState()) {
        		errMsg = "平台设备及终端状态不能为空";
        	} else {
         	    errMsg = new cn.comtom.linkage.main.access.model.ebd.validate.EBRDTStateValidator().validateEntity(ebdEnty.getEBRDTState());
         	}
        }
        
        if(null != errMsg) {
        	return errMsg;
        }
        
		return null;
	}

}
