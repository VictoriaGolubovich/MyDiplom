package com.example.project.service;

import com.example.project.model.Image;
import com.example.project.model.Product;
import com.example.project.model.User;
import com.example.project.repository.ImageRepository;
import com.example.project.repository.ProductRepository;
import com.example.project.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Slf4j
@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final ImageRepository imageRepository;
    private final UserRepository userRepository;


    public ProductService(ProductRepository productRepository, ImageRepository imageRepository,
                          UserRepository userRepository) {
        this.productRepository = productRepository;
        this.imageRepository = imageRepository;
        this.userRepository = userRepository;
    }

    public List<Product> listProducts(String title) {
        if (title != null) return productRepository.findByTitle(title);
        return productRepository.findAll();
    }

    public void saveProduct(Product product, MultipartFile file, Long id) throws IOException {

        if (!file.isEmpty()) {
            Image image = new Image();
            image.setBytes(file.getBytes());
            product.addImageToProduct(image);
        }
        User user = userRepository.findUserById(id);

        product.setAuthor(user.getName());
        product.setPhoneNumber(user.getPhoneNumber());
        product.setEmail(user.getEmail());
        productRepository.save(product);

        user.getProducts().add(product);
        userRepository.save(user);

    }

    public void deleteProduct(Long productId, Long userId) {
        Product product = productRepository.findProductById(productId);
        User user = userRepository.findUserById(userId);
        if (user.getProducts().contains(product)){
            try {
                user.getProducts().remove(product);
                userRepository.save(user);
                productRepository.deleteById(productId);
            }catch (Exception e) {
                log.info(e.toString());
            }
        }
    }

    public Product getById(Long id) {
        return productRepository.findProductById(id);
    }

    public byte[] getImage(Long productId) {
        Optional<Image> productImageOptional = imageRepository.findByProductId(productId);
        if (productImageOptional.isPresent()) {
            return productImageOptional.get().getBytes();
        } else {
            return null;
        }
    }

    public String getImageBase64(Long productId) {
        String base64ImageData = new String();
        byte[] imageData = getImage(productId);
        if (imageData != null) {
            base64ImageData = Base64.getEncoder().encodeToString(imageData);
        }
        return base64ImageData;
    }

    public Set<Product> findProductsByUserId(Long id){
        User user = userRepository.findUserById(id);
        return user.getProducts();
    }
}


