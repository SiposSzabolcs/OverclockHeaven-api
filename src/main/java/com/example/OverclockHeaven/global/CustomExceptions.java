package com.example.OverclockHeaven.global;

public class CustomExceptions {

    public static class ProductNotFoundException extends RuntimeException {
        public ProductNotFoundException(Integer id){
            super("No product found with id: " + id);
        }
    }

    public static class UserNotFoundExceptionString extends RuntimeException {
        public UserNotFoundExceptionString(String email){
            super("No trainer found with id: " + email);
        }
    }

    public static class UserNotFoundException extends RuntimeException {
        public UserNotFoundException(Integer id){
            super("No user found with id: " + id);
        }
    }

    public static class RatingException extends RuntimeException {
        public RatingException(){
            super("Rating must be in range of 0-5");
        }
    }

    public static class DuplicateInCartException extends RuntimeException {
        public DuplicateInCartException(){
            super("Item already in cart.");
        }
    }

    public static class CartIsEmptyException extends RuntimeException{
        public CartIsEmptyException(){
            super("No items in cart.");
        }
    }

}


