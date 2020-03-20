package cn.comtom.gateway.filter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.RequestPath;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.server.ServerWebExchange;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * @author liuhy
 * token校验全局过滤器
 * @Version V1.0
 */
@Component
@ConfigurationProperties(prefix = "service.ignored-token")
@Slf4j
public class AuthorizeFilter implements GlobalFilter, Ordered {
	
    private static final String AUTHORIZE_TOKEN = "token";
    
    private static final String AUTHORIZE_UID = "uid";
 
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
 
    private List<String> paths = new ArrayList<String>();
    
    public List<String> getPaths() {
		return paths;
	}

	public void setPaths(List<String> paths) {
		this.paths = paths;
	}

	@Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        
        HttpHeaders headers = request.getHeaders();
        String token = headers.getFirst(AUTHORIZE_TOKEN);
        String uid = headers.getFirst(AUTHORIZE_UID);
        RequestPath reqpath = exchange.getRequest().getPath();
        PathMatcher matcher = new AntPathMatcher();
        String withPath = reqpath.pathWithinApplication().value();
        log.info("gateway request: url:{},token:{},uid:{}",withPath,token,uid);
        for (String path : paths) {
        	if(matcher.match(path, withPath)) {
        		return chain.filter(exchange);
        	}
		}
        if (token == null) {
            token = request.getQueryParams().getFirst(AUTHORIZE_TOKEN);
        }
        if (uid == null) {
            uid = request.getQueryParams().getFirst(AUTHORIZE_UID);
        }
        if (StringUtils.isEmpty(token) || StringUtils.isEmpty(uid)) {
        	return printLog(response);
        }
        String authToken = stringRedisTemplate.opsForValue().get(uid);
        if (authToken == null || !authToken.equals(token)) {
        	return printLog(response);
        }
        return chain.filter(exchange);
    }
	
	private Mono<Void> printLog(ServerHttpResponse response) {
		Map<String, Object> result = Maps.newHashMap();
    	result.put("code", 1);
    	result.put("msg","无效token");
    	String resp = JSON.toJSONString(result);
    	DataBuffer bodyDataBuffer = response.bufferFactory().wrap(resp.getBytes());
    	response.getHeaders().add("Content-Type", "text/plain;charset=UTF-8");
        return response.writeWith(Mono.just(bodyDataBuffer));
	}
	

    @Override
    public int getOrder() {
        return 0;
    }
}