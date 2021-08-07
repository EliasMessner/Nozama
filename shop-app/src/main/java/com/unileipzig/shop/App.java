package com.unileipzig.shop;

import com.unileipzig.shop.controller.MainController;
import com.unileipzig.shop.exceptions.AmbiguousCategoryNameException;
import com.unileipzig.shop.exceptions.CategoryNotFoundException;
import com.unileipzig.shop.exceptions.InputException;
import com.unileipzig.shop.model.*;
import org.hibernate.exception.ConstraintViolationException;

import java.util.*;

public class App {

    public static void main( String[] args ) {
        MainController mainController = new MainController();
        Scanner scanner = new Scanner(System.in);
        askForActionLoop(mainController, scanner);
        mainLoop(mainController, scanner);
    }

    private static void mainLoop(MainController mainController, Scanner scanner) {
        boolean repeat = true;
        while (repeat) {
            if (!scanner.hasNextLine()) continue;
            String input = scanner.nextLine();
            String[] inputTokens = getInputTokens(input);
            switch (inputTokens[0].toLowerCase()) {
                case "help":
                    printHelp();
                    break;
                case "finish":
                    mainController.finish();
                    System.out.println("Goodbye.");
                    repeat = false;
                    break;
                case "getcategorytree":
                    printCategoryTree(mainController);
                    break;
                case "gettrolls":
                    tryPrintTrolls(mainController, inputTokens);
                    break;
                case "addnewreview":
                    tryAddNewReview(mainController, inputTokens);
                    break;
                case "getproductsbycategorypath":
                case "getbypath":
                    tryGetProductsByCatPath(mainController, inputTokens);
                    break;
                case "getproduct":
                    tryPrintProduct(mainController, inputTokens);
                    break;
                case "getproducts":
                    tryGetProducts(mainController, inputTokens);
                    break;
                case "gettopproducts":
                    tryGetTopProducts(mainController, inputTokens);
                    break;
                case "getsimilarcheaperproducts":
                case "getsimcheaper":
                    tryGetSimilarCheaperProducts(mainController, inputTokens);
                    break;
                case "getoffers":
                    tryGetOffers(mainController, inputTokens);
                    break;
                case "getreviews":
                    tryGetReviewsByProdNumber(mainController, inputTokens);
                    break;
                default:
                    System.out.printf("Unrecognized command '%s'%n", inputTokens[0]);
            }
            System.out.print("\n"); // new line after each command for better overview
        }
    }

    private static String[] getInputTokens(String input) {
        List<String> inputTokens = new ArrayList<>();
        StringBuilder currentToken = new StringBuilder();
        boolean insideApostrophe = false;
        for (char c: input.toCharArray()) {
            if (c == '\'') {
                insideApostrophe = !insideApostrophe;
                continue;
            }
            if ((c == ' ' || c == ',') && !insideApostrophe) {
                if (currentToken.length() != 0) {
                    inputTokens.add(currentToken.toString());
                    currentToken.setLength(0);
                }
                continue;
            }
            currentToken.append(c);
        }
        if (currentToken.length() != 0) {
            inputTokens.add(currentToken.toString());
        }
        return inputTokens.toArray(String[]::new);
    }

    private static void askForActionLoop(MainController mainController, Scanner scanner) {
        System.out.println("Do you want to initialize Hibernate (including the database connection)? (Y/N)");
        while (true) {
            if (!scanner.hasNextLine()) continue;
            String input = scanner.nextLine().toLowerCase();
            if (input.equals("y")) {
                mainController.init();
                break;
            }
            if (input.equals("n") || input.equals("quit")) {
                System.out.println("Closing App. Goodbye!");
                System.exit(0);
            }
            else {
                System.out.printf("Unrecognized input '%s'%n" +
                        "Type 'Y' if you want to initialize the Database, " +
                        "type 'N' or 'quit' to exit the app.", input);
            }
        }
        System.out.println("Database ready. Enter command or type 'help'.");
    }

