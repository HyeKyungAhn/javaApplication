package pachinko;

import org.junit.Before;
import org.junit.Test;

import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.Assert.*;

public class DrawServiceTest {
    private DrawService service;
    private ProductContainer container;

    static final int PRICE_PER_DRAW = 100;
    static final int B_GRADE_DRAW_AVAILABLE_NUM = 3;
    static final double A_GRADE_DRAW_PROBABILITY = 0.9;

    private User getUserWithWallet(int amount){
        return new User(new VirtualWallet((amount)));
    }

    @Before
    public void setUp(){
        service = new DrawService();
        container = new ProductContainer();
    }

    @Test
    public void testDrawNotPossibleWithoutSufficientBalance(){
        //given
        int drawNum = 15;
        int balance = 1000; // not enough to draw
        User user = getUserWithWallet(balance);

        //when
        List<Product> drawResult = service.draw(drawNum, LocalDateTime.now(), container, user);

        //then
        assertNull(drawResult);
    }

    @Test
    public void testExpiredProductsNotIncludedInDrawResult(){
        //given
        int drawNum = 10000;
        User user = getUserWithWallet(drawNum * PRICE_PER_DRAW);

        DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
        LocalDateTime expirationDate;
        LocalDateTime drawDateTime = LocalDateTime.now();

        //when
        List<Product> drawResult = service.draw(drawNum, drawDateTime, container, user);

        //then
        for(Product p : drawResult){
            if(p.getExpirationDate().equals("")) continue;
            expirationDate = LocalDateTime.from(formatter.parse(p.getExpirationDate()));
            assertFalse("Expired products are included in the draw result", expirationDate.isBefore(drawDateTime));
        }
    }

    @Test
    public void testBGradeProductDrawCountNotOver3(){
        //given
        int drawNum = 10000;
        User user = getUserWithWallet(drawNum * PRICE_PER_DRAW);

        int bGradeDrawCount = 0;

        //when
        List<Product> drawResult = service.draw(drawNum, LocalDateTime.now(), container, user);

        //then
        for(Product p : drawResult){
            if(p.getGrade().equals("B")){
                bGradeDrawCount++;
            }
        }

        assertEquals(B_GRADE_DRAW_AVAILABLE_NUM, bGradeDrawCount);
    }

    @Test
    public void testNoStockLimit(){
        //given
        int drawNum = 100000;
        User user = getUserWithWallet(drawNum * PRICE_PER_DRAW);

        //when
        List<Product> products = service.draw(drawNum, LocalDateTime.now(), container, user);

        //then
        for(Product p : products){
            assert p != null;
        }

        assertEquals(drawNum, products.size());
    }

    @Test
    public void testGradeOfSelectedProductAOrB(){
        //given
        int drawNum = 10000;
        User user = getUserWithWallet(drawNum * PRICE_PER_DRAW);
        String grade;

        //when
        List<Product> products = service.draw(drawNum, LocalDateTime.now(), container, user);

        //then
        for(Product p : products){
            grade = p.getGrade();
            if(grade.equals("")) continue;

            assert grade.equals("A") || grade.equals("B");
        }
    }

    @Test
    public void testDrawResultHasOnlyThreeDistinctKinds(){
        //given
        int drawNum = 100;
        User user = getUserWithWallet(drawNum * PRICE_PER_DRAW);
        int aGradeCount = 0;
        int bGradeCount = 0;
        int loseDrawCount = 0;
        String grade;

        //when
        List<Product> drawResult = service.draw(100, LocalDateTime.now(), container, user);

        for (Product product : drawResult) {
            grade = product.getGrade();

            switch (grade) {
                case "A" -> aGradeCount++;
                case "B" -> {
                    bGradeCount++;
                    assertTrue(bGradeCount <= 3);
                }
                case "" ->  //lose draw
                        loseDrawCount++;
            }
        }

        //then
        assertEquals(drawNum, aGradeCount + bGradeCount + loseDrawCount);
    }

    @Test
    public void testProbabilitiesAndLimits() {
        //A, B 등급의 상품 모두 최소 1개의 유통기한 내의 제품이 있다는 전제 하에
        // given
        int drawNum = 100000;
        double significanceLevel = 0.01;
        User user = getUserWithWallet(drawNum * PRICE_PER_DRAW);

        int aGradeCount = 0;
        int bGradeCount = 0;
        int loseDrawCount = 0;
        String grade;

        // when
        List<Product> drawResult = service.draw(drawNum, LocalDateTime.now(), container, user);


        for (Product product : drawResult) {
            grade = product.getGrade();

            if (grade.equals("A")) {
                aGradeCount++;
                continue;
            } else if (grade.equals("B")) {
                bGradeCount++;
                assertTrue(bGradeCount<=3);
                continue;
            }

            assertEquals(3, bGradeCount);
            loseDrawCount++;
        }

        double aGradeProbability = (double) aGradeCount / drawNum;
        double loseDrawProbability = (double) loseDrawCount / drawNum;
        double bGradeAndLoseDrawProbability = (double) (bGradeCount + loseDrawCount) / drawNum;

        double expectedLoseDrawProbability = (1 - A_GRADE_DRAW_PROBABILITY) * (1 - (double)B_GRADE_DRAW_AVAILABLE_NUM / drawNum);

        // then
        //90%의 확률로 A등급의 상품이 뽑히는가?
        assertEquals(A_GRADE_DRAW_PROBABILITY, aGradeProbability, significanceLevel); // 0.001의 허용 오차 범위로 검증
        //A 등급 상품이 뽑히지 않으면 B상품이 뽑히는가?
        assertEquals(B_GRADE_DRAW_AVAILABLE_NUM,bGradeCount);
        //B 등급의 상품이 3번 이상이거나 유통기한 내의 B등급의 상품이 없으면 꽝을 뽑는가?
        assertEquals(expectedLoseDrawProbability, loseDrawProbability, significanceLevel); // 0.001의 허용 오차 범위로 검증

        //10%의 확률로 B등급의 상품을 뽑을 수 있는가?
        assertEquals(1-A_GRADE_DRAW_PROBABILITY, bGradeAndLoseDrawProbability, significanceLevel);
    }
}