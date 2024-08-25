package az.abb.news.exceptions;

public class FileNotFoundException extends RuntimeException{
    public FileNotFoundException(){
        super("No such file");
    }
}
