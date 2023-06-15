package pachinko;

import java.util.List;

public class PachinkoViewer {

    public static void printDrawResults(List<Product> products) {
        if (products == null) {
            System.out.println("가상지갑의 잔액이 부족합니다. 충전 후 다시 시도해주세요.");
            return;
        }

        for (Product p : products) {
            String type = p.getType();
            String grade = p.getGrade();

            if(grade.equals("")){
                System.out.println("꽝! 다음기회에!");
            } else if (grade.equals("A")) {
                System.out.printf("%s 상품이 당첨되었습니다.\n", type);
            } else {
                System.out.printf("축하합니다! %s 상품이 당첨되었습니다! \n", type);
            }
        }
    }

    public static void printBalance(int balance) {
        System.out.printf("현재 잔액은 %d 입니다.\n", balance);
    }

    public static void printDepositResult(boolean isDepositSuccess) {
        if (isDepositSuccess) {
            System.out.println("충전 완료되었습니다.");
            return;
        }

        System.out.println("충전할 수 없는 입력 형식이거나, 충전 가능한 범위를 초과하였습니다\n"
                + "\n"
                + "돈 충전입력형식 : DEPOSIT [원하는 충전 액수]\n"
                + "- 원하는 충전 액수에는 자연수만 입력 가능합니다.\n"
                + "- 충전하여 지갑에 보유할 수 있는 총 액수는 21억입니다.\n");
    }

    public static void print(PachinkoPrintStatus status){
        switch (status){
            case PRINT_INFORMATION : System.out.println("<안내>\n"
                    + "SHARETREATS의 빠칭코 상품 뽑기 서비스입니다\n"
                    + "빠칭코 서비스를 이용하시려면 다음의 명령어를 입력해주세요\n"
                    + "보유금액 확인 : BALANCE\n"
                    + "뽑기 : DRAW [원하는 뽑기 횟수]\n"
                    + "돈충전 : DEPOSIT [원하는 충전 액수]\n"
                    + "서비스 종료 : q\n"
                    + "\n"
                    + "- 뽑기 1회에 100이 차감됩니다\n"
                    + "- 원하는 충전 액수에는 자연수만 입력 가능합니다.\n"
                    + "- 충전하여 지갑에 보유할 수 있는 총 액수는 21억입니다\n"); break;
            case PRINT_WRONG_INPUT : System.out.println("잘못된 입력입니다. 다시 입력해주세요.\n"
                    + "\n"
                    + "<명령어 형식>\n"
                    + "보유금액 확인 : BALANCE\n"
                    + "뽑기 : DRAW [원하는 뽑기 횟수]\n"
                    + "돈충전 : DEPOSIT [원하는 충전 액수]\n"
                    + "서비스 종료 : q\n"
                    + "\n"
                    + "- 원하는 충전 액수에는 자연수만 입력 가능합니다.\n"
                    + "- 충전하여 지갑에 보유할 수 있는 총 액수는 21억입니다\n"); break;
            case NO_PRODUCT_AVAILABLE : System.out.println("현재 뽑기 가능한 상품이 부족합니다. 뽑기를 종료합니다."); break;
        }
    }
}
