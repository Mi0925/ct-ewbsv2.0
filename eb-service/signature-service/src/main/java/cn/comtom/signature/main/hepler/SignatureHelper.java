package cn.comtom.signature.main.hepler;

import cn.tass.exceptions.YJException;
import cn.tass.yingjgb.YingJGBCALLDLL;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * 签名生成和验签
 * 
 * @author liuhy
 *
 */
@Slf4j
public class SignatureHelper {


	// 连接设备的类型标识  0 ：代表SJJ1507密码机 , 1:代表SJJ1313密码器
	public static final Integer flag = 1;

	/**
	 * 生成签名
	 * 
	 * @param xmlFile
	 * @return String
	 */
	public static String sign(File xmlFile) {
		String xmlSign = "";
		synchronized (YingJGBCALLDLL.class) {
			// 打开密码机 , 0 ：代表SJJ1507密码机 , 1:代表SJJ1313密码器
			openDevice(flag);

			byte[] data = readFile(xmlFile);

			try {
				xmlSign = YingJGBCALLDLL.platformCalculateSignature(flag, data);
			} catch (YJException e) {
				log.error("SignatureUtil  sign failed.", e);
			}
			// 关闭密码机
			closeDevice();
			return xmlSign;
		}
	}

	/**
	 * 验证签名
	 * 
	 * @return boolean
	 */
	public static boolean verifySign(File xmlFile, String signValue) {
		boolean result = false;
		synchronized (YingJGBCALLDLL.class) {
			// 打开密码机
			openDevice(flag);
			byte[] data = readFile(xmlFile);
			// 验证数据签名
			try {
				result = YingJGBCALLDLL.platformVerifySignature(flag, data, signValue);
				log.info(" 验证签名结果  :" + result);
			} catch (YJException e) {
				log.error("Signature helper sign verify failed.", e);
			}

			// 关闭密码机
			closeDevice();

			return result;
		}
	}
	
	/**
	 * 导入证书
	 * @param pucCert
	 * @return
	 */
	public static boolean platformImportTrustedCert(byte[] pucCert) {
		boolean result = false;
		synchronized (YingJGBCALLDLL.class) {
			// 打开密码机
			openDevice(flag);
			// 导入证书
			try {
				result = YingJGBCALLDLL.platformImportTrustedCert(pucCert);
				log.info("证书结果  :{}", result);
			} catch (YJException e) {
				log.error("Signature helper platformImportTrustedCert failed.", e);
			}
			// 关闭密码机
			closeDevice();
			return result;
		}
	}
	
	/**
	 * 打开密码机
	 * 
	 * @param flag
	 */
	public static void openDevice(int flag) {
		try {
			// 打开sjj1507密码机
			synchronized (YingJGBCALLDLL.class) {
				YingJGBCALLDLL.openDevice(flag);
			}
		} catch (Exception e) {
			log.error("Signature helper close device failed.", e);
		}
	}

	/**
	 * 关闭密码机
	 */
	public static void closeDevice() {
		try {
			// 关闭sjj1507密码机
			YingJGBCALLDLL.closeDevice();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 读取文件内容
	 * 
	 * @return byte[]
	 * @throws IOException
	 */
	public static byte[] readFile(File xmlFile) {

		FileInputStream input = null;

		try {
			input = new FileInputStream(xmlFile);
		} catch (FileNotFoundException e) {
			log.error("读取文件异常");
			e.printStackTrace();
		}

		byte[] msg = new byte[(int) xmlFile.length()];

		try {
			while (input.read(msg) != -1) {
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			input.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return msg;
	}
	
	public static void main(String[] args) {
		try {
			YingJGBCALLDLL.openDevice(flag);
			System.out.println(YingJGBCALLDLL.deviceState);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (YJException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
