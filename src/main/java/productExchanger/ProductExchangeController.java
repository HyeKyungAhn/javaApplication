package productExchanger;

import java.util.Scanner;

public class ProductExchangeController implements Controller{
    private ProductCodeContainer p;

    public void main(){
        initialize();

//        PEViewer.print(PEResultStatus.HELP);
//        PEViewer.print(p.getProductCodes());

        Scanner s = new Scanner(System.in);

        while(true){
            String input = s.nextLine();

            if(input.equals("q")){
                break;
            }

            String[] dividedInput = InputProcessor.processInput(input);

            if(dividedInput == null){
//                PEViewer.print(PEResultStatus.WRONG_INPUT);
                continue;
            }

//            PEViewer.print(CommandProcessor.processCommand(dividedInput, p));
        }
    }

    private void initialize(){
        p = ProductCodeContainer.getInstance();
    }
}
