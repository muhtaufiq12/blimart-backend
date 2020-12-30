package com.bliblifuturebackend.bliblimart.command.impl;

import com.bliblifuturebackend.bliblimart.command.GetAllSuppliesCommand;
import com.bliblifuturebackend.bliblimart.model.entity.Supplier;
import com.bliblifuturebackend.bliblimart.model.entity.Supply;
import com.bliblifuturebackend.bliblimart.model.request.PagingRequest;
import com.bliblifuturebackend.bliblimart.model.response.PagingResponse;
import com.bliblifuturebackend.bliblimart.model.response.SupplyResponse;
import com.bliblifuturebackend.bliblimart.model.response.responseUtil.ProductResponseUtil;
import com.bliblifuturebackend.bliblimart.repository.ProductRepository;
import com.bliblifuturebackend.bliblimart.repository.SupplierRepository;
import com.bliblifuturebackend.bliblimart.repository.SupplyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class GetAllSuppliesCommandImpl implements GetAllSuppliesCommand {

    @Autowired
    private SupplyRepository supplyRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductResponseUtil productResponseUtil;

    @Override
    public Mono<PagingResponse<SupplyResponse>> execute(PagingRequest request) {
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        PagingResponse<SupplyResponse> response = new PagingResponse<>();
        return supplyRepository.findAll(pageable)
                .flatMap(this::getSupplyResponseMono)
                .collectList()
                .flatMap(supplyResponses -> {
                    response.setData(supplyResponses);
                    return supplyRepository.count();
                })
                .map(total -> response.getPagingResponse(request, Math.toIntExact(total)));
    }

    private Mono<SupplyResponse> getSupplyResponseMono(Supply supply) {
        return productRepository.findById(supply.getProductId())
                .flatMap(product -> productResponseUtil.getResponse(product))
                .flatMap(productResponse -> {
                    SupplyResponse supplyResponse = supply.createResponse();
                    supplyResponse.setProductResponse(productResponse);
                    return applySupplier(supply, supplyResponse);
                });
    }

    private Mono<SupplyResponse> applySupplier(Supply supply, SupplyResponse supplyResponse) {
        return supplierRepository.findById(supply.getSupplierId())
                .map(Supplier::createResponse)
                .map(supplierResponse -> {
                    supplyResponse.setSupplierResponse(supplierResponse);
                    return supplyResponse;
                });
    }

}
