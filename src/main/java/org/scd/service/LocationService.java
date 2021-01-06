package org.scd.service;


import org.scd.config.exception.BusinessException;
import org.scd.model.Location;
import org.scd.model.dto.*;

import java.util.Date;
import java.util.List;

public interface LocationService {

    /**
     * create new location
     *
     * @param locationCreationDTO
     * @return
     */
    Location createLocation(final LocationCreationDTO locationCreationDTO) throws BusinessException;


    /**et location by id
     *
     * g
     * @param locationId
     * @return
     * @throws BusinessException
     */
    LocationDetailsResponseDTO getLocationById(final Long locationId) throws BusinessException;

    /**
     * update location by id
     *
     * @param locationId
     * @param locationUpdateRequestDTO
     * @return
     * @throws BusinessException
     */
    Location updateLocationById(final Long locationId,final LocationUpdateRequestDTO locationUpdateRequestDTO)
            throws BusinessException;

    /**
     * delete location by id
     *
     * @param locationId
     * @throws BusinessException
     */
    void deleteLocationById(Long locationId) throws BusinessException;

    /**
     *
     * @param userId
     * @param startDate
     * @param endDate
     * @return
     * @throws BusinessException
     */

     List<FilteredLocationsResponseDTO> filterLocations(Long userId, Date startDate, Date endDate) throws BusinessException;




}

