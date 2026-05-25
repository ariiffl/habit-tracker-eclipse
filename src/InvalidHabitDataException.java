/**
 * InvalidHabitDataException
 * Özel exception sınıfı - geçersiz alışkanlık verisi durumunda fırlatılır
 * Exception Handling için custom exception örneği
 */
public class InvalidHabitDataException extends Exception {
    public InvalidHabitDataException(String message) {
        super(message);
    }
    
    public InvalidHabitDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
