package org.demo.controller;

import lombok.RequiredArgsConstructor;
import org.demo.dto.MovieSpaceShipsDto;
import org.demo.service.MovieSpaceShipService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

import static org.demo.utils.Constants.API_VERSION_PATH;

@RestController
@RequestMapping(API_VERSION_PATH)
@RequiredArgsConstructor
public class MovieSpaceShipsController {

    private final MovieSpaceShipService movieSpaceShipService;

    @GetMapping(path = "/space-ships")
    public ResponseEntity<Page<MovieSpaceShipsDto>> findAll(@RequestParam(value = "page", defaultValue = "0") int page,
                                                            @RequestParam(value = "size", defaultValue = "10") int size) {

        return ResponseEntity.ok().body(movieSpaceShipService.getSpaceShips(PageRequest.of(page, size)));
    }

    @GetMapping(path = "/space-ships/{id}")
    public ResponseEntity<MovieSpaceShipsDto> findById(@PathVariable int id) {
        return ResponseEntity.ok().body(movieSpaceShipService.getSpaceShipById(id));
    }

    @GetMapping(path = "/space-ships/search")
    public ResponseEntity<Set<MovieSpaceShipsDto>> findByName(@RequestParam String name) {
        return ResponseEntity.ok().body(movieSpaceShipService.getSpaceShipByName(name));
    }

    @PostMapping(path = "/space-ships")
    public ResponseEntity<MovieSpaceShipsDto> save(@RequestBody MovieSpaceShipsDto movieSpaceShipsDto) {
        movieSpaceShipService.saveSpaceShip(movieSpaceShipsDto);
        return ResponseEntity.ok().build();
    }

    @PutMapping(path = "/space-ships/{id}")
    public ResponseEntity<MovieSpaceShipsDto> update(@PathVariable int id, @RequestBody MovieSpaceShipsDto movieSpaceShipsDto) {
        movieSpaceShipsDto.setId(id);
        movieSpaceShipService.updateSpaceShip(movieSpaceShipsDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(path = "/space-ships/{id}")
    public ResponseEntity<MovieSpaceShipsDto> delete(@PathVariable int id) {
        movieSpaceShipService.deleteSpaceShip(id);
        return ResponseEntity.ok().build();
    }
}
