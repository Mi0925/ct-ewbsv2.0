package cn.comtom.tools.constants;

public class Constants {

    public static final String DATETIME_FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT_PATTERN = "yyyy-MM-dd";
    public static final String TIME_FORMAT_PATTERN = "HH:mm:ss";

    public static final String EBD_TAR_FILENAME_SUFFIX = "EBDT_";
    public static final String EBD_XML_FILENAME_SUFFIX = "EBDB_";


    public static final String EBR_SEQUENCE_ID = "EBRID";
    public static final String EBM_SEQUENCE_ID = "EBMID";
    public static final String EBD_SEQUENCE_ID = "EBDID";



    public static final String EBM_DIAPATCH_INFO_TITLE_SUFFIX = "消息专题";




    /**
     * 参数【键值，字典】Key
     */
    public static final String JSESSIONID_TIMEOUT = "session_timeout_";
    public static final String DEFAULT_LOAD_LEVEL = "default_load_level_";
    public static final String FILE_PATH = "file_path_";

    public static final String EBR_PLATFORM_ID = "ebr_platform_id_";

    public static final String PARENT_PLATFORM_ID = "parent_platform_id_";
    public static final String RECEIVE_FILE_PATH = "receive_file_path_";
    public static final String SEND_FILE_PATH = "send_file_path_";
    public static final String USER_LOG_EXPIRE_TIME = "user_log_expire_time_";
    public static final String SCHEME_AUDIT_FLAG = "scheme_audit_";
    public static final String USER_LOGIN = "user_login_";
    public static final String INFO_ACCESS_AUDIT = "info_access_audit_";
    public static final String FINGERPRINT_THRESHOLD = "fingerprint_threshold";
    public static final String TEMP_FILE_PATH = "temp_file_path_";
    public static final String FTP_FILE_PATH = "ftp_file_path_";
    public static final String CONNECT_CHECK_EXPIRE_DURATION = "connect_check_expire_duration_";
    public static final String STATE_REPORT_INTERVAL = "state_report_interval_";
    public static final String STATE_SCAN_INTERVAL = "state_scan_interval_";
    public static final String MAX_SEND_RETRY_TIMES = "max_send_retry_times_";
    public static final String EBM_DISPATCH_INFO_AUTO_AUDIT = "ebm_dispatch_info_auto_audit";
    public static final String DEFAULT_LIBRARY_ID = "default_library_id";
    public static final String EBM_FILE_LIBRARY_ID = "ebm_file_library_id";
    public static final String EBM_SEND_SEND_ADVANCE_SEC = "ebm_send_send_advance_sec";
    public static final String EBR_ALARM_VALUE = "ebr_alarm_value";
    
    public static final String MAP_ZOOM = "map_zoom";
    public static final String MAP_LONGITUDE = "map_longitude";
    public static final String MAP_LATITUDE = "map_latitude";
    
    /**
     * 电视插播配置
     */
    public static final String TV_PROGRAM_PLAYTIMES = "tv_program_playtimes";
    
    /**
     * EBM消息自动审核
     */
    public static final String EBM_AUDIT_PARAM_KEY = "ebm_auto_audit";
    public static final String DEFAULT_USER = "admin";
    public static final String EBM_DEFAULT_VERSION = "1";

    /**
     * 资源状态
     */
    public static final Integer EBR_STATE_RUNNING = 1;
    public static final Integer EBR_STATE_STOP = 2;
    public static final Integer EBR_STATE_FAULT = 3;
    public static final Integer EBR_STATE_RECOVER = 4;

    public static final String EBR_TYPE_PLATFORM = "01";   //平台
    public static final String EBR_TYPE_EWBS_BS = "02";   //播出系统
//    public static final String EBR_TYPE_STATION = "03";   //站台
//    public static final String EBR_TYPE_ADAPTOR = "04"; // 应急广播适配器
//    public static final String EBR_TYPE_BROADCAST = "05"; // 传输覆盖播出设备
    public static final String EBR_TYPE_TERMINAL = "03"; // 终端
    public static final String EBR_TYPE_PBULISH = "09"; // 发布系统


