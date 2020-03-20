package cn.comtom.domain.core.program.request;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class EbmCreateRequest implements Serializable {

    private List<String> unitIds;
}
