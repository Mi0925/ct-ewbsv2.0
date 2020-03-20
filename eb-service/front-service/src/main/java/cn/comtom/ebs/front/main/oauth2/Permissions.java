package cn.comtom.ebs.front.main.oauth2;

/**
 * 系统内所有可配置的权限项
 */
public interface Permissions {
    /**
     * 菜单管理
     */
    String SYS_MENU_ALL = "sys:menu:*";
    /**
     * 菜单查看
     */
    String SYS_MENU_VIEW = "sys:menu:view";
    /**
     * 菜单编辑
     */
    String SYS_MENU_EDIT = "sys:menu:edit";


    /**
     * 用户管理
     */
    String SYS_USER_ALL = "sys:user:*";
    /**
     * 用户查看
     */
    String SYS_USER_VIEW = "sys:user:view";
    /**
     * 用户创建
     */
    String SYS_USER_ADD = "sys:user:add";
    /**
     * 用户编辑
     */
    String SYS_USER_EDIT = "sys:user:edit";
    /**
     * 用户删除
     */
    String SYS_USER_DELETE = "sys:user:delete";
    /**
     * 用户重置密码
     */
    String SYS_USER_PWD_RESET = "sys:user:pwd:reset";
    /**
     * 用户启用
     */
    String SYS_USER_ENABLE = "sys:user:enable";
    /**
     * 用户禁用
     */
    String SYS_USER_DISABLE = "sys:user:disable";
    /**
     * 用户日志
     */
    String SYS_USER_LOG = "sys:user:log";

    /**
     * 角色查看
     */
    String SYS_ROLE_VIEW = "sys:role:view";
    /**
     * 角色编辑
     */
    String SYS_ROLE_EDIT = "sys:role:edit";
    /**
     * 角色新增
     */
    String SYS_ROLE_ADD = "sys:role:add";
    /**
     * 角色删除
     */
    String SYS_ROLE_DELETE = "sys:role:delete";
    /**
     * 用户绑定角色
     */
    String SYS_USER_ROLE_REF = "sys:userRole:ref";

    /**
     * 角色授予权限
     */
    String SYS_ROLE_RES_REF = "sys:roleRes:ref";

    /**
     * 系统参数查看
     */
    String SYS_PARAM_VIEW = "sys:param:view";
    /**
     * 系统参数修改
     */
    String SYS_PARAM_EDIT = "sys:param:edit";
    /**
     * 匹配预案查看
     */
    String SYS_PLAN_VIEW = "sys:plan:view";
    /**
     * 匹配预案新增
     */
    String SYS_PLAN_ADD = "sys:plan:add";
    /**
     * 匹配预案修改
     */
    String SYS_PLAN_EDIT = "sys:plan:edit";
    /**
     * 匹配预案删除
     */
    String SYS_PLAN_DELETE = "sys:plan:delete";
    /**
     * 信息源列表
     */
    String SYS_INFO_SRC_LIST = "sys:infoSrc:list";
    /**
     * 信息源详情
     */
    String SYS_INFO_SRC_DETAIL = "sys:infoSrc:detail";
    /**
     * 信息源添加
     */
    String SYS_INFO_SRC_ADD = "sys:infoSrc:add";
    /**
     * 信息源修改
     */
    String SYS_INFO_SRC_EDIT = "sys:infoSrc:edit";
    /**
     * 信息源删除
     */
    String SYS_INFO_SRC_DELETE = "sys:infoSrc:delete";







    /**
     * 节目列表查询
     */
    String CORE_PROGRAM_LIST = "core:program:list";
    /**
     * 节目新增
     */
    String CORE_PROGRAM_ADD = "core:program:add";
    /**
     * 节目列编辑
     */
    String CORE_PROGRAM_EDIT = "core:program:edit";
    /**
     * 节目审核
     */
    String CORE_PROGRAM_AUDIT = "core:program:audit";
    /**
     * 节目删除
     */
    String CORE_PROGRAM_DELETE = "core:program:delete";

    /**
     * 节目列表查询
     */
    String CORE_PROGRAM_UNIT_LIST = "core:programunit:list";
    /**
     * 节目新增
     */
    String CORE_PROGRAM_UNIT_ADD = "core:programunit:add";
    /**
     * 节目列编辑
     */
    String CORE_PROGRAM_UNIT_EDIT = "core:programunit:edit";
    /**
     * 节目审核
     */
    String CORE_PROGRAM_UNIT_AUDIT = "core:programunit:audit";
    /**
     * 节目删除
     */
    String CORE_PROGRAM_UNIT_DELETE = "core:programunit:delete";

    /**
     * 数据接入查询
     */
    String CORE_ACCESS_QUERY = "core:access:query";
    /**
     * 信息中心查询
     */
    String CORE_EBD_QUERY = "core:ebd:query";
    /**
     * 播发记录查询
     */
    String CORE_BRD_RECORD_QUERY = "core:brdRecord:query";
    /**
     * 预警接入查询
     */
    String CORE_EBM_QUERY = "core:ebm:query";
    /**
     * 预警接入消息审核
     */
    String CORE_EBM_AUDIT = "core:ebm:audit";
    /**
     * 预警接入取消播发
     */
    String CORE_EBM_CANCELPLAY = "core:ebm:cancelPlay";
    /**
     * 预警反馈
     */
    String CORE_EBM_WARN_BACK = "core:ebm:warnBack";
    /**
     * 方案审核
     */
    String CORE_SCHEME_AUDIT = "core:scheme:audit";
    /**
     * 方案详情
     */
    String CORE_SCHEME_DETAIL = "core:scheme:detail";



    /**
     * 文件列表查询
     */
    String RES_FILE_LIST = "res:file:list";
    /**
     * 文件详情查询
     */
    String RES_FILE_DETAIL = "res:file:detail";
    /**
     * 文件上传
     */
    String RES_FILE_UPLOAD = "res:file:upload";
    /**
     * 文件下载
     */
    String RES_FILE_DOWNLOAD = "res:file:download";
    /**
     * 文件修改
     */
    String RES_FILE_EIDT = "res:file:edit";
    /**
     * 文件审核
     */
    String RES_FILE_AUDIT = "res:file:audit";
    /**
     * 文件删除
     */
    String RES_FILE_DELETE = "res:file:delete";
    /**
     * 文件夹创建
     */
    String RES_FILE_DIR_CREATE = "res:filedir:create";
    /**
     * 文件夹修改
     */
    String RES_FILE_DIR_EDIT = "res:filedir:edit";
    /**
     * 文件夹修改
     */
    String RES_FILE_DIR_DETAIL= "res:filedir:detail";
    /**
     * 文件夹删除
     */
    String RES_FILE_DIR_DELETE = "res:filedir:delete";



}
