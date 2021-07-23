package com.unileipzig.shop.exceptions;

public class AmbiguousCategoryNameException extends Exception {
    public AmbiguousCategoryNameException(String[] categoryPath, int index) {
        super(String.format("Category name '%s' is ambiguous, in path '%s'", categoryPath[index],
                String.join("/", categoryPath)));
    }
}