    private static void tryGetProducts(MainController mainController, String[] inputTokens) {
        try {
            checkArgumentCount(inputTokens, new int[]{0, 1});
        } catch (InputException e) {
            System.out.println(e.getMessage());
            return;
        }
        String pattern;
        if (inputTokens.length == 1) {
            pattern = "%";
        } else {
            pattern = inputTokens[1];
        }
        List<Product> products = mainController.getProducts(pattern);
        if (products.isEmpty()) {
            System.out.printf("No products found matching pattern '%s'%n", pattern);
            return;
        }
        System.out.printf("Products matching pattern '%s' (found %d):%n", pattern, products.size());
        products.forEach(p -> System.out.println(p + "\n"));
    }

    private static void tryGetOffers(MainController mainController, String[] inputTokens) {
        try {
            checkArgumentCount(inputTokens, 1);
        } catch (InputException e) {
            System.out.println(e.getMessage());
            return;
        }
        String prodNumber = inputTokens[1];
        List<Offer> offers = mainController.getOffers(prodNumber);
        if (offers.isEmpty()) {
            System.out.printf("No offers found for product '%s'%n", prodNumber);
            return;
        }
        System.out.printf("Offers for product %s (found %d):%n", prodNumber, offers.size());
        offers.forEach(o -> System.out.println(o.toString() + "\n"));
    }

