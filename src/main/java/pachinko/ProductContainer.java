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
    DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
    ProductContainer(){
        productList = new HashSet<>();
        String filePath = String.valueOf(Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "productSource.txt"));
        setProductList(generateProductList(filePath));
    }

    ProductContainer(String filePath) {
        productList = new HashSet<>();
        setProductList(generateProductList(filePath));
    }

    private Set<Product> generateProductList(String filePath){
        File productResource = new File(filePath);
        BufferedReader br;
        String line;

        try{
            br = new BufferedReader(new FileReader(productResource));
            String[] headers = br.readLine().split(","); //remove header
            LocalDateTime expirationDate;

            while ((line = br.readLine()) != null) {
                String[] lineArr = line.split(",");
                expirationDate = LocalDateTime.from(formatter.parse(lineArr[2]));
                Product p = new Product(lineArr[0], lineArr[1], expirationDate);
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

        for(Product p : productList){
            if(p.getGrade().equals(grade) && p.getExpirationDate().isAfter(drawDateTime)) {
                avaliableProductList.add(p);
            }
        }

        return avaliableProductList;
    }

    public boolean haveAtLeastTwoNotExpiredProductsPerGrade(LocalDateTime drawDateTime){
        int notExpiredGradeANum = 0;
        int notEdpiredGradeBNum = 0;

        for (Product p : productList) {

            if(p.getExpirationDate().isAfter(drawDateTime)){
                if(p.getGrade().equals("A")) {
                    notExpiredGradeANum++;
                    continue;
                }
                notEdpiredGradeBNum++;
            }
        }

        return notExpiredGradeANum >= 2 && notEdpiredGradeBNum >= 2;
    }
}
