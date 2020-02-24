package cz.interview.exam.check.packager.model;

import cz.interview.exam.check.packager.exceptions.InitialLoadException;
import cz.interview.exam.check.packager.exceptions.InvalidUserInputException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class PackageContentTest {


    @Test
    public void testPpackageContentCreationExceptions() throws InvalidUserInputException, InitialLoadException {

        Exception exception = assertThrows(InitialLoadException.class, () -> {
            PackageContent packageContent = PackageContent.build(null);
        });
        assertEquals("Error At Address: 14, Treacle Mine Road, Ankh-Morpork", exception.getMessage());

        exception = assertThrows(InvalidUserInputException.class, () -> {
            PackageContent packageContent = PackageContent.build("abcd asdd");
        });
        assertEquals("+++Whoops! Here comes the cheese! +++", exception.getMessage());

        exception = assertThrows(InvalidUserInputException.class, () -> {
            PackageContent packageContent = PackageContent.build("-1 -1");
        });
        assertEquals("+++Whoops! Here comes the cheese! +++", exception.getMessage());

        exception = assertThrows(InvalidUserInputException.class, () -> {
            PackageContent packageContent = PackageContent.build("-1 12345");
        });
        assertEquals("+++Wahhhhhhh! Mine!+++", exception.getMessage());
    }

    @ParameterizedTest
    @MethodSource("providesDataForInputs")
    public void testValidInputs(String line, String postCode, BigDecimal weight) throws InvalidUserInputException, InitialLoadException {
        PackageContent packageContent = PackageContent.build(line);
        assertEquals(postCode, packageContent.getPostCode());
        assertEquals(weight, packageContent.getWeight());

    }

    @ParameterizedTest
    @MethodSource("providesInvalidDataForInputs")
    public void testInvalidInputs(String line) {
        Exception exception = assertThrows(InvalidUserInputException.class, () -> {
            PackageContent packageContent = PackageContent.build(line);
        });
    }

    private static Stream<Arguments> providesDataForInputs() {
        return Stream.of(
                Arguments.of("1 11111", "11111", BigDecimal.valueOf(1f)),
                Arguments.of("2 13451", "13451", BigDecimal.valueOf(2f)),
                Arguments.of("1,5555 98754", "98754", BigDecimal.valueOf(1.5555f)),
                Arguments.of("1,555555555555555555 13121", "13121", BigDecimal.valueOf(Float.parseFloat("1.555555555555555555"))),
                Arguments.of("1,5555555555555555555 88888", "88888", BigDecimal.valueOf(Float.parseFloat("1.5555555555555555555"))),
                Arguments.of("1,55555555555551 12345", "12345", BigDecimal.valueOf(Float.parseFloat("1.55555555555551")))
        );
    }

    private static Stream<Arguments> providesInvalidDataForInputs() {
        return Stream.of(
                Arguments.of("0 0"),
                Arguments.of("0 1"),
                Arguments.of("-2 0"),
                Arguments.of("-1 1,5555"),
                Arguments.of("-1,555555555555555555 1,5555"),
                Arguments.of("-0 -0"),
                Arguments.of("1,55555555555551 -56789"),
                Arguments.of("-1,555555555555511111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111119561111111111111111111111111111111111111119684798419651 1,55555555555551")
        );
    }
}