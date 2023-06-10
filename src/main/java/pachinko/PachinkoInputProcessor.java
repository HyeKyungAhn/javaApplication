package pachinko;

import java.util.Locale;

public class PachinkoInputProcessor implements InputProcessor{
    @Override
    public String[] processInput(String input) {
        String[] dividedInput = divideInput(input);

        if(verifyInputValue(input) && verifyInputForm(dividedInput)){
            return dividedInput;
        }
        return null;
    }

    @Override
    public String[] divideInput(String input) {
        return input.toLowerCase(Locale.ROOT).split(" ");
    }

    @Override
    public boolean verifyInputValue(String input) {
        return input.matches("^[a-zA-Z0-9\s]*$");//0~9, a~z, A~Z, space

    }

    @Override
    public boolean verifyInputForm(String[] inputArr) {
        boolean result;
        int inputArrLength = inputArr.length;

        try {
            if(inputArrLength == 0){ //only space
                result = false;
            } else if(inputArrLength == 1 && inputArr[0].trim().equals("")){ //only enter
                result = false;
            } else if(inputArr[0].equals("balance")){
                result = (inputArrLength == 1);
            } else if (inputArr[0].equals("draw") || inputArr[0].equals("deposit")) {
                result = (inputArrLength == 2 && isNaturalNumber(inputArr[1]) && isInIntegerLimit(inputArr[1]));
            } else {
                result = false;
            }
        } catch (Exception e) {
            result = false;
        }

        return result;
    }

    private boolean isNaturalNumber(String str) {
        return str.matches("^[1-9]\\d*$");
    }

    private boolean isInIntegerLimit(String str){
        long numValue = Long.parseLong(str);
        return numValue < Integer.MAX_VALUE;
    }
}
