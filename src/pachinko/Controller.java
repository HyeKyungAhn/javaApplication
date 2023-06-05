package pachinko;

import java.util.Map;

public interface Controller {
    void main();

    void initialize();

    Map<String, Object> executeCommand(String[] dividedInput);
}
