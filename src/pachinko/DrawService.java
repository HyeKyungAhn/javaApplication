package pachinko;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class DrawService {
    private static final double A_PRIZE_PROBABILITY = 0.9;
    private static final double B_GRADE_DRAW_AVAILABLE_NUM = 3;

    public List<Product> draw(int drawNum, LocalDateTime dateTime, ProductContainer container, User user) {
        List<Product> earnedProducts = new ArrayList<>(drawNum);
        int drawBGradeCount = user.getbGradeDrawCount();

        if(!user.getWallet().deductBalance(drawNum)){
            return null;
        }

        Random random = new Random();

        for(int i=0; i<drawNum; i++){
            Product temp;
            if (random.nextDouble() < A_PRIZE_PROBABILITY) {
                temp = container.getRandomPrizeByGrade("A", dateTime);
            } else if(drawBGradeCount < B_GRADE_DRAW_AVAILABLE_NUM){
                    temp = container.getRandomPrizeByGrade("B", dateTime);

                    if(temp != null){
                        drawBGradeCount++;
                    }
            } else {
                temp = null;
            }

            earnedProducts.add(Objects.requireNonNullElseGet(temp, () -> new Product("꽝", "", "")));
        }

        user.setbGradeDrawCount(drawBGradeCount);

        return earnedProducts;
    }
}
