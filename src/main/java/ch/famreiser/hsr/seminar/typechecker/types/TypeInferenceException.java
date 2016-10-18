package ch.famreiser.hsr.seminar.typechecker.types;

public class TypeInferenceException extends RuntimeException {
    public TypeInferenceException(String message) {
        super(message);
    }
}
