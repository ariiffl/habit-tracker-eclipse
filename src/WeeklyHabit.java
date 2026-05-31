import java.text.SimpleDateFormat;
import java.util.*;

/**
 * WeeklyHabit Sınıfı
 * Haftalık olarak yapılan alışkanlıklar için kullanılır
 */
public class WeeklyHabit extends Habit {
    private Set<String> targetDaysOfWeek; // "Pazartesi", "Salı", vs.
    
    /**
     * Varsayılan constructor
     */
    public WeeklyHabit() {
        super();
        this.targetDaysOfWeek = new HashSet<>();
    }
    
    /**
     * Parametreli constructor
     * @param name Alışkanlık adı
     * @param description Açıklama
     * @param category Kategori
     * @param targetDaysOfWeek Hedef günler (Pazartesi, Salı, vb.)
     */
    public WeeklyHabit(String name, String description, Category category, Set<String> targetDaysOfWeek) {
        super(name, description, category);
        this.targetDaysOfWeek = targetDaysOfWeek;
    }
    
    public Set<String> getTargetDaysOfWeek() {
        return targetDaysOfWeek;
    }
    
    public void setTargetDaysOfWeek(Set<String> targetDaysOfWeek) {
        this.targetDaysOfWeek = targetDaysOfWeek;
    }
    
    @Override
    public String getHabitType() {
        return "Haftalık Alışkanlık";
    }
    
    @Override
    public String calculateStats() {
        StringBuilder stats = new StringBuilder();
        stats.append("\n--- İstatistikler ---\n");
        stats.append("Hedef Günler: ").append(targetDaysOfWeek).append("\n");
        stats.append("Toplam Tamamlama: ").append(getTotalCompletions()).append(" hafta\n");
        
        // Bu haftaki performans
        int thisWeekCompletions = getCompletionsThisWeek();
        stats.append("Bu Hafta: ").append(thisWeekCompletions).append("/")
             .append(targetDaysOfWeek.size()).append(" gün\n");
        
        // Başarı oranı
        if (targetDaysOfWeek.size() > 0) {
            double successRate = (double) thisWeekCompletions / targetDaysOfWeek.size() * 100;
            stats.append("Başarı Oranı (Bu Hafta): ").append(String.format("%.1f%%", successRate)).append("\n");
        }
        
        return stats.toString();
    }
    
    /**
     * Bu haftaki tamamlanma sayısını hesaplar
     * @return Tamamlanma sayısı
     */
    private int getCompletionsThisWeek() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        
        // Haftanın başına git (Pazartesi)
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        int daysToSubtract = (dayOfWeek == Calendar.SUNDAY) ? 6 : dayOfWeek - Calendar.MONDAY;
        cal.add(Calendar.DAY_OF_MONTH, -daysToSubtract);
        
        int count = 0;
        for (int i = 0; i < 7; i++) {
            String dateStr = sdf.format(cal.getTime());
            if (isCompletedOn(dateStr)) {
                count++;
            }
            cal.add(Calendar.DAY_OF_MONTH, 1);
        }
        
        return count;
    }
}
