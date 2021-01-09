package org.scd.service;


import org.scd.config.exception.BusinessException;
import org.scd.model.Location;
import org.scd.model.User;
import org.scd.model.dto.*;
import org.scd.model.security.CustomUserDetails;
import org.scd.model.security.Role;
import org.scd.repository.LocationRepository;
import org.scd.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class LocationServiceImpl implements LocationService {
    private final LocationRepository locationRepository;
    private final UserRepository userRepository;

    public LocationServiceImpl(LocationRepository locationRepository, UserRepository userRepository) {
        this.locationRepository = locationRepository;
        this.userRepository = userRepository;
    }


    @Override
    public Location createLocation(LocationCreationDTO locationCreationDTO) throws BusinessException {

        final User currentUser = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();

        if (Objects.isNull(locationCreationDTO)) {
            throw new BusinessException(401, "Body null !");
        }

        if (Objects.isNull(locationCreationDTO.getLatitude())) {
            throw new BusinessException(400, "Latitude cannot be null ! ");
        }

        if (Objects.isNull(locationCreationDTO.getLongitude())) {
            throw new BusinessException(400, "Longitude cannot be null !");
        }

        Location location = new Location();
        location.setLatitude(locationCreationDTO.getLatitude());
        location.setLongitude(locationCreationDTO.getLongitude());
        location.setUser(currentUser);
        locationRepository.save(location);

        return location;
    }

    @Override
    public LocationDetailsResponseDTO getLocationById(Long locationId) throws BusinessException {

        Location location = locationRepository.findById(locationId).orElse(null);

        if (Objects.isNull(location)) {
            throw new BusinessException(404, "Location does not exist!");
        }
        final User currentUser = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();

        boolean isAdmin = false;
        String adminRole = "ADMIN";
        Set<Role> roles = currentUser.getRoles();
        for(Role role : roles){
            if ((role.getRole()).equals(adminRole)){
                isAdmin = true;
            }
        }


        if ((isAdmin)|| ((location.getUser().getId()) == (currentUser.getId()))) {

            final LocationDetailsResponseDTO locationDetailsResponseDTO = new LocationDetailsResponseDTO();
            locationDetailsResponseDTO.setId(location.getId());
            locationDetailsResponseDTO.setLatitude(location.getLatitude());
            locationDetailsResponseDTO.setLongitude(location.getLongitude());
            locationDetailsResponseDTO.setUser(location.getUser());
            locationDetailsResponseDTO.setCreatedAt(location.getCreatedAt());
            locationDetailsResponseDTO.setUpdatedAt(location.getUpdatedAt());

            return locationDetailsResponseDTO;

        } else throw new BusinessException(401, "Unauthorized!");

    }

    @Override
    public Location updateLocationById(Long locationId, LocationUpdateRequestDTO locationUpdateRequestDTO)
            throws BusinessException {

        Location location = locationRepository.findById(locationId).orElse(null);

        if (Objects.isNull(location)) {
            throw new BusinessException(404, "Location does not exist!");
        }

        final User currentUser = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();

        boolean isAdmin = false;
        String adminRole = "ADMIN";
        Set<Role> roles = currentUser.getRoles();
        for(Role role : roles){
            if ((role.getRole()).equals(adminRole)){
                isAdmin = true;
            }
        }

        if ((isAdmin)|| ((location.getUser().getId()) == (currentUser.getId()))) {
            location.setLatitude(locationUpdateRequestDTO.getLatitude());
            location.setLongitude(locationUpdateRequestDTO.getLongitude());

            locationRepository.save(location);
            return location;
        } else throw new BusinessException(401, "Unauthorized!");
    }

    @Override
    public void deleteLocationById(Long locationId) throws BusinessException {

        Location location = locationRepository.findById(locationId).orElse(null);
        if (Objects.isNull(location)) {
            throw new BusinessException(404, "Location does not exist!");
        }

        final User currentUser = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        boolean isAdmin = false;
        String adminRole = "ADMIN";
        Set<Role> roles = currentUser.getRoles();
        for(Role role : roles){
            if ((role.getRole()).equals(adminRole)){
                isAdmin = true;
            }
        }


        if ((isAdmin)|| ((location.getUser().getId()) == (currentUser.getId()))) {
            locationRepository.delete(location);

        } else throw new BusinessException(401, "Unauthorized!");
    }

    @Override
    public List<FilteredLocationsResponseDTO> filterLocations(Long userId, Date startDate, Date endDate) throws BusinessException {
        final User currentUser = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        boolean isAdmin = false;
        String adminRole = "ADMIN";
        Set<Role> roles = currentUser.getRoles();
        for(Role role : roles){
            if ((role.getRole()).equals(adminRole)){
                isAdmin = true;
            }
        }

        if (isAdmin) {
            User user=userRepository.findById(userId).orElse(null);
            if(Objects.isNull(user)){
                throw new BusinessException(404,"Not found");
            }
            final List<Location> locations = locationRepository.findByUserId(userId);
            final List<FilteredLocationsResponseDTO> filteredLocations = new ArrayList<>();

            int i=0;
          for(Location loc : locations) {
                if ((loc.getCreatedAt().after(startDate) ||loc.getCreatedAt().equals(startDate)  )&& (loc.getCreatedAt().before(endDate)||loc.getCreatedAt().equals(endDate) )) {
                    FilteredLocationsResponseDTO filteredLocationsResponseDTO = new FilteredLocationsResponseDTO();
                    filteredLocationsResponseDTO.setId(loc.getId());
                    filteredLocationsResponseDTO.setLatitude(loc.getLatitude());
                    filteredLocationsResponseDTO.setLongitude(loc.getLongitude());
                    filteredLocationsResponseDTO.setUser(loc.getUser());
                    filteredLocationsResponseDTO.setCreatedAt(loc.getCreatedAt());
                    filteredLocationsResponseDTO.setUpdatedAt(loc.getUpdatedAt());
                    filteredLocations.add(filteredLocationsResponseDTO);
                }
            }

        return filteredLocations;
        }
        else throw new BusinessException(401, "Unauthorized!");

    }

}
