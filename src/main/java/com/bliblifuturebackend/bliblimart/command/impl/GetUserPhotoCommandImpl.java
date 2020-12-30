package com.bliblifuturebackend.bliblimart.command.impl;

import com.bliblifuturebackend.bliblimart.command.GetUserPhotoCommand;
import com.bliblifuturebackend.bliblimart.constant.FileConstant;
import com.bliblifuturebackend.bliblimart.constant.UserConstant;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

@Service
public class GetUserPhotoCommandImpl implements GetUserPhotoCommand {

    @SneakyThrows
    @Override
    public Mono<byte[]> execute(String id) {
        String path = UserConstant.USER_PHOTO_STORAGE_PATH + id + ".jpg";

        File file = new File(path);

        if (!file.exists()){
            file = new File(UserConstant.USER_PHOTO_STORAGE_PATH + FileConstant.DEFAULT_FILE);
        }

        InputStream in = new FileInputStream(file.getAbsoluteFile());

        return Mono.just(IOUtils.toByteArray(in));
    }

}
