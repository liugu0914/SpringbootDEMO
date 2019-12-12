package com.jiopeel.util;

import lombok.extern.slf4j.Slf4j;
import org.nutz.http.Http;
import org.nutz.http.Request;
import org.nutz.http.Request.METHOD;
import org.nutz.http.Response;
import org.nutz.http.Sender;
import org.nutz.http.sender.FilePostSender;
import org.nutz.lang.Lang;
import org.nutz.lang.Streams;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

/**
 * @author ：lyc
 * @description：Http工具包，用于访问API，上传或下载微信素材
 * @date ：2019/12/12 18:25
 */
@Slf4j
public class HttpTool {


    private static final int CONNECT_TIME_OUT = 5 * 1000;
    private static final String FILE_NAME_FLAG = "filename=";

    public static String get(String url) {
        if (log.isDebugEnabled()) {
            log.debug("Request url: {}, default timeout: {}", url, CONNECT_TIME_OUT);
        }
        try {
            Response resp = Http.get(url, CONNECT_TIME_OUT);
            if (resp.isOK()) {
                String content = resp.getContent("UTF-8");
                if (log.isInfoEnabled()) {
                    log.info("GET Request success. Response content: {}", content);
                }
                return content;
            }

            throw Lang.wrapThrow(new RuntimeException(String.format("Get request [{}] failed. status: {}",
                    url,
                    resp.getStatus())));
        } catch (Exception e) {
            throw Lang.wrapThrow(e);
        }
    }

    public static String post(String url, String body) {
        if (log.isDebugEnabled()) {
            log.debug("Request url: {}, post data: {}, default timeout: {}",
                    url,
                    body,
                    CONNECT_TIME_OUT);
        }

        try {
            Request req = Request.create(url, METHOD.POST);
            req.setEnc("UTF-8");
            req.setData(body);

            Response resp = Sender.create(req, CONNECT_TIME_OUT).send();
            if (resp.isOK()) {
                String content = resp.getContent();
                if (log.isInfoEnabled()) {
                    log.info("POST Request success. Response content: {}", content);
                }
                return content;
            }

            throw Lang.wrapThrow(new RuntimeException(String.format("Post request [{}] failed. status: {}",
                    url,
                    resp.getStatus())));
        } catch (Exception e) {
            throw Lang.wrapThrow(e);
        }
    }

    public static String post(String url, Map<String, Object> params) {
        try {
            Request req = Request.create(url, METHOD.POST);
            req.setEnc("UTF-8");
            req.setParams(params);
            Response resp = Sender.create(req, CONNECT_TIME_OUT).send();
            if (resp.isOK()) {
                String content = resp.getContent();
                if (log.isInfoEnabled()) {
                    log.info("POST Request success. Response content: {}", content);
                }
                return content;
            }

            throw Lang.wrapThrow(new RuntimeException(String.format("Post request [{}] failed. status: {}",
                    url,
                    resp.getStatus())));
        } catch (Exception e) {
            throw Lang.wrapThrow(e);
        }
    }

    public static String upload(String url, File file) {
        if (log.isDebugEnabled()) {
            log.debug("Upload url: {}, file name: {}, default timeout: {}",
                    url,
                    file.getName(),
                    CONNECT_TIME_OUT);
        }

        try {
            Request req = Request.create(url, METHOD.POST);
            req.getParams().put("media", file);
            Response resp = new FilePostSender(req).send();
            if (resp.isOK()) {
                String content = resp.getContent();
                return content;
            }

            throw Lang.wrapThrow(new RuntimeException(String.format("Upload file [{}] failed. status: {}",
                    url,
                    resp.getStatus())));
        } catch (Exception e) {
            throw Lang.wrapThrow(e);
        }
    }

    public static Object download(String url) {
        if (log.isDebugEnabled()) {
            log.debug("Upload url: {}, default timeout: {}", url, CONNECT_TIME_OUT);
        }

        try {
            Response resp = Http.get(url);
            if (resp.isOK()) {
                String cd = resp.getHeader().get("Content-disposition");
                if (log.isInfoEnabled()) {
                    log.info("Get download file info: {}", cd);
                }

                if (Lang.isEmpty(cd)) {
                    return resp.getContent();
                }

                cd = cd.substring(cd.indexOf(FILE_NAME_FLAG) + FILE_NAME_FLAG.length());
                String tmp = cd.startsWith("\"") ? cd.substring(1) : cd;
                tmp = tmp.endsWith("\"") ? cd.replace("\"", "") : cd;
                String filename = tmp.substring(0, tmp.lastIndexOf("."));
                String fileext = tmp.substring(tmp.lastIndexOf("."));
                if (log.isInfoEnabled()) {
                    log.info("Download file name: {}", filename);
                    log.info("Download file ext: {}", fileext);
                }

                File tmpfile = File.createTempFile(filename, fileext);
                InputStream is = resp.getStream();
                OutputStream os = new FileOutputStream(tmpfile);
                Streams.writeAndClose(os, is);
                return tmpfile;
            }

            throw Lang.wrapThrow(new RuntimeException(String.format("Download file [{}] failed. status: {}, content: {}",
                    url,
                    resp.getStatus(),
                    resp.getContent())));
        } catch (Exception e) {
            throw Lang.wrapThrow(e);
        }
    }
}
