package deptHeadCounter;

import java.util.Arrays;
import java.util.Locale;
import java.util.StringTokenizer;

public class DeptHeadCounterInputProcessor implements InputProcessor{

    @Override
    public String[] processInput(String input) {
        String[] dividedInput = divideInput(input);

        if(verifyInputValue(input) && verifyInputForm(dividedInput)){
            return dividedInput;
        }
        return null;
    }

    @Override
    public String[] divideInput(String input) {//,<>가 없으면?
        StringTokenizer tokenizer = new StringTokenizer(input, ",<>", true);
        String[] dividedInput = new String[tokenizer.countTokens()];
        int idx = 0;

        while (tokenizer.hasMoreTokens()) {
            dividedInput[idx] = tokenizer.nextToken();
            idx++;
        }

        return dividedInput;
    }

    @Override
    public boolean verifyInputValue(String input) {
        return input.matches("^[a-zA-Z*]{1,10}\\s*[,<>]\\s*[a-zA-Z0-9*]{1,10}$");//0~9, a~z, A~Z, *, <, >, ',', space;
    }

    @Override
    public boolean verifyInputForm(String[] inputArr) {
        Arrays.stream(inputArr).forEach(i -> i = i.trim());
        boolean result = true;
        String verifier = inputArr[1];

        if (inputArr.length != 3) {
            return false;
        }

        if(verifier.equals(",")){
            if(inputArr[0].matches("^[^a-zA-Z]{1,10}$")){
                result = false;
            } else if (inputArr[2].matches("^[^0-9]{1,10}$")) {
                result = false;
            }
        } else if(verifier.equals("<") && inputArr[0].contains("^[^0-9]{1,10}$")){
            result = false;
        } else if(verifier.equals(">") && inputArr[2].contains("*")){
            result = false;
        }

        return result;
    }

    private boolean isNaturalNumber(String str){
        return str.matches("^[1-9]\\d*$");
    }
}
