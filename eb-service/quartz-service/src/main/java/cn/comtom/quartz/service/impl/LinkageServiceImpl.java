package cn.comtom.quartz.service.impl;

import cn.comtom.quartz.fegin.LinkageFegin;
import cn.comtom.quartz.service.ILinkageService;
import cn.comtom.tools.response.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LinkageServiceImpl implements ILinkageService {

    @Autowired
    private LinkageFegin linkageFegin;

    @Override
    public ApiResponse dispatch(){
        return linkageFegin.dispatch();
    }

    @Override
    public ApiResponse scanResourceState() {
        return linkageFegin.scanResourceState();
    }

    @Override
    public ApiResponse sendHartBeat() {
        return linkageFegin.sendHartBeat();
    }

    @Override
    public ApiResponse ebrpsInfoReport() {
        return linkageFegin.ebrpsInfoReport();
    }

    @Override
    public ApiResponse ebrpsStateReport() {
        return linkageFegin.ebrpsStateReport();
    }

    @Override
    public ApiResponse ebrbsInfoReport() {
        return linkageFegin.ebrbsInfoReport();
    }

    @Override
    public ApiResponse ebrbsStateReport() {
        return linkageFegin.ebrbsStateReport();
    }

    @Override
    public ApiResponse ebrdtInfoReport() {
        return linkageFegin.ebrdtInfoReport();
    }

    @Override
    public ApiResponse brdLogReport() {
        return linkageFegin.brdLogReport();
    }

    @Override
    public ApiResponse ebrdtStateReport() {
        return linkageFegin.ebrdtStateReport();
    }

	@Override
	public ApiResponse ebrstInfoReport() {
		return linkageFegin.ebrstInfoReport();
	}

	@Override
	public ApiResponse ebrasInfoReport() {
		return linkageFegin.ebrasInfoReport();
	}

	@Override
	public ApiResponse ebrasStateReport() {
		return linkageFegin.ebrasStateReport();
	}

}
