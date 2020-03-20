package cn.comtom.linkage.main.fegin.service;

import cn.comtom.tools.response.ApiEntityResponse;
import cn.comtom.tools.response.ApiListResponse;
import cn.comtom.tools.response.ApiPageResponse;
import cn.comtom.tools.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class FeginBaseService {
    /**
     * 获取数据体集合
     * @param response
     * @return
     */
    public List getDataList(ApiPageResponse response) {
        if(response.getSuccessful()){
            return response.getData();
        }else{
            log.info("未查询到数据"+response.getMsg());
        }
        return null;
    }

    /**
     * 获取总记录数
     * @param response
     * @return
     */
    public Long getTotal(ApiPageResponse response) {
        if(response.getSuccessful()){
            return response.getTotalCount();
        }else{
            log.info("未查询到数据"+response.getMsg());
        }
        return 0L;
    }

    /**
     * 提取数据体对象
     * @param response
     * @return
     */
    public <T> T getData(ApiEntityResponse<T> response) {
        if(response.getSuccessful()){
            return response.getData();
        }else{
            log.info("未查询到数据"+response.getMsg());
        }
        return null;
    }

    public  boolean getBoolean(ApiResponse response){
        if(response.getSuccessful()){
            return true;
        }else{
            return false;
        }
    }



    /**
     * 获取数据体集合
     * @param response
     * @return
     */
    public List getDataList(ApiListResponse response) {
        if(response.getSuccessful()){
            return response.getData();
        }else{
            log.info("未查询到数据"+response.getMsg());
        }
        return null;
    }
}