    private static void tryGetSimilarCheaperProducts(MainController mainController, String[] inputTokens) {
        String prodNumber = inputTokens[1];
        try {
            checkArgumentCount(inputTokens, 1);
            List<Product> products = mainController.getSimilarCheaperProducts(prodNumber);
            if (products.isEmpty()) {
                System.out.printf("No cheaper products found similar to '%s'%n", prodNumber);
                return;
            }
            System.out.printf("Similar cheaper products for product %s (found %d):%n", prodNumber, products.size());
            products.forEach(p -> System.out.printf("%s, %s%n", p.getProdNumber(), p.getTitle()));
        } catch (InputException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void tryGetTopProducts(MainController mainController, String[] inputTokens) {
        try {
            checkArgumentCount(inputTokens, 1);
            int k = Integer.parseInt(inputTokens[1]);
            List<Product> topProducts = mainController.getTopProducts(k);
            System.out.printf("Top %d products: (id, rating, salesrank, title)%n", k);
            topProducts
                    .stream().sorted((p1, p2) -> Double.compare(p2.getRating(), p1.getRating())) // highest first
                    .forEach(p -> System.out.printf("%s, %1.2f, %d, %s%n", p.getProdNumber(), p.getRating(), p.getSalesRank(), p.getTitle()));
        } catch (NumberFormatException e) {
            System.out.printf("Invalid argument '%s', expected integer specifying amount of top products.%n", inputTokens[1]);
        } catch (InputException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void tryPrintProduct(MainController mainController, String[] inputTokens) {
        try {
            checkArgumentCount(inputTokens, 1);
        } catch (InputException e) {
            System.out.println(e.getMessage());
            return;
        }
        String prodNumber = inputTokens[1];
        Product product = mainController.getProduct(prodNumber);
        if (product == null) {
            System.out.printf("Product with id '%s' does not exist in the database.%n", prodNumber);
        } else {
            System.out.println(product);
        }
    }

    private static void tryGetProductsByCatPath(MainController mainController, String[] inputTokens) {
        try {
            checkArgumentCount(inputTokens, 1);
            String path = inputTokens[1];
            List<Product> products = mainController.getProductsByCategoryPath(path);
            String[] pathTokens = Arrays.stream(path.split("/")).filter(s -> s.length() != 0).toArray(String[]::new);
            String categoryName = pathTokens[pathTokens.length - 1];
            System.out.printf("Products in Category '%s' (%d):%n", categoryName, products.size());
            products.forEach(System.out::println);
        } catch (CategoryNotFoundException | AmbiguousCategoryNameException | InputException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void tryAddNewReview(MainController mainController, String[] inputTokens) {
        try {
            checkArgumentCount(inputTokens, 5);
            int stars = Integer.parseInt(inputTokens[3]);
            if (stars > 5 || stars < 1) {
                throw new InputException(String.format("Rating must be from 1-5, got %d instead.", stars));
            }
            mainController.addNewReview(inputTokens[1], inputTokens[2], stars, inputTokens[4], inputTokens[5]);
            System.out.printf("Inserted Values (%s, %s, %s, %s, %s) into table review.%n",
                    inputTokens[1],
                    inputTokens[2],
                    inputTokens[3],
                    inputTokens[4],
                    inputTokens[5]);
        } catch (NumberFormatException e) {
            System.out.printf("Invalid rating '%s', expected integer from 1-5%n", inputTokens[3]);
        } catch (InputException | ConstraintViolationException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void printCategoryTree(MainController mainController) {
        Category root = mainController.getCategoryTree();
        System.out.println(root.toString());
    }

    private static void tryPrintTrolls(MainController mainController, String[] inputTokens) {
        try {
            checkArgumentCount(inputTokens, 1);
            double ratingLimit = Double.parseDouble(inputTokens[1]);
            List<Customer> trolls = mainController.getTrolls(ratingLimit);
            System.out.printf("Trolls (found %d):%n", trolls.size());
            trolls.forEach(t -> System.out.println(t.getUsername()));
        } catch (InputException e) {
            System.out.println(e.getMessage());
        }
        catch (NumberFormatException e) {
            System.out.printf("Invalid rating-limit: '%s'%n", inputTokens[1]);
        }
    }

    private static void tryGetReviewsByProdNumber(MainController mainController, String[] inputTokens) {
        try {
            checkArgumentCount(inputTokens, 1);
            List<Review> reviews = mainController.getReviewsByProdNumber(inputTokens[1]);
            if (reviews.isEmpty()) {
                System.out.printf("No reviews found for product number '%s'%n", inputTokens[1]);
                return;
            }
            System.out.printf("Reviews for product '%s' (found %d):%n", inputTokens[1], reviews.size());
            reviews.forEach(r -> System.out.println(r.toString() + "\n"));
        } catch (InputException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void printHelp() {
        System.out.printf(
                "Commands (before init):\n" + 
                        "%-70s Quits the application\n" +
                        "%-70s Initializes the main controller\n" +
                        "Commands (after init):\n" +
                        "%-70s Finishes the maincontroller and quits the application\n" +
                        "%-70s Prints product-details for specified product-number\n" +
                        "%-70s Prints all products having titles matching specified SQL-pattern, or all products if no pattern specified\n" +
                        "%-70s Prints entire category-tree\n" +
                        "%-70s Prints all products in specified category. " +
                        "Category needs to be specified as path containing category names separated by '/'\n" +
                        "%-70s Prints top k products, with regard to rating limit\n" +
                        "%-70s Prints all products similar to and cheaper than the specified product. Product must be " +
                        "specified by product-number\n" +
                        "%-70s Adds a new review to the 'review' table. rating must be int from 1-5\n" +
                        "%-70s Prints all usernames of users having average rating below specified floating point value l\n" +
                        "%-70s Prints all offers belonging to specified product-number\n" +
                        "%-70s Prints all reviews for specified product number\n",
                "(quit | N):",
                "Y:",
                "finish:",
                "getProduct <prod_number>:",
                "getProducts [<pattern>]:",
                "getCategoryTree:",
                "(getProductsByCategoryPath | getByPath) <path>:",
                "getTopProducts <k>:",
                "(getSimilarCheaperProduct | getSimCheaper) <prod_number>:",
                "addNewReview <username> <prod_number> <rating> <summary> <detail>:",
                "getTrolls <l>:",
                "getOffers <prod_number>:",
                "getReviews <prod_number>:");
    }

    private static void checkArgumentCount(String[] inputTokens, int expected) throws InputException {
        if (inputTokens.length - 1 != expected) {
            throw new InputException(String.format("Too many arguments. Expected %d, got %d.%n", expected,
                    inputTokens.length - 1));
        }
    }

    private static void checkArgumentCount(String[] inputTokens, int[] expectedOptions) throws InputException {
        if (Arrays.stream(expectedOptions).noneMatch(v -> v == inputTokens.length - 1)) {
            throw new InputException(String.format("Too many arguments. Expected any of %s, got %d.%n", Arrays.toString(expectedOptions),
                    inputTokens.length - 1));
        }
    }
}
