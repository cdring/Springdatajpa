package com.cdring.jpa.security;

import com.alibaba.fastjson.JSONObject;
import com.cdring.jpa.jwt.JwtToken;
import com.cdring.jpa.jwt.JwtUserDetails;
import com.google.common.base.Strings;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.MissingClaimException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

/**
 * @author qiang
 */
@Slf4j
public class AuthenticationTokenFilter extends OncePerRequestFilter {

    /**
     * 错误码
     */
    private static final String ERROR_CODE = "errcode";

    /**
     * 错误消息
     */
    private static final String ERROR_MESSAGE = "errmsg";

    private static final String BEARER = "Bearer";

    private final MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();

//    @Autowired
//    private UserDetailsService userDetailsService;

    @Autowired
    private JwtToken jwtTokenUtil;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        try {
            String authorization = request.getHeader("Authorization");
            if (!Strings.isNullOrEmpty(authorization) && authorization.startsWith(BEARER)) {
                String accessToken = authorization.substring(BEARER.length()).trim();
                JwtUserDetails jwtUserDetails = jwtTokenUtil.getAuthentication(accessToken);
                //String ip = IpUtil.getIpAddr(request);
//                if (Optional.ofNullable(jwtUserDetails).isPresent()) {
//                    throw new LockedException(messages.getMessage(
//                            "AccountStatusUserDetailsChecker.locked", "User account is locked"));
//                }
                if (jwtTokenUtil.validateToken(accessToken, jwtUserDetails)) {
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(jwtUserDetails, null, jwtUserDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    log.info("authenticated user " + jwtUserDetails.getUsername() + ", setting security context");
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
                 else if (Strings.isNullOrEmpty(jwtUserDetails.getUsername())) {
                    response.setContentType("application/json;charset=UTF-8");
                    JSONObject result = new JSONObject();
                    result.put(ERROR_CODE, 41001);
                    result.put(ERROR_MESSAGE, "access_token missing hint");
                    response.getWriter().write(result.toJSONString());
                    return;
                }
            }
            chain.doFilter(request, response);
        } catch (ExpiredJwtException eje) {
            response.setContentType("application/json;charset=UTF-8");
            log.info("Security exception for user {} - {}", eje.getClaims().getSubject(), eje.getMessage());
            JSONObject result = new JSONObject();
            result.put(ERROR_CODE, 40001);
            result.put(ERROR_MESSAGE, eje.getMessage());
            response.getWriter().write(result.toJSONString());
        } catch (MalformedJwtException e) {
            response.setContentType("application/json;charset=UTF-8");
            log.info(e.getMessage());
            JSONObject result = new JSONObject();
            result.put(ERROR_CODE, 41001);
            result.put(ERROR_MESSAGE, "access_token missing hint");
            response.getWriter().write(result.toJSONString());
        } catch (MissingClaimException e) {
            response.setContentType("application/json;charset=UTF-8");
            log.info(e.getMessage());
            JSONObject result = new JSONObject();
            result.put(ERROR_CODE, 41002);
            result.put(ERROR_MESSAGE, "access_token missing claim");
            response.getWriter().write(result.toJSONString());
        } catch (LockedException e) {
            response.setContentType("application/json;charset=UTF-8");
            log.info(e.getMessage());
            JSONObject result = new JSONObject();
            result.put(ERROR_CODE, 41004);
            result.put(ERROR_MESSAGE, "User account has been locked");
            response.getWriter().write(result.toJSONString());
        }
    }
}