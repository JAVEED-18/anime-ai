package com.turfease.booking.controller;

import com.turfease.booking.dto.BookingDtos;
import com.turfease.booking.model.Booking;
import com.turfease.booking.model.BookingStatus;
import com.turfease.booking.repository.BookingRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingRepository repo;

    public BookingController(BookingRepository repo) { this.repo = repo; }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody BookingDtos.CreateBookingRequest request, Authentication auth) {
        Long userId = extractUserId(auth.getName());
        if (!repo.findConflicts(request.turfId, request.startTime, request.endTime).isEmpty()) {
            return ResponseEntity.badRequest().body("Time slot not available");
        }
        Booking b = new Booking();
        b.setUserId(userId);
        b.setTurfId(request.turfId);
        b.setStartTime(request.startTime);
        b.setEndTime(request.endTime);
        b.setStatus(BookingStatus.CONFIRMED);
        b.setTotalAmount(request.totalAmount);
        b.setUserEmail(request.userEmail);
        repo.save(b);
        return ResponseEntity.ok(b);
    }

    @GetMapping
    public List<Booking> myBookings(Authentication auth) {
        Long userId = extractUserId(auth.getName());
        return repo.findByUserId(userId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public List<Booking> all() { return repo.findAll(); }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> cancel(@PathVariable Long id, Authentication auth) {
        return repo.findById(id).map(b -> {
            if (!hasAccess(auth, b)) return ResponseEntity.status(403).build();
            b.setStatus(BookingStatus.CANCELLED);
            repo.save(b);
            return ResponseEntity.noContent().build();
        }).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/internal/markPaid/{id}")
    public ResponseEntity<?> markPaid(@PathVariable Long id, @RequestHeader(value = "X-Internal-Token", required = false) String token,
                                      @RequestHeader(value = "X-Payment-Ref", required = false) String paymentRef) {
        String expected = System.getenv().getOrDefault("INTERNAL_SERVICE_TOKEN", "dev-internal-token");
        if (token == null || !token.equals(expected)) return ResponseEntity.status(401).build();
        return repo.findById(id).map(b -> {
            b.setStatus(BookingStatus.PAID);
            repo.save(b);
            return ResponseEntity.ok(b);
        }).orElse(ResponseEntity.notFound().build());
    }

    private Long extractUserId(String emailOrName) {
        // In this simplified approach, the auth name is the email; a real implementation would embed userId in token
        // For demo, hash email to a stable positive long
        return Math.abs(emailOrName.hashCode()) + 0L;
    }

    private boolean hasAccess(Authentication auth, Booking b) {
        boolean isAdmin = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        return isAdmin || b.getUserId().equals(extractUserId(auth.getName()));
    }
}