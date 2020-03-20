package cn.comtom.linkage.main.access.service.impl;


import cn.comtom.linkage.commons.EBDType;
import cn.comtom.linkage.main.access.constant.SendFlag;
import cn.comtom.linkage.main.access.model.ebd.EBD;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

/**
 * @author nobody
 * 通用结果反馈(不需要处理)
 */
@Service
public class EBDResponseServiceImpl extends AbstractEMDService{

	@Override
	public String serviceType() {
		return EBDType.EBDResponse.name();
	}

	@Override
	public void service(EBD ebd, List<File> resourceFiles) {
//		recordEbdResponse(ebd, ebd.getEBDResponse(), SendFlag.receive);
	}
}
