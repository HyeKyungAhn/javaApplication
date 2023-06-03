public class VirtualWallet {
    private int balance;

    VirtualWallet(){}

    VirtualWallet(int balance){
        this.balance = balance;
    }

    public int getBalance() {
        return this.balance;
    }

    public boolean deductBalance(int drawNum){
        int costForDraw = drawNum * 100;
        if(canDeduct(costForDraw)){
            balance -= costForDraw;
            return true;
        }

        return false;
    }

    private boolean canDeduct(int amount){
        return amount <= balance;
    }
}
