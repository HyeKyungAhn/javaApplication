package productExchanger;

import java.util.Arrays;
import java.util.Locale;

public class InputProcessor {
    public static String[] processInput(String input) {
        if(!InputValidator.isValidInputString(input)){
            return null;
        }

        String[] dividedInput = divideInput(input);

        return InputValidator.isValidInputFormat(dividedInput) ? dividedInput : null;
    }

    private static String[] divideInput(String input) {
        String[] inputs = input.toLowerCase(Locale.ROOT).split(" ");
        String command = inputs[PEInput.getCommandIndex()];
        String[] result;


        if(command.equals(PEInput.CHECK.getCommandName())){
            appendProductCode(inputs, PEInput.CHECK.getLength());
            result = Arrays.copyOfRange(inputs,0,PEInput.CHECK.getLength());
        } else if(command.equals(PEInput.CLAIM.getCommandName())){
            appendProductCode(inputs, PEInput.CLAIM.getLength());
            result = Arrays.copyOfRange(inputs,0,PEInput.CLAIM.getLength());
        } else {
            result = inputs;
        }

        return result;
    }

    private static void appendProductCode(String[] inputs, int num) {
        if(inputs.length > num){
            for(int i=num; i<inputs.length; i++){
                inputs[num-1] += inputs[i];
            }
        }
    }
}
