/**
 * Reportable Interface
 * Raporlama yetenekleri olan nesneler için davranış kontratını tanımlar.
 */
public interface Reportable {
    /**
     * Detaylı rapor üretir
     * @return Rapor metni
     */
    String generateReport();
    
    /**
     * Özet bilgi döndürür
     * @return Özet metin
     */
    String getSummary();
}
