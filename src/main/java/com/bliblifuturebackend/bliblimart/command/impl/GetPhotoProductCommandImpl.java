package com.bliblifuturebackend.bliblimart.command.impl;

import com.bliblifuturebackend.bliblimart.command.GetPhotoProductCommand;
import com.bliblifuturebackend.bliblimart.constant.FileConstant;
import com.bliblifuturebackend.bliblimart.constant.ProductConstant;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

@Service
public class GetPhotoProductCommandImpl implements GetPhotoProductCommand {

    @SneakyThrows
    @Override
    public Mono<byte[]> execute(String id) {
        String path = ProductConstant.PRODUCT_PHOTO_PATH + id + ".jpg";

        File file = new File(path);

        if (!file.exists()){
            file = new File(ProductConstant.PRODUCT_PHOTO_PATH + FileConstant.DEFAULT_FILE);
        }

        InputStream in = new FileInputStream(file.getAbsoluteFile());

        return Mono.just(IOUtils.toByteArray(in));
    }

}