   /* public static final String EBR_SOURCE_TYPE_PLATFORM = "01";   //平台
    public static final String EBR_SOURCE_TYPE_EWBS_BS = "02";   //播出系统
    public static final String EBR_SOURCE_TYPE_TERMINAL = "03"; // 终端*/



    /**
     * 最新资源类型编码，适用于生成资源编码
     */
    public static final String EBR_SOURCE_TYPE_PLATEFORM = "0101"; // 通用应急平台
    public static final String EBR_SOURCE_TYPE_PUBLISH = "0102"; // 制作播发系统
    public static final String EBR_SOURCE_TYPE_DISPATCH = "0103"; // 调度控制系统
    public static final String EBR_SOURCE_TYPE_BROADCAST = "0314"; // 应急广播大喇叭系统

    /**
     * 最新资源子类型编码，适用于生成资源编码
     */
    public static final String EBR_SUB_SOURCE_TYPE_PLATEFORM = "01"; // 本级平台/机构/系统
    public static final String EBR_SUB_SOURCE_TYPE_ADAPTOR = "02"; // 应急广播适配器
    public static final String EBR_SUB_SOURCE_TYPE_BROADCAST = "03"; // 传输覆盖播出设备
    public static final String EBR_SUB_SOURCE_TYPE_TERMINAL = "04"; // 终端
    public static final String EBR_SUB_SOURCE_TYPE_STATION = "05"; // 台站





    /**
     * 非心跳数据包
     */
    public static final String EBDTYPE = "10";

    /**
     * 心跳数据包
     */
    public static final String EBD_HB_TYPE = "01";


    public static final String SQL_ORDER_TYPE_DESC = "desc";
    public static final String SQL_ORDER_TYPE_ASC = "asc";
    /**
     * sql查询的最大记录数
     */
    public static final int MAX_SQL_ROWS = 1000;
    /**
     * sql查询的默认记录数
     */
    public static final int DEFAULT_SQL_ROWS = 10;


    public static final String ROOT_FILE_LIB_ID = "0";

    /**
     * EBDID生成的序列号的长度
     */
    public static final int EBDID_SEQUENCE_LENGTH = 16;



    public static final String DECODE_TYPE_UTF8 = "utf-8";


    public static final String FILE_LIB_TYPE_SYS = "1";
    public static final String FILE_LIB_TYPE_USER = "2";



    public static final Integer PROGRAM_TYPE_YJ = 1;
    public static final Integer PROGRAM_TYPE_RC = 2;
    public static final Integer PROGRAM_TYPE_YL = 3;


    public static final Integer PROGRAM_STRATEGY_TYPE_ONE = 1;
    public static final Integer PROGRAM_STRATEGY_TYPE_DAY = 2;
    public static final Integer PROGRAM_STRATEGY_TYPE_WEEK = 3;
    public static final Integer PROGRAM_STRATEGY_TYPE_MONTH = 4;

    public static final Integer PROGRAM_CONTENT_TYPE_TXT = 1;
    public static final Integer PROGRAM_CONTENT_TYPE_FILE_UPLOAD = 2;
    public static final Integer PROGRAM_CONTENT_TYPE_SONG = 3;

    public static final Integer PROGRAM_CONTENT_TYPE_TEXT = 1;
    public static final Integer PROGRAM_CONTENT_TYPE_UPLOAD_FILE = 2;
    public static final Integer PROGRAM_CONTENT_TYPE_SELECT_FILE = 3;
    public static final Integer PROGRAM_CONTENT_TYPE_TXT_TO_AUDIO = 4;
    
    //签名方式
	public static final String SIGNATURE_TYPE = "signature_type";



}
