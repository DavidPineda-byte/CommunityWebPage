package com.DavidsCode.CommunityWebPage.controller;

import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/checkout")
public class checkout {


    private static final String YOUR_DOMAIN = "localhost:8080";

    @PostMapping
    public String checkout(Model model) {

        SessionCreateParams params =
                SessionCreateParams.builder()
                        .setMode(SessionCreateParams.Mode.PAYMENT)
                        .setSuccessUrl(YOUR_DOMAIN + "/success.html")
                        .addLineItem(
                                SessionCreateParams.LineItem.builder()
                                        .setQuantity(1L)
                                        // Provide the exact Price ID (for example, price_1234) of the product you want to sell
                                        .setPrice("{{PRICE_ID}}")
                                        .build())
                        .build();

        return "checkout";
    }




}
