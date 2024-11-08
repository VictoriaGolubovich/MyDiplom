package com.example.project.controller;

import com.example.project.model.Product;
import com.example.project.model.User;
import com.example.project.service.ProductService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;
    public ProductController(ProductService productService){
        this.productService = productService;
    }
    @GetMapping
    public String products(@RequestParam(name = "title", required = false) String title, Model model) {
        model.addAttribute("products", productService.listProducts(title));
        return "products";
    }
    @GetMapping("/{id}")
    public String productInfo(@PathVariable Long id, Model model) {
        model.addAttribute("product", productService.getById(id));
        model.addAttribute("image", productService.getImageBase64(id));
        return "product-info";
    }
    @GetMapping("/user/{id}")
    public String myProduct(@PathVariable Long id, Model model) {
        model.addAttribute("product", productService.getById(id));
        model.addAttribute("image", productService.getImageBase64(id));
        return "my-product";
    }
    @GetMapping("/user")
    public String products(Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("user_products", productService.findProductsByUserId(user.getId()));
        return "my-products";
    }
    @GetMapping("/add")
    public String newProduct(Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("product");
        return "new-product";
    }
    @PostMapping("/create")
    public String createProduct(@RequestParam("file1") MultipartFile file, Product product,
                                @AuthenticationPrincipal User user) throws IOException {
        productService.saveProduct(product, file, user.getId());
        return "redirect:/products/user";
    }
    @PostMapping("/delete/{productId}")
    public String deleteProduct(@PathVariable Long productId, @AuthenticationPrincipal User user) {
        productService.deleteProduct(productId, user.getId());
        return "redirect:/products/user";
    }
}