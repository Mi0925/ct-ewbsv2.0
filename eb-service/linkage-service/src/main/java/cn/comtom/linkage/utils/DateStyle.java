package cn.comtom.linkage.utils;


/**
 *时间展示常用格式结合<br>
 */
public enum DateStyle {  
    
    MM_DD("MM-dd"),  
    YYYY_MM("yyyy-MM"),  
    YYYY_MM_DD("yyyy-MM-dd"),  
    MM_DD_HH_MM("MM-dd HH:mm"),  
    MM_DD_HH_MM_SS("MM-dd HH:mm:ss"),  
    YYYY_MM_DD_HH_MM("yyyy-MM-dd HH:mm"),  
    YYYY_MM_DD_HH_MM_SS("yyyy-MM-dd HH:mm:ss"),  
      
    MM_DD_EN("MM/dd"),  
    YYYY_MM_EN("yyyy/MM"),  
    YYYY_MM_DD_EN("yyyy/MM/dd"),  
    MM_DD_HH_MM_EN("MM/dd HH:mm"),  
    MM_DD_HH_MM_SS_EN("MM/dd HH:mm:ss"),  
    YYYY_MM_DD_HH_MM_EN("yyyy/MM/dd HH:mm"),  
    YYYY_MM_DD_HH_MM_SS_EN("yyyy/MM/dd HH:mm:ss"),  
      
    MM_DD_CN("MM月dd日"),  
    YYYY_MM_CN("yyyy年MM月"),  
    YYYY_MM_DD_CN("yyyy年MM月dd日"),  
    MM_DD_HH_MM_CN("MM月dd日 HH:mm"),  
    MM_DD_HH_MM_SS_CN("MM月dd日 HH:mm:ss"),  
    YYYY_MM_DD_HH_MM_CN("yyyy年MM月dd日 HH:mm"),  
    YYYY_MM_DD_HH_MM_SS_CN("yyyy年MM月dd日 HH:mm:ss"),  
      
    YYYYMMDDHHMMSS("yyyyMMddHHmmss"),
    
    YYYYMMDDHHMMSSSS("yyyyMMddHHmmssSSS"),
    
    HH_MM("HH:mm"),  
    HH_MM_SS("HH:mm:ss");  
    
      
    private String m_value;  
    
    
    DateStyle(String value) {  
        this.m_value = value;  
    }  
      
    public String getValue() {  
        return m_value;  
    }  

    /**
     * 获取所有的时间格式
     * @return
     */
    public static String[] getAllStyle(){
        return new String[]
        {
            MM_DD.getValue (),                  
            YYYY_MM.getValue (),                
            YYYY_MM_DD.getValue (),             
            MM_DD_HH_MM.getValue (),            
            MM_DD_HH_MM_SS.getValue (),         
            YYYY_MM_DD_HH_MM.getValue (),       
            YYYY_MM_DD_HH_MM_SS.getValue (),    
                                                   
            MM_DD_EN.getValue (),               
            YYYY_MM_EN.getValue (),             
            YYYY_MM_DD_EN.getValue (),          
            MM_DD_HH_MM_EN.getValue (),         
            MM_DD_HH_MM_SS_EN.getValue (),      
            YYYY_MM_DD_HH_MM_EN.getValue (),    
            YYYY_MM_DD_HH_MM_SS_EN.getValue (), 
                                   
            MM_DD_CN.getValue (),               
            YYYY_MM_CN.getValue (),             
            YYYY_MM_DD_CN.getValue (),          
            MM_DD_HH_MM_CN.getValue (),         
            MM_DD_HH_MM_SS_CN.getValue (),      
            YYYY_MM_DD_HH_MM_CN.getValue (),    
            YYYY_MM_DD_HH_MM_SS_CN.getValue (), 
            
            YYYYMMDDHHMMSS.getValue(),
            
            HH_MM.getValue (),                  
            HH_MM_SS.getValue ()                
        };
    }
}
