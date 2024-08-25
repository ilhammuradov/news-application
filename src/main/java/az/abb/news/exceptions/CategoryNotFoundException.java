package az.abb.news.exceptions;

public class CategoryNotFoundException extends RuntimeException{
    public CategoryNotFoundException(){
        super("No such category");
    }
}
