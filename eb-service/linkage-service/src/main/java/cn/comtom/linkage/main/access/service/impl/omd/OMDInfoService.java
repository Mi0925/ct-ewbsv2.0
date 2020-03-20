package cn.comtom.linkage.main.access.service.impl.omd;


import cn.comtom.linkage.main.access.model.ebd.EBD;
import cn.comtom.linkage.main.access.model.ebd.details.other.OMDRequest;

public interface OMDInfoService {

	public String OMDType();
	
	public EBD service(String ebdId, OMDRequest odmRequest);
}
