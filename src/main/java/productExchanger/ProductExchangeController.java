package productExchanger;

import java.util.Scanner;
import java.util.Set;

public class ProductExchangeController implements Controller{
    private ProductCodeContainer p;

    public void main(){
        initialize();

        ProductExchangeViewer.print(PEResultStatus.HELP);

        if(checkProductCodeEmpty()){
            ProductExchangeViewer.print(PEResultStatus.NO_PRODUCT_CODE);
            System.exit(0);
        }

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

    protected PEResultStatus processCommand(String[] command, ProductCodeContainer p){
        PEResultStatus result;

        switch (command[PEInput.getCommandIndex()]) {
            case "check" : result = ProductExchangeService.check(command, p); break;
            case "help" : result = ProductExchangeService.help(); break;
            case "claim" : result = ProductExchangeService.claim(command, p); break;
            default : result = PEResultStatus.WRONG_INPUT;
        }

        return result;
    }

    private void initialize(){
        p = new ProductCodeContainer();
    }

    private boolean checkProductCodeEmpty() {
        Set<ProductCode> productCodes = p.getProductCodes();
        return productCodes.size() < 10;
    }
}
