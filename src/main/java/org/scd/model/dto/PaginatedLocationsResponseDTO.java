package org.scd.model.dto;

import javassist.runtime.Inner;

import java.util.List;

public class PaginatedLocationsResponseDTO {
    private Integer page;
    private Integer pageSize;
    private Integer totalPages;
    private Integer totalCount;
    private List<LocationDetailsResponseDTO> locationDetailsResponseDTOS;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public List<LocationDetailsResponseDTO> getLocationDetailsResponseDTOS() {
        return locationDetailsResponseDTOS;
    }

    public void setLocationDetailsResponseDTOS(List<LocationDetailsResponseDTO> locationDetailsResponseDTOS) {
        this.locationDetailsResponseDTOS = locationDetailsResponseDTOS;
    }
}
