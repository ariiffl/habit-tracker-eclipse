import java.text.SimpleDateFormat;
import java.util.*;

/**
 * DailyHabit Sınıfı
 * Günlük tekrarlanan alışkanlıklar için kullanılır
 * Inheritance: Habit abstract sınıfından türer
 */
public class DailyHabit extends Habit {
    private int targetDaysPerWeek;
    
    /**
     * Varsayılan constructor
     */
    public DailyHabit() {
        super();
        this.targetDaysPerWeek = 7;
    }
    
    /**
     * Parametreli constructor
     * @param name Alışkanlık adı
     * @param description Açıklama
     * @param category Kategori
     * @param targetDaysPerWeek Haftalık hedef gün sayısı
     */
    public DailyHabit(String name, String description, Category category, int targetDaysPerWeek) {
        super(name, description, category);
        this.targetDaysPerWeek = targetDaysPerWeek;
    }
    
    // Getter ve Setter
    public int getTargetDaysPerWeek() {
        return targetDaysPerWeek;
    }
    
    public void setTargetDaysPerWeek(int targetDaysPerWeek) {
        this.targetDaysPerWeek = targetDaysPerWeek;
    }
    
    @Override
    public String getHabitType() {
        return "Günlük Alışkanlık";
    }
    
    @Override
    public String calculateStats() {
        StringBuilder stats = new StringBuilder();
        stats.append("\n--- İstatistikler ---\n");
        stats.append("Hedef: Haftada ").append(targetDaysPerWeek).append(" gün\n");
        stats.append("Toplam Tamamlama: ").append(getTotalCompletions()).append(" gün\n");
        
        // Son 7 günün performansı
        int last7Days = getCompletionsInLastNDays(7);
        stats.append("Son 7 Günde: ").append(last7Days).append("/7 gün\n");
        
        // Başarı oranı
        double successRate = (double) last7Days / 7 * 100;
        stats.append("Başarı Oranı (Son 7 Gün): ").append(String.format("%.1f%%", successRate)).append("\n");
        
        return stats.toString();
    }
    
    /**
     * Son N günde tamamlanan gün sayısını hesaplar
     * @param days Gün sayısı
     * @return Tamamlanma sayısı
     */
    private int getCompletionsInLastNDays(int days) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        int count = 0;
        
        for (int i = 0; i < days; i++) {
            String dateStr = sdf.format(cal.getTime());
            if (isCompletedOn(dateStr)) {
                count++;
            }
            cal.add(Calendar.DAY_OF_MONTH, -1);
        }
        
        return count;
    }
}
