package com.turfease.booking.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class BookingDtos {
    public static class CreateBookingRequest {
        @NotNull public Long turfId;
        @NotNull @Future public LocalDateTime startTime;
        @NotNull @Future public LocalDateTime endTime;
        @NotNull @Min(0) public Double totalAmount;
        @NotNull public String userEmail;
    }
}