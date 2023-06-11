package pachinko;

import java.time.LocalDateTime;

public class Product {
    private String type;
    private String grade;
    private LocalDateTime expirationDate;

    public Product(){}

    public Product(String type, String grade, LocalDateTime expirationDate) {
        this.type = type;
        this.grade = grade;
        this.expirationDate = expirationDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDateTime expirationDate) {
        this.expirationDate = expirationDate;
    }

    @Override
    public String toString() {
        return "pachinko.Product{" +
                type + ',' +
                grade + ',' +
                expirationDate +
                '}';
    }
}
