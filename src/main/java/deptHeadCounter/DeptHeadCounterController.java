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

        printInfo(DHCOutputStatus.INFORMATION);

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
            System.out.println(scanner.hasNextLine());
        } else {
            System.out.println("개발자 임의 입력값 입력이 끝났습니다. 수동으로 전환합니다.");
            scanner.close();
            scanner = new Scanner(System.in);
        }
        return scanner.nextLine();
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
                printInfo(DHCOutputStatus.WRONG_INPUT);
                continue;
            }

            processOutput(executeCommand(dividedInput));
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

    private void processOutput(Map<String, Object> result) {
        if(result == null){
            printInfo(DHCOutputStatus.WRONG_INPUT);
        } else if(result.containsKey("saveResult")){
            printInfo((DHCOutputStatus) result.get("saveResult"));
        } else if(result.containsKey("updateResult")){
            printInfo((DHCOutputStatus) result.get("updateResult"));

            if (result.get("updateResult").equals(DHCOutputStatus.UPDATE_DEPT_COMPOSITION_SUCCESS)) {
                printTotalHeadCount(service.calculateTopLevelDeptPersonnelSum(container));
            }
        } else {
            printInfo(DHCOutputStatus.WRONG_INPUT);
        }
    }

    private void printTotalHeadCount(Map<String,Integer> results){
        System.out.println("\n<상위부서별 부서 인원 합계>");
        results.forEach((k,v) -> System.out.printf("%s, %d \n", k, v));
        System.out.println();
    }

    private void printInfo(DHCOutputStatus status){
        switch (status) {
            case INFORMATION -> {
                System.out.println("""
                    <안내>
                    부서 인원 합계 알림이 서비스입니다.
                    이용하시려면 다음의 명령어를 입력해주세요.""");

                    printInfo(DHCOutputStatus.FORM_INFORMATION);
            }
            case WRONG_INPUT -> {
                System.out.println("잘못된 입력입니다. 다시 입력해주세요.");
                printInfo(DHCOutputStatus.FORM_INFORMATION);
            }
            case SAVE_PERSONNEL_INFO_SUCCESS -> System.out.println("부서 정보가 정상적으로 입력되었습니다.");
            case SAVE_PERSONNEL_INFO_FAIL -> {
                System.out.println("부서 정보 저장이 실패했습니다. 입력 형식을 확인해주세요.");
                printInfo(DHCOutputStatus.FORM_INFORMATION);
            }
            case UPDATE_DEPT_COMPOSITION_SUCCESS -> System.out.println("부서 관계가 정상적으로 저장되었습니다.");
            case UPDATE_FAIL_DEPT_NOT_EXIST -> System.out.println("부서 관계 입력이 실패하였습니다. 존재하지 않는 부서입니다.");
            case UPDATE_FAIL_PARENT_NOT_ASSIGNED -> System.out.println("부서 관계 입력이 실패하였습니다. \n" +
                    "상위 부서가 조직도 내에 존재하지 않습니다.");
            case UPDATE_FAIL_ALREADY_HAVE_PARENT -> System.out.println("부서 관계 입력이 실패하였습니다. \n" +
                    "하위부서가 다른 상위부서에 속해있습니다.");
            case UPDATE_FAIL_CANNOT_ADD_ITSELF_AS_CHILD -> System.out.println("부서 관계 입력이 실패하였습니다. \n" +
                    "부서 관계를 다시 확인해주세요.");
            case FORM_INFORMATION -> System.out.println("""
            
                    부서 정보 입력 : [부서 이름] , [부서인원]
                    부서 관계 입력 : [상위부서 이름] > [하위부서 이름]
                    
                    - 부서 이릅에는 영문 대문자 9자까지 입력 가능합니다.
                    - 부서 인원은 0~1000명까지 0과 자연수로 입력 가능합니다.
                    - 부서 관계 입력 전, 두 부서의 부서 정보를 입력해야합니다.
                    - 저장된 부서 이름과 동일한 부서 정보를 입력시, 부서 인원의 수가 갱신됩니다.
                    - 최상위 부서 입력은 [상위부서 이름]에 * 를, [하위부서 이름]에는 최상위 부서 이름을 입력합니다
                    - 부서 관계 입력 시 상위부서는 반드시 최상위 부서 또는 그 하위 부서에 입력되어있어야 합니다.
                    - 하위부서에 이미 상위부서가 입력되어있다면 변경할 수 없습니다.
                    - 부서 인원 확인은 최상위 부서와 하위부서 내의 모든 직원수를 합한 수를 제공합니다.
                    """);
        }
    }
}
