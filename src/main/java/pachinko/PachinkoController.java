package pachinko;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class PachinkoController implements Controller{
    private User user;
    private VirtualWallet wallet;
    private ProductContainer productContainer;
    private DrawService drawService;
    private PachinkoInputProcessor inputProcessor;

    private static final int PRINT_INFORMATION = 1;
    private static final int PRINT_WRONG_INPUT = 2;
    private static final int NO_PRODUCT_AVAILABLE = 3;

    @Override
    public void main() {
        initialize();

        print(PRINT_INFORMATION);

        Scanner scanner = new Scanner(System.in);

        while (true) {
            String input = scanner.nextLine();

            if (productContainer.haveAtLeastTwoNotExpiredProductsPerGrade(LocalDateTime.now())) {
                print(NO_PRODUCT_AVAILABLE);
                System.exit(0);
            }

            if (input.equals("q")) {
                break;
            }

            String[] dividedInput = inputProcessor.processInput(input);

            if (dividedInput == null) {
                print(PRINT_WRONG_INPUT);
                continue;
            }

            print(executeCommand(dividedInput));
        }

        scanner.close();
    }

    private void initialize() {
        int initialDeposit = 10000;
        wallet = new VirtualWallet(initialDeposit);
        user = new User(wallet);
        productContainer = new ProductContainer();
        drawService = new DrawService();
        inputProcessor = new PachinkoInputProcessor();
    }

    private Map<String, Object> executeCommand(String[] dividedInput) {
        Map<String, Object> returnValue = new HashMap<>();
        String command = dividedInput[0];

        switch (command) {
            case "balance" -> returnValue.put("balance", wallet.getBalance());
            case "draw" -> {
                int drawNum = Integer.parseInt(dividedInput[1]);
                returnValue.put("draw", drawService.draw(drawNum, LocalDateTime.now(), productContainer, user));
            }
            case "deposit" -> {
                int depositAmount = Integer.parseInt(dividedInput[1]);
                returnValue.put("deposit", wallet.depositMoney(depositAmount));
            }
            default -> returnValue.put("wrong",null);
        }

        return returnValue;
    }

    private void print(Map<String, Object> results){
        if (results.containsKey("balance")) {
            printBalance((Integer) results.get("balance"));
        } else if (results.containsKey("draw")) {
            printDrawResults((List<Product>) results.get("draw"));
        } else if (results.containsKey("deposit")) {
            printDepositResult((Boolean) results.get("deposit"));
        } else if (results.containsKey("wrong")) {
            print(PRINT_WRONG_INPUT);
        }
    }

    private void printDrawResults(List<Product> products) {
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

    private void printBalance(int balance) {
        System.out.printf("현재 잔액은 %d 입니다.\n", balance);
    }

    private void printDepositResult(boolean isDepositSuccess) {
        if (isDepositSuccess) {
            System.out.println("충전 완료되었습니다.");
            return;
        }

        System.out.println("""
                충전할 수 없는 입력 형식이거나, 충전 가능한 범위를 초과하였습니다
                
                돈 충전입력형식 : DEPOSIT [원하는 충전 액수]
                - 원하는 충전 액수에는 자연수만 입력 가능합니다.
                - 충전하여 지갑에 보유할 수 있는 총 액수는 21억입니다.""");
    }

    private void print(int status){
        if (status == PRINT_INFORMATION) {
            System.out.println("""
                    <안내>
                    SHARETREATS의 빠칭코 상품 뽑기 서비스입니다
                    빠칭코 서비스를 이용하시려면 다음의 명령어를 입력해주세요
                    보유금액 확인 : BALANCE
                    뽑기 : DRAW [원하는 뽑기 횟수]
                    돈충전 : DEPOSIT [원하는 충전 액수]
                    서비스 종료 : q
                    
                    - 뽑기 1회에 100이 차감됩니다
                    - 원하는 충전 액수에는 자연수만 입력 가능합니다.
                    - 충전하여 지갑에 보유할 수 있는 총 액수는 21억입니다
                    """);
        } else if (status == PRINT_WRONG_INPUT) {
            System.out.println("""
                    잘못된 입력입니다. 다시 입력해주세요.
                    
                    <명령어 형식>
                    보유금액 확인 : BALANCE
                    뽑기 : DRAW [원하는 뽑기 횟수]
                    돈충전 : DEPOSIT [원하는 충전 액수]
                    서비스 종료 : q
                    
                    - 원하는 충전 액수에는 자연수만 입력 가능합니다.
                    - 충전하여 지갑에 보유할 수 있는 총 액수는 21억입니다
                    """);
        } else if (status == NO_PRODUCT_AVAILABLE) {
            System.out.println("현재 뽑기 가능한 상품이 부족합니다. 뽑기를 종료합니다.");
        }
    }
}
