package com.study.store.web.controller;

import com.study.store.model.Product;
import com.study.store.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Optional;

@Slf4j
@Controller
public class ProductController {

    private final static String LIST_TEMPLATE = "%s-list";
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping(path = "/")
    protected String getAllProductPage(@RequestAttribute Optional<String> prefixOptional, Model model) {
        model.addAttribute("products", productService.getAll());
        var templateName = String.format(LIST_TEMPLATE, prefixOptional.orElse("user"));
        return templateName;
    }

    @GetMapping(path = "/search")
    @ResponseBody
    protected String getSearchProductPage(@RequestAttribute Optional<String> prefixOptional, @RequestParam String searchInput, Model model) {
        model.addAttribute("products", productService.search(searchInput));
        var templateName = String.format(LIST_TEMPLATE, prefixOptional.orElse("user"));
        return templateName;
    }

    @GetMapping(path = "/add")
    protected String getAddPage() {
        return "add";
    }

    @PostMapping(path = "/add")
    protected String addProduct(@ModelAttribute Product product) {
        productService.create(product);
        return "redirect:/";
    }

    @PostMapping(path = "/edit")
    protected String editProduct(@ModelAttribute Product product) {
        productService.create(product);
        return "redirect:/";
    }

    @PostMapping(path = "/delete/{id}")
    protected String editProduct(@PathVariable Long id) {
        productService.delete(id);
        return "redirect:/";
    }
}
