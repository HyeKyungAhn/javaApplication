package pachinko;

public class User {
    private VirtualWallet wallet;
    private int bGradeDrawCount;

    User(){}

    User(VirtualWallet wallet){
        this.wallet = wallet;
    }

    public VirtualWallet getWallet() {
        return wallet;
    }

    public int getbGradeDrawCount() {
        return bGradeDrawCount;
    }

    public void setbGradeDrawCount(int bGradeDrawCount) {
        this.bGradeDrawCount = bGradeDrawCount;
    }
}
