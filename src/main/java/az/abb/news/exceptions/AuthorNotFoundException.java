package az.abb.news.exceptions;

public class AuthorNotFoundException extends RuntimeException{
    public AuthorNotFoundException() {
        super("No such author");
    }
}
