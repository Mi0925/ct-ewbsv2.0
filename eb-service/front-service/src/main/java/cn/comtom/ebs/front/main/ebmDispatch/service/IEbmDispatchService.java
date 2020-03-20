package cn.comtom.ebs.front.main.ebmDispatch.service;

import cn.comtom.domain.core.ebm.info.EbmDispatchAndEbdFileInfo;

import java.util.List;

/**
 * @author:WJ
 * @date: 2019/1/19 0019
 * @time: 上午 11:24
 */
public interface IEbmDispatchService {

    List<EbmDispatchAndEbdFileInfo> getEbmDispatchAndEbdFileByEbmId(String ebmId);

}
