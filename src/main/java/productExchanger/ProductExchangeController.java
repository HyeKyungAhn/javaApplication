package productExchanger;

import java.util.Scanner;

public class ProductExchangeController implements Controller{
    private ProductCodeContainer p;

    public void main(){
        initialize();

        ProductExchangeViewer.print(PEResultStatus.HELP);
        ProductExchangeViewer.print(p.getProductCodes());

        Scanner s = new Scanner(System.in);

        while(true){
            String input = s.nextLine();

            if(input.equals("q")){
                break;
            }

            String[] dividedInput = InputProcessor.processInput(input);

            if(dividedInput == null){
                ProductExchangeViewer.print(PEResultStatus.WRONG_INPUT);
                continue;
            }

            ProductExchangeViewer.print(processCommand(dividedInput, p));
        }
    }

    public static PEResultStatus processCommand(String[] command, ProductCodeContainer p){
        PEResultStatus result;

        switch (command[PEInput.getCommandIndex()]) {
            case "check" -> result = ProductExchangeService.check(command, p);
            case "help" -> result = ProductExchangeService.help();
            case "claim" -> result = ProductExchangeService.claim(command, p);
            default -> result = PEResultStatus.WRONG_INPUT;
        }

        return result;
    }

    private void initialize(){
        p = new ProductCodeContainer();
    }
}
