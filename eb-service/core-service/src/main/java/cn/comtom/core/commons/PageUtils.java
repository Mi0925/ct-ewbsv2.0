package cn.comtom.core.commons;

import cn.comtom.domain.core.fw.CriterionRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.mapperhelper.EntityHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 分页转换工具类
 * @author guomao
 */
public class PageUtils {

    /**
     * 排序分隔符
     */
    private static final String ORDER_BY_SEPARATOR = ",";

    public static String buildOrderTo(CriterionRequest request, Class<?> entityClass) {
        String sidx = request.getSidx();
        String order = request.getOrder();

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
            Map<String, EntityColumn> map = EntityHelper.getEntityTable(entityClass).getPropertyMap();
            sb.append(map.get(indexes[i].trim()).getColumn()).append(" ").append(orders[i].trim().toUpperCase()).append(",");
        }
        sb.setLength(sb.length() - 1);
        return sb.toString();
    }

    /**
     * controller构建查询请求
     * @param request
     * @return
     */
    public static  PageRequest buildPageRequest(CriterionRequest request) {
        if(!request.validated()){
            return PageRequest.of(request.getPage(), request.getLimit());
        }
        String[] sorts = request.sorts();
        String[] orders = request.orders();
        List<Sort.Order> sortOrders = new ArrayList<>();
        for (int i = 0; i < sorts.length; i++) {
            sortOrders.add(new Sort.Order(Sort.Direction.fromString(orders[i]), sorts[i]));
        }
        Sort sort = Sort.by(sortOrders);
        return PageRequest.of(request.getPage(), request.getLimit(), sort);
    }


}
