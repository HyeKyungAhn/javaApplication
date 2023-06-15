package productExchanger;
public class InputValidator {
    private static final int TOTAL_INPUT_LENGTH = 30;
    private static final int STORE_CODE_LENGTH = 6;
    private static final int NUMBER_OF_DIGIT = 9;

    public static boolean isValidInputFormat(String[] inputs) {
        boolean result;

        if(inputs.length == 0){ //only space
            return false;
        } else if(inputs.length == 1 && inputs[0].trim().equals("")){ //only enter
            return false;
        }

        String command = inputs[PEInput.getCommandIndex()];

        switch (command){
            case "check" : result = (inputs.length == PEInput.CHECK.getLength()
                    && isNumber(inputs[PEInput.CHECK.getProductCodeIndex()])); break;

            case "help" : result = (inputs.length == PEInput.HELP.getLength()); break;

            case "claim" : result = (inputs.length == PEInput.CLAIM.getLength()
                    && isEnglishLetter(inputs[PEInput.CLAIM.getStoreCodeIndex()])
                    && isNumber(inputs[PEInput.CLAIM.getProductCodeIndex()])); break;

            default : result = false;
        }

        return result;
    }

    public static boolean isValidInputString(String input) {
        return input.matches("^[a-zA-Z0-9\\s]{0,"+TOTAL_INPUT_LENGTH+"}$"); //0~9, a~z, A~Z, space
    }

    private static boolean isEnglishLetter(String storeCode){
        return (storeCode != null) && storeCode.matches("^[a-zA-Z]{"+STORE_CODE_LENGTH+"}$");
    }

    private static boolean isNumber(String productCode){
        return (productCode != null) && productCode.matches("^[0-9]{"+NUMBER_OF_DIGIT+"}$");
    }
}
