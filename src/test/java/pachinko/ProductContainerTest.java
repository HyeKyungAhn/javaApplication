package pachinko;

import org.junit.Before;
import org.junit.Test;

import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class ProductContainerTest {
    ProductContainer productContainer;

    @Before
    public void setup(){
        productContainer = new ProductContainer();
    }

    //총 10종류의 다른 상품이 제공되는가?
    @Test
    public void testProvidedProductDistinctKind(){
        Set<String> types = productContainer.getProductList().stream()
                .map(Product::getType)
                .collect(Collectors.toSet());

        assertEquals(10, types.size());
    }

    //A,B 등급의 상품이 최소 2종류 이상인가?
    @Test
    public void testMoreThanTwoProvidedProductKindsPerGrade(){
        Set<Product> products = productContainer.getProductList();
        int aGradeCount = 0;
        int bGradeCount = 0;

        for(Product p : products){
            if (p.getGrade().equals("A")) {
                aGradeCount++;
            } else if (p.getGrade().equals("B")) {
                bGradeCount++;
            }

        }

        assertTrue(aGradeCount >= 2);
        assertTrue(bGradeCount >= 2);
    }

    @Test
    public void testResultFalseIfLessThanTwoNotExpiredProducts(){
        //만료되지 않은 A등급 상품 1개, 만료되지 않은 B등급 상품 2개
        String filePath = String.valueOf(Paths.get(System.getProperty("user.dir"), "src", "test", "resources", "productSourceAllExpired.txt"));
        productContainer = new ProductContainer(filePath);

        //when
        boolean result = productContainer.haveAtLeastTwoNotExpiredProductsPerGrade(LocalDateTime.now());

        //then
        assertFalse(result);
    }
}