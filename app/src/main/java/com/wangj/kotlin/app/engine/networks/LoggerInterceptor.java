package com.wangj.kotlin.app.engine.networks;

import com.orhanobut.logger.Logger;

import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.concurrent.TimeUnit;

import okhttp3.Connection;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

/**
 * 当前类注释:api请求log拦截器
 * <p>
 * Author : junwang <p>
 * Created: 2018/3/6.上午11:38 <P>
 * Description:
 * 使用{@link Logger} 打印日志
 * <p>
 * E-mail:lijiawangjun@gmail.com
 */

public class LoggerInterceptor implements Interceptor {
    //标签
    private static final String TAG = "OkHttp";
    private static final Charset UTF8 = Charset.forName("UTF-8");

    public enum Level {
        /**
         * logs request and response basic info
         * <p>
         * request info [method url Protocol]
         * <p>
         * response info [code message tookTime responseBody]
         */
        BASIC,

        /**
         * include all info{@link #BASIC}
         * <p>logs request headers and response headers
         */
        HEADERS
    }

    private final Level mLevel;

    public LoggerInterceptor() {
        this(Level.BASIC);
    }

    /**
     * @param level {@link Level}
     */
    public LoggerInterceptor(Level level) {
        mLevel = level;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        StringBuilder requestBuilder = new StringBuilder();

        Connection connection = chain.connection();
        Protocol protocol = connection != null ? connection.protocol() : Protocol.HTTP_1_1;
        requestBuilder.append(request.method())
                .append(" ")
                .append(request.url())
                .append(" ")
                .append(protocol)
                .append("\n");

        if (mLevel == Level.HEADERS) {
            Headers requestHeaders = request.headers();
            for (int i = 0, count = requestHeaders.size(); i < count; i++) {
                String name = requestHeaders.name(i);
                if (!"Content-Type".equalsIgnoreCase(name) && !"Content-Length".equalsIgnoreCase(name)) {
                    requestBuilder.append(name)
                            .append(": ")
                            .append(requestHeaders.value(i))
                            .append("\n");
                }
            }
        }

        RequestBody requestBody = request.body();
        if (requestBody != null) {
            if (requestBody.contentType() != null) {
                requestBuilder.append("Content-Type: ")
                        .append(requestBody.contentType())
                        .append("\n");
            }
            if (requestBody.contentLength() != -1) {
                requestBuilder.append("Content-Length: ")
                        .append(requestBody.contentLength())
                        .append("\n");
            }
            Buffer buffer = new Buffer();
            requestBody.writeTo(buffer);
            requestBuilder.append("Request-Body: ")
                    .append(buffer.readUtf8())
                    .append("\n");
        }

        Logger.t(TAG).d(requestBuilder.toString());

        StringBuilder responseBuilder = new StringBuilder();
        long startNs = System.nanoTime();
        Response response;
        try {
            response = chain.proceed(request);
        } catch (IOException e) {
            Logger.t(TAG).e(e, "HTTP FAILED");
            throw e;
        }
        long tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);
        ResponseBody responseBody = response.body();
        long contentLength = responseBody.contentLength();

        responseBuilder.append(response.code())
                .append(" ")
                .append(response.message())
                .append(" ")
                .append(response.request().url())
                .append(" (took: ")
                .append(tookMs)
                .append("ms")
                .append(")\n");

        if (mLevel == Level.HEADERS) {
            Headers responseHeaders = response.headers();
            for (int i = 0, count = responseHeaders.size(); i < count; i++) {
                responseBuilder.append(responseHeaders.name(i))
                        .append(": ")
                        .append(responseHeaders.value(i))
                        .append("\n");
            }
        }

        if (bodyEncoded(response.headers())) {
            responseBuilder.append("END HTTP (encoded body omitted)");
            Logger.t(TAG).d(responseBuilder.toString());
        } else {
            BufferedSource source = responseBody.source();
            source.request(Long.MAX_VALUE); // Buffer the entire body.
            Buffer buffer = source.buffer();

            Charset charset = UTF8;
            MediaType contentType = responseBody.contentType();
            if (contentType != null) {
                try {
                    charset = contentType.charset(UTF8);
                } catch (UnsupportedCharsetException e) {
                    responseBuilder.append("Couldn't decode the response body; charset is likely malformed.")
                            .append("END HTTP");
                    Logger.t(TAG).d(responseBuilder.toString());
                    return response;
                }
            }

            if (!isPlaintext(buffer)) {
                responseBuilder.append("END HTTP (binary ")
                        .append(buffer.size())
                        .append("-byte body omitted)");
                Logger.t(TAG).d(responseBuilder.toString());
                return response;
            }

            responseBuilder.append("END HTTP (")
                    .append(buffer.size())
                    .append("-byte body)");

            Logger.t(TAG).d(responseBuilder.toString());

            if (contentLength != 0) {
                Logger.t(TAG).json(buffer.clone().readString(charset));
            }
        }

        return response;
    }

    /**
     * Returns true if the body in question probably contains human readable text. Uses a small sample
     * of code points to detect unicode control characters commonly used in binary file signatures.
     */
    private boolean isPlaintext(Buffer buffer) {
        try {
            Buffer prefix = new Buffer();
            long byteCount = buffer.size() < 64 ? buffer.size() : 64;
            buffer.copyTo(prefix, 0, byteCount);
            for (int i = 0; i < 16; i++) {
                if (prefix.exhausted()) {
                    break;
                }
                int codePoint = prefix.readUtf8CodePoint();
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false;
                }
            }
            return true;
        } catch (EOFException e) {
            return false; // Truncated UTF-8 sequence.
        }
    }

    private boolean bodyEncoded(Headers headers) {
        String contentEncoding = headers.get("Content-Encoding");
        return contentEncoding != null && !contentEncoding.equalsIgnoreCase("identity");
    }
}
