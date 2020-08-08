package io.florianlopes.usersmanagement.api.common.web;

import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import io.florianlopes.usersmanagement.api.common.web.dto.OrderDto;
import io.florianlopes.usersmanagement.api.common.web.dto.PageDto;

@Mapper(componentModel = "spring")
public interface PageDtoMapper {

    default <T> PageDto<T> asPageDto(Page<T> page) {
        final PageDto<T> pageDto = new PageDto<>();
        pageDto.setItems(page.getContent());
        pageDto.setFirstPage(page.isFirst());
        pageDto.setLastPage(page.isLast());
        pageDto.setTotalPages(page.getTotalPages());
        pageDto.setTotalItems(page.getTotalElements());
        pageDto.setNbItems(page.getNumberOfElements());
        pageDto.setPageSize(page.getSize());
        pageDto.setPageNumber(page.getNumber());
        pageDto.setSort(page.getSort().stream().map(this::asOrderDto).collect(Collectors.toList()));

        return pageDto;
    }

    OrderDto asOrderDto(Sort.Order order);

    OrderDto.Direction asOrderDirection(Sort.Direction direction);
}
