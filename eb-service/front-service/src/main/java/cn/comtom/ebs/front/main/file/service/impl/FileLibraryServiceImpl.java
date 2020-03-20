package cn.comtom.ebs.front.main.file.service.impl;

import cn.comtom.domain.reso.file.info.FileLibraryInfo;
import cn.comtom.domain.reso.file.request.FileLibraryAddRequest;
import cn.comtom.domain.reso.file.request.FileLibraryPageRequest;
import cn.comtom.domain.reso.file.request.FileLibraryUpdateRequest;
import cn.comtom.ebs.front.fegin.service.IResoFeginService;
import cn.comtom.ebs.front.main.file.service.IFileLibraryService;
import cn.comtom.tools.constants.Constants;
import cn.comtom.tools.response.ApiPageResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class FileLibraryServiceImpl implements IFileLibraryService {

    @Autowired
    private IResoFeginService resoFeginService;

    @Override
    public FileLibraryInfo getFileLibraryInfoById(String libId) {
        return resoFeginService.getFileLibraryInfoByLibId(libId);
    }

    @Override
    public Boolean updateFileLibraryInfo(FileLibraryUpdateRequest request) {
        return resoFeginService.updateFileLibraryInfo(request);
    }

    @Override
    public Boolean deleteFileLibrary(String libId) {
        return resoFeginService.deleteFileLibraryInfo(libId);
    }

    @Override
    public List<FileLibraryInfo> getList(FileLibraryPageRequest request) {
        request.setLimit(Constants.MAX_SQL_ROWS);
        ApiPageResponse<FileLibraryInfo> response = resoFeginService.getFileLibraryInfoByPage(request);
        if(response.getSuccessful()){
            return response.getData();
        }
        return null;
    }

    @Override
    public Integer updateFileLibraryBatch(List<FileLibraryUpdateRequest> requestList) {
        return resoFeginService.updateFileLibraryInfoBatch(requestList);
    }

    @Override
    public Integer deleteFileLibraryAll(String libId) {
        return resoFeginService.deleteFileLibraryAll(libId);
    }

    @Override
    public FileLibraryInfo saveFileLibraryInfo(FileLibraryAddRequest request) {
        return resoFeginService.saveFileLibraryInfo(request);
    }
}
