**Proje Adı:** Alışkanlık Takip Sistemi (Habit Tracker)  
**Öğrenci Adı Soyadı:** ARİF ÜZÜMCÜ
**Öğrenci Numarası:** 361525821

---

## Projenin Amacı

Bu projenin temel amacı, bireylerin kişisel hedeflerine ve günlük rutinlerine sadık kalmalarını kolaylaştıran, tamamlanma oranlarını analiz edip raporlayan modern bir **Alışkanlık Takip Sistemi** geliştirmektir. 

---

## Hedefler

Uygulamanın hem kullanıcı hem de teknik açıdan gerçekleştirmeyi hedeflediği maddeler şunlardır:
1. **Kullanıcı Hedefleri**:
   * Farklı türde alışkanlıkları tek bir çatı altından yönetebilmek.
   * Tarih bazlı takip yapıp gelişim istatistiklerini görsel ve metinsel olarak görebilmek.
   * Basit ve yormayan arayüzler ile hızlıca alışkanlık kaydı girmek.
2. **Teknik Hedefler**:
   * **Modüler Tasarım**: Arayüz katmanları ile veri/iş katmanlarının birbirinden bağımsız çalışması (Seperation of Concerns).
   * **Esneklik**: Open/Closed prensibine uygun olarak sisteme yeni alışkanlık türleri eklendiğinde mevcut kodların değiştirilmesine gerek duyulmaması.
   * **Güvenilirlik**: Yanlış veri girişlerinde veya bulunamayan dosya işlemlerinde sistemin hata fırlatıp güvenli bir şekilde çalışmaya devam etmesi (Robustness).
   * **Kalıcılık**: Uygulama kapatılsa dahi tüm kullanıcı kayıtlarının JSON dosyası aracılığıyla saklanıp bir sonraki açılışta yüklenmesi.

---

## Kullanılan Araçlar ve Sistem Gereksinimleri

### Kullanılan Araçlar:
* **Programlama Dili**: Java SE (Version 21)
* **Grafik Arayüz Kitleri**: Java Swing & AWT
* **Veri Yönetimi Kütüphanesi**: Google Gson
* **Geliştirme Ortamı (IDE)**: Eclipse IDE
* **Versiyon Kontrol**: Git & GitHub

### Sistem Gereksinimleri:
* **Çalışma Zamanı**: JRE veya JDK 21 ya da üzeri.
* **İşletim Sistemi**: Windows, macOS veya Linux
* **Ekstra Bağımlılık**: Proje classpath'inde gson kütüphanesi bulunmalıdır.

---

## UML Class Diyagramı

Sistemin sınıfları arasındaki kalıtım, arayüz implementasyonu ve sahiplik ilişkileri aşağıdaki UML diyagramında gösterilmiştir. 

![UML Class Diyagramı](resources/Diagram.png)

---

## Tasarım Süreci ve OOP Prensiplerinin Uygulanışı

Proje, yazılımın genişletilebilir, okunabilir ve bakımı kolay olmasını sağlayan temel OOP ilkeleri üzerine inşa edilmiştir:

### A. Abstraction ve Inheritance
Uygulamadaki tüm alışkanlık sınıfları için [Habit.java](src/Habit.java) sınıfı `abstract` bir temel sınıf olarak tasarlanmıştır. Bu soyut sınıf ortak alanları (`id`, `name`, `description`, `category`) içerirken, alt sınıfların mutlaka ezmesi gereken soyut metotları (`calculateStats()`, `getHabitType()`) belirler.
* [DailyHabit](src/DailyHabit.java), [WeeklyHabit](src/WeeklyHabit.java) ve [NumericHabit](src/NumericHabit.java) sınıfları `Habit` sınıfından `extends` kelimesiyle kalıtım alır ve kendilerine özgü niteliklerle (örneğin NumericHabit için `targetValue` ve `unit`) davranışları genişletirler.

