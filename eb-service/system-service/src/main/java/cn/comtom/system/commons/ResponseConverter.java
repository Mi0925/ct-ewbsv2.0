package cn.comtom.system.commons;

import cn.comtom.tools.response.ApiResponse;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractGenericHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

/**
 *  使用FastJson转换输出国际化转换后的结果
 * @author wj
 */
@Slf4j
public class ResponseConverter extends AbstractGenericHttpMessageConverter<Object> {


    private FastJsonConfig fastJsonConfig = new FastJsonConfig();

    private MessageSource messageSource = null;

    public void setFastJsonConfig(FastJsonConfig fastJsonConfig) {
        this.fastJsonConfig = fastJsonConfig;
    }

    public ResponseConverter() {
        super(MediaType.ALL);
    }

    public void setMessageSource(MessageSource messageSource){
        this.messageSource = messageSource;
    }

    @Override
    protected boolean supports(Class<?> paramClass) {
        return true;
    }

    @Override
    public Object read(Type type, Class<?> contextClass, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        InputStream in = inputMessage.getBody();

        StringBuffer out = new StringBuffer();
        byte[] b = new byte[4096];
        for (int n; (n = in.read(b)) != -1; ) {
            out.append(new String(b, 0, n));
        }
        String str = out.toString();
        log.info(" request param : {}", str);
        InputStream inWithCode = new ByteArrayInputStream(str.getBytes(StandardCharsets.UTF_8));

        return JSON.parseObject(inWithCode, this.fastJsonConfig.getCharset(), type, this.fastJsonConfig.getFeatures());
    }

    @Override
    protected Object readInternal(Class<? extends Object> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        InputStream in = inputMessage.getBody();
        return JSON.parseObject(in, this.fastJsonConfig.getCharset(), clazz, this.fastJsonConfig.getFeatures());
    }

    @Override
    protected void writeInternal(Object obj, Type type, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        HttpHeaders headers = outputMessage.getHeaders();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if (obj instanceof ApiResponse) {
            ApiResponse response = (ApiResponse) obj;
            if (response.getCode() != null) {
                //国际化转换
                response.setMsg(messageSource.getMessage(response.getCode(),null,response.getCode(), LocaleContextHolder.getLocale()));
            }
        }
        int len = JSON.writeJSONString(baos, this.fastJsonConfig.getCharset(), obj, this.fastJsonConfig.getSerializeConfig(), this.fastJsonConfig.getSerializeFilters(), this.fastJsonConfig.getDateFormat(), JSON.DEFAULT_GENERATE_FEATURE, this.fastJsonConfig.getSerializerFeatures());
        headers.setContentLength((long) len);
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        OutputStream out = outputMessage.getBody();
        baos.writeTo(out);
        baos.close();
    }

}
