# 🎯 Alışkanlık Takip Sistemi (Habit Tracker)

Bu proje, kullanıcıların günlük, haftalık ve sayısal hedeflere dayalı alışkanlıklarını planlamalarını, takip etmelerini ve istatistiklerini raporlamalarını sağlayan **Java** tabanlı bir masaüstü uygulamasıdır. Proje hem **Konsol (CLI)** hem de görsel **Grafiksel Arayüz (Swing GUI)** sunmaktadır.

Proje, Nesne Yönelimli Programlama (OOP) prensiplerini ve Java programlama dilinin temel yapı taşlarını uygulamalı olarak göstermek amacıyla geliştirilmiştir.

---

## 🚀 Özellikler

* **Çoklu Alışkanlık Türleri**:
  * **Günlük Alışkanlıklar**: Her gün tekrarlanan ve haftalık gün hedefi olan alışkanlıklar (örn: Kitap Okumak).
  * **Haftalık Alışkanlıklar**: Haftanın belirli günlerinde yapılması hedeflenen alışkanlıklar (örn: Pazartesi ve Cuma günleri Spor Yapmak).
  * **Sayısal Alışkanlıklar**: Değer ve birim bazlı hedefleri olan alışkanlıklar (örn: 10000 Adım Atmak, 2 Litre Su İçmek).
* **Kategori Yönetimi**: Alışkanlıkları "Sağlık", "Eğitim", "İş", "Sosyal" ve "Kişisel" gibi renk kodlu kategorilere ayırma.
* **Detaylı İstatistik Raporlama**: Son 7 günün tamamlama oranları, başarı yüzdesi ve sayısal ortalamalar.
* **JSON Tabanlı Kalıcı Hafıza**: Verilerin kapatıldığında otomatik kaydedilip açıldığında geri yüklenmesi (Google Gson entegrasyonu ile).
* **Çift Arayüz Desteği**: İster terminal üzerinden konsol menüsüyle, ister görsel Swing penceresiyle kullanım.

---

## 🛠️ Kullanılan Teknolojiler ve Yapı

* **Dil**: Java (SE 21 ve üzeri önerilir)
* **Kütüphaneler**: [Google Gson 2.10.1](https://github.com/google/gson) (JSON Serileştirme için)
* **Arayüz Teknolojileri**: Java Swing & AWT (GUI için)
* **Veri Yapıları**: `ArrayList`, `HashMap`, `HashSet`, Java Stream API ve Lambda İfadeleri.

---

## 📐 Nesne Yönelimli Programlama (OOP) Prensipleri

Projede uygulanan temel OOP kavramları ve kod örneklerinin bulunduğu dosyalar:

1. **Soyutlama (Abstraction) & Kalıtım (Inheritance)**:
   * [Habit](src/Habit.java) sınıfı `abstract` (soyut) bir üst sınıf olarak tasarlanmış olup ortak alanları (`id`, `name`, `description`, `category`) içerir.
   * [DailyHabit](src/DailyHabit.java), [WeeklyHabit](src/WeeklyHabit.java) ve [NumericHabit](src/NumericHabit.java) sınıfları bu sınıftan türemiştir.

2. **Çok Biçimlilik (Polymorphism)**:
   * [HabitTracker](src/HabitTracker.java) sınıfındaki metotlar, dinamik olarak çalışma zamanında alt sınıfların davranışlarını çağırır (Dynamic Method Dispatch). 
   * JSON serileştirme ve geri yükleme işlemlerinde polimorfik yapıyı kaybetmemek adına özel bir Gson `TypeAdapter` (`HabitAdapter`) yazılmıştır.

3. **Kapsülleme (Encapsulation)**:
   * Tüm model sınıflarındaki veri alanları `private` olarak tanımlanmış, verilere erişim kontrollü `getter` ve `setter` metotları ile sağlanmıştır.

4. **Arayüz Kontratları (Interfaces)**:
   * [Trackable](src/Trackable.java): Alışkanlıkların tamamlanma durumunu izlemek ve istatistik hesaplamak için gerekli kontratı belirler.
   * [Reportable](src/Reportable.java): Alışkanlıklar için özet ve detaylı rapor çıktısı alma standartlarını tanımlar.

5. **Özel Hata Yönetimi (Custom Exceptions)**:
   * [HabitNotFoundException](src/HabitNotFoundException.java) ve [InvalidHabitDataException](src/InvalidHabitDataException.java) sınıfları ile uygulama içi mantıksal hatalar `try-catch` bloklarıyla güvenli şekilde yakalanır.

---

## 📁 Sınıf ve Dosya Yapısı

```text
HabitTracker/
│
├── src/
│   ├── Category.java                  # Alışkanlık kategorilerini temsil eder
│   ├── Trackable.java                 # Takip kontratını belirleyen interface
│   ├── Reportable.java                # Raporlama kontratını belirleyen interface
│   │
│   ├── Habit.java                     # Temel soyut (abstract) Alışkanlık sınıfı
│   ├── DailyHabit.java                # Günlük alışkanlık modeli
│   ├── WeeklyHabit.java               # Haftalık alışkanlık modeli
│   ├── NumericHabit.java              # Sayısal alışkanlık modeli
│   │
│   ├── HabitTracker.java              # Veri yönetimini ve JSON kayıt işlemlerini yapar
│   ├── HabitTrackerApp.java           # Konsol (CLI) tabanlı giriş noktası
│   ├── HabitTrackerGUI.java           # Görsel (Swing GUI) tabanlı giriş noktası
│   │
│   ├── HabitNotFoundException.java    # Alışkanlık bulunamadığında fırlatılan hata
│   └── InvalidHabitDataException.java # Geçersiz veri girişlerinde fırlatılan hata
│
└── README.md                          # Proje tanıtım dosyası
```

---

## 🔧 Kurulum ve Çalıştırma

### Bağımlılıkların Eklenmesi (Google Gson)
Projenin çalışabilmesi için **Google Gson** kütüphanesinin projeye dahil edilmesi gerekir.
1. [Gson JAR (v2.10.1)](https://repo1.maven.org/maven2/com/google/code/gson/gson/2.10.1/gson-2.10.1.jar) dosyasını indirin.
2. Kullandığınız IDE'de (Eclipse/IntelliJ) projenizin **Build Path** ayarlarına girerek bu JAR dosyasını **External JARs** olarak ekleyin.

### Uygulamayı Çalıştırma
* **Konsol Sürümü İçin**: `HabitTrackerApp.java` sınıfını çalıştırın.
* **Görsel Arayüz (GUI) İçin**: `HabitTrackerGUI.java` sınıfını çalıştırın.

Uygulama çalıştırıldığında verileri otomatik olarak kaydetmek için proje dizininde bir `habits_data.json` dosyası oluşturacaktır.
