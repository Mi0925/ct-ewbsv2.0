package cn.comtom.reso.fw;

import cn.comtom.domain.reso.fw.CriterionRequest;
import cn.comtom.reso.commons.PageUtils;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BaseController {
    /**
     * 分页参数提取
     *
     * @param request
     */
    protected Page<Object> page;
    protected void startPage(CriterionRequest request) {
         page = PageHelper.startPage(request.getPage(), request.getLimit(), request.orderBy());
    }

    protected void startPage(CriterionRequest request, Class<?> entityClass) {
        page = PageHelper.startPage(request.getPage(), request.getLimit(), PageUtils.buildOrderTo(request, entityClass));
    }

    protected PageRequest buildPageRequest(CriterionRequest request) {
        return PageUtils.buildPageRequest(request);
    }


    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), false));
    }
    
    public static <T> List copyList(List<T> list,Class destClass) {
        if (CollectionUtils.isEmpty(list)) {
            return new ArrayList();
        }
        return JSON.parseArray(JSON.toJSONString(list), destClass);
    }
 

}
