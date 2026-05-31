/**
 * InvalidHabitDataException
 * Özel exception sınıfı - geçersiz alışkanlık verisi durumunda fırlatılır
 */
public class InvalidHabitDataException extends Exception {
    public InvalidHabitDataException(String message) {
        super(message);
    }
    
    public InvalidHabitDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
