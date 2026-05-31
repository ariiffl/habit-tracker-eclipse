/**
 * Category Sınıfı
 * Alışkanlıkları kategorize etmek için kullanılır.
 */
public class Category {
    private String name;
    private String color;
    private String description;
    
    /**
     * Varsayılan constructor
     */
    public Category() {
        this.name = "Genel";
        this.color = "Gri";
        this.description = "Genel kategori";
    }
    
    /**
     * Parametreli constructor
     * @param name Kategori adı
     * @param color Kategori rengi
     * @param description Kategori açıklaması
     */
    public Category(String name, String color, String description) {
        this.name = name;
        this.color = color;
        this.description = description;
    }
    
    public String getName() {
        return name;
    }
    
    public String getColor() {
        return color;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public void setColor(String color) {
        this.color = color;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    @Override
    public String toString() {
        return String.format("[%s] %s - %s", color, name, description);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Category category = (Category) obj;
        return name.equals(category.name);
    }
}
