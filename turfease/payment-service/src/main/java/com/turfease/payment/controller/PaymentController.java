package com.turfease.payment.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
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

    private final RestClient restClient = RestClient.create();

    @PostMapping("/initiate/{bookingId}")
    public ResponseEntity<?> pay(@PathVariable Long bookingId, Authentication auth) {
        // Simulate payment success
        String bookingServiceUrl = "http://localhost:8080/api/bookings/internal/markPaid/" + bookingId;
        ResponseEntity<String> resp = restClient.post()
                .uri(bookingServiceUrl)
                .header("X-Internal-Token", internalToken)
                .header("X-Payment-Ref", "SIM-" + System.currentTimeMillis())
                .retrieve()
                .toEntity(String.class);

        // Notify
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

        return ResponseEntity.ok(Map.of("status", "PAID"));
    }
}