package com.immobiliere.visits_service.exception;

public class DuplicateVisitException extends RuntimeException {
    public DuplicateVisitException(String message){
        super(message);
    }
}
