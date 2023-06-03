package pachinko;

import org.junit.Test;

import static org.junit.Assert.*;

public class VirtualWalletTest {

    @Test
    public void chargeMoneyTest() {
        //given
        int initialBalance = 10000;
        VirtualWallet virtualWallet = new VirtualWallet(initialBalance);

        //when
        int amountOfMoneyForCharge = 10000;

        virtualWallet.depositMoney(amountOfMoneyForCharge);
        int balance = virtualWallet.getBalance();

        //then
        assertEquals(initialBalance + amountOfMoneyForCharge, balance);
    }

    @Test
    public void chargeMoneyLimitTest(){
        //given
        int initialBalance = 2000000000;
        VirtualWallet virtualWallet1 = new VirtualWallet(initialBalance);
        VirtualWallet virtualWallet2 = new VirtualWallet(initialBalance);

        //when
        int faultyInput = 1000000000;

        int correctInput = 10000;

        boolean resultWithFaultyInput = virtualWallet1.depositMoney(faultyInput);
        boolean resultWithCorrectInput = virtualWallet2.depositMoney(correctInput);

        //then
        assertFalse(resultWithFaultyInput);
        assertTrue(resultWithCorrectInput);
    }

    @Test
    public void deduct100PerDrawTest() {
        //given
        int drawNum = 10;
        int costPerDraw = 100;
        int balance = 10000;

        VirtualWallet virtualWallet = new VirtualWallet(balance);

        //when
        boolean canDeduct = virtualWallet.deductBalance(drawNum);
        int deductedBalance = virtualWallet.getBalance();

        //then
        assertTrue(canDeduct);

        assertEquals(balance - (drawNum * costPerDraw), deductedBalance);
    }

    @Test
    public void canDeductBalanceTest() {
        //given
        int balance = 10000;
        VirtualWallet virtualWallet1 = new VirtualWallet(balance);
        VirtualWallet virtualWallet2 = new VirtualWallet(balance);

        //when
        int correctInput = 10;
        int faultyInput = 150;

        boolean resultWithCorrectInput = virtualWallet1.deductBalance(correctInput);
        boolean resultWithFaultyInput = virtualWallet2.deductBalance(faultyInput);

        int deductedBalance = virtualWallet1.getBalance();
        int notDeductedBalance = virtualWallet2.getBalance();

        //then
        assertTrue(resultWithCorrectInput);
        assertFalse(resultWithFaultyInput);

        assertEquals(9000, deductedBalance);
        assertEquals(10000, notDeductedBalance);
    }
}