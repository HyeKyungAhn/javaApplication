package pachinko;

import org.junit.Before;
import org.junit.Test;

import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class ProductContainerTest {
    ProductContainer productContainer;

    @Before
    public void setup(){
        productContainer = new ProductContainer();
    }

    //총 10종류의 상품이 제공되는가?
    @Test
    public void testProvidedProductKindNum(){
        Set<String> types1 = productContainer.getProductList().stream()
                .map(Product::getType)
                .collect(Collectors.toSet());

        assertEquals(10, types1.size());
    }

}