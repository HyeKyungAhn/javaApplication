package deptHeadCounter;

public interface InputProcessor {
    String[] processInput(String input);

    String[] divideInput(String input);

    boolean verifyInputValue(String input);

    boolean verifyInputForm(String[] inputArr);
}
