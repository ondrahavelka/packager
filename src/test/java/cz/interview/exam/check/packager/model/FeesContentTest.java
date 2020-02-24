package cz.interview.exam.check.packager.model;

import cz.interview.exam.check.packager.exceptions.InitialLoadException;
import cz.interview.exam.check.packager.exceptions.InvalidUserInputException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
class FeesContentTest {

    @Test
    public void testFeesContentCreationExceptions() throws InvalidUserInputException, InitialLoadException {

        Exception exception = assertThrows(InitialLoadException.class, () -> {
            FeesContent feesContent = FeesContent.build(null);
        });
        assertEquals("Error At Address: 14, Treacle Mine Road, Ankh-Morpork", exception.getMessage());

        exception = assertThrows(InvalidUserInputException.class, () -> {
            FeesContent feesContent = FeesContent.build("abcd asdd");
        });
        assertEquals("+++Whoops! Here comes the cheese! +++", exception.getMessage());

        exception = assertThrows(InvalidUserInputException.class, () -> {
            FeesContent feesContent = FeesContent.build("-1 -1");
        });
        assertEquals("+++Wahhhhhhh! Mine!+++", exception.getMessage());
    }

    @ParameterizedTest
    @MethodSource("providesDataForInputs")
    public void testValidInputs(String line, BigDecimal price, BigDecimal weight) throws InvalidUserInputException, InitialLoadException {
        FeesContent feesContent = FeesContent.build(line);
        assertEquals(price, feesContent.getPrice());
        assertEquals(weight, feesContent.getWeight());

    }

    @ParameterizedTest
    @MethodSource("providesInvalidDataForInputs")
    public void testInvalidInputs(String line) {
        Exception exception = assertThrows(InvalidUserInputException.class, () -> {
            FeesContent feesContent = FeesContent.build(line);
        });
        assertEquals("+++Wahhhhhhh! Mine!+++", exception.getMessage());
    }

    private static Stream<Arguments> providesDataForInputs() {
        return Stream.of(
                Arguments.of("1 1", BigDecimal.valueOf(1f), BigDecimal.valueOf(1f)),
                Arguments.of("2 2", BigDecimal.valueOf(2f), BigDecimal.valueOf(2f)),
                Arguments.of("1,5555 1,5555", BigDecimal.valueOf(1.5555f), BigDecimal.valueOf(1.5555f)),
                Arguments.of("1,5555 1,555555555555555555", BigDecimal.valueOf(Float.parseFloat("1.555555555555555555")), BigDecimal.valueOf(Float.parseFloat("1.5555"))),
                Arguments.of("1,5555555555555555555 1,5555", BigDecimal.valueOf(Float.parseFloat("1.5555")), BigDecimal.valueOf(Float.parseFloat("1.5555555555555555555"))),
                Arguments.of("1,55555555555551 1,55555555555551", BigDecimal.valueOf(Float.parseFloat("1.55555555555551")), BigDecimal.valueOf(Float.parseFloat("1.55555555555551")))
        );
    }

    private static Stream<Arguments> providesInvalidDataForInputs() {
        return Stream.of(
                Arguments.of("0 0"),
                Arguments.of("0 1"),
                Arguments.of("2 0"),
                Arguments.of("-1 1,5555"),
                Arguments.of("-1,555555555555555555 1,5555"),
                Arguments.of("1,5555 -0"),
                Arguments.of("-1,555555555555511111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111119561111111111111111111111111111111111111119684798419651 1,55555555555551")
        );
    }



}