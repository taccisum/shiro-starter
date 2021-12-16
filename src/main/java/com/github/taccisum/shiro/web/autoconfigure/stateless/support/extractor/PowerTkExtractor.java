package com.github.taccisum.shiro.web.autoconfigure.stateless.support.extractor;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * <pre>
 * 一个功能强大的 token 提取器
 *
 * 1. 支持从 query string、cookies 指定 names 中获取 token，names 支持指定多个，优先级按指定顺序排列
 * 2. 支持从标准 HTTP Authorization 请求头获取 token
 * 3. 支持 mock 虚拟 token（便于调试）
 * 4. 优先级：mock > Authorization > cookie > query string
 * </pre>
 *
 * @author taccisum - liaojinfeng6938@dingtalk.com
 * @since v2.4.0
 */
@Slf4j
public class PowerTkExtractor implements TokenExtractor {
    public String[] tkNames;
    @Setter
    private boolean mock = false;
    @Setter
    private String mockTk = "";

    public PowerTkExtractor(String tkName) {
        this(new String[]{tkName});
    }

    public PowerTkExtractor(String... tkNames) {
        this.tkNames = tkNames;
    }

    @Override
    public String getToken(HttpServletRequest req) {
        String from = "unknown";
        String token = null;

        if (mock) {
            // mock token, only for test
            from = "mock";
            token = mockTk;
        } else {
            String h = req.getHeader(HttpHeaders.AUTHORIZATION);
            if (h != null) {
                from = "Header Authorization";
                token = h;
            } else {
                Cookie c = null;
                for (String tkName : tkNames) {
                    if (req.getCookies() != null) {
                        c = Arrays.stream(req.getCookies())
                                .filter(cookie -> tkName.equals(cookie.getName()))
                                .findFirst()
                                .orElse(null);
                    }
                    if (c != null) {
                        from = "cookie " + tkName;
                        token = c.getValue();
                    } else {
                        from = "query string " + tkName;
                        token = req.getParameter(tkName);
                    }
                    if (!StringUtils.isEmpty(token)) {
                        break;
                    }
                }
            }
        }

        if (token != null) {
            log.debug("Found token from {}: {}", from, token);
        } else {
            log.warn("Token not found. Method: {}, URI: {}", req.getMethod(), req.getRequestURI());
        }
        return token;
    }
}
