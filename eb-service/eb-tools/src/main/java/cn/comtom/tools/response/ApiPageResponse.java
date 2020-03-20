package cn.comtom.tools.response;


import cn.comtom.tools.enums.BasicError;
import com.alibaba.fastjson.annotation.JSONField;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = false)
public class ApiPageResponse<T> extends ApiResponse {

    @JSONField(ordinal = 4)
    @ApiModelProperty(value = "总数")
    private long totalCount;

    @JSONField(ordinal = 5)
    @ApiModelProperty(value = "每页记录数")
    private int pageSize;

    @JSONField(ordinal = 6)
    @ApiModelProperty(value = "当前页数")
    private int currPage;

    @JSONField(ordinal = 8)
    @ApiModelProperty(value = "列表数据")
    private List<T> data;

    private static ApiPageResponse response(boolean isOK, String code, String msg) {
        ApiPageResponse response = new ApiPageResponse();
        response.setCode(code);
        response.setSuccessful(isOK);
        response.setMsg(msg);
        return response;
    }

    public ApiPageResponse<T> setData(List<T> data) {
        this.data = data;
        return this;
    }

    public static ApiPageResponse error(String code, String msg) {
        return response(false, code, msg);
    }



    public static ApiPageResponse ok(String msg) {
        return response(true, BasicError.OK_KEY_VALUE.getCode(), msg);
    }


    public static <T> ApiPageResponse<T> ok(List<T> data) {
        PageInfo<T> pageInfo = new PageInfo<>(data);
        return ok(pageInfo.getTotal(), pageInfo.getPageNum(), pageInfo.getPageSize(), data);
    }

    public static <T> ApiPageResponse<T> ok(List<T> data,Page<Object> pageInfo) {
        return ok(pageInfo.getTotal(), pageInfo.getPageNum(), pageInfo.getPageSize(), data);
    }

    public static <T> ApiPageResponse<T> ok(long total, int page, int limit, List<T> data) {
        ApiPageResponse<T> response = ok("OK");
        response.setData(data);
        response.setTotalCount(total);
        response.setCurrPage(page);
        response.setPageSize(limit);
        return response;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public ApiPageResponse<T> setTotalCount(long totalCount) {
        this.totalCount = totalCount;
        return this;
    }

    public int getPageSize() {
        return pageSize;
    }

    public ApiPageResponse<T> setPageSize(int pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    @JSONField(ordinal = 7)
    public int getTotalPage() {
        if (pageSize == 0) {
            return 0;
        }
        long tPage = (totalCount % pageSize) == 0 ? (totalCount / pageSize) : (totalCount / pageSize + 1);
        return Integer.parseInt(tPage + "");
    }


    public int getCurrPage() {
        return currPage;
    }

    public ApiPageResponse<T> setCurrPage(int currPage) {
        this.currPage = currPage;
        return this;
    }

    public List<T> getData() {
        return data;
    }




}
