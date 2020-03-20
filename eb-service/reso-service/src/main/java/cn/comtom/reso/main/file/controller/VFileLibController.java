package cn.comtom.reso.main.file.controller;


import cn.comtom.domain.reso.file.info.VFileLibInfo;
import cn.comtom.domain.reso.file.request.VFileLibPageRequest;
import cn.comtom.reso.fw.BaseController;
import cn.comtom.reso.main.file.service.IVFileLibService;
import cn.comtom.tools.response.ApiPageResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/res/vFileLib")
@Api(tags = "文件夹和文件视图管理")
@Slf4j
public class VFileLibController extends BaseController {

    @Autowired
    private IVFileLibService fileLibService;


    @GetMapping("/page")
    @ApiOperation(value = "分页查询文件列表", notes = "分页查询文件列表")
    public ApiPageResponse<VFileLibInfo> getByPage(@ModelAttribute VFileLibPageRequest request) {
        if(log.isDebugEnabled()){
            log.debug("get File lib view by page . request=[{}]",request);
        }
        startPage(request);
        List<VFileLibInfo> fileLibInfoList = fileLibService.getList(request);
        return ApiPageResponse.ok(fileLibInfoList,page);
    }

   /* @GetMapping("/listAllByParentLibId/{parentLibId}")
    @ApiOperation(value = "查询文件夹所有子文件（夹）", notes = "查询文件夹所有子文件（夹）")
    public ApiListResponse<VFileLibInfo> listByParentLibId(@PathVariable(name = "parentLibId") String parentLibId) {
        if(log.isDebugEnabled()){
            log.debug("get all sub Files and libraries  by parentLibId . parentLibId=[{}]",parentLibId);
        }

        List<VFileLibInfo> fileLibInfoList = fileLibService.getList(request);
        return ApiPageResponse.ok(fileLibInfoList,page);
    }
*/

}
