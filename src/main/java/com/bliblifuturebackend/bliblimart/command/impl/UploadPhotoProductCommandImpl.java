package com.bliblifuturebackend.bliblimart.command.impl;

import com.bliblifuturebackend.bliblimart.command.UploadPhotoProductCommand;
import com.bliblifuturebackend.bliblimart.constant.FileConstant;
import com.bliblifuturebackend.bliblimart.constant.ProductConstant;
import com.bliblifuturebackend.bliblimart.model.entity.Product;
import com.bliblifuturebackend.bliblimart.model.request.ImageRequest;
import com.bliblifuturebackend.bliblimart.model.response.MessageResponse;
import com.bliblifuturebackend.bliblimart.repository.ProductRepository;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class UploadPhotoProductCommandImpl implements UploadPhotoProductCommand {

    @Autowired
    private ProductRepository productRepository;

    @SneakyThrows
    @Override
    public Mono<MessageResponse> execute(ImageRequest request) {

        return productRepository.findById(request.getId())
                .doOnSuccess(this::checkNull)
                .map(product -> uploadPhoto(request.getImage(), product.getId()))
                .map(res -> MessageResponse.SUCCESS);
    }

    private void checkNull(Product product) {
        if (product == null){
            throw new NullPointerException("Product not found!");
        }
    }

    private String uploadPhoto(FilePart file, String productId) {
        String filename = productId + FileConstant.JPG;

        String uploadPath = ProductConstant.PRODUCT_PHOTO_PATH + filename;

        Path path = Paths.get(uploadPath);

        file.transferTo(path);
//        byte[] bytes = file.getBytes();
//        Files.write(path, bytes);

        return productId;
    }
}
