package az.abb.news.exceptions;

import az.abb.news.model.view.ErrorView;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AuthorNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public @ResponseBody ErrorView authorNotFoundException(AuthorNotFoundException ex) {
        return ErrorView.builder()
                .errorMessage(ex.getMessage()).errorCode(HttpStatus.NOT_FOUND.value()).build();
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public @ResponseBody ErrorView categoryNotFoundException(CategoryNotFoundException ex) {
        return ErrorView.builder()
                .errorMessage(ex.getMessage()).errorCode(HttpStatus.NOT_FOUND.value()).build();
    }

    @ExceptionHandler(InvalidIdException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody ErrorView invalidIdException(InvalidIdException ex) {
        return ErrorView.builder()
                .errorMessage(ex.getMessage()).errorCode(HttpStatus.BAD_REQUEST.value()).build();
    }

    @ExceptionHandler(PostNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public @ResponseBody ErrorView postNotFoundException(PostNotFoundException ex) {
        return ErrorView.builder()
                .errorMessage(ex.getMessage()).errorCode(HttpStatus.NOT_FOUND.value()).build();
    }

    @ExceptionHandler(FileNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public @ResponseBody ErrorView fileNotFoundException(FileNotFoundException ex) {
        return ErrorView.builder()
                .errorMessage(ex.getMessage()).errorCode(HttpStatus.NOT_FOUND.value()).build();
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public @ResponseBody ErrorView exception(Exception ex) {
        return ErrorView.builder()
                .errorMessage(ex.getMessage()).errorCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).build();
    }

    @ExceptionHandler(FileStorageException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public @ResponseBody ErrorView fileStorageException(FileStorageException ex) {
        return ErrorView.builder()
                .errorMessage(ex.getMessage()).errorCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).build();
    }
}