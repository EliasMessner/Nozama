package com.unileipzig.shop.exceptions;

public class CategoryNotFoundException extends Exception {
    public CategoryNotFoundException(String[] categoryPath, int index) {
        super(String.format("Category '%s' in path '%s' not found.", categoryPath[index],
                String.join("/", categoryPath)));
    }

    public CategoryNotFoundException(String categoryPath) {
        super(String.format("Category path '%s' not found.", categoryPath));
    }
}
