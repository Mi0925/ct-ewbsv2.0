package cn.comtom.linkage.utils;


import cn.comtom.linkage.commons.LinkageConstants;
import org.apache.commons.lang.StringUtils;

/**
 * 区域码工具类
 * @author comtom
 *
 */
public class RegionUtil {
	
	
	private static String[] complements=new String[13];//补全数组
	
	private static final String FULL_ZERO=String.format("%012d", 0);//12个0
	
	static{
		complement(2);
		complement(4);
		complement(6);
		complement(9);
		complement(12);
	}
	
	private static void complement(int n){
		complements[n]=FULL_ZERO.substring(n);
	}
	
	
	/** 区域长码转短码
	 * @param regionCode 长码
	 * @return 短码
	 */
	public static String long2Short(String regionCode){
		if(regionCode.length()>6){
			regionCode=regionCode.replaceAll("(000){1,2}$", "");
		}
		if(regionCode.length()<=6){
			regionCode=regionCode.replaceAll("(00)+$", "");
		}
		return regionCode;
	}
	
	/** 区域短码转12位长码
	 * @param regionCode 短码
	 * @return 长码
	 */
	public static String short2Long(String regionCode){
		return regionCode+complements[regionCode.length()];
	}
	
	
	/**
	 * 逗号分隔区域短码转12位长码
	 * @param areaCode
	 */
	public static String areaShort2Long(String areaCode) {
		if (StringUtils.isNotEmpty(areaCode)) {
			String[] areaCodes = areaCode.split(String.valueOf(LinkageConstants.COMMA_SPLIT));
			StringBuffer codesStr = new StringBuffer();
			for (int i = 0; i < areaCodes.length; i++) {
				areaCode = short2Long(areaCodes[i]);
				codesStr.append(areaCode).append(LinkageConstants.COMMA_SPLIT);
			}
			areaCode = codesStr.substring(0, codesStr.length() - 1);
		}
		return areaCode;
	}
	
	
	/**
	 * 逗号分隔区域长码转短码
	 * @param areaCode
	 * @return
	 */
	public static String areaLong2Short(String areaCode) {
		if (StringUtils.isNotEmpty(areaCode)) {
			String[] areaCodes = areaCode.split(String.valueOf(LinkageConstants.COMMA_SPLIT));
			StringBuffer codesStr = new StringBuffer();
			for (int i = 0; i < areaCodes.length; i++) {
				areaCode = long2Short(areaCodes[i]);
				codesStr.append(areaCode).append(LinkageConstants.COMMA_SPLIT);
			}
			areaCode = codesStr.substring(0, codesStr.length() - 1);
		}
		return areaCode;
	}
	
	
}
