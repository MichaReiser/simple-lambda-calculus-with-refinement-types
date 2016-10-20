package ch.famreiser.hsr.seminar.typechecker.types;

public class TypeError extends RuntimeException {
    public TypeError(String message) {
        super(message);
    }
}
