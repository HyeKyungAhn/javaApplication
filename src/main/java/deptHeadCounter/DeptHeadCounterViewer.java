package deptHeadCounter;

import java.util.Map;

public class DeptHeadCounterViewer {
    protected static void processOutput(Map<String, Object> result) {
        if(result == null){
            printInfo(DHCOutputStatus.WRONG_INPUT);
        } else if(result.containsKey("saveResult")){
            printInfo((DHCOutputStatus) result.get("saveResult"));
        } else if(result.containsKey("updateResult")){
            printInfo((DHCOutputStatus) result.get("updateResult"));
        } else {
            printInfo(DHCOutputStatus.WRONG_INPUT);
        }
    }

    protected static void printTotalHeadCount(Map<String,Integer> results){
        System.out.println("\n<상위부서별 부서 인원 합계>");
        if (results.size() == 0) {
            System.out.println("없음\n");
            return;
        }
        results.forEach((k,v) -> System.out.printf("%s, %d \n", k, v));
        System.out.println();
    }

    protected static void printInfo(DHCOutputStatus status){
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
                    서비스 종료 : q
                    
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
