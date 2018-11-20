package org.crown.common.http;


import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.crown.common.api.ApiAssert;
import org.crown.common.emuns.ErrorCodeEnum;
import org.crown.common.emuns.HTTPMethod;
import org.crown.common.kit.JWTTokenUtils;
import org.crown.common.spring.ApplicationUtils;
import org.springframework.util.StreamUtils;

import io.jsonwebtoken.Claims;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;


/**
 * Request 请求工具类
 *
 * @author Caratacus
 * @data 2016-07-04
 */
@SuppressWarnings("ALL")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public abstract class RequestKit {

    /**
     * 判断请求方式GET
     *
     * @param request
     * @return
     */
    public static boolean isGet(HttpServletRequest request) {
        return HTTPMethod.GET.toString().equalsIgnoreCase(request.getMethod());
    }

    /**
     * 判断请求方式POST
     *
     * @param request
     * @return
     */
    public static boolean isPost(HttpServletRequest request) {
        return HTTPMethod.POST.toString().equalsIgnoreCase(request.getMethod());
    }

    /**
     * 判断请求方式PUT
     *
     * @param request
     * @return
     */
    public static boolean isPut(HttpServletRequest request) {
        return HTTPMethod.PUT.toString().equalsIgnoreCase(request.getMethod());
    }

    /**
     * 判断请求方式DELETE
     *
     * @param request
     * @return
     */
    public static boolean isDelete(HttpServletRequest request) {
        return HTTPMethod.DELETE.toString().equalsIgnoreCase(request.getMethod());
    }

    /**
     * 判断请求方式PATCH
     *
     * @param request
     * @return
     */
    public static boolean isPatch(HttpServletRequest request) {
        return HTTPMethod.PATCH.toString().equalsIgnoreCase(request.getMethod());
    }

    /**
     * 判断请求方式TRACE
     *
     * @param request
     * @return
     */
    public static boolean isTrace(HttpServletRequest request) {
        return HTTPMethod.TRACE.toString().equalsIgnoreCase(request.getMethod());
    }

    /**
     * 判断请求方式HEAD
     *
     * @param request
     * @return
     */
    public static boolean isHead(HttpServletRequest request) {
        return HTTPMethod.HEAD.toString().equalsIgnoreCase(request.getMethod());
    }

    /**
     * 判断请求方式OPTIONS
     *
     * @param request
     * @return
     */
    public static boolean isOptions(HttpServletRequest request) {
        return HTTPMethod.OPTIONS.toString().equalsIgnoreCase(request.getMethod());
    }


    /**
     * 获取请求
     *
     * @param request
     * @return
     */
    public static String getRequestBody(HttpServletRequest request) {
        String requestBody = null;
        if (isContainBody(request))
            try {
                StringWriter writer = new StringWriter();
                IOUtils.copy(request.getInputStream(), writer, StandardCharsets.UTF_8.name());
                requestBody = writer.toString();
            } catch (IOException ignored) {
            }
        return requestBody;
    }

    /**
     * 获取请求
     *
     * @param request
     * @return
     */
    public static byte[] getByteBody(HttpServletRequest request) {
        byte[] body = new byte[0];
        try {
            body = StreamUtils.copyToByteArray(request.getInputStream());
        } catch (IOException e) {
            log.error("Error: Get RequestBody byte[] fail," + e);
        }
        return body;
    }

    /**
     * 是否包含请求体
     *
     * @param request
     * @return
     */
    public static boolean isContainBody(HttpServletRequest request) {
        return isPost(request) || isPut(request) || isPatch(request);
    }

    /**
     * 获取当前用户id
     */
    public static Integer currentUid() {
        String token = ApplicationUtils.getRequest().getHeader("Authorization");
        ApiAssert.notNull(ErrorCodeEnum.UNAUTHORIZED, token);
        token = token.replaceFirst("Bearer ", "");
        Claims claims = JWTTokenUtils.getClaim(token);
        ApiAssert.notNull(ErrorCodeEnum.UNAUTHORIZED, claims);
        return claims.get(JWTTokenUtils._ID, Integer.class);
    }

}