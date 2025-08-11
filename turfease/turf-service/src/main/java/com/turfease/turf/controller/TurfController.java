package com.turfease.turf.controller;

import com.turfease.turf.model.SportType;
import com.turfease.turf.model.Turf;
import com.turfease.turf.repository.TurfRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/turfs")
public class TurfController {

    private final TurfRepository repo;

    public TurfController(TurfRepository repo) { this.repo = repo; }

    @GetMapping("/public")
    public List<Turf> listAll(@RequestParam(value = "sport", required = false) SportType sport) {
        if (sport != null) return repo.findBySportType(sport);
        return repo.findAll();
    }

    @GetMapping("/public/{id}")
    public ResponseEntity<Turf> getOne(@PathVariable Long id) {
        return repo.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public Turf create(@Valid @RequestBody Turf turf) { return repo.save(turf); }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Turf> update(@PathVariable Long id, @Valid @RequestBody Turf turf) {
        return repo.findById(id).map(existing -> {
            existing.setName(turf.getName());
            existing.setLocation(turf.getLocation());
            existing.setPricePerHour(turf.getPricePerHour());
            existing.setSportType(turf.getSportType());
            existing.setImages(turf.getImages());
            existing.setDescription(turf.getDescription());
            return ResponseEntity.ok(repo.save(existing));
        }).orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!repo.existsById(id)) return ResponseEntity.notFound().build();
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}