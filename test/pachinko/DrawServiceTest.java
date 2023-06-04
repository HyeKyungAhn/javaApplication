package pachinko;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.Assert.*;

public class DrawServiceTest {

    private DrawService getDrawService(){
        return new DrawService();
    }

    private User getUserWithWallet(int amount){
        return new User(new VirtualWallet((amount)));
    }

    private ProductContainer getContainer(){
        return new ProductContainer();
    }

    @Test
    public void testNotEnoughMoneyDontDraw(){
        //given
        DrawService drawService = getDrawService();
        User user = getUserWithWallet(1000);
        ProductContainer container = getContainer();

        //when
        List<Product> drawResult = drawService.draw(15, LocalDateTime.now(), container, user);
        //then
        assertNull(drawResult);
    }

    @Test
    public void testDontDrawExpiredProduct(){
        //given
        DrawService drawService = getDrawService();
        User user = getUserWithWallet(1000000);
        ProductContainer container = getContainer();
        DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
        boolean isExpiredProductExist = false;
        LocalDateTime dateTime;

        //when
        List<Product> drawResult = drawService.draw(10000, LocalDateTime.now(), container, user);

        //then
        for(Product p : drawResult){
            dateTime = LocalDateTime.from(formatter.parse(p.getExpirationDate()));
            if(dateTime.isBefore(LocalDateTime.now())){
                isExpiredProductExist = true;
            }
        }

        assertFalse(isExpiredProductExist);
    }

    @Test
    public void testBGradeProductDrawCount(){
        //given
        DrawService drawService = getDrawService();
        User user = getUserWithWallet(1000000);
        ProductContainer container = getContainer();

        int availableBGradeDrawCount = 3;
        int bGradeDrawCount = 0;

        //when
        List<Product> drawResult = drawService.draw(10000, LocalDateTime.now(), container, user);

        //then
        for(Product p : drawResult){
            if(p.getGrade().equals("B")){
                bGradeDrawCount++;
            }
        }

        assertEquals(availableBGradeDrawCount, bGradeDrawCount);
    }

    // 뽑기의 결과가 A, B, 등급의 상품 또는 꽝인가?
    @Test
    public void testDrawResultKind(){
        //given
        DrawService drawService = getDrawService();
        User user = getUserWithWallet(10000);
        ProductContainer container = getContainer();
        String grade;
        //when
        List<Product> drawResult = drawService.draw(100, LocalDateTime.now(), container, user);

        //then
        for(Product p : drawResult){
            grade = p.getGrade();
            assertTrue(grade.equals("A") || grade.equals("B") || grade.equals(""));
        }
    }

    @Test
    public void testNoStockLimit(){
        //given
        int balance = 1000000;
        DrawService drawService = getDrawService();
        User user = getUserWithWallet(balance);
        ProductContainer container = getContainer();

        //when
        List<Product> products = drawService.draw(balance/10, LocalDateTime.now(), container, user);

        //then
        for(Product p : products){
            if(p == null){
                assert false;
            }
        }
        assert true;
    }

    @Test
    public void testSelectedProductGrade(){
        //given
        DrawService drawService = getDrawService();
        User user = getUserWithWallet(1000000);
        ProductContainer container = getContainer();
        String grade;
        //when
        List<Product> products = drawService.draw(10000, LocalDateTime.now(), container, user);

        for(Product p : products){
            grade = p.getGrade();
            if(!grade.equals("")){ //꽝
                if(grade.equals("A") || grade.equals("B")){
                    continue;
                }
                continue;
            }
            assert false;
        }
        //then
    }
}