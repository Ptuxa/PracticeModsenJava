package com.example.practicemodsenjava.service.impl;

import com.example.practicemodsenjava.exceptionHandling.GlobalExceptionHandler;
import com.example.practicemodsenjava.model.dto.response.ProductResponse;
import com.example.practicemodsenjava.mapper.ProductMapper;
import com.example.practicemodsenjava.model.entity.Product;
import com.example.practicemodsenjava.repository.ProductRepository;
import com.example.practicemodsenjava.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse getProductById(UUID productId) {
        Product product = getProductOrThrow(productId);
        return productMapper.toProductResponse(product);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getProductsByCategoryId(UUID categoryId) {
        List<Product> products = productRepository.findByCategory_Id(categoryId);
        return products.stream()
                .map(productMapper::toProductResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ProductResponse createProduct(UUID categoryId, String productName) {
        Product product = new Product();
        // Устанавливаем свойства для product
        // product.setCategory(...);
        // product.setName(productName);

        Product savedProduct = productRepository.save(product);
        return productMapper.toProductResponse(savedProduct);
    }

    @Override
    @Transactional
    public ProductResponse updateProduct(UUID productId, String productName) {
        Product product = getProductOrThrow(productId);
        // Обновляем свойства для product
        // product.setName(productName);

        Product updatedProduct = productRepository.save(product);
        return productMapper.toProductResponse(updatedProduct);
    }

    @Override
    @Transactional
    public void deleteProduct(UUID productId) {
        Product product = getProductOrThrow(productId);
        productRepository.delete(product);
    }

    private Product getProductOrThrow(UUID productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new GlobalExceptionHandler("Product with id " + productId + " not found"));
    }
}