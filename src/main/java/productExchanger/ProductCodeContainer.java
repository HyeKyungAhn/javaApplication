package productExchanger;
import java.io.*;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class ProductCodeContainer {
    private HashSet<ProductCode> productCodes;
    static final int TOTAL_PRODUCT_CODE_NUM = 20;
    static final int PROVIDED_PRODUCT_CODE_NUM = 10;

    private ProductCodeContainer(){
        setProductCodes(get20ProductCodes());
        change10ProductCodeProvided();
    }

    public static ProductCodeContainer getInstance(){
        return new ProductCodeContainer();
    }

    public Set<ProductCode> getProductCodes() {
        return productCodes;
    }

    private void setProductCodes(HashSet<ProductCode> productCodes){
        this.productCodes = productCodes;
    }

    private HashSet<ProductCode> generate20ProductCodes(){
        int ranNum;
        HashSet<ProductCode> productCodes = new HashSet<>();

        for(int i=0; i<TOTAL_PRODUCT_CODE_NUM; i++){
            try {
                ranNum = (int) (Math.random() * (999999999 - 100000000) + 100000000);
                productCodes.add(new ProductCode(ranNum + ""));
            } catch (IllegalArgumentException e){
                i--;
            }
        }

        return productCodes;
    }

    private HashSet<ProductCode> get20ProductCodes(){
        HashSet<ProductCode> productCodes = new HashSet<>();

        String filePath = String.valueOf(Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "inputSource.txt"));
        File file = new File(filePath);
        BufferedReader reader;
        String line;
        try{
            reader = new BufferedReader(new FileReader(file));
            while((line = reader.readLine()) != null){
                productCodes.add(new ProductCode(line));
            }
        } catch (IOException e){
            return generate20ProductCodes();
        }

        return productCodes;
    }

    public HashSet<ProductCode> getCodes(){
        return productCodes;
    }

    private void change10ProductCodeProvided(){
        Iterator<ProductCode> iterator = productCodes.iterator();
        for(int i=0; i<PROVIDED_PRODUCT_CODE_NUM; i++){
            ProductCode productCode = iterator.next();
            productCode.setProvided(true);
        }
    }

    public boolean isExchangeable(String productCode){
        for(ProductCode pCode : productCodes){
            if(pCode.getProductCode().equals(productCode) && pCode.isProvided()){
                return pCode.isExchangeable();
            }
        }
        return false;
    }

    public PEResultStatus exchange(String[] command){
        return setProductCodeStatus(command);
    }

    private PEResultStatus setProductCodeStatus(String[] command){
        String productCode = command[2];
        String storeCode = command[1];

        for(ProductCode pCode : productCodes){
            if(pCode.getProductCode().equals(productCode)){
                pCode.setStoreCode(storeCode);
                pCode.setExchangeable(false);
                return PEResultStatus.EXCHANGE_SUCCESS;
            }
        }
        return PEResultStatus.EXCHANGE_FAIL;
    }
}
