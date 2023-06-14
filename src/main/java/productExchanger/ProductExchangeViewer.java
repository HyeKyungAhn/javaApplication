package productExchanger;

import java.util.Set;

public class ProductExchangeViewer {
    public static void print(Set<ProductCode> pCodeList){
        System.out.print("\n<제공된 상품코드>\n");
        for (ProductCode pCode : pCodeList) {
            if(pCode.isProvided()){
                System.out.println(pCode.getProductCode());
            }
        }
    }

    public static void print(PEResultStatus status){
        if(status == PEResultStatus.HELP){
            System.out.print("""
                |-----------------------------------------------------------|
                |                 SHARETREATS 상품교환 안내                   |
                |-----------------------------------------------------------|
                |아래와 같이 입력해주시기 바랍니다.                               |
                |                                                           |
                |교환가능여부 확인 :   CHECK [상품코드]                          |
                |도움말 보기 :        HELP                                    |
                |상품교환 하기 :      CLAIM [상점코드] [상품코드]                 |
                |종료 :              q                                       |
                |-----------------------------------------------------------|
                |- 상점코드는 영대소문자로 6글자만 입력하실 수 있습니다.             |
                |- 0~9, a~z, A~Z, SPACE 까지의 문자를 무작위로 입력할 수 있습니다. |
                |- 총 30자 까지 입력할 수 있습니다.                              |
                ------------------------------------------------------------|
                """);
        } else if (status == PEResultStatus.WRONG_INPUT){
            System.out.println("요청이 올바르지 않습니다. 형식에 맞게 다시 입력해주세요.");
            print(PEResultStatus.HELP);
        } else if(status == PEResultStatus.EXCHANGEABLE_CODE){
            System.out.println("교환가능한 제품코드입니다.");
        } else if(status == PEResultStatus.UNEXCHANGEABLE_CODE){
            System.out.println("교환 불가한 제품코드입니다. 코드를 다시 입력해주세요.");
        } else if(status == PEResultStatus.EXCHANGE_SUCCESS){
            System.out.print("교환가능한 제품코드입니다.\n교환에 성공했습니다.\n");
        } else if(status == PEResultStatus.EXCHANGE_FAIL){
            System.out.print("교환불가한 제품코드입니다.\n교환에 실패했습니다.\n");
        } else if (status == PEResultStatus.NO_PRODUCT_CODE) {
            System.out.println("교환가능한 제품코드어 서비스를 종료합니다.\n관리자에게 문의 바랍니다.");
        }
    }
}
