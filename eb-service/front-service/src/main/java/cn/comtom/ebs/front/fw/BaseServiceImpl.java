package cn.comtom.ebs.front.fw;

import cn.comtom.tools.response.ApiEntityResponse;
import cn.comtom.tools.response.ApiListResponse;
import cn.comtom.tools.response.ApiPageResponse;
import cn.comtom.tools.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class BaseServiceImpl {

    protected Object getData(ApiEntityResponse response){
        if(!response.getSuccessful()){
            log.error("提取接口响应参数失败：响应码：[{}] ， 响应信息：[{}]",response.getCode(),response.getMsg());
            return null;
        }
        Object data = response.getData();
        return data;
    }

    protected List  getList(ApiListResponse response){
        if(!response.getSuccessful()){
            log.error("提取接口响应参数失败：响应码：[{}] ， 响应信息：[{}]",response.getCode(),response.getMsg());
            return null;
        }
        List list = response.getData();
        return list;
    }

    protected List  getList(ApiPageResponse response){
        if(!response.getSuccessful()){
            log.error("提取接口响应参数失败：响应码：[{}] ， 响应信息：[{}]",response.getCode(),response.getMsg());
            return null;
        }
        List list = response.getData();
        return list;
    }

    protected Boolean getBoolean(ApiResponse response){
        return response.getSuccessful();
    }

    protected Long getTotal(ApiPageResponse response){
        if(!response.getSuccessful()){
            log.error("提取接口响应参数失败：响应码：[{}] ， 响应信息：[{}]",response.getCode(),response.getMsg());
            return 0L;
        }
        return response.getTotalCount();
    }

}
