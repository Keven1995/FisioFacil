package br.com.kevenaraujo.fisiofacil.exception;

public class EmailAlreadyRegisteredException extends RuntimeException {
    public EmailAlreadyRegisteredException(String message) {
        super(message);
    }
}
