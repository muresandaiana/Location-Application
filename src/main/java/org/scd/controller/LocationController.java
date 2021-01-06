package org.scd.controller;

import org.scd.config.exception.BusinessException;
import org.scd.model.Location;
import org.scd.model.dto.*;
import org.scd.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(path="/locations")
@CrossOrigin
public class LocationController {
    private final LocationService locationService;

    @Autowired
    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }


    @PostMapping
    public ResponseEntity createLocation(@RequestBody final LocationCreationDTO locationCreationDTO)
            throws BusinessException {
        final URI location = ServletUriComponentsBuilder // http://localhost:8080
                .fromCurrentRequest()// http://localhost:8080/locations
                .path("/{id}")// http://localhost:8080/locations/{id}
                .buildAndExpand(locationService.createLocation(locationCreationDTO).getId())// http://localhost:8080/locations/location.getId()
                .toUri();
        return ResponseEntity.created(location).build();

    }

    @GetMapping(path = "/{locationId}")
    public ResponseEntity<LocationDetailsResponseDTO> getLocationById(@PathVariable(value = "locationId") Long locationId)
            throws BusinessException {
        return ResponseEntity.ok(locationService.getLocationById(locationId));
    }

    @PutMapping(path = "/{locationId}")
    public ResponseEntity<Location> updateLocationById(@PathVariable(value = "locationId") Long locationId,
                                                       @RequestBody final LocationUpdateRequestDTO locationUpdateRequestDTO)
            throws BusinessException {
        return ResponseEntity.ok(locationService.updateLocationById(locationId, locationUpdateRequestDTO));
    }

    @DeleteMapping(path = "/{locationId}")
    public ResponseEntity deleteLocationById(@PathVariable(value = "locationId") Long locationId)
            throws BusinessException {
        locationService.deleteLocationById(locationId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<FilteredLocationsResponseDTO>> filterLocations(
            @RequestParam(name = "userId", required = true) final Long userId,
            @RequestParam(name = "startDate", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") final Date startDate,
            @RequestParam(name = "endDate", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") final Date endDate
    ) throws BusinessException {
        return ResponseEntity.ok(locationService.filterLocations(userId, startDate, endDate));
    }

}




