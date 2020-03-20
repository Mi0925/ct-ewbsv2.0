package cn.comtom.linkage.main.access.model.ebd.validate;

import cn.comtom.linkage.commons.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;


public class ValidatorHelper {

	public static boolean isDate(String dttm, String format) {
	    boolean retValue = false;
	    if (dttm != null) {
	        SimpleDateFormat formatter = new SimpleDateFormat(format);
	        try {
	            formatter.parse(dttm);
	            retValue = true;
	        } catch (ParseException e) {
	        }
	    }
	    return retValue;
	}
	
	public static boolean validEBDType(String ebdType) {
		boolean retValue = false;
		for(EBDType tp : EBDType.values()) {
			if(tp.name().equals(ebdType)) {
				retValue = true;
				break;
			}
		}
		return retValue;
	}
	
	public static boolean validEBMBasicMsgType(Integer msgType) {
		boolean retValue = false;
		//TODO 2017年9、5 这里加了 3，4 
		Integer[] range = new Integer[]{1, 2,3,4}; //消息的类型，1-请求播发，2-取消播发 , 3 模拟演练，4,实际演练
		for(Integer mtp : range) {
			if(mtp.equals(msgType)) {
				retValue = true;
				break;
			}
		}
		
		return retValue;
	}

	public static boolean validSeverity(Integer severity) {
		boolean retValue = false;
		for(EventSeverityEnum svrity : EventSeverityEnum.values()) {
			if(svrity.getCode().equals(severity)) {
				retValue = true;
				break;
			}
		}
		return retValue;
	}
	
	public static boolean validBroadcastState(Integer brdStateCode) {
		boolean retValue = false;
		for(BroadcastStateEnum bdState : BroadcastStateEnum.values()) {
			if(bdState.getCode().equals(brdStateCode)) {
				retValue = true;
				break;
			}
		}
		return retValue;
	}
	
	public static boolean validResultCode(Integer ebdResResultCode) {
		boolean retValue = false;
		for(EBDRespResultEnum resResult : EBDRespResultEnum.values()) {
			if(resResult.getCode().equals(ebdResResultCode)) {
				retValue = true;
				break;
			}
		}
		return retValue;
	}
	
	public static boolean validResStateCode(Integer resourceStateCode) {
		boolean retValue = false;
		for(RSStateEnum stat : RSStateEnum.values()) {
			if(stat.getCode().equals(resourceStateCode)) {
				retValue = true;
				break;
			}
		}
		return retValue;
	}
	
	public static boolean validOMDType(String omdType) {
		boolean retValue = false;
		String[] range = new String[] {
					EBDType.EBRPSInfo.name(), 
					EBDType.EBRSTInfo.name(),
					EBDType.EBRASInfo.name(),
					EBDType.EBRBSInfo.name(),
					EBDType.EBRDTInfo.name(),
					EBDType.EBMBrdLog.name(),
					EBDType.EBRPSState.name(),
					EBDType.EBRASState.name(),
					EBDType.EBRBSState.name(),
					EBDType.EBRDTState.name()
				};
		
        for(String rg : range) {
        	if(rg.equals(omdType)) {
        		retValue = true;
        		break;
        	}
        }
        
		return retValue;
	}
	
}
