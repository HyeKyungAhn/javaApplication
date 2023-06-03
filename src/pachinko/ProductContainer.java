package pachinko;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ProductContainer {
    private Set<Product> productList;

    ProductContainer(){
        productList = new HashSet<>();
        String filePath = System.getProperty("user.dir") + "\\src\\pachinko\\productSource.txt";
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
}
