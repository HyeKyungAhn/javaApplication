package productExchanger;

import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class ProductCodeContainerTest {
    ProductCodeContainer pcc;

    @Before
    public void setUp(){
        pcc = new ProductCodeContainer();
    }

    //20개의 코드를 파일에서 정상적으로 불러오는가?
    @Test
    public void successToGenerate20ProductCodesFromFile(){
        //given
        //when
        HashSet<ProductCode> codes = pcc.getCodes();

        //then
        assertEquals(20, codes.size());
        for (ProductCode pcode : codes) {
            assertNotEquals("", pcode.getProductCode());
        }
    }

    //20개의 코드 중, 10개가 사용자에게 제공되는가?
    @Test
    public void successToProvide10ProductCodes(){
        //given
        //when
        Set<ProductCode> codes = pcc.getProductCodes();

        int providedProductCodeNum = 0;
        for(ProductCode productCode : codes){
            if(productCode.isProvided()){
                providedProductCodeNum++;
            }
        }

        //then
        assertEquals(10, providedProductCodeNum);
    }

    //상품교환 코드가 제공되지 않으면 productCode set size = 0
    @Test
    public void returnSizeZeroIfProductSourceFileIsNotFound(){
        //given
        pcc = new ProductCodeContainer("nofile");

        //when
        HashSet<ProductCode> productCodes = (HashSet<ProductCode>) pcc.getProductCodes();

        //then
        assertEquals(0, productCodes.size());
    }
}