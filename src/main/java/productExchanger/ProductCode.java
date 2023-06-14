package productExchanger;

public class ProductCode {
    private String productCode;
    private String storeCode;
    private boolean isExchangeable;
    private boolean isProvided;

    ProductCode(){}

    ProductCode(String productCode){
        this.productCode = productCode;
        this.isExchangeable = true;
        this.isProvided = false;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setStoreCode(String storeCode) {
        this.storeCode = storeCode;
    }

    public boolean isExchangeable() {
        return isExchangeable;
    }

    public void setExchangeable(boolean exchangeable) {
        isExchangeable = exchangeable;
    }

    public boolean isProvided() { return isProvided; }

    public void setProvided(boolean provided) { isProvided = provided; }

    @Override
    public String toString() {
        return "ProductCode{" +
                "productCode='" + productCode + '\'' +
                ", storeCode='" + storeCode + '\'' +
                ", isExchangeable=" + isExchangeable +
                ", isProvided=" + isProvided +
                '}';
    }
}
