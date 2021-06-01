package com.unileipzig.shop;

/**
 * Class holding some util methods for comparing Strings or other objects in a specific way.
 */
public class CompareUtil {

    /**
     * Compares two strings for equality only considering alphanumerical characters. Case sensitive.
     * @param s1 String 1 for comparison.
     * @param s2 String 2 for comparison.
     * @return true iff the passed strings would be equal after removing all non-alphanumerical characters.
     */
    public static boolean alphanumericallyEquals(String s1, String s2) {
        return s1.replaceAll("[^a-zA-Z0-9]", "").equals(s2.replaceAll("[^a-zA-Z0-9]", ""));
    }

    /**
     * Checks if a String is contained in the given iterable of strings only considering alphanumerical characters. Case sensitive.
     * @param iterable The Iterable to check for whether the string is contained.
     * @param value The String to check for whether it is contained in the iterable.
     * @return true iff the string is contained in the iterable with regard to equality by CompareUtil.alphanumericallyEquals.
     */
    public static boolean alphanumericallyContains(Iterable<String> iterable, String value) {
        for (String s : iterable) {
            if (alphanumericallyEquals(s, value)) return true;
        }
        return false;
    }

    /**
     * Checks if one iterable of strings is a subset of the other one with regard to CompareUtil.alphanumericallyContains.
     * @param superIterable the iterable to check for whether it is a superset of the other one.
     * @param subIterable the iterable to check for whether it is a subset of the other one.
     * @return true iff the above condition is satisfied.
     */
    public static boolean alphanumericallyContainsAll(Iterable<String> superIterable, Iterable<String> subIterable) {
        for (String s : subIterable) {
            if (!(alphanumericallyContains(superIterable, s))) return false;
        }
        return true;
    }

    /**
     * Checks for two iterables of strings if they are both a subset of each other with regard to CompareUtil.alphanumericallyContainsAll.
     * @param iterable1 Iterable 1 for comparison.
     * @param iterable2 Iterable 2 for comparison.
     * @return true iff the above condition is satisfied.
     */
    public static boolean alphanumericallyEqualsIgnoreOrder(Iterable<String> iterable1, Iterable<String> iterable2) {
        return (alphanumericallyContainsAll(iterable1, iterable2)
                && alphanumericallyContainsAll(iterable2, iterable1));
    }

    /**
     * Checks if two objects are equal or either is null or both are null.
     * @param o1 Object 1 to compare.
     * @param o2 Object 2 to compare.
     * @return True if any or both of the passed objects are null, also true if the objects are equal, false otherwise.
     */
    public static boolean equalsAllowNull(Object o1, Object o2) {
        return (o1 == null || o2 == null || o1.equals(o2));
    }
}
