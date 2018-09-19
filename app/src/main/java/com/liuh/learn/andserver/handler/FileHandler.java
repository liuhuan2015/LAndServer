package com.liuh.learn.andserver.handler;

import com.liuh.learn.andserver.BaseApp;
import com.yanzhenjie.andserver.RequestHandler;
import com.yanzhenjie.andserver.RequestMethod;
import com.yanzhenjie.andserver.annotation.RequestMapping;

import org.apache.commons.io.IOUtils;
import org.apache.httpcore.HttpEntity;
import org.apache.httpcore.HttpException;
import org.apache.httpcore.HttpRequest;
import org.apache.httpcore.HttpResponse;
import org.apache.httpcore.entity.ContentType;
import org.apache.httpcore.entity.FileEntity;
import org.apache.httpcore.protocol.HttpContext;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

import static com.yanzhenjie.andserver.util.FileUtils.getMimeType;

/**
 * Date: 2018/9/19 15:19
 * Description: Return a file.
 */
public class FileHandler implements RequestHandler {

    @RequestMapping(method = RequestMethod.GET)
    @Override
    public void handle(HttpRequest request, HttpResponse response, HttpContext context) throws HttpException, IOException {

        File file = File.createTempFile("AndServerLiu", ".txt", BaseApp.getContext().getCacheDir());
        OutputStream outputStream = new FileOutputStream(file);

        IOUtils.write("liuh.....LAN server of Android platform.", outputStream, Charset.defaultCharset());

        HttpEntity httpEntity = new FileEntity(file, ContentType.create(getMimeType(file.getAbsolutePath()), Charset.defaultCharset()));

        response.setHeader("Content-Disposition", "attachment;filename=AndServer.txt");
        response.setStatusCode(200);
        response.setEntity(httpEntity);

    }
}






























