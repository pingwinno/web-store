package com.study.store.web.servlet;

import com.study.store.Basket;
import com.study.store.service.BasketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@Scope("session")
@RequestMapping("/basket")
public class BasketServlet {

    private static final String BASKET = "basket";
    @Autowired
    private BasketService basketService;
    @Autowired
    private Basket basket;

    @GetMapping
    protected String getBasketPage(Model model) {
        model.addAttribute("products", basketService.getBasketProducts(basket.getProducts()));
        return BASKET;
    }


    @PostMapping("/add")
    public String addToBasket(@RequestParam Long productId) {
        basket.addProductId(productId);
        return "redirect:/";
    }

    @PostMapping("/delete")
    public String deleteFromBasket(@RequestParam Long productId) {
        basket.removeProduct(productId);
        return "redirect:/basket";
    }
}
