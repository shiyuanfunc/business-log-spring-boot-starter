package com.shiyuanfunc.businesslogstarter.filter;

import org.springframework.http.MediaType;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @Author SHI YUAN
 * @DATE 2022/3/31 5:54 PM
 * @Version 1.0
 * @Desc
 */
public class OpLogServletRequestWrapper extends HttpServletRequestWrapper {

    private byte[] body = "".getBytes(StandardCharsets.UTF_8);

    public OpLogServletRequestWrapper(HttpServletRequest request){
        super(request);
        // 读取request 中的body数据
        if (isRequestBody(request)){
            try {
                ServletInputStream inputStream = request.getInputStream();
                ByteArrayOutputStream result = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) != -1) {
                    result.write(buffer, 0, length);
                }
                body = result.toByteArray();
            }catch (Exception ex) {

            }
        }
    }


    @Override
    public ServletInputStream getInputStream() throws IOException {
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(body);

        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {

            }

            @Override
            public int read() throws IOException {
                return inputStream.read();
            }
        };
    }

    public static boolean isRequestBody(HttpServletRequest request){
        return request.getContentType() != null && (
                request.getContentType().equalsIgnoreCase(MediaType.APPLICATION_JSON_VALUE)
                        || request.getContentType().equalsIgnoreCase(MediaType.APPLICATION_JSON_UTF8_VALUE));
    }
}
