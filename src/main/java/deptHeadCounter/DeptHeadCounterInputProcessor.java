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
    public String[] divideInput(String input) {
        StringTokenizer tokenizer = new StringTokenizer(input, ",<>", true);
        String[] dividedInput = new String[tokenizer.countTokens()];
        int idx = 0;

        while (tokenizer.hasMoreTokens()) {
            dividedInput[idx] = tokenizer.nextToken().trim();
            idx++;
        }

        return dividedInput;
    }

    @Override
    public boolean verifyInputValue(String input) {
        return input.matches("^[A-Z*]{1,10}\\s*[,<>]\\s*[A-Z0-9*]{1,10}$");//0~9, a~z, A~Z, *, <, >, ',', space;
    }

    @Override
    public boolean verifyInputForm(String[] inputArr) {
        boolean result = false;
        String verifier = inputArr[1];
        String parentDept;
        String childDept;

        if (inputArr.length != 3) {
            return false;
        }

        if(verifier.equals(",")
                && isCapitalAlphabet(inputArr[0])
                && isNaturalNumber(inputArr[2])){
            result = true;
        }

        if(verifier.equals("<") || verifier.equals(">")){
            parentDept = verifier.equals("<") ? inputArr[2] : inputArr[0];
            childDept = verifier.equals("<") ? inputArr[0] : inputArr[2];

            if(isCapitalAlphabetOrAsterisk(parentDept)
                    && isCapitalAlphabet(childDept)) result = true;
        }

        return result;
    }

    private boolean isNaturalNumber(String str){
        return str.matches("^[1-9]\\d{1,10}$");
    }

    private boolean isCapitalAlphabet(String str){
        return str.matches("^[A-Z]{1,10}$");
    }

    private boolean isCapitalAlphabetOrAsterisk(String str){
        return str.matches("^\\*$|^[A-Z]{1,10}$");
    }
}
