package com.turfease.payment.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;

import java.util.Map;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Value("${INTERNAL_SERVICE_TOKEN:dev-internal-token}")
    private String internalToken;

    @Value("${razorpay.key_id}")
    private String keyId;
    @Value("${razorpay.key_secret}")
    private String keySecret;

    @Value("${customer.service.confirm-url}")
    private String customerConfirmUrl;

    private final RestClient restClient = RestClient.create();

    @PostMapping("/initiate/{bookingId}")
    public ResponseEntity<?> initiatePayment(@PathVariable Long bookingId, Authentication auth) {
        // In a real integration, create a Razorpay order with amount/currency/receipt using keyId/keySecret
        // Here we simulate successful payment and proceed with downstream effects
        try {
            String bookingServiceUrl = "http://localhost:8080/api/bookings/internal/markPaid/" + bookingId;
            restClient.post()
                    .uri(bookingServiceUrl)
                    .header("X-Internal-Token", internalToken)
                    .header("X-Payment-Ref", "RZP-" + System.currentTimeMillis())
                    .retrieve()
                    .toEntity(String.class);
        } catch (Exception e) {
            return ResponseEntity.status(502).body(Map.of("error", "Failed to confirm booking payment"));
        }

        try {
            String notificationUrl = "http://localhost:8080/api/notifications/email";
            restClient.post()
                    .uri(notificationUrl)
                    .body(Map.of(
                            "to", auth.getName(),
                            "subject", "TurfEase Booking Payment Confirmed",
                            "body", "Your payment for booking #" + bookingId + " is successful."
                    ))
                    .retrieve()
                    .toBodilessEntity();
        } catch (Exception e) {
            // continue even if email fails
        }
        return ResponseEntity.ok(Map.of("status", "PAID", "bookingId", bookingId));
    }
}