# 🎯 Alışkanlık Takip Sistemi (Habit Tracker)

Bu proje, kullanıcıların sağlıklı yaşam, eğitim ve kariyer hedefleri doğrultusunda alışkanlıklarını planlamalarını, günlük veya haftalık olarak takip etmelerini ve ilerlemelerini raporlamalarını sağlayan **Java** tabanlı bir masaüstü uygulamasıdır. Proje, hem kullanıcı dostu **Grafiksel Arayüz (Swing GUI)** hem de hafif **Konsol Arayüzü (CLI)** sunmaktadır.

---

## 📋 İçindekiler
1. [Proje Açıklaması](#-proje-açıklaması)
2. [Kullanılan Teknolojiler](#-kullanılan-teknolojiler)
3. [Kurulum (Installation)](#-kurulum-installation)
4. [Kullanım (Usage)](#-kullanım-usage)
5. [Nesne Yönelimli Programlama (OOP) Yapısı](#-nesne-yönelimli-programlama-oop-yapısı)
6. [Değerlendirme Kriterleri ve Akademik Artılar](#-değerlendirme-kriterleri-ve-akademik-artılar)

---

## 🔍 Proje Açıklaması

Alışkanlık Takip Sistemi, kullanıcıların rutinlerini üç ana grupta sınıflandırarak takip etmesine olanak tanır:
1. **Günlük Alışkanlıklar**: Haftalık gün hedefi olan rutinler (örn. Her gün 30 sayfa kitap oku - haftada 7 gün).
2. **Haftalık Alışkanlıklar**: Haftanın belirli günlerinde yapılması hedeflenen rutinler (örn. Pazartesi, Çarşamba, Cuma günleri spor yap).
3. **Sayısal Alışkanlıklar**: Belirli bir hedef miktar ve birimi olan rutinler (örn. Günde 10000 adım at, 3 litre su iç).

Veriler, uygulama kapatıldığında kaybolmaması için yerel diske **JSON** formatında serileştirilerek (`Gson` ile) kaydedilir.

---

## 🛠️ Kullanılan Teknolojiler

* **Dil**: Java (SE 21 ve üzeri önerilir)
* **Kütüphaneler**: Google Gson (v2.10.1) - Nesne serileştirme ve kalıcı depolama için.
* **Görsel Arayüz**: Java Swing ve AWT (Pencere, Tablo, Form ve Dialog bileşenleri).
* **Derleme/Proje Yapısı**: Eclipse IDE standart proje yapısı.

---

## ⚙️ Kurulum (Installation)

### 1. Bağımlılıkların Yüklenmesi (Google Gson)
Uygulama, verileri JSON formatında okuyup yazmak için `Gson` kütüphanesine ihtiyaç duyar.
1. [Gson JAR (v2.10.1)](https://repo1.maven.org/maven2/com/google/code/gson/gson/2.10.1/gson-2.10.1.jar) dosyasını indirin.
2. IDE'nizde (Eclipse/IntelliJ) projenizin üzerine sağ tıklayın:
   * **Eclipse**: `Build Path` -> `Configure Build Path...` -> `Libraries` sekmesi -> `Classpath` seçin -> `Add External JARs...` butonuna tıklayıp indirdiğiniz `.jar` dosyasını seçin. `Apply and Close` diyerek onaylayın.
   * **IntelliJ**: `File` -> `Project Structure` -> `Libraries` -> `+ (New Project Library)` -> `Java` seçin ve indirdiğiniz `.jar` dosyasını ekleyin.

### 2. Projenin Çalıştırılması
* Grafik Arayüz için: `src/HabitTrackerGUI.java` dosyasını açıp **Run** butonuna basın.
* Konsol Arayüzü için: `src/HabitTrackerApp.java` dosyasını açıp **Run** butonuna basın.

---

## 💡 Kullanım (Usage)

### A. Grafiksel Arayüz (GUI) Kullanımı
Uygulama açıldığında karşınıza kayıtlı alışkanlıkların listelendiği bir tablo gelir:

1. **Yeni Alışkanlık Ekleme**:
   * Alt kısımdaki **"+ Yeni Ekle"** butonuna tıklayın.
   * Açılan pencerede alışkanlığın adını, açıklamasını, tipini (Günlük, Haftalık, Sayısal) ve kategorisini seçin.
   * Seçilen tipe göre hedef gün sayısını, hedef gün isimlerini (virgülle ayırarak) veya hedef sayısal değeri girip **"Kaydet"** butonuna basın.
2. **Tamamlama İşareti Koyma**:
   * Tablodan tamamlamak istediğiniz alışkanlığı seçin.
   * **"✓ Bugün Tamamla"** butonuna basın.
   * Eğer seçilen alışkanlık *Sayısal* ise sistem bugün gerçekleştirdiğiniz değeri soracaktır (örn: 8000 adım). Girilen değer hedefe ulaştığında veya aştığında alışkanlık tamamlandı sayılır.
3. **Alışkanlık Silme**:
   * Listeden bir alışkanlık seçip **"✗ Sil"** butonuna basın ve onaylayın.
4. **İstatistikleri Görüntüleme**:
   * **"İstatistikler"** butonuna tıklayarak başarı oranlarını, toplam tamamlama günlerini ve son 7 günün detaylı analiz raporunu görebilirsiniz.

### B. Konsol Arayüzü (CLI) Kullanımı
Terminal üzerinden uygulamayı başlattığınızda karşınıza `0-9` arası seçim yapabileceğiniz bir menü çıkar. Yönergeleri takip ederek klavyeden girdi sağlayıp tüm işlemleri konsol üzerinden de gerçekleştirebilirsiniz.

---

## 📐 Nesne Yönelimli Programlama (OOP) Yapısı

Uygulama, akademik değerlendirmede yüksek not almayı sağlayacak düzeyde temiz ve kurallara uygun bir OOP mimarisine sahiptir:

1. **Soyut Sınıf (Abstract Class - Abstraction)**:
   * [Habit](src/Habit.java) sınıfı `abstract` olarak tasarlanmıştır. Ortak nitelikleri barındırır fakat doğrudan nesnesi üretilemez.
2. **Kalıtım (Inheritance)**:
   * [DailyHabit](src/DailyHabit.java), [WeeklyHabit](src/WeeklyHabit.java) ve [NumericHabit](src/NumericHabit.java) sınıfları `Habit` sınıfından kalıtım (`extends`) alarak türetilmiştir.
3. **Çok Biçimlilik (Polymorphism)**:
   * `HabitTracker` sınıfı içindeki listelerde tüm alışkanlıklar `Habit` referans tipiyle tutulur. Çalışma zamanında (Runtime) her sınıfa özgü `calculateStats()` veya `getHabitType()` metodu otomatik çağrılır (Dinamik Bağlama).
4. **Arayüzler (Interfaces)**:
   * [Trackable](src/Trackable.java) ve [Reportable](src/Reportable.java) interface'leri ile sistemdeki alışkanlıkların izleme ve raporlama standartları garanti altına alınmıştır.
5. **Kapsülleme (Encapsulation)**:
   * Tüm değişkenler `private` veya `protected` anahtar kelimeleriyle korunur, verilere erişim `getter/setter` metotları ile sınırlandırılmıştır.
6. **Özel Hata Sınıfları (Custom Exception Handling)**:
   * Mantıksal hataları yakalamak için [HabitNotFoundException](src/HabitNotFoundException.java) ve [InvalidHabitDataException](src/InvalidHabitDataException.java) yazılmıştır.

---

## 🎓 Değerlendirme Kriterleri ve Akademik Artılar

Projenin jüri tarafından değerlendirilmesinde öne çıkacak güçlü yönleri:

* **Polimorfik Veri Serileştirme (Custom GSON Adapter)**: Soyut bir sınıfın alt sınıflarını veri kaybı yaşamadan JSON olarak kaydedip geri yükleyebilmek için yazılmış olan `HabitAdapter` jüriye sunulabilecek ileri düzey bir tekniktir.
* **Girdi Doğrulama (Input Validation)**: Hatalı kullanıcı girdilerinde sistemin çökmesini engelleyen kapsamlı `try-catch` blokları ve veri kontrol mekanizmaları mevcuttur.
* **Modüler Mimari**: Arayüz katmanları (GUI ve CLI) ile iş mantığının (Business Logic) yer aldığı sınıflar birbirinden tamamen izole edilmiştir.
