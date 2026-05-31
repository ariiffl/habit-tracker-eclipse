import com.google.gson.*;
import java.io.*;
import java.util.*;

/**
 * HabitTracker Sınıfı
 * Tüm alışkanlıkları yönetir ve JSON dosya işlemlerini gerçekleştirir
 */
public class HabitTracker {
    private List<Habit> habits;
    private List<Category> categories;
    private String dataFilePath;
    private Gson gson;
    
    /**
     * Constructor
     * @param dataFilePath JSON veri dosyasının yolu
     */
    public HabitTracker(String dataFilePath) {
        this.habits = new ArrayList<>();
        this.categories = new ArrayList<>();
        this.dataFilePath = dataFilePath;
        this.gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(Habit.class, new HabitAdapter())
            .create();
        
        // Varsayılan kategoriler
        initializeDefaultCategories();
    }
    
    /**
     * Varsayılan kategorileri oluşturur
     */
    private void initializeDefaultCategories() {
        categories.add(new Category("Sağlık", "Yeşil", "Sağlık ve fitness alışkanlıkları"));
        categories.add(new Category("Eğitim", "Mavi", "Öğrenme ve gelişim"));
        categories.add(new Category("İş", "Turuncu", "Kariyer ve iş alışkanlıkları"));
        categories.add(new Category("Sosyal", "Pembe", "Sosyal ilişkiler"));
        categories.add(new Category("Kişisel", "Mor", "Kişisel gelişim"));
    }
    
    /**
     * Yeni alışkanlık ekler
     * @param habit Eklenecek alışkanlık
     */
    public void addHabit(Habit habit) throws InvalidHabitDataException {
        if (habit == null) {
            throw new InvalidHabitDataException("Alışkanlık null olamaz!");
        }
        if (habit.getName() == null || habit.getName().trim().isEmpty()) {
            throw new InvalidHabitDataException("Alışkanlık adı boş olamaz!");
        }
        habits.add(habit);
        System.out.println("✓ Alışkanlık başarıyla eklendi: " + habit.getName());
    }
    
    /**
     * ID'ye göre alışkanlık bulur
     * @param id Alışkanlık ID
     * @return Bulunan alışkanlık
     * @throws HabitNotFoundException Alışkanlık bulunamazsa
     */
    public Habit findHabitById(String id) throws HabitNotFoundException {
        return habits.stream()
            .filter(h -> h.getId().equals(id))
            .findFirst()
            .orElseThrow(() -> new HabitNotFoundException("ID ile alışkanlık bulunamadı: " + id));
    }
    
    /**
     * İsme göre alışkanlık bulur
     * @param name Alışkanlık adı
     * @return Bulunan alışkanlık
     * @throws HabitNotFoundException Alışkanlık bulunamazsa
     */
    public Habit findHabitByName(String name) throws HabitNotFoundException {
        return habits.stream()
            .filter(h -> h.getName().equalsIgnoreCase(name))
            .findFirst()
            .orElseThrow(() -> new HabitNotFoundException("İsimle alışkanlık bulunamadı: " + name));
    }
    
    /**
     * Alışkanlık siler
     * @param habitId Silinecek alışkanlığın ID'si
     * @throws HabitNotFoundException Alışkanlık bulunamazsa
     */
    public void removeHabit(String habitId) throws HabitNotFoundException {
        Habit habit = findHabitById(habitId);
        habits.remove(habit);
        System.out.println("✓ Alışkanlık silindi: " + habit.getName());
    }
    
    /**
     * Kategoriye göre alışkanlıkları filtreler
     * @param categoryName Kategori adı
     * @return Filtrelenmiş alışkanlık listesi
     */
    public List<Habit> getHabitsByCategory(String categoryName) {
        List<Habit> result = new ArrayList<>();
        for (Habit habit : habits) {
            if (habit.getCategory().getName().equalsIgnoreCase(categoryName)) {
                result.add(habit);
            }
        }
        return result;
    }
    
    /**
     * Tüm alışkanlıkları listeler
     * @return Alışkanlık listesi
     */
    public List<Habit> getAllHabits() {
        return new ArrayList<>(habits);
    }
    
    /**
     * Tüm kategorileri listeler
     * @return Kategori listesi
     */
    public List<Category> getAllCategories() {
        return new ArrayList<>(categories);
    }
    
    /**
     * Yeni kategori ekler
     * @param category Eklenecek kategori
     */
    public void addCategory(Category category) {
        if (!categories.contains(category)) {
            categories.add(category);
            System.out.println("✓ Kategori eklendi: " + category.getName());
        }
    }
    
    /**
     * İsme göre kategori bulur
     * @param name Kategori adı
     * @return Bulunan kategori veya null
     */
    public Category findCategoryByName(String name) {
        return categories.stream()
            .filter(c -> c.getName().equalsIgnoreCase(name))
            .findFirst()
            .orElse(null);
    }
    
