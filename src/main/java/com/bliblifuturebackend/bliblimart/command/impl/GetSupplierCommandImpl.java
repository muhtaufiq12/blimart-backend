package com.bliblifuturebackend.bliblimart.command.impl;

import com.bliblifuturebackend.bliblimart.command.GetSupplierCommand;
import com.bliblifuturebackend.bliblimart.model.entity.Supplier;
import com.bliblifuturebackend.bliblimart.model.response.SupplierResponse;
import com.bliblifuturebackend.bliblimart.repository.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class GetSupplierCommandImpl implements GetSupplierCommand {

    @Autowired
    private SupplierRepository supplierRepository;

    @Override
    public Mono<SupplierResponse> execute(String id) {
        return Mono.fromCallable(() -> id)
                .flatMap(supplierId -> supplierRepository.findById(supplierId))
                .map(Supplier::createResponse);
    }

}
