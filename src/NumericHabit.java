import java.text.SimpleDateFormat;
import java.util.*;

/**
 * NumericHabit Sınıfı
 * Sayısal hedef gerektiren alışkanlıklar için kullanılır (örn: 10000 adım, 2L su)
 */
public class NumericHabit extends Habit {
    private double targetValue;
    private String unit; // "adım", "litre", "dakika", vs.
    private Map<String, Double> valueMap; // Tarih -> gerçekleşen değer
    
    /**
     * Varsayılan constructor
     */
    public NumericHabit() {
        super();
        this.targetValue = 1.0;
        this.unit = "birim";
        this.valueMap = new HashMap<>();
    }
    
    /**
     * Parametreli constructor
     * @param name Alışkanlık adı
     * @param description Açıklama
     * @param category Kategori
     * @param targetValue Hedef değer
     * @param unit Birim
     */
    public NumericHabit(String name, String description, Category category, double targetValue, String unit) {
        super(name, description, category);
        this.targetValue = targetValue;
        this.unit = unit;
        this.valueMap = new HashMap<>();
    }
    
    public double getTargetValue() {
        return targetValue;
    }
    
    public void setTargetValue(double targetValue) {
        this.targetValue = targetValue;
    }
    
    public String getUnit() {
        return unit;
    }
    
    public void setUnit(String unit) {
        this.unit = unit;
    }
    
    public Map<String, Double> getValueMap() {
        return valueMap;
    }
    
    public void setValueMap(Map<String, Double> valueMap) {
        this.valueMap = valueMap;
    }
    
    /**
     * Belirli bir tarih için değer kaydeder
     * @param date Tarih
     * @param value Değer
     */
    public void recordValue(String date, double value) {
        valueMap.put(date, value);
        // Hedef aşılırsa tamamlanmış say
        if (value >= targetValue) {
            markComplete(date);
        }
    }
    
    /**
     * Belirli bir tarihteki değeri getirir
     * @param date Tarih
     * @return Kaydedilen değer
     */
    public double getValueOn(String date) {
        return valueMap.getOrDefault(date, 0.0);
    }
    
    @Override
    public String getHabitType() {
        return "Sayısal Alışkanlık";
    }
    
    @Override
    public String calculateStats() {
        StringBuilder stats = new StringBuilder();
        stats.append("\n--- İstatistikler ---\n");
        stats.append("Hedef: ").append(targetValue).append(" ").append(unit).append(" (günlük)\n");
        stats.append("Hedefe Ulaşılan Gün: ").append(getTotalCompletions()).append("\n");
        
        // Ortalama değer
        if (!valueMap.isEmpty()) {
            double average = valueMap.values().stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);
            stats.append("Ortalama Değer: ").append(String.format("%.2f", average))
                 .append(" ").append(unit).append("\n");
            
            // Son 7 günün ortalaması
            double last7DaysAvg = getAverageInLastNDays(7);
            stats.append("Son 7 Gün Ortalaması: ").append(String.format("%.2f", last7DaysAvg))
                 .append(" ").append(unit).append("\n");
            
            // Başarı oranı
            double successRate = (double) getTotalCompletions() / valueMap.size() * 100;
            stats.append("Başarı Oranı: ").append(String.format("%.1f%%", successRate)).append("\n");
        }
        
        return stats.toString();
    }
    
    /**
     * Son N günün ortalamasını hesaplar
     * @param days Gün sayısı
     * @return Ortalama değer
     */
    private double getAverageInLastNDays(int days) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        List<Double> values = new ArrayList<>();
        
        for (int i = 0; i < days; i++) {
            String dateStr = sdf.format(cal.getTime());
            if (valueMap.containsKey(dateStr)) {
                values.add(valueMap.get(dateStr));
            }
            cal.add(Calendar.DAY_OF_MONTH, -1);
        }
        
        return values.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
    }
}
