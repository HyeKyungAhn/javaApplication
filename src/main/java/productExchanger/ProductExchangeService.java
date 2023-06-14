package productExchanger;

public class ProductExchangeService {
    public static PEResultStatus processCommand(String[] command, ProductCodeContainer p){
        PEResultStatus result;

        switch (command[PEInput.getCommandIndex()]) {
            case "check" -> result = check(command, p);
            case "help" -> result = PEResultStatus.HELP;
//            case "claim" -> result = claim(command, p);
            default -> result = PEResultStatus.WRONG_INPUT;
        }

        return result;
    }

    public static PEResultStatus check(String[] command, ProductCodeContainer p){
        String productCode = command[PEInput.CHECK.getProductCodeIndex()];

        if(p.isExchangeable(productCode)){
            return PEResultStatus.EXCHANGEABLE_CODE;
        }
        return PEResultStatus.UNEXCHANGEABLE_CODE;
    }
}
