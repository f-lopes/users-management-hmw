package io.florianlopes.usersmanagement.api.common.web.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("Page")
public class PageDto<T> {

    private List<T> items;
    private boolean firstPage;
    private boolean lastPage;
    private int totalPages;
    private long totalItems;
    private int nbItems;
    private int pageSize;
    private int pageNumber;

    @JsonProperty("sort")
    private List<OrderDto> sort;

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }

    public boolean isFirstPage() {
        return firstPage;
    }

    public void setFirstPage(boolean firstPage) {
        this.firstPage = firstPage;
    }

    public boolean isLastPage() {
        return lastPage;
    }

    public void setLastPage(boolean lastPage) {
        this.lastPage = lastPage;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public long getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(long totalItems) {
        this.totalItems = totalItems;
    }

    public int getNbItems() {
        return nbItems;
    }

    public void setNbItems(int nbItems) {
        this.nbItems = nbItems;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public List<OrderDto> getSort() {
        return sort;
    }

    public void setSort(List<OrderDto> sort) {
        this.sort = sort;
    }

    @Override
    public String toString() {
        return "PageDto{"
                + "items="
                + items
                + ", firstPage="
                + firstPage
                + ", lastPage="
                + lastPage
                + ", totalPages="
                + totalPages
                + ", totalItems="
                + totalItems
                + ", nbItems="
                + nbItems
                + ", pageSize="
                + pageSize
                + ", pageNumber="
                + pageNumber
                + ", sort="
                + sort
                + '}';
    }
}
