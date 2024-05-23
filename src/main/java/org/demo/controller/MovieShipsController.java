package org.demo.controller;

import lombok.RequiredArgsConstructor;
import org.demo.dto.SpaceShipDto;
import org.demo.service.SpaceShipService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MovieShipsController {

    private final SpaceShipService spaceShipService;

    @GetMapping(path = "/space-ships")
    public ResponseEntity<Page<SpaceShipDto>> findAll(@RequestParam(value = "page", defaultValue = "0") int page,
                                                      @RequestParam(value = "size", defaultValue = "10") int size) {

        return ResponseEntity.ok().body(spaceShipService.getSpaceShips(PageRequest.of(page, size)));
    }

    @GetMapping(path = "/space-ships/{id}")
    public ResponseEntity<SpaceShipDto> findById(@PathVariable int id) {
        return ResponseEntity.ok().body(spaceShipService.getSpaceShipById(id));
    }

    @GetMapping(path = "/space-ships/search")
    public ResponseEntity<Set<SpaceShipDto>> findByName(@RequestParam String name) {
        return ResponseEntity.ok().body(spaceShipService.getSpaceShipByName(name));
    }
}
