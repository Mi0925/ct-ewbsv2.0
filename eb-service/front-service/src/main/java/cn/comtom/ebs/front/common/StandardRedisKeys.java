package cn.comtom.ebs.front.common;

/**
 * rest模块在redis中工具类
 * @author WJD
 *
 */
class StandardRedisKeys {

	/**
	 * 平台接入令牌_redis key
	 */
	private static final String ACCESS_TOKEN_KEY_FORMAT = "accessToken:%s";

	private static final String REFRESH_TOKEN_KEY_FORMAT = "refreshToken:%s";

	/**
	 * 用户信息_redis key
	 */
	private static final String ACCESS_TOKEN_KEY_USERINFO = "user:%s";

	/**
	 * 注册时用的校验码_redis key
	 */
	private static final String KEY_REG_AUTH = "sms:RegCaptcha:%s";


	private static final String CAPTCHA_KEY_FORMAT ="captchaId:captcha:%s";


	/**
	 * 重置时时用的校验码_redis key
	 */
	private static final String KEY_USERINFO = "sms:pwdCaptcha:%d";
	

	/** 获取令牌在redis中的KEY
	 * @param token
	 * @return
	 */
	public static String getAccessTokenKey(String token){
		return String.format(ACCESS_TOKEN_KEY_FORMAT,token);
	}

	public static String getRefreshTokenKey(String refreshToken){
		return String.format(REFRESH_TOKEN_KEY_FORMAT,refreshToken);
	}

	/**
	 * 获取用户信息KEY
	 * @param userId
	 * @return
	 */
	public static String getUserInfoKey(String userId){
		return String.format(ACCESS_TOKEN_KEY_USERINFO,userId);
	}

	/**
	 * 获取注册用验证码的KEY
	 * @param captcha
	 * @return
	 */
	public static String getRegCaptcha(String captcha){
		return String.format(KEY_REG_AUTH,captcha);
	}

	/**
	 * 获取密码重置用验证码的KEY
	 * @param captcha
	 * @return
	 */
	public static String getPwdCaptcha(String captcha){
		return String.format(KEY_REG_AUTH,captcha);
	}


	/**
	 * 获取登录用验证码的KEY
	 * @param captchaId
	 * @return
	 */
	public static String getCaptchaKey(String captchaId){
		return String.format(CAPTCHA_KEY_FORMAT,captchaId);
	}

}
