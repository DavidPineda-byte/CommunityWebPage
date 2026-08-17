package com.DavidsCode.CommunityWebPage.controller;

import com.DavidsCode.CommunityWebPage.dto.PoemDraft;
import com.DavidsCode.CommunityWebPage.service.FluxImageAPI;
import com.DavidsCode.CommunityWebPage.service.TempPoemStorage;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stripe")
public class StripeWebhookController {

    private final TempPoemStorage tempPoemStorage;
    private final FluxImageAPI fluxImageAPI;

    @Value("${stripe.webhook.secret}")
    private String endpointSecret;

    public StripeWebhookController(TempPoemStorage tempPoemStorage, FluxImageAPI fluxImageAPI) {
        this.tempPoemStorage = tempPoemStorage;
        this.fluxImageAPI = fluxImageAPI;
    }

    @PostMapping("/webhook")
    public void handleWebhook(@RequestBody String payload, @RequestHeader("Stripe-Signature") String sigHeader) {
        Event event;
        try {
            event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
        } catch (Exception e) {
            System.out.println("Webhook signature verification failed.");
            return;
        }

        if ("checkout.session.completed".equals(event.getType())) {
            Session session = (Session) event.getDataObjectDeserializer().getObject().get();
            String tempId = session.getMetadata().get("tempId");

            if (tempId != null) {
                PoemDraft draft = tempPoemStorage.get(tempId);
                if (draft != null) {
                    System.out.println("Generating image for tempId: " + tempId);
                    String imageUrl = fluxImageAPI.generateImageURL(draft.getPrompt()).join();
                    draft.setImageUrl(imageUrl);
                    System.out.println("Image generated and saved to draft: " + imageUrl);
                }
            }
        }
    }
}