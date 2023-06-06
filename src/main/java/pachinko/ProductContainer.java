package pachinko;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ProductContainer {
    private Set<Product> productList;

    ProductContainer(){
        productList = new HashSet<>();
        String filePath = String.valueOf(Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "productSource.txt"));
        setProductList(generateProductList(filePath));
    }

    private Set<Product> generateProductList(String filePath){
        File productResource = new File(filePath);
        BufferedReader br;
        String line;

        try{
            br = new BufferedReader(new FileReader(productResource));
            String[] headers = br.readLine().split(","); //remove header

            while ((line = br.readLine()) != null) {
                String[] lineArr = line.split(",");

                Product p = new Product(lineArr[0], lineArr[1], lineArr[2]);
                productList.add(p);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return productList;
    }


    public Set<Product> getProductList(){
        return productList;
    }

    private void setProductList(Set<Product> productList){
        this.productList = productList;
    }

    public Product getRandomPrizeByGrade(String grade, LocalDateTime dateTime) {
        List<Product> availablePrizes = getAvailableProductsByGrade(grade, dateTime);
        if (availablePrizes.isEmpty()) {
            return null;
        }

        int randomIndex = (int) (Math.random() * availablePrizes.size());

        return availablePrizes.get(randomIndex);
    }

    private List<Product> getAvailableProductsByGrade(String grade, LocalDateTime drawDateTime) {
        List<Product> avaliableProductList = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
        LocalDateTime expirationDate;

        for(Product p : productList){
            expirationDate = LocalDateTime.from(formatter.parse(p.getExpirationDate()));

            if(p.getGrade().equals(grade) && expirationDate.isAfter(drawDateTime)) {
                avaliableProductList.add(p);
            }
        }

        return avaliableProductList;
    }
}
