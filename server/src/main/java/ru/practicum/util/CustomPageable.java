package ru.practicum.util;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.lang.NonNull;

import javax.validation.ValidationException;
import java.util.Optional;

public class CustomPageable implements Pageable {
    private final int offset;
    private final int limit;
    private final Sort sort;

    private static final Integer DEFAULT_PAGE_SIZE = 20;

    protected CustomPageable(int offset, int limit, Sort sort) {
        this.offset = offset;
        this.limit = limit;
        this.sort = sort;
    }

    public static Pageable of(Integer from, Integer size) {
        if (from == null && size == null) {
            from = 0;
            size = DEFAULT_PAGE_SIZE;
        }
        validateOrThrowException(from, size);
        return new CustomPageable(saveUnboxing(from), saveUnboxing(size), Sort.unsorted());
    }

    public static Pageable of(Integer from, Integer size, Sort sort) {
        if (from == null && size == null) {
            from = 0;
            size = DEFAULT_PAGE_SIZE;
        }
        validateOrThrowException(from, size);
        return new CustomPageable(saveUnboxing(from), saveUnboxing(size), sort);
    }

    private static void validateOrThrowException(Integer from, Integer size) {
        if (saveUnboxing(size) < 1 || saveUnboxing(from) < 0) {
            throw new ValidationException("from must be positive and size must be more then 0");
        }
    }

    public static int saveUnboxing(Integer value) {
        return Optional.ofNullable(value).orElse(0);
    }

    @Override
    public int getPageNumber() {
        return 0;
    }

    @Override
    public int getPageSize() {
        return limit;
    }

    @Override
    public long getOffset() {
        return offset;
    }

    @NonNull
    @Override
    public Sort getSort() {
        return sort;
    }

    @NonNull
    @Override
    public Pageable next() {
        return new CustomPageable(offset + limit, limit, sort);
    }

    @NonNull
    @Override
    public Pageable previousOrFirst() {
        return new CustomPageable(offset, limit, sort);
    }

    @NonNull
    @Override
    public Pageable first() {
        return new CustomPageable(offset, limit, sort);
    }

    @NonNull
    @Override
    public Pageable withPage(int pageNumber) {
        return new CustomPageable(offset + limit * pageNumber, limit, sort);
    }

    @Override
    public boolean hasPrevious() {
        return false;
    }
}

