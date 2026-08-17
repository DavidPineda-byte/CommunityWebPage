package com.DavidsCode.CommunityWebPage.controller;

import com.DavidsCode.CommunityWebPage.dto.PoemDraft;
import com.DavidsCode.CommunityWebPage.service.TempPoemStorage;
import com.stripe.StripeClient;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
public class StripeAPIController {

    private final TempPoemStorage tempPoemStorage;

    public StripeAPIController(TempPoemStorage tempPoemStorage) {
        this.tempPoemStorage = tempPoemStorage;
    }

    @GetMapping("/check-status/{tempId}")
    public PoemDraft checkStatus(@PathVariable String tempId) {
        return tempPoemStorage.get(tempId);
    }

    @PostMapping("/create-checkout-session")
    public String createCheckoutSession(@RequestBody PoemDraft draft) throws StripeException {
        // 1. Save the draft to our "Filing Cabinet"
        String tempId = tempPoemStorage.save(draft);

        StripeClient client = new StripeClient(System.getenv("STRIPE_SECRET_KEY"));
        String YOUR_DOMAIN = "http://localhost:8080";

        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                // 2. Add tempId to the Success URL so our Controller can find it later
                .setSuccessUrl(YOUR_DOMAIN + "/poem/add?tempId=" + tempId)
                .setCancelUrl(YOUR_DOMAIN + "/poem/add")
                // 3. Add tempId to Metadata so the Webhook can find it
                .putMetadata("tempId", tempId)
                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setQuantity(1L)
                                .setPrice("price_1TOjPFAAVrHEMuXVawYsOl7z")
                                .build())
                .build();

        Session session = client.v1().checkout().sessions().create(params);
        return session.getUrl();
    }
}
