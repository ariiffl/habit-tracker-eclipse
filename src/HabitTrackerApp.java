import java.text.SimpleDateFormat;
import java.util.*;

/**
 * HabitTrackerApp - Ana Uygulama Sınıfı
 * Console tabanlı kullanıcı arayüzü sağlar
 */
public class HabitTrackerApp {
    private HabitTracker tracker;
    private Scanner scanner;
    private SimpleDateFormat dateFormat;
    
    /**
     * Constructor
     */
    public HabitTrackerApp() {
        this.tracker = new HabitTracker("habits_data.json");
        this.scanner = new Scanner(System.in);
        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        
        // Uygulama başlarken verileri yükle
        tracker.loadFromFile();
    }
    
    /**
     * Ana uygulama döngüsü
     */
    public void run() {
        System.out.println("\n╔═══════════════════════════════════════════╗");
        System.out.println("║   ALIŞKANLIK TAKİP SİSTEMİNE HOŞ GELDİNİZ   ║");
        System.out.println("╚═══════════════════════════════════════════╝\n");
        
        boolean running = true;
        while (running) {
            try {
                displayMainMenu();
                int choice = getIntInput("Seçiminiz: ");
                
                switch (choice) {
                    case 1:
                        addNewHabit();
                        break;
                    case 2:
                        listAllHabits();
                        break;
                    case 3:
                        markHabitComplete();
                        break;
                    case 4:
                        recordNumericValue();
                        break;
                    case 5:
                        viewHabitDetails();
                        break;
                    case 6:
                        viewHabitsByCategory();
                        break;
                    case 7:
                        manageCategories();
                        break;
                    case 8:
                        deleteHabit();
                        break;
                    case 9:
                        viewOverallReport();
                        break;
                    case 0:
                        running = false;
                        tracker.saveToFile();
                        System.out.println("\n👋 Görüşmek üzere! Alışkanlıklarınız kaydedildi.\n");
                        break;
                    default:
                        System.out.println("✗ Geçersiz seçim! Lütfen 0-9 arası bir sayı girin.");
                }
                
            } catch (InputMismatchException e) {
                System.out.println("✗ Hata: Geçerli bir sayı girmelisiniz!");
                scanner.nextLine(); // Buffer'ı temizle
            } catch (Exception e) {
                System.out.println("✗ Beklenmeyen hata: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Ana menüyü gösterir
     */
    private void displayMainMenu() {
        System.out.println("\n" + "─".repeat(45));
        System.out.println("              ANA MENÜ");
        System.out.println("─".repeat(45));
        System.out.println("1. Yeni Alışkanlık Ekle");
        System.out.println("2. Tüm Alışkanlıkları Listele");
        System.out.println("3. Alışkanlığı Tamamla (Bugün)");
        System.out.println("4. Sayısal Değer Kaydet");
        System.out.println("5. Alışkanlık Detayları");
        System.out.println("6. Kategoriye Göre Görüntüle");
        System.out.println("7. Kategori Yönetimi");
        System.out.println("8. Alışkanlık Sil");
        System.out.println("9. Genel Rapor");
        System.out.println("0. Çıkış");
        System.out.println("─".repeat(45));
    }
    
    /**
     * Yeni alışkanlık ekler
     */
    private void addNewHabit() {
        try {
            System.out.println("\n=== YENİ ALIŞKANLIK EKLE ===\n");
            
            // Kategori seç
            Category category = selectCategory();
            if (category == null) return;
            
            // Temel bilgiler
            System.out.print("Alışkanlık Adı: ");
            String name = scanner.nextLine();
            
            System.out.print("Açıklama: ");
            String description = scanner.nextLine();
            
            // Alışkanlık tipi seç
            System.out.println("\nAlışkanlık Tipi:");
            System.out.println("1. Günlük Alışkanlık (Her gün tekrarlanır)");
            System.out.println("2. Haftalık Alışkanlık (Belirli günlerde tekrarlanır)");
            System.out.println("3. Sayısal Alışkanlık (Hedef değer gerektirir)");
            int type = getIntInput("Tip seçin: ");
            
            Habit habit = null;
            
            switch (type) {
                case 1:
                    int targetDays = getIntInput("Haftalık hedef gün sayısı (1-7): ");
                    habit = new DailyHabit(name, description, category, targetDays);
                    break;
                    
                case 2:
                    Set<String> targetDays2 = new HashSet<>();
                    System.out.println("Hedef günleri seçin (virgülle ayırın):");
                    System.out.println("Pazartesi, Salı, Çarşamba, Perşembe, Cuma, Cumartesi, Pazar");
                    String daysInput = scanner.nextLine();
                    for (String day : daysInput.split(",")) {
                        targetDays2.add(day.trim());
                    }
                    habit = new WeeklyHabit(name, description, category, targetDays2);
                    break;
                    
                case 3:
                    System.out.print("Hedef değer: ");
                    double targetValue = scanner.nextDouble();
                    scanner.nextLine(); // Buffer temizle
                    System.out.print("Birim (örn: adım, litre, dakika): ");
                    String unit = scanner.nextLine();
                    habit = new NumericHabit(name, description, category, targetValue, unit);
                    break;
                    
                default:
                    System.out.println("✗ Geçersiz tip!");
                    return;
            }
            
            if (habit != null) {
                tracker.addHabit(habit);
                tracker.saveToFile();
            }
            
        } catch (InvalidHabitDataException e) {
            System.out.println("✗ Veri hatası: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("✗ Hata: " + e.getMessage());
        }
    }
    
    /**
     * Tüm alışkanlıkları listeler
     */
    private void listAllHabits() {
        System.out.println("\n=== TÜM ALIŞKANLIKLAR ===\n");
        List<Habit> habits = tracker.getAllHabits();
        
        if (habits.isEmpty()) {
            System.out.println("Henüz alışkanlık eklenmemiş.");
            return;
        }
        
        for (int i = 0; i < habits.size(); i++) {
            Habit habit = habits.get(i);
            System.out.println((i + 1) + ". " + habit.toString());
            System.out.println("   ID: " + habit.getId());
            System.out.println("   " + habit.getSummary());
            System.out.println();
        }
    }
    
    /**
     * Alışkanlığı bugün için tamamlanmış olarak işaretler
     */
    private void markHabitComplete() {
        try {
            listAllHabits();
            System.out.print("\nTamamlamak istediğiniz alışkanlığın adı: ");
            String name = scanner.nextLine();
            
            Habit habit = tracker.findHabitByName(name);
            String today = dateFormat.format(new Date());
            
            boolean success = habit.markComplete(today);
            
            if (success) {
                System.out.println("✓ Tebrikler! '" + habit.getName() + "' bugün için tamamlandı!");
                tracker.saveToFile();
            }
            
        } catch (HabitNotFoundException e) {
            System.out.println("✗ " + e.getMessage());
        } catch (Exception e) {
            System.out.println("✗ Hata: " + e.getMessage());
        }
    }
    
    /**
     * Sayısal alışkanlık için değer kaydeder
     */
    private void recordNumericValue() {
        try {
            // Sadece sayısal alışkanlıkları listele
            List<Habit> allHabits = tracker.getAllHabits();
            List<NumericHabit> numericHabits = new ArrayList<>();
            
            System.out.println("\n=== SAYISAL ALIŞKANLIKLAR ===\n");
            for (Habit habit : allHabits) {
                if (habit instanceof NumericHabit) {
                    numericHabits.add((NumericHabit) habit);
                    System.out.println(numericHabits.size() + ". " + habit.getName() +
                        " (Hedef: " + ((NumericHabit) habit).getTargetValue() + 
                        " " + ((NumericHabit) habit).getUnit() + ")");
                }
            }
            
            if (numericHabits.isEmpty()) {
                System.out.println("Sayısal alışkanlık bulunmuyor.");
                return;
            }
            
            int choice = getIntInput("\nSeçim: ") - 1;
            if (choice < 0 || choice >= numericHabits.size()) {
                System.out.println("✗ Geçersiz seçim!");
                return;
            }
            
            NumericHabit habit = numericHabits.get(choice);
            double value = getDoubleInput("Değer girin (" + habit.getUnit() + "): ");
            String today = dateFormat.format(new Date());
            
            habit.recordValue(today, value);
            System.out.println("✓ Değer kaydedildi!");
            
            if (value >= habit.getTargetValue()) {
                System.out.println("🎉 Tebrikler! Hedefi aştınız!");
            }
            
            tracker.saveToFile();
            
        } catch (Exception e) {
            System.out.println("✗ Hata: " + e.getMessage());
        }
    }
    
    /**
     * Alışkanlık detaylarını gösterir
     */
    private void viewHabitDetails() {
        try {
            listAllHabits();
            System.out.print("\nDetayını görmek istediğiniz alışkanlığın adı: ");
            String name = scanner.nextLine();
            
            Habit habit = tracker.findHabitByName(name);
            System.out.println(habit.generateReport());
            
        } catch (HabitNotFoundException e) {
            System.out.println("✗ " + e.getMessage());
        } catch (Exception e) {
            System.out.println("✗ Hata: " + e.getMessage());
        }
    }
    
    /**
     * Kategoriye göre alışkanlıkları gösterir
     */
    private void viewHabitsByCategory() {
        System.out.println("\n=== KATEGORİLER ===\n");
        List<Category> categories = tracker.getAllCategories();
        
        for (int i = 0; i < categories.size(); i++) {
            System.out.println((i + 1) + ". " + categories.get(i));
        }
        
        int choice = getIntInput("\nKategori seçin: ") - 1;
        if (choice < 0 || choice >= categories.size()) {
            System.out.println("✗ Geçersiz seçim!");
            return;
        }
        
        Category selectedCategory = categories.get(choice);
        List<Habit> habits = tracker.getHabitsByCategory(selectedCategory.getName());
        
        System.out.println("\n=== " + selectedCategory.getName().toUpperCase() + " KATEGORİSİ ===\n");
        if (habits.isEmpty()) {
            System.out.println("Bu kategoride alışkanlık bulunmuyor.");
        } else {
            for (Habit habit : habits) {
                System.out.println("• " + habit.toString());
                System.out.println("  " + habit.getSummary());
                System.out.println();
            }
        }
    }
    
    /**
     * Kategori yönetimi
     */
    private void manageCategories() {
        System.out.println("\n=== KATEGORİ YÖNETİMİ ===\n");
        System.out.println("1. Kategorileri Listele");
        System.out.println("2. Yeni Kategori Ekle");
        int choice = getIntInput("Seçim: ");
        
        switch (choice) {
            case 1:
                List<Category> categories = tracker.getAllCategories();
                for (Category cat : categories) {
                    System.out.println("• " + cat);
                }
                break;
                
            case 2:
                System.out.print("Kategori Adı: ");
                String name = scanner.nextLine();
                System.out.print("Renk: ");
                String color = scanner.nextLine();
                System.out.print("Açıklama: ");
                String desc = scanner.nextLine();
                
                Category newCategory = new Category(name, color, desc);
                tracker.addCategory(newCategory);
                tracker.saveToFile();
                break;
                
            default:
                System.out.println("✗ Geçersiz seçim!");
        }
    }
    
    /**
     * Alışkanlık siler
     */
    private void deleteHabit() {
        try {
            listAllHabits();
            System.out.print("\nSilmek istediğiniz alışkanlığın adı: ");
            String name = scanner.nextLine();
            
            Habit habit = tracker.findHabitByName(name);
            System.out.print("'" + habit.getName() + "' silinecek. Emin misiniz? (E/H): ");
            String confirm = scanner.nextLine();
            
            if (confirm.equalsIgnoreCase("E")) {
                tracker.removeHabit(habit.getId());
                tracker.saveToFile();
            } else {
                System.out.println("İşlem iptal edildi.");
            }
            
        } catch (HabitNotFoundException e) {
            System.out.println("✗ " + e.getMessage());
        } catch (Exception e) {
            System.out.println("✗ Hata: " + e.getMessage());
        }
    }
    
    /**
     * Genel rapor gösterir
     */
    private void viewOverallReport() {
        System.out.println(tracker.generateOverallReport());
    }
    
    /**
     * Kategori seçimi için yardımcı metod
     */
    private Category selectCategory() {
        System.out.println("\n=== KATEGORİ SEÇİN ===\n");
        List<Category> categories = tracker.getAllCategories();
        
        for (int i = 0; i < categories.size(); i++) {
            System.out.println((i + 1) + ". " + categories.get(i).getName());
        }
        
        int choice = getIntInput("Kategori seçin: ") - 1;
        if (choice < 0 || choice >= categories.size()) {
            System.out.println("✗ Geçersiz kategori seçimi!");
            return null;
        }
        
        return categories.get(choice);
    }
    
    /**
     * Integer input almak için yardımcı metod (Exception Handling)
     */
    private int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                int value = scanner.nextInt();
                scanner.nextLine(); // Buffer temizle
                return value;
            } catch (InputMismatchException e) {
                System.out.println("✗ Geçerli bir sayı girmelisiniz!");
                scanner.nextLine(); // Buffer temizle
            }
        }
    }
    
    /**
     * Double input almak için yardımcı metod (Exception Handling)
     */
    private double getDoubleInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                double value = scanner.nextDouble();
                scanner.nextLine(); // Buffer temizle
                return value;
            } catch (InputMismatchException e) {
                System.out.println("✗ Geçerli bir sayı girmelisiniz!");
                scanner.nextLine(); // Buffer temizle
            }
        }
    }
    
    /**
     * Main metod - uygulamayı başlatır
     */
    public static void main(String[] args) {
        HabitTrackerApp app = new HabitTrackerApp();
        app.run();
    }
}
