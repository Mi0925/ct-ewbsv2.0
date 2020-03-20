package cn.comtom.tools.request;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;

public class ApiPageRequest extends ApiBaseRequest{

    @JSONField(ordinal = 5)
    @ApiModelProperty(value = "每页记录数")
    private Integer pageSize = 10;

    @JSONField(ordinal = 6)
    @ApiModelProperty(value = "当前页数")
    private Integer currPage = 1;

    @ApiModelProperty(value = "查询起始记录")
    private Integer beginNum;

    @ApiModelProperty(value = "查询结束记录")
    private Integer endNum;

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getCurrPage() {
        return currPage;
    }

    public void setCurrPage(int currPage) {
        this.currPage = currPage;
    }

    public int getBeginNum() {
        if(beginNum != null){
            return beginNum;
        }else if(pageSize == null || currPage == null){
            return 0;
        }else{
            return pageSize * (currPage-1);
        }
    }

    public void setBeginNum(int beginNum) {
        this.beginNum = beginNum;
    }

    public int getEndNum() {
        if(endNum != null){
            return endNum;
        }else if(pageSize == null || currPage == null){
            return 0;
        }else{
            return (pageSize * currPage)-1;
        }
    }

    public void setEndNum(int endNum) {
        this.endNum = endNum;
    }
}
