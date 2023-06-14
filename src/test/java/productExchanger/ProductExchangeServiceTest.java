package productExchanger;

import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.StringJoiner;

import static org.junit.Assert.*;

public class ProductExchangeServiceTest {
    ProductCodeContainer pcc;

    @Before
    public void setUp(){
        pcc = new ProductCodeContainer();
    }

    //고객에게 제공된 상품코드로 교환 가능 확인 가능
    @Test
    public void successToCheckWithProvidedProductCode(){
        //given
        initProductCodeStatus();
        HashSet<ProductCode> productCodes = (HashSet<ProductCode>) pcc.getProductCodes();
        String productCode = getOneProvidedProductCode(productCodes);
        String checkInput = "check " + productCode;

        //when
        String[] dividedCheckInput = InputProcessor.processInput(checkInput);
        PEResultStatus result = ProductExchangeService.check(dividedCheckInput, pcc);

        //then
        assertNotNull(dividedCheckInput);
        assertEquals(PEResultStatus.EXCHANGEABLE_CODE, result);
    }

    //임의의 상품코드로 교환 가능 여부 확인 불가능
    @Test
    public void failToCheckWithRandomProductCode(){
        //given
        initProductCodeStatus();
        String randomCode = getRandomCode();

        while(!checkNotGeneratedCode(randomCode)){
            randomCode = getRandomCode();
        }

        String checkInput = "check " + randomCode;

        //when
        String[] dividedCheckInput = InputProcessor.processInput(checkInput);
        PEResultStatus result = ProductExchangeService.check(dividedCheckInput, pcc);

        //then
        assertNotNull(dividedCheckInput);
        assertEquals(PEResultStatus.UNEXCHANGEABLE_CODE, result);
    }

    //개발자가 제공하였으나 사용자에게 제공되지 않은 상품코드로 교한 가능 여부 확인 불가능
    @Test
    public void failCheckWithGenerateButNotProvidedProductCode(){
        //given
        initProductCodeStatus();
        HashSet<ProductCode> productCodes = (HashSet<ProductCode>) pcc.getProductCodes();
        String productCode = getOneGeneratedButNotProvidedProductCode(productCodes);
        String checkInput = "check " + productCode;

        //when
        String[] dividedCheckInput = InputProcessor.processInput(checkInput);
        PEResultStatus result = ProductExchangeService.check(dividedCheckInput, pcc);

        //then
        assertNotNull(dividedCheckInput);
        assertEquals(PEResultStatus.UNEXCHANGEABLE_CODE, result);
    }

    //이미 사용한 상품코드 사용시 교환 불가능
    @Test
    public void failCheckWithUsedProductCode(){
        //given
        initProductCodeStatus();
        HashSet<ProductCode> productCodes = (HashSet<ProductCode>) pcc.getProductCodes();
        String productCode = getOneProvidedProductCode(productCodes);
        String randomStoreCoe = "AASSDD";

        StringJoiner joiner = new StringJoiner(" ");
        joiner.add("check");
        joiner.add(productCode);
        String checkInput = joiner.toString();

        joiner = new StringJoiner(" ");
        joiner.add("claim");
        joiner.add(randomStoreCoe);
        joiner.add(productCode);
        String claimInput = joiner.toString();

        //when
        String[] dividedCheckInput = InputProcessor.processInput(checkInput);
        String[] dividedClaimInput = InputProcessor.processInput(claimInput);

        PEResultStatus checkResult1 = ProductExchangeService.check(dividedCheckInput, pcc);
        PEResultStatus claimResult = ProductExchangeService.claim(dividedClaimInput, pcc);
        PEResultStatus checkResult2 = ProductExchangeService.check(dividedCheckInput, pcc);

        //then
        assertNotNull(dividedCheckInput);
        assertEquals(PEResultStatus.EXCHANGE_SUCCESS, claimResult);

        assertEquals(PEResultStatus.EXCHANGEABLE_CODE, checkResult1);
        assertEquals(PEResultStatus.UNEXCHANGEABLE_CODE, checkResult2);
    }

    //사용법 안내 확인 가능
    @Test
    public void successToHelpWithValidInputFormat(){
        //given
        initProductCodeStatus();
        //when
        PEResultStatus result = ProductExchangeService.help();

        //then
        assertEquals(PEResultStatus.HELP, result);
    }

