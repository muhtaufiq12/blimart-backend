package com.bliblifuturebackend.bliblimart.controller;

import com.blibli.oss.command.CommandExecutor;
import com.blibli.oss.common.response.Response;
import com.blibli.oss.common.response.ResponseHelper;
import com.bliblifuturebackend.bliblimart.command.*;
import com.bliblifuturebackend.bliblimart.constant.ApiConstant;
import com.bliblifuturebackend.bliblimart.model.request.ImageRequest;
import com.bliblifuturebackend.bliblimart.model.request.PagingRequest;
import com.bliblifuturebackend.bliblimart.model.request.ProfileRequest;
import com.bliblifuturebackend.bliblimart.model.response.MessageResponse;
import com.bliblifuturebackend.bliblimart.model.response.ProfileResponse;
import com.bliblifuturebackend.bliblimart.model.response.UserResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.security.Principal;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(ApiConstant.API_USER)
public class UserController {

    @Autowired
    private CommandExecutor commandExecutor;

    @GetMapping(value = "/profile/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<Response<ProfileResponse>> getProfileByUserId(@PathVariable String id){
        return commandExecutor.execute(GetProfileCommand.class, id)
                .map(ResponseHelper::ok)
                .subscribeOn(Schedulers.elastic());
    }

    @GetMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<Response<List<UserResponse>>> getAllUsers(@RequestParam("page") int page, @RequestParam("size") int size){
        PagingRequest request = PagingRequest.builder().page(page-1).size(size).build();
        return commandExecutor.execute(GetAllUsersCommand.class, request)
                .map(pagingResponse -> {
                    Response<List<UserResponse>> response = ResponseHelper.ok(pagingResponse.getData());
                    response.setPaging(pagingResponse.getPaging());
                    return response;
                })
                .subscribeOn(Schedulers.elastic());
    }

    @PutMapping(value = "/profile")
    @PreAuthorize("hasRole('USER')")
    public Mono<Response<ProfileResponse>> updateProfile(@RequestBody ProfileRequest request, Principal principal){
        request.setRequester(principal.getName());
        return commandExecutor.execute(UpdateProfileCommand.class, request)
                .map(ResponseHelper::ok)
                .subscribeOn(Schedulers.elastic());
    }

    @GetMapping(value = "/profile/me")
    @PreAuthorize("hasRole('USER')")
    public Mono<Response<ProfileResponse>> getMyProfile(Principal principal){
        return commandExecutor.execute(GetMyProfileCommand.class, principal.getName())
                .map(ResponseHelper::ok)
                .subscribeOn(Schedulers.elastic());
    }

    @GetMapping(value = "/photo/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    public Mono<byte[]> getPhoto(@PathVariable String id){
        return commandExecutor.execute(GetUserPhotoCommand.class, id)
                .subscribeOn(Schedulers.elastic());
    }

    @PostMapping(value = "/photo")
    @PreAuthorize("hasRole('USER')")
    public Mono<Response<MessageResponse>> uploadPhoto(@RequestPart("photo") FilePart photo, Principal principal){
        ImageRequest request = new ImageRequest(photo);
        request.setRequester(principal.getName());
        return commandExecutor.execute(UploadUserPhotoCommand.class, request)
                .map(ResponseHelper::ok)
                .subscribeOn(Schedulers.elastic());
    }

    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<Response<UserResponse>> blockUser(@PathVariable("id") String id){
        return commandExecutor.execute(BlockUserCommand.class ,id)
                .map(ResponseHelper::ok)
                .subscribeOn(Schedulers.elastic());
    }

    @PostMapping(value = "/unblock/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<Response<UserResponse>> unblockUser(@PathVariable("id") String id){
        return commandExecutor.execute(UnblockUserCommand.class ,id)
                .map(ResponseHelper::ok)
                .subscribeOn(Schedulers.elastic());
    }

}
