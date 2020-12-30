package com.bliblifuturebackend.bliblimart.command.impl;

import com.bliblifuturebackend.bliblimart.command.UpdateSupplierCommand;
import com.bliblifuturebackend.bliblimart.model.entity.Supplier;
import com.bliblifuturebackend.bliblimart.model.request.SupplierRequest;
import com.bliblifuturebackend.bliblimart.model.response.SupplierResponse;
import com.bliblifuturebackend.bliblimart.repository.SupplierRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Date;

@Service
public class UpdateSupplierCommandImpl implements UpdateSupplierCommand {

    @Autowired
    private SupplierRepository supplierRepository;

    @Override
    public Mono<SupplierResponse> execute(SupplierRequest request) {
        return supplierRepository.findById(request.getId())
                .doOnSuccess(this::checkNull)
                .map(supplier -> updateSupplier(request, supplier))
                .flatMap(supplier -> supplierRepository.save(supplier))
                .map(Supplier::createResponse);
    }

    private void checkNull(Supplier supplier) {
        if (supplier == null){
            throw new NullPointerException("Supplier not found!");
        }
    }

    private Supplier updateSupplier(SupplierRequest request, Supplier supplier) {
        BeanUtils.copyProperties(request, supplier);
        String username = request.getRequester();
        Date date = new Date();
        supplier.setUpdatedDate(date);
        supplier.setUpdatedBy(username);
        return supplier;
    }
}
