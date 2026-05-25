/**
 * Trackable Interface
 * Tüm takip edilebilir alışkanlıklar için temel davranış kontratını tanımlar.
 * Bu interface, polymorphism için temel yapıyı sağlar.
 */
public interface Trackable {
    /**
     * Alışkanlığı belirli bir tarih için işaretler
     * @param date İşaretlenecek tarih
     * @return İşlem başarılı ise true
     */
    boolean markComplete(String date);
    
    /**
     * Alışkanlığın tamamlanma durumunu kontrol eder
     * @param date Kontrol edilecek tarih
     * @return Tamamlanmış ise true
     */
    boolean isCompletedOn(String date);
    
    /**
     * Toplam tamamlanma sayısını döndürür
     * @return Tamamlanma sayısı
     */
    int getTotalCompletions();
    
    /**
     * Alışkanlık istatistiklerini hesaplar
     * @return İstatistik metni
     */
    String calculateStats();
}
