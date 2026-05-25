import java.util.*;

/**
 * Habit Abstract Sınıfı
 * Tüm alışkanlık türleri için temel sınıf (Inheritance için üst sınıf)
 * Trackable ve Reportable interface'lerini implement eder (Interface kullanımı)
 * Encapsulation: private alanlar, getter/setter metodları
 */
public abstract class Habit implements Trackable, Reportable {
    private String id;
    private String name;
    private String description;
    private Category category;
    private Date createdDate;
    protected Map<String, Boolean> completionMap; // Alt sınıflar erişebilsin
    
    /**
     * Varsayılan constructor
     */
    public Habit() {
        this.id = UUID.randomUUID().toString();
        this.completionMap = new HashMap<>();
        this.createdDate = new Date();
        this.category = new Category();
    }
    
    /**
     * Parametreli constructor
     * @param name Alışkanlık adı
     * @param description Alışkanlık açıklaması
     * @param category Kategori
     */
    public Habit(String name, String description, Category category) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.description = description;
        this.category = category;
        this.createdDate = new Date();
        this.completionMap = new HashMap<>();
    }
    
    // Getter metodları
    public String getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public Category getCategory() {
        return category;
    }
    
    public Date getCreatedDate() {
        return createdDate;
    }
    
    public Map<String, Boolean> getCompletionMap() {
        return completionMap;
    }
    
    // Setter metodları
    public void setId(String id) {
        this.id = id;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public void setCategory(Category category) {
        this.category = category;
    }
    
    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
    
    public void setCompletionMap(Map<String, Boolean> completionMap) {
        this.completionMap = completionMap;
    }
    
    // Trackable interface implementasyonu
    @Override
    public boolean markComplete(String date) {
        completionMap.put(date, true);
        return true;
    }
    
    @Override
    public boolean isCompletedOn(String date) {
        return completionMap.getOrDefault(date, false);
    }
    
    @Override
    public int getTotalCompletions() {
        return (int) completionMap.values().stream().filter(v -> v).count();
    }
    
    // Reportable interface implementasyonu
    @Override
    public String getSummary() {
        return String.format("%s (%s) - %d tamamlama", 
            name, category.getName(), getTotalCompletions());
    }
    
    @Override
    public String generateReport() {
        StringBuilder report = new StringBuilder();
        report.append("=".repeat(50)).append("\n");
        report.append("Alışkanlık Raporu\n");
        report.append("=".repeat(50)).append("\n");
        report.append("Ad: ").append(name).append("\n");
        report.append("Açıklama: ").append(description).append("\n");
        report.append("Kategori: ").append(category).append("\n");
        report.append("Oluşturulma: ").append(createdDate).append("\n");
        report.append("Toplam Tamamlama: ").append(getTotalCompletions()).append("\n");
        report.append(calculateStats()).append("\n");
        report.append("=".repeat(50)).append("\n");
        return report.toString();
    }
    
    // Abstract metod - alt sınıflar kendi tipine göre implement edecek
    @Override
    public abstract String calculateStats();
    
    /**
     * Alışkanlık tipini döndürür (Polymorphism için)
     * @return Alışkanlık tipi
     */
    public abstract String getHabitType();
    
    @Override
    public String toString() {
        return String.format("[%s] %s - %s", category.getName(), name, getHabitType());
    }
}
