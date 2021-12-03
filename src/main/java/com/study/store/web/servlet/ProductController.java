package com.study.store.web.servlet;

import com.study.store.model.Product;
import com.study.store.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Optional;

@Slf4j
@Controller
public class ProductController {

    private final static String LIST_TEMPLATE = "%s-list";
    @Autowired
    private ProductService productService;


    @RequestMapping(path = "/", method = RequestMethod.GET)
    protected String getAllProductPage(@RequestAttribute Optional<String> prefixOptional, Model model) {
        model.addAttribute("products", productService.getAll());
        var templateName = String.format(LIST_TEMPLATE, prefixOptional.orElse("user"));
        return templateName;
    }

    @RequestMapping(path = "/search", method = RequestMethod.GET)
    @ResponseBody
    protected String getSearchProductPage(@RequestAttribute Optional<String> prefixOptional, @RequestParam String searchInput, Model model) {
        model.addAttribute("products", productService.search(searchInput));
        var templateName = String.format(LIST_TEMPLATE, prefixOptional.orElse("user"));
        return templateName;
    }

    @RequestMapping(path = "/add", method = RequestMethod.GET)
    protected String getAddPage() {
        return "add";
    }

    @RequestMapping(path = "/add", method = RequestMethod.POST)
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

    public void setProductService(ProductService productService) {
        this.productService = productService;
    }
}
