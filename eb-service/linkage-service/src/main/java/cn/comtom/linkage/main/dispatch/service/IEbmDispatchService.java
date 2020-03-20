package cn.comtom.linkage.main.dispatch.service;

import cn.comtom.linkage.main.access.model.ebd.EBD;

import java.util.List;

public interface IEbmDispatchService {
    void beforeDispatch(EBD ebd);

    void dispatch(String ebdId);

    List<EBD> getEBDPackageList(List<String> state);

	void dispatchTextInsertionEBM(List<String> stateList);

}