    /**
     * Verileri JSON dosyasına kaydeder
     */
    public void saveToFile() {
        try {
            // Veri yapısı oluştur
            JsonObject root = new JsonObject();
            
            // Kategorileri JSON'a çevir
            JsonArray categoriesArray = gson.toJsonTree(categories).getAsJsonArray();
            root.add("categories", categoriesArray);
            
            // Alışkanlıkları JSON'a çevir
            JsonArray habitsArray = new JsonArray();
            for (Habit habit : habits) {
                habitsArray.add(gson.toJsonTree(habit));
            }
            root.add("habits", habitsArray);
            
            // Dosyaya yaz
            try (FileWriter writer = new FileWriter(dataFilePath)) {
                gson.toJson(root, writer);
                System.out.println("✓ Veriler başarıyla kaydedildi: " + dataFilePath);
            }
            
        } catch (IOException e) {
            System.err.println("✗ Dosya yazma hatası: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("✗ Kaydetme sırasında beklenmeyen hata: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * JSON dosyasından verileri yükler
     */
    public void loadFromFile() {
        try {
            File file = new File(dataFilePath);
            if (!file.exists()) {
                System.out.println("ℹ Veri dosyası bulunamadı, yeni dosya oluşturulacak.");
                return;
            }
            
            // Dosyayı oku
            try (FileReader reader = new FileReader(dataFilePath)) {
                JsonObject root = gson.fromJson(reader, JsonObject.class);
                
                if (root == null) {
                    System.out.println("ℹ Veri dosyası boş.");
                    return;
                }
                
                // Kategorileri yükle
                if (root.has("categories")) {
                    JsonArray categoriesArray = root.getAsJsonArray("categories");
                    categories.clear();
                    for (JsonElement element : categoriesArray) {
                        Category category = gson.fromJson(element, Category.class);
                        categories.add(category);
                    }
                }
                
                // Alışkanlıkları yükle
                if (root.has("habits")) {
                    JsonArray habitsArray = root.getAsJsonArray("habits");
                    habits.clear();
                    for (JsonElement element : habitsArray) {
                        Habit habit = gson.fromJson(element, Habit.class);
                        habits.add(habit);
                    }
                }
                
                System.out.println("✓ Veriler başarıyla yüklendi: " + habits.size() + " alışkanlık");
                
            }
            
        } catch (FileNotFoundException e) {
            System.err.println("✗ Dosya bulunamadı: " + e.getMessage());
        } catch (JsonSyntaxException e) {
            System.err.println("✗ JSON format hatası: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("✗ Dosya okuma hatası: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("✗ Yükleme sırasında beklenmeyen hata: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Genel rapor üretir
     * @return Rapor metni
     */
    public String generateOverallReport() {
        StringBuilder report = new StringBuilder();
        report.append("\n");
        report.append("═".repeat(60)).append("\n");
        report.append("           ALIŞKANLIK TAKİP SİSTEMİ - GENEL RAPOR\n");
        report.append("═".repeat(60)).append("\n\n");
        
        report.append("Toplam Alışkanlık: ").append(habits.size()).append("\n");
        report.append("Toplam Kategori: ").append(categories.size()).append("\n\n");
        
        // Kategoriye göre grupla
        report.append("--- Kategorilere Göre Dağılım ---\n");
        for (Category category : categories) {
            long count = habits.stream()
                .filter(h -> h.getCategory().equals(category))
                .count();
            if (count > 0) {
                report.append(String.format("  %s: %d alışkanlık\n", category.getName(), count));
            }
        }
        
        report.append("\n--- Alışkanlık Detayları ---\n");
        for (int i = 0; i < habits.size(); i++) {
            Habit habit = habits.get(i);
            report.append(String.format("\n%d. %s\n", i + 1, habit.getSummary()));
        }
        
        report.append("\n").append("═".repeat(60)).append("\n");
        return report.toString();
    }
    
    /**
     * Custom Gson Adapter - farklı Habit türlerini serialize/deserialize etmek için
     */
    private static class HabitAdapter implements JsonSerializer<Habit>, JsonDeserializer<Habit> {
        
        @Override
        public JsonElement serialize(Habit src, java.lang.reflect.Type typeOfSrc, JsonSerializationContext context) {
            JsonObject result = new JsonObject();
            result.addProperty("type", src.getClass().getSimpleName());
            result.add("properties", context.serialize(src, src.getClass()));
            return result;
        }
        
        @Override
        public Habit deserialize(JsonElement json, java.lang.reflect.Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();

            String type;
            JsonElement element;

            if (jsonObject.has("type") && jsonObject.get("type") != null
                    && !jsonObject.get("type").isJsonNull()) {
                // Yeni format: {"type": "...", "properties": {...}}
                type = jsonObject.get("type").getAsString();
                element = jsonObject.has("properties") ? jsonObject.get("properties") : jsonObject;
            } else {
                // Eski / düz format: alanlardan tipi tahmin et
                if (jsonObject.has("targetDaysOfWeek")) {
                    type = "WeeklyHabit";
                } else if (jsonObject.has("targetValue") || jsonObject.has("valueMap")) {
                    type = "NumericHabit";
                } else {
                    type = "DailyHabit";
                }
                element = jsonObject;
            }

            try {
                if (type.equals("DailyHabit")) {
                    return context.deserialize(element, DailyHabit.class);
                } else if (type.equals("WeeklyHabit")) {
                    return context.deserialize(element, WeeklyHabit.class);
                } else if (type.equals("NumericHabit")) {
                    return context.deserialize(element, NumericHabit.class);
                }
            } catch (Exception e) {
                throw new JsonParseException("Habit deserialization hatası: " + e.getMessage());
            }

            throw new JsonParseException("Bilinmeyen habit tipi: " + type);
        }
    }
}
