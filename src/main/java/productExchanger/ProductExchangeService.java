package productExchanger;

public class ProductExchangeService {

    public static PEResultStatus check(String[] command, ProductCodeContainer p){
        String productCode = command[PEInput.CHECK.getProductCodeIndex()];

        if(p.isExchangeable(productCode)){
            return PEResultStatus.EXCHANGEABLE_CODE;
        }
        return PEResultStatus.UNEXCHANGEABLE_CODE;
    }

    public static PEResultStatus claim(String[] command, ProductCodeContainer p){
        String productCode = command[PEInput.CLAIM.getProductCodeIndex()];

        if(p.isExchangeable(productCode)){
            return p.exchange(command);
        }

        return PEResultStatus.EXCHANGE_FAIL;
    }

    public static PEResultStatus help(){
        return PEResultStatus.HELP;
    }
}
