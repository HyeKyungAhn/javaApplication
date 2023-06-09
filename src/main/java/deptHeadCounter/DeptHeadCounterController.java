package deptHeadCounter;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Scanner;

public class DeptHeadCounterController implements Controller{
    private DepartmentContainer container;
    private DeptHeadCounterService service;
    private Scanner scanner;

    @Override
    public void main() {
        initialize();

        DeptHeadCounterViewer.printInfo(DHCOutputStatus.INFORMATION);

        scanner = getInputScanner();

        processCommand();

        scanner.close();
    }

    private void initialize() {
        container = new DepartmentContainer();
        service = new DeptHeadCounterService();
    }

    private Scanner getInputScanner() {
        try {
            File inputFile = new File(String.valueOf(Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "dhcInputs.txt")));
            return new Scanner(inputFile);
        } catch (FileNotFoundException e) {
            System.out.println("개발자 임의 입력값 파일을 찾을 수 없습니다. 수동으로 전환합니다.");
            return new Scanner(System.in);
        }
    }

    private String getNextInput() {
        if (scanner.hasNextLine()) {
            String nextLine = scanner.nextLine();
            System.out.println(nextLine);
            return nextLine;
        } else {
            System.out.println("개발자 임의 입력값 입력이 끝났습니다. 수동으로 전환합니다.");
            scanner.close();
            scanner = new Scanner(System.in);
            return scanner.nextLine();
        }

    }

    private void processCommand(){
        DeptHeadCounterInputProcessor inputProcessor = new DeptHeadCounterInputProcessor();

        while (true) {
            String input = getNextInput();

            if (input.equals("q")) {
                break;
            }

            String[] dividedInput = inputProcessor.processInput(input);

            if (dividedInput == null) {
                DeptHeadCounterViewer.printInfo(DHCOutputStatus.WRONG_INPUT);
                continue;
            }

            DeptHeadCounterViewer.processOutput(executeCommand(dividedInput));
            DeptHeadCounterViewer.printTotalHeadCount(service.calculateTopLevelDeptPersonnelSum(container));
        }
    }

    private Map<String, Object> executeCommand(String[] dividedInput) {
        String verifier = dividedInput[1];

        if(verifier.equals(",")){
            return service.saveDeptPersonnelInfo(container, dividedInput);
        } else if(verifier.equals("<")||verifier.equals(">")){
            return service.updateDeptStructure(container, dividedInput);
        }
        return null;
    }
}
