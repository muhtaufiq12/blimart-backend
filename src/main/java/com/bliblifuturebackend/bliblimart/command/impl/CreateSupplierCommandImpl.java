package com.bliblifuturebackend.bliblimart.command.impl;

import com.bliblifuturebackend.bliblimart.command.CreateSupplierCommand;
import com.bliblifuturebackend.bliblimart.model.entity.Supplier;
import com.bliblifuturebackend.bliblimart.model.request.SupplierRequest;
import com.bliblifuturebackend.bliblimart.model.response.SupplierResponse;
import com.bliblifuturebackend.bliblimart.repository.SupplierRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.UUID;

@Service
public class CreateSupplierCommandImpl implements CreateSupplierCommand {

    @Autowired
    private SupplierRepository supplierRepository;

    @Override
    public Mono<SupplierResponse> execute(SupplierRequest request) {
        return supplierRepository.findByName(request.getName())
                .doOnSuccess(this::checkIfExists)
                .switchIfEmpty(Mono.just(createSupplier(request)))
                .flatMap(supplier -> supplierRepository.save(supplier))
                .map(Supplier::createResponse);
    }

    private void checkIfExists(Supplier supplier) {
        if (supplier != null){
            throw new IllegalArgumentException("Supplier with the same name already exists!");
        }
    }

    private Supplier createSupplier(SupplierRequest request) {
        Supplier supplier = new Supplier();
        BeanUtils.copyProperties(request, supplier);
        supplier.setId(UUID.randomUUID().toString());

        String username = request.getRequester();

        Date date = new Date();
        supplier.setCreatedDate(date);
        supplier.setCreatedBy(username);
        supplier.setUpdatedDate(date);
        supplier.setUpdatedBy(username);

        return supplier;
    }
}
