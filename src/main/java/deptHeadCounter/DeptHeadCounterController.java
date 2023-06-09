package deptHeadCounter;

import java.util.Map;
import java.util.Scanner;

public class DeptHeadCounterController implements Controller{
    DepartmentContainer container;
    DeptHeadCounterService service;

    @Override
    public void main() {
        DeptHeadCounterInputProcessor inputProcessor = new DeptHeadCounterInputProcessor();
        initialize();

        print(DHCOutputStatus.INFORMATION);

        Scanner s = new Scanner(System.in);

        while (true) {
            String input = s.nextLine();

            if (input.equals("q")) {
                break;
            }

            String[] dividedInput = inputProcessor.processInput(input);

            if (dividedInput == null) {
                print(DHCOutputStatus.WRONG_INPUT);
                continue;
            }

            print(executeCommand(dividedInput));
        }
    }

    @Override
    public void initialize() {
        container = new DepartmentContainer();
        service = new DeptHeadCounterService();
    }

    @Override
    public Map<String, Object> executeCommand(String[] dividedInput) {
        if(dividedInput.length==1&&dividedInput[0].equalsIgnoreCase("print")){
            service.calculateTopLevelDeptPersonnelSum(container);

        }
        return null;
    }

    private void print(Map<String, Object> totalHeadCountList) {
        totalHeadCountList.forEach((k,v) -> System.out.printf("%s, %d", k, (int)v));
    }

    private void print(DHCOutputStatus status){
        switch (status) {
            case INFORMATION -> System.out.println("""
                    <안내>
                    부서 인원 합계 알림이 서비스입니다.
                    이용하시려면 다음의 명령어를 입력해주세요.
                                            
                    부서 정보 입력 : [부서 이름] , [부서인원]
                    부서 관계 입력 : [상위부서 이름] > [하위부서 이름]
                    부서 정보 확인 : PRINT
                                            
                    - 부서 이릅에는 영문 대문자 9자까지 입력 가능합니다.
                    - 부서 인원은 0~1000명까지 숫자로 입력 가능합니다.
                    - 부서 인원 확인은 최상위 부서와 하위부서 내의 모든 직원수를 합한 수를 제공합니다.
                    """);
            case WRONG_INPUT -> System.out.println("""
                    잘못된 입력입니다. 다시 입력해주세요.
                    """);
        }
    }
}