    //고객에게 제공된 코드와 올바른 형식으로 claim 호출시 상품 교환 가능
    @Test
    public void successToClaimWithValidInputAndFormat(){
        //given
        initProductCodeStatus();

        HashSet<ProductCode> productCodes = (HashSet<ProductCode>) pcc.getProductCodes();
        String productCode = getOneProvidedProductCode(productCodes);
        String randomStoreCode = "AASSDD";

        StringJoiner joiner = new StringJoiner(" ");
        joiner.add("claim");
        joiner.add(randomStoreCode);
        joiner.add(productCode);
        String claimInput = joiner.toString();

        //when
        String[] dividedClaimInput = InputProcessor.processInput(claimInput);
        PEResultStatus result = ProductExchangeService.claim(dividedClaimInput, pcc);

        //then
        assertNotNull(dividedClaimInput);
        assertEquals(PEResultStatus.EXCHANGE_SUCCESS, result);
    }

    //임의의 상품코드 20개안에 포함되나,
    //사용자에게 제공된 10개의 상품 코드가 아니라면
    //상품 교환 불가
    @Test
    public void failToClaimWithGeneratedButNotProvidedProductCode(){
        //given
        initProductCodeStatus();

        HashSet<ProductCode> productCodes = (HashSet<ProductCode>) pcc.getProductCodes();
        String productCode = getOneGeneratedButNotProvidedProductCode(productCodes);
        String randomStoreCode = "AASSDD";

        StringJoiner joiner = new StringJoiner(" ");
        joiner.add("claim");
        joiner.add(randomStoreCode);
        joiner.add(productCode);
        String claimInput = joiner.toString();

        //when
        String[] dividedClaimInput = InputProcessor.processInput(claimInput);
        PEResultStatus result = ProductExchangeService.claim(dividedClaimInput, pcc);

        //then
        assertNotNull(dividedClaimInput);
        assertEquals(PEResultStatus.EXCHANGE_FAIL, result);
    }

    //이미 교환한 상품 코드 재입력시 교환 불가
    @Test
    public void failToClaimWithUsedProductCode(){
        //given
        initProductCodeStatus();

        HashSet<ProductCode> productCodes = (HashSet<ProductCode>) pcc.getProductCodes();
        String productCode = getOneProvidedProductCode(productCodes);
        String randomStoreCode = "AASSDD";

        StringJoiner joiner = new StringJoiner(" ");
        joiner.add("claim");
        joiner.add(randomStoreCode);
        joiner.add(productCode);
        String claimInput = joiner.toString();

        //when
        String[] dividedClaimInput = InputProcessor.processInput(claimInput);
        PEResultStatus result1 = ProductExchangeService.claim(dividedClaimInput, pcc);
        PEResultStatus result2 = ProductExchangeService.claim(dividedClaimInput, pcc);

        //then
        assertNotNull(dividedClaimInput);
        assertEquals(PEResultStatus.EXCHANGE_SUCCESS, result1);
        assertEquals(PEResultStatus.EXCHANGE_FAIL, result2);

    }

    //무작위 상품 코드 입력시 교환 불가
    @Test
    public void failToClaimWithRandomProductCode(){
        //given
        initProductCodeStatus();
        String randomProductCode = getRandomCode();

        while(!checkNotGeneratedCode(randomProductCode)){
            randomProductCode = getRandomCode();
        }

        String randomStoreCode = "AASSDD";

        StringJoiner joiner = new StringJoiner(" ");
        joiner.add("claim");
        joiner.add(randomStoreCode);
        joiner.add(randomProductCode);
        String claimInput = joiner.toString();

        //when
        String[] dividedClaimInput = InputProcessor.processInput(claimInput);
        PEResultStatus result = ProductExchangeService.claim(dividedClaimInput, pcc);

        //then
        assertNotNull(dividedClaimInput);
        assertEquals(PEResultStatus.EXCHANGE_FAIL, result);
    }

    private String getOneProvidedProductCode(HashSet<ProductCode> productCodes){
        String providedPCode = null;

        for(ProductCode pCode : productCodes){
            if(pCode.isProvided()){
                providedPCode = pCode.getProductCode();
                break;
            }
        }
        return providedPCode;
    }

    private String getOneGeneratedButNotProvidedProductCode(HashSet<ProductCode> productCodes){
        String providedPCode = null;

        for(ProductCode pCode : productCodes){
            if(!pCode.isProvided()){
                providedPCode = pCode.getProductCode();
                break;
            }
        }
        return providedPCode;
    }

    private String getRandomCode(){
        int ranNum = (int) (Math.random() * (999999999 - 100000000) + 100000000);
        return ranNum+"";
    }

    private boolean checkNotGeneratedCode(String code){
        HashSet<ProductCode> productCodes = (HashSet<ProductCode>) pcc.getProductCodes();

        for(ProductCode pc : productCodes){
            if(pc.getProductCode().equals(code)) return false;
        }

        return true;
    }

    private void initProductCodeStatus(){
        HashSet<ProductCode> productCodes = (HashSet<ProductCode>) pcc.getProductCodes();

        for(ProductCode pc : productCodes){
            pc.setExchangeable(true);
            pc.setStoreCode("");
        }
    }
}