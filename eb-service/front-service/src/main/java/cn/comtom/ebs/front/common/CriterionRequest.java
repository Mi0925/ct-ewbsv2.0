package cn.comtom.ebs.front.common;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotNull;

import static cn.comtom.tools.sql.SQLFilterUtils.sqlInject;


/**
 * 标准请求
 */
public class CriterionRequest {

    /**
     * 排序分隔符
     */
    public static final String ORDER_BY_SEPARATOR = ",";

    /**
     * 页数
     */
    @ApiModelProperty(value = "当前页数", required = true, example = "1")
    @NotNull(message = "页数不能为空！")
    @JSONField(ordinal = 3)
    private int page;

    /**
     * 分页大小
     */
    @ApiModelProperty(value = "分页大小", required = true, example = "10")
    @NotNull(message = "分页大小不能为空！")
    @JSONField(ordinal = 4)
    private int limit;

    @ApiModelProperty(value = "排序字段")
    @JSONField(ordinal = 5)
    private String sidx;

    @ApiModelProperty(value = "升序or降序")
    @JSONField(ordinal = 6)
    private String order;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getLimit() {
        return limit == 0 ? 10 : limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public String getSidx() {
        return sidx;
    }

    public void setSidx(String sidx) {
        this.sidx = sqlInject(sidx);
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = sqlInject(order);
    }

    public int offset() {
        return (page - 1) * limit;
    }

    public String orderBy() {
        if (StringUtils.isBlank(sidx) || StringUtils.isBlank(order)) {
            return null;
        }
        String[] indexes = sidx.split(ORDER_BY_SEPARATOR);
        String[] orders = order.split(ORDER_BY_SEPARATOR);
        if (indexes.length != orders.length) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < indexes.length; i++) {
            sb.append(indexes[i].trim()).append(" ").append(orders[i].trim().toUpperCase()).append(",");
        }
        sb.setLength(sb.length() - 1);
        return sb.toString();
    }

    public String[] orders() {
        if (StringUtils.isBlank(order)) {
            return new String[0];
        }
        return order.split(ORDER_BY_SEPARATOR);
    }

    public String[] sorts() {
        if (StringUtils.isBlank(sidx)) {
            return new String[0];
        }
        return sidx.split(ORDER_BY_SEPARATOR);
    }

    public boolean validated() {
        if (StringUtils.isBlank(sidx) || StringUtils.isBlank(order)) {
            return false;
        }
        String[] indexes = sidx.split(ORDER_BY_SEPARATOR);
        String[] orders = order.split(ORDER_BY_SEPARATOR);
        return indexes.length == orders.length;
    }
}
