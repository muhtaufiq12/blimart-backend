package com.bliblifuturebackend.bliblimart.command.impl;

import com.bliblifuturebackend.bliblimart.command.UploadUserPhotoCommand;
import com.bliblifuturebackend.bliblimart.constant.FileConstant;
import com.bliblifuturebackend.bliblimart.constant.UserConstant;
import com.bliblifuturebackend.bliblimart.model.entity.User;
import com.bliblifuturebackend.bliblimart.model.request.ImageRequest;
import com.bliblifuturebackend.bliblimart.model.response.MessageResponse;
import com.bliblifuturebackend.bliblimart.repository.UserRepository;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class UploadUserPhotoCommandImpl implements UploadUserPhotoCommand {

    @Autowired
    private UserRepository userRepository;

    @SneakyThrows
    @Override
    public Mono<MessageResponse> execute(ImageRequest request) {

        return userRepository.findByUsername(request.getRequester())
                .doOnSuccess(this::checkNull)
                .map(user -> uploadPhoto(request.getImage(), user.getId()))
                .map(res -> MessageResponse.SUCCESS);
    }

    private void checkNull(User user) {
        if (user == null){
            throw new NullPointerException("User not found");
        }
    }

    private String uploadPhoto(FilePart file, String id) {
        String filename = id + FileConstant.JPG;

        String uploadPath = UserConstant.USER_PHOTO_STORAGE_PATH + filename;

        Path path = Paths.get(uploadPath);

        file.transferTo(path);
//        byte[] bytes = file.getBytes();
//        Files.write(path, bytes);

        return id;
    }
}
