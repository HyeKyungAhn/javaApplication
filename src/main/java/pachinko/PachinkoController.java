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

    @Override
    public void main() {
        initialize();

        PachinkoViewer.print(PachinkoPrintStatus.PRINT_INFORMATION);

        Scanner scanner = new Scanner(System.in);

        while (true) {
            String input = scanner.nextLine();

            if (!productContainer.haveAtLeastTwoNotExpiredProductsPerGrade(LocalDateTime.now())) {
                PachinkoViewer.print(PachinkoPrintStatus.NO_PRODUCT_AVAILABLE);
                System.exit(0);
            }

            if (input.equals("q")) {
                break;
            }

            String[] dividedInput = inputProcessor.processInput(input);

            if (dividedInput == null) {
                PachinkoViewer.print(PachinkoPrintStatus.PRINT_WRONG_INPUT);
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
            case "balance" : returnValue.put("balance", wallet.getBalance()); break;
            case "draw" : {
                int drawNum = Integer.parseInt(dividedInput[1]);
                returnValue.put("draw", drawService.draw(drawNum, LocalDateTime.now(), productContainer, user));
            } break;
            case "deposit" : {
                int depositAmount = Integer.parseInt(dividedInput[1]);
                returnValue.put("deposit", wallet.depositMoney(depositAmount));
            } break;
            default : returnValue.put("wrong",null);
        }

        return returnValue;
    }

    private void print(Map<String, Object> results){
        if (results.containsKey("balance")) {
            PachinkoViewer.printBalance((Integer) results.get("balance"));
        } else if (results.containsKey("draw")) {
            PachinkoViewer.printDrawResults((List<Product>) results.get("draw"));
        } else if (results.containsKey("deposit")) {
            PachinkoViewer.printDepositResult((Boolean) results.get("deposit"));
        } else if (results.containsKey("wrong")) {
            PachinkoViewer.print(PachinkoPrintStatus.PRINT_WRONG_INPUT);
        }
    }
}
