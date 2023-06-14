package productExchanger;

import org.junit.Test;

import java.util.Locale;

import static org.junit.Assert.*;

public class ProductExchangeInputProcessorTest {
    //0~9, a~z, A~Z, SPACE 외의 문자 입력 불가 && 30문자 이상 입력불가?
    @Test
    public void FailToInputIFUserInputNotValidInputString(){
        //given
        String correctInput = "claim AACCdD 123123123";

        String faultyInput1 = "claim AACCdD 12312312+";
        String faultyInput2 = "claim AACCdD                             ";
        String faultyInput3 = "check - 123123123";
        String faultyInput4 = "claim AACCdD (123123123)";

        //when
        boolean correctInputResult = InputValidator.isValidInputString(correctInput);

        boolean faultyInputResult1 = InputValidator.isValidInputString(faultyInput1);
        boolean faultyInputResult2 = InputValidator.isValidInputString(faultyInput2);
        boolean faultyInputResult3 = InputValidator.isValidInputString(faultyInput3);
        boolean faultyInputResult4 = InputValidator.isValidInputString(faultyInput4);

        //then
        assertTrue(correctInputResult);

        assertFalse(faultyInputResult1);
        assertFalse(faultyInputResult2);
        assertFalse(faultyInputResult3);
        assertFalse(faultyInputResult4);
    }

    //명령어와 맞지 않는 형식 입력 불가?
    @Test
    public void failToInputStringWithWrongFormat(){
        //given
        String faultyInput1 = "claim 123 123 123";
        String faultyInput2 = "claim AASSDd";
        String faultyInput3 = "claim";
        String faultyInput4 = "claim           ";
        String faultyInput5 = "check           ";
        String faultyInput6 = "check AASSDd 123123123";
        String faultyInput7 = "check AASSDd";
        String faultyInput8 = "check";
        String faultyInput9 = "help AASSDd 123 123 123";
        String faultyInput10 = "help AASSDd";
        String faultyInput11 = "help 123123123";
        String faultyInput12 = "";
        String faultyInput13 = "\n";

        //when
        String[] result1 = InputProcessor.processInput(faultyInput1);
        String[] result2 = InputProcessor.processInput(faultyInput2);
        String[] result3 = InputProcessor.processInput(faultyInput3);
        String[] result4 = InputProcessor.processInput(faultyInput4);
        String[] result5 = InputProcessor.processInput(faultyInput5);
        String[] result6 = InputProcessor.processInput(faultyInput6);
        String[] result7 = InputProcessor.processInput(faultyInput7);
        String[] result8 = InputProcessor.processInput(faultyInput8);
        String[] result9 = InputProcessor.processInput(faultyInput9);
        String[] result10 = InputProcessor.processInput(faultyInput10);
        String[] result11 = InputProcessor.processInput(faultyInput11);
        String[] result12 = InputProcessor.processInput(faultyInput12);
        String[] result13 = InputProcessor.processInput(faultyInput13);

        //then
        assertNull(result1);
        assertNull(result2);
        assertNull(result3);
        assertNull(result4);
        assertNull(result5);
        assertNull(result6);
        assertNull(result7);
        assertNull(result8);
        assertNull(result9);
        assertNull(result10);
        assertNull(result11);
        assertNull(result12);
        assertNull(result13);
    }

    //자연수로 9문자까지만 입력 가능?
    @Test
    public void failToInputInvalidProductCode(){
        //given
        String faultyInput1 = "claim ssddff 123123123456";
        String faultyInput2 = "claim ssddff 123123aaa";
        String faultyInput3 = "claim ssddff 123123@@@";
        String faultyInput4 = "claim ssddff  ";
        String faultyInput5 = "check 123123123456";
        String faultyInput6 = "check 123123aaa";
        String faultyInput7 = "check 123123@@@";
        String faultyInput8 = "check  ";

        //when
        String[] result1 = InputProcessor.processInput(faultyInput1);
        String[] result2 = InputProcessor.processInput(faultyInput2);
        String[] result3 = InputProcessor.processInput(faultyInput3);
        String[] result4 = InputProcessor.processInput(faultyInput4);
        String[] result5 = InputProcessor.processInput(faultyInput5);
        String[] result6 = InputProcessor.processInput(faultyInput6);
        String[] result7 = InputProcessor.processInput(faultyInput7);
        String[] result8 = InputProcessor.processInput(faultyInput8);

        //then
        assertNull(result1);
        assertNull(result2);
        assertNull(result3);
        assertNull(result4);
        assertNull(result5);
        assertNull(result6);
        assertNull(result7);
        assertNull(result8);
    }

    //상품코드 가운데 space가 있어도 정상 처리?
    @Test
    public void successToInputValidProductCodeWithSpace(){
        //given
        String correctInput1 = "claim aassSA 123 123 123";
        String correctInput2 = "check 123 123 123";
        String correctInput3 = "help      ";

        //when
        String[] result1 = InputProcessor.processInput(correctInput1);
        String[] result2 = InputProcessor.processInput(correctInput2);
        String[] result3 = InputProcessor.processInput(correctInput3);

        //then
        assertNotNull(result1);
        assertEquals("claim",  result1[0]);
        assertEquals("aassSA".toLowerCase(Locale.ROOT),  result1[1]);
        assertEquals("123123123",  result1[2]);

        assertNotNull(result2);
        assertEquals("check",  result2[0]);
        assertEquals("123123123",  result2[1]);

        assertNotNull(result3);
        assertEquals("help",result3[0]);
    }

    //A~Z,a~z 대소영문자로 6글자만 사용 가능?
    @Test
    public void failToInputInvalidStoreCode(){
        //given
        String correctInput1 = "claim aassSA 123 123 123";
        String faultyInput1 = "claim 11111 1231 23 123";
        String faultyInput2 = "claim aa     1231 23 123";
        String faultyInput3 = "claim aa1111 1231 23 123";

        //when
        String[] result1 = InputProcessor.processInput(correctInput1);

        String[] result3 = InputProcessor.processInput(faultyInput1);
        String[] result4 = InputProcessor.processInput(faultyInput2);
        String[] result5 = InputProcessor.processInput(faultyInput3);

        //then
        assert result1 != null;
        assertEquals("claim", result1[0]);
        assertEquals("aassSA".toLowerCase(Locale.ROOT), result1[1]);
        assertEquals("123123123", result1[2]);

        assertNull(result3);
        assertNull(result4);
        assertNull(result5);
    }
}