### B. Interface ile Kontrat Tabanlı Tasarım
Uygulamada iki adet interface kullanılmıştır:
* **`Trackable`**: Tamamlama durumunu (`markComplete`, `isCompletedOn`) ve istatistik hesaplama (`calculateStats`) davranışlarını tanımlar.
* **`Reportable`**: Alışkanlık raporlama standartlarını (`generateReport`, `getSummary`) belirler.
* `Habit` soyut sınıfı bu interface'leri uygulayarak alt sınıfların bu davranış sözleşmelerine uymasını zorunlu kılar.

### C. Polymorphism
[HabitTracker](src/HabitTracker.java) sınıfı, bünyesindeki tüm alışkanlıkları `List<Habit>` türünde tutar. Bu polimorfik liste sayesinde program, listedeki nesnenin gerçekte bir günlük, haftalık ya da sayısal alışkanlık olduğunu bilmeye gerek duymadan tek bir referans tipi üzerinden yönetebilir.
* **Polimorfik Serileştirme Zorluğu ve Çözümü**: Normal şartlarda Gson soyut bir üst sınıfa ait listeyi JSON'a yazarken ve okurken alt sınıfların tip bilgisini kaybeder. Bu projede, `HabitTracker` içinde statik olarak tanımlanmış özel bir adaptör olan `HabitAdapter` (`JsonSerializer` ve `JsonDeserializer` uygulayan sınıf) geliştirilmiştir. Bu adaptör, nesneyi kaydederken sınıfın adını JSON'a `"type"` etiketiyle ekler, okurken de bu tipe bakarak doğru nesneyi (`DailyHabit`, `WeeklyHabit` veya `NumericHabit`) dinamik olarak oluşturur.

### D. Encapsulation
Tüm sınıflarda üye değişkenler `private` veya `protected` erişim belirteçleri ile korunmuştur. Sınıf dışından verilere kontrolsüz erişim engellenmiş olup verilere erişim `getter/setter` metotları ile sınırlandırılmıştır. 
* Örnek: `HabitTracker` içindeki alışkanlıklar doğrudan manipüle edilemez; ekleme için `addHabit()`, bulma için `findHabitById()` gibi güvenli metotlar kullanılır.

### E. Hata Yönetimi
Çalışma zamanında meydana gelebilecek öngörülebilir hatalar için özel istisna (`Exception`) sınıfları yazılmıştır:
* [InvalidHabitDataException](src/InvalidHabitDataException.java): Alışkanlık ismi boş bırakıldığında veya geçersiz veri girildiğinde fırlatılır.
* [HabitNotFoundException](src/HabitNotFoundException.java): Olmayan bir alışkanlık ismiyle işlem yapılmaya çalışıldığında fırlatılır.
* Her iki hata sınıfı da hem konsol arayüzünde hem de GUI arayüzündeki `JOptionPane` dialog kutularında kullanıcıyı düzgünce yönlendirmek üzere `try-catch` blokları içinde yakalanır.

---

## Sonuç

Alışkanlık Takip Sistemi projesi; nesne yönelimli programlama, grafik arayüz tasarımı ve dosya işlemlerini başarıyla bir araya getiren modüler bir yazılımdır. 

### Elde Edilen Çıktılar:
1. **Modülerlik ve Genişletilebilirlik**: Arayüzlerin ve iş mantığının ayrılması sayesinde ileride veritabanı (SQLite/MySQL) entegrasyonu yapılmak istendiğinde GUI veya CLI sınıflarında değişiklik yapılmasına gerek kalmayacaktır.
2. **Kullanıcı Deneyimi**: Swing GUI sayesinde tablolar üzerinden görsel olarak alışkanlık takibi yapılabilmektedir. JSON entegrasyonu sayesinde de veri kaybı yaşanmamaktadır.
3. **Akademik Değerlendirme**: Proje, OOP standartlarına tam uyum sağlamakta olup polimorfik Gson adaptörü, özel istisna sınıfları ve çift arayüz desteği ile tamamlanmıştır.
