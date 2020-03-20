package cn.comtom.linkage.utils;

import java.util.Date;
import java.util.Random;

public class FileNameUtil {

	/**
	 * 生成应急广播业务数据文件(应急广播消息指令文件)
	 * 
	 * @return
	 */
	public static String createEBDBName() {
		String dateString = DateTimeUtil.dateToString(new Date(), DateStyle.YYYYMMDDHHMMSSSS);
		return "EBDB_" + dateString + "_" + getFixLenthString(4) + ".xml";
	}

	/**
	 * 生成应急广播业务数据文件(应急广播消息指令文件)
	 * 
	 * @return
	 */
	public static String createEBDBName(String ebdId) {
		return "EBDB_" + ebdId + ".xml";
	}

	/**
	 * 生成应急广播签名文件(应急广播消息指令文件)
	 * 
	 * @return String
	 */
	public static String createEBDSName(String ebdId) {
		return "EBDS_EBDB_" + ebdId + ".xml";
	}

	/*
	 * 返回长度为【strLength】的随机数，在前面补0
	 */
	private static String getFixLenthString(int strLength) {
		Random rm = new Random();
		// 获得随机数
		double pross = (1 + rm.nextDouble()) * Math.pow(10, strLength);
		// 将获得的获得随机数转化为字符串
		String fixLenthString = String.valueOf(pross);
		// 返回固定的长度的随机数
		return fixLenthString.substring(1, strLength + 1);
	}

	/**
	 * 应急广播数据文件tar包
	 * 
	 * @return
	 */
	public static String generateTarFileName() {
		String dateString = DateTimeUtil.dateToString(new Date(), DateStyle.YYYYMMDDHHMMSSSS);
		return "EBDT_" + dateString + "_" + getFixLenthString(4) + ".tar";
	}

	/**
	 * 应急广播数据文件tar包
	 * 
	 * @return
	 */
	public static String generateTarFileName(String id) {
		return "EBDT_" + id + ".tar";
	}

/*	public static void main(String[] args) {
		for (int i = 0; i < 100000; i++) {
			System.err.println(generateTarFileName());
		}
	}*/

}
