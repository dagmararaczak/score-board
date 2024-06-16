package game.exceptions;

public class InvalidScoreException extends RuntimeException {

    private static final String INVALID_SCORE = "Provided score is incorrect";

    public InvalidScoreException() {
        super(INVALID_SCORE);
    }
}
