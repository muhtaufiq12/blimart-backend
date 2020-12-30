package com.bliblifuturebackend.bliblimart.command.impl;

import com.bliblifuturebackend.bliblimart.command.CreateSupplyCommand;
import com.bliblifuturebackend.bliblimart.model.entity.Product;
import com.bliblifuturebackend.bliblimart.model.entity.Supplier;
import com.bliblifuturebackend.bliblimart.model.entity.Supply;
import com.bliblifuturebackend.bliblimart.model.request.SupplyRequest;
import com.bliblifuturebackend.bliblimart.model.response.SupplyResponse;
import com.bliblifuturebackend.bliblimart.repository.ProductRepository;
import com.bliblifuturebackend.bliblimart.repository.SupplierRepository;
import com.bliblifuturebackend.bliblimart.repository.SupplyRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.UUID;

@Service
public class CreateSupplyCommandImpl implements CreateSupplyCommand {

    @Autowired
    private SupplyRepository supplyRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Mono<SupplyResponse> execute(SupplyRequest request) {
        return supplierRepository.findById(request.getSupplierId())
                .doOnSuccess(this::checkSupplierNull)
                .flatMap(supplier -> productRepository.findById(request.getProductId())
                        .doOnSuccess(this::checkProductNull)
                        .flatMap(product -> {
                            Supply supply = createSupply(request);
                            return supplyRepository.save(supply)
                                    .flatMap(res -> updateProductStock(res, product))
                                    .map(product1 -> supply.createResponse())
                                    .map(supplyResponse -> {
                                        supplyResponse.setSupplierResponse(supplier.createResponse());
                                        supplyResponse.setProductResponse(product.createResponse());
                                        return supplyResponse;
                                    });
                        })
                );
    }

    private void checkProductNull(Product product) {
        if (product == null){
            throw new NullPointerException("Product not found");
        }
    }

    private void checkSupplierNull(Supplier supplier) {
        if (supplier == null){
            throw new NullPointerException("Supplier not found");
        }
    }

    private Mono<Product> updateProductStock(Supply supply, Product product) {
        product.setStock(product.getStock() + supply.getQty());
        product.setUpdatedDate(new Date());
        product.setUpdatedBy(supply.getCreatedBy());
        return productRepository.save(product);
    }

    private Supply createSupply(SupplyRequest request) {
        Supply supply = new Supply();

        BeanUtils.copyProperties(request, supply);

        supply.setId(UUID.randomUUID().toString());

        String username = request.getRequester();

        Date date = new Date();
        supply.setCreatedDate(date);
        supply.setCreatedBy(username);
        supply.setUpdatedDate(date);
        supply.setUpdatedBy(username);

        return supply;
    }
}
