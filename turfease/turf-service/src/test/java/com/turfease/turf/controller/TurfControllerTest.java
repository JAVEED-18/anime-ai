package com.turfease.turf.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.turfease.turf.repository.TurfRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TurfController.class)
class TurfControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TurfRepository repo;

    @Test
    void createForbiddenWithoutAuth() throws Exception {
        mockMvc.perform(post("/api/turfs")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"A\",\"location\":\"B\",\"pricePerHour\":10,\"sportType\":\"FOOTBALL\"}"))
                .andExpect(status().isForbidden());
    }
}