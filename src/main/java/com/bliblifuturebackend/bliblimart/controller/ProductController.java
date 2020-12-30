package com.bliblifuturebackend.bliblimart.controller;

import com.blibli.oss.command.CommandExecutor;
import com.blibli.oss.common.response.Response;
import com.blibli.oss.common.response.ResponseHelper;
import com.bliblifuturebackend.bliblimart.command.*;
import com.bliblifuturebackend.bliblimart.constant.ApiConstant;
import com.bliblifuturebackend.bliblimart.model.request.*;
import com.bliblifuturebackend.bliblimart.model.response.MessageResponse;
import com.bliblifuturebackend.bliblimart.model.response.ProductResponse;
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
@RequestMapping(ApiConstant.API_PRODUCT)
public class ProductController {

    @Autowired
    private CommandExecutor commandExecutor;

    @GetMapping(value = "/{id}")
    public Mono<Response<ProductResponse>> getProductById(@PathVariable("id") String id){
        return commandExecutor.execute(GetProductCommand.class, id)
                .map(ResponseHelper::ok)
                .subscribeOn(Schedulers.elastic());
    }

    @GetMapping()
    public Mono<Response<List<ProductResponse>>> getAllProducts(@RequestParam("page") int page, @RequestParam("size") int size){
        PagingRequest request = PagingRequest.builder().page(page-1).size(size).build();
        return commandExecutor.execute(GetAllProductsCommand.class, request)
                .map(pagingResponse -> {
                    Response<List<ProductResponse>> response = ResponseHelper.ok(pagingResponse.getData());
                    response.setPaging(pagingResponse.getPaging());
                    return response;
                })
                .subscribeOn(Schedulers.elastic());
    }

    @GetMapping("/blimart/{id}")
    public Mono<Response<List<ProductResponse>>> getProductsByBliblimartId(@PathVariable("id") String blimartId, @RequestParam("page") int page, @RequestParam("size") int size){
        PagingRequest request = PagingRequest.builder().page(page-1).size(size).blimartId(blimartId).build();
        return commandExecutor.execute(GetProductsByBlimartIdCommand.class, request)
                .map(pagingResponse -> {
                    Response<List<ProductResponse>> response = ResponseHelper.ok(pagingResponse.getData());
                    response.setPaging(pagingResponse.getPaging());
                    return response;
                })
                .subscribeOn(Schedulers.elastic());
    }

    @GetMapping("/category/{id}")
    public Mono<Response<List<ProductResponse>>> getProductsByCategoryId(@PathVariable("id") String categoryId, @RequestParam("blimartId") String blimartId, @RequestParam("page") int page, @RequestParam("size") int size){
        PagingRequest request = PagingRequest.builder().page(page-1).size(size).blimartId(blimartId).build();
        request.setId(categoryId);
        return commandExecutor.execute(GetProductsByCategoryIdCommand.class, request)
                .map(pagingResponse -> {
                    Response<List<ProductResponse>> response = ResponseHelper.ok(pagingResponse.getData());
                    response.setPaging(pagingResponse.getPaging());
                    return response;
                })
                .subscribeOn(Schedulers.elastic());
    }

    @GetMapping("/top-sold")
    public Mono<Response<List<ProductResponse>>> getTopSoldProducts(@RequestParam("blimartId") String blimartId, @RequestParam("size") int size){
        PagingRequest request = PagingRequest.builder().size(size).blimartId(blimartId).build();
        request.setPage(0);
        return commandExecutor.execute(GetTopSoldProductsCommand.class, request)
                .map(ResponseHelper::ok)
                .subscribeOn(Schedulers.elastic());
    }

    @GetMapping("/promo")
    public Mono<Response<List<ProductResponse>>> getPromoProducts(@RequestParam("blimartId") String blimartId, @RequestParam("size") int size){
        PagingRequest request = PagingRequest.builder().size(size).blimartId(blimartId).build();
        request.setPage(0);
        return commandExecutor.execute(GetPromoProductsCommand.class, request)
                .map(ResponseHelper::ok)
                .subscribeOn(Schedulers.elastic());
    }

    @GetMapping(value = "/random")
    public Mono<Response<List<ProductResponse>>> getRandomProducts(@RequestParam("blimartId") String blimartId, @RequestParam("size") int size){
        PagingRequest request = PagingRequest.builder().size(size).blimartId(blimartId).build();
        request.setPage(0);
        return commandExecutor.execute(GetRandomProductsCommand.class, request)
                .map(ResponseHelper::ok)
                .subscribeOn(Schedulers.elastic());
    }

    @GetMapping(value = "/random/{id}")
    public Mono<Response<List<ProductResponse>>> getRandomRelatedProducts(@RequestParam("blimartId") String blimartId, @PathVariable("id") String id, @RequestParam("size") int size){
        PagingRequest request = PagingRequest.builder().size(size).blimartId(blimartId).build();
        request.setId(id);
        return commandExecutor.execute(GetRandomByRelatedProductsCommand.class, request)
                .map(ResponseHelper::ok)
                .subscribeOn(Schedulers.elastic());
    }

    @GetMapping(value = "/search")
    public Mono<Response<List<ProductResponse>>> getByKeyword(@RequestParam("blimartId") String blimartId, @RequestParam("keyword") String keyword, @RequestParam("page") int page, @RequestParam("size") int size){
        SearchRequest request = new SearchRequest();
        request.setKeyword(keyword.toLowerCase());
        request.setPage(page-1);
        request.setSize(size);
        request.setBlimartId(blimartId);
        return commandExecutor.execute(GetProductsByKeywordCommand.class, request)
                .map(pagingResponse -> {
                    Response<List<ProductResponse>> response = ResponseHelper.ok(pagingResponse.getData());
                    response.setPaging(pagingResponse.getPaging());
                    return response;
                })
                .subscribeOn(Schedulers.elastic());
    }

    @PostMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<Response<ProductResponse>> addProduct(@RequestBody ProductRequest request, Principal principal){
        request.setRequester(principal.getName());
        return commandExecutor.execute(CreateProductCommand.class, request)
                .map(ResponseHelper::ok)
                .subscribeOn(Schedulers.elastic());
    }

    @PutMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<Response<ProductResponse>> updateProduct(@RequestBody ProductRequest request, Principal principal){
        request.setRequester(principal.getName());
        return commandExecutor.execute(UpdateProductCommand.class, request)
                .map(ResponseHelper::ok)
                .subscribeOn(Schedulers.elastic());
    }

    @GetMapping(value = "/photo/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    public Mono<byte[]> getPhoto(@PathVariable String id){
        return commandExecutor.execute(GetPhotoProductCommand.class, id)
                .subscribeOn(Schedulers.elastic());
    }

    @PostMapping(value = "/photo/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<Response<MessageResponse>> uploadPhoto(@RequestPart("photo") FilePart photo, @PathVariable("id") String  id, Principal principal){
        ImageRequest request = new ImageRequest(photo);
        request.setId(id);
        request.setRequester(principal.getName());
        return commandExecutor.execute(UploadPhotoProductCommand.class, request)
                .map(ResponseHelper::ok)
                .subscribeOn(Schedulers.elastic());
    }

    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<Response<MessageResponse>> deleteProduct(@PathVariable("id") String id, Principal principal){
        BaseRequest request = new BaseRequest(id, principal.getName());
        return commandExecutor.execute(DeleteProductCommand.class, request)
                .map(ResponseHelper::ok)
                .subscribeOn(Schedulers.elastic());
    }
}
