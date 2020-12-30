package com.bliblifuturebackend.bliblimart.command.impl;

import com.bliblifuturebackend.bliblimart.command.DeleteSupplierCommand;
import com.bliblifuturebackend.bliblimart.model.entity.Supplier;
import com.bliblifuturebackend.bliblimart.model.entity.SupplierDeleted;
import com.bliblifuturebackend.bliblimart.model.request.BaseRequest;
import com.bliblifuturebackend.bliblimart.model.response.MessageResponse;
import com.bliblifuturebackend.bliblimart.repository.SupplierDeletedRepository;
import com.bliblifuturebackend.bliblimart.repository.SupplierRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Date;

@Service
public class DeleteSupplierCommandImpl implements DeleteSupplierCommand {

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private SupplierDeletedRepository supplierDeletedRepository;

    @Override
    public Mono<MessageResponse> execute(BaseRequest request) {
        return supplierRepository.findById(request.getId())
                .doOnSuccess(supplier -> {
                    checkNull(supplier);
                    supplierRepository.deleteById(supplier.getId()).subscribe();
                })
                .map(supplier -> createDeletedSupplier(supplier, request.getRequester()))
                .flatMap(supplierDeleted -> supplierDeletedRepository.save(supplierDeleted))
                .map(supplierDeleted -> MessageResponse.SUCCESS);
    }

    private SupplierDeleted createDeletedSupplier(Supplier supplier, String username) {
        SupplierDeleted supplierDeleted = new SupplierDeleted();
        BeanUtils.copyProperties(supplier, supplierDeleted);
        supplierDeleted.setDeletedBy(username);
        supplierDeleted.setDeletedDate(new Date());
        return supplierDeleted;
    }

    private void checkNull(Supplier supplier) {
        if (supplier == null){
            throw new NullPointerException("Supplier not found!");
        }
    }

}
