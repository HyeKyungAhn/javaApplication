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

    public String getGrade() {
        return grade;
    }

    public LocalDateTime getExpirationDate() {
        return expirationDate;
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
