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

    ProductCodeContainer(){
        String filePath = String.valueOf(Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "productCodeSource.txt"));
        setProductCodes(get20ProductCodes(filePath));
        change10ProductCodeProvided();
    }

    ProductCodeContainer(String filePath){
        setProductCodes(get20ProductCodes(filePath));
        change10ProductCodeProvided();
    }

    public Set<ProductCode> getProductCodes() {
        return productCodes;
    }

    private void setProductCodes(HashSet<ProductCode> productCodes){
        this.productCodes = productCodes;
    }

    private HashSet<ProductCode> get20ProductCodes(String filePath){
        HashSet<ProductCode> productCodes = new HashSet<>();

        File file = new File(filePath);
        BufferedReader reader;
        String line;
        try{
            reader = new BufferedReader(new FileReader(file));
            while((line = reader.readLine()) != null){
                productCodes.add(new ProductCode(line));
            }
        } catch (IOException e){
            productCodes.clear();
        }

        return productCodes;
    }

    public HashSet<ProductCode> getCodes(){
        return productCodes;
    }

    private void change10ProductCodeProvided(){
        if(productCodes.size() < 10) return;

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
