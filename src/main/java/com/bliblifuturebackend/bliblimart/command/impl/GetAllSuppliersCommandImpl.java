package com.bliblifuturebackend.bliblimart.command.impl;

import com.bliblifuturebackend.bliblimart.command.GetAllSuppliersCommand;
import com.bliblifuturebackend.bliblimart.model.entity.Supplier;
import com.bliblifuturebackend.bliblimart.model.request.PagingRequest;
import com.bliblifuturebackend.bliblimart.model.response.PagingResponse;
import com.bliblifuturebackend.bliblimart.model.response.SupplierResponse;
import com.bliblifuturebackend.bliblimart.repository.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class GetAllSuppliersCommandImpl implements GetAllSuppliersCommand {

    @Autowired
    private SupplierRepository supplierRepository;

    @Override
    public Mono<PagingResponse<SupplierResponse>> execute(PagingRequest request) {
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        PagingResponse<SupplierResponse> response = new PagingResponse<>();
        return supplierRepository.findAll(pageable)
                .map(Supplier::createResponse)
                .collectList()
                .flatMap(supplierResponses -> {
                    response.setData(supplierResponses);
                    return supplierRepository.count();
                })
                .map(total -> response.getPagingResponse(request, Math.toIntExact(total)));
    }

}
