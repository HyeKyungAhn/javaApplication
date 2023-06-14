package productExchanger;

public enum PEInput {
    HELP("help",1,-1,-1),
    CHECK("check",2, -1, 1),
    CLAIM("claim",3, 1, 2);

    private final String commandName;
    private final int length;
    private final int storeCodeIndex;
    private final int productCodeIndex;
    private static final int COMMEND_INDEX = 0;

    PEInput(String commandName, int length, int storeCodeIndex, int productCodeIndex){
        this.commandName = commandName;
        this.length = length;
        this.storeCodeIndex = storeCodeIndex;
        this.productCodeIndex = productCodeIndex;
    }

    public String getCommandName(){
        return commandName;
    }

    public int getLength(){
        return length;
    }

    public int getStoreCodeIndex(){
        return storeCodeIndex;
    }

    public int getProductCodeIndex(){
        return productCodeIndex;
    }

    public static int getCommandIndex(){
        return COMMEND_INDEX;
    }
}
