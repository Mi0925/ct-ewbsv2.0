package cn.comtom.system.fw;

import cn.comtom.domain.system.fw.CriterionRequest;
import cn.comtom.system.commons.PageUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import java.text.SimpleDateFormat;
import java.util.Date;

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

}
