package pachinko;

public class VirtualWallet {
    private int balance;

    VirtualWallet(){}

    VirtualWallet(int balance){
        this.balance = balance;
    }

    public int getBalance() {
        return this.balance;
    }

    public boolean depositMoney(int amount) {
        if(checkBalanceLimit(amount)){
            this.balance += amount;
            return true;
        }

        return false;
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

    private boolean checkBalanceLimit(int amount){
        int totalMoney = amount + balance;
        //충전시 holdingAmount와 amount를 더한 값이 int의 범위보다 큰지 확인하는 메서드
        return totalMoney > 0 && totalMoney < 2100000000;
    }
}
