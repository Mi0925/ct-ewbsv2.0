package cn.comtom.quartz.service;

import cn.comtom.tools.response.ApiResponse;

public interface ILinkageService {

    public ApiResponse dispatch();

    ApiResponse scanResourceState();

    ApiResponse sendHartBeat();

    ApiResponse ebrpsStateReport();

    ApiResponse ebrbsStateReport();

    ApiResponse ebrdtStateReport();

    ApiResponse ebrpsInfoReport();

    ApiResponse ebrbsInfoReport();

    ApiResponse ebrdtInfoReport();

    ApiResponse brdLogReport();

	ApiResponse ebrstInfoReport();

    ApiResponse ebrasInfoReport();

	ApiResponse ebrasStateReport();

}
