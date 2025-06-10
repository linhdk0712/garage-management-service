package vn.utc.service.dtos;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class PaginatedResponseDto<T> implements Serializable {
    private List<T> content;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
    private boolean hasNext;
    private boolean hasPrevious;
    private boolean isFirst;
    private boolean isLast;

    public PaginatedResponseDto() {
    }

    public PaginatedResponseDto(List<T> content, int page, int size, long totalElements) {
        this.content = content;
        this.page = page;
        this.size = size;
        this.totalElements = totalElements;
        this.totalPages = (int) Math.ceil((double) totalElements / size);
        this.hasNext = page < totalPages - 1;
        this.hasPrevious = page > 0;
        this.isFirst = page == 0;
        this.isLast = page == totalPages - 1 || totalPages == 0;
    }

    public static <T> PaginatedResponseDto<T> of(List<T> content, int page, int size, long totalElements) {
        return new PaginatedResponseDto<>(content, page, size, totalElements);
    }
} 