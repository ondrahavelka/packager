package cz.interview.exam.check.packager.model;

import cz.interview.exam.check.packager.exceptions.InitialLoadException;
import cz.interview.exam.check.packager.exceptions.InvalidUserInputException;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;

@Getter
@EqualsAndHashCode(callSuper = true)
public final class PackageContent extends Content {

    public static final String INVALID_LOAD = "Error At Address: 14, Treacle Mine Road, Ankh-Morpork";
    private static final String WHOOPS_HERE_COMES_THE_CHEESE = "+++Whoops! Here comes the cheese! +++";
    private String postCode;

    private PackageContent() {
    }

    public static PackageContent build(String line) throws InitialLoadException, InvalidUserInputException {
        if (line == null) {
            throw new InitialLoadException(INVALID_LOAD);
        }
        PackageContent pc = new PackageContent();
        line = line.replace(",", ".");
        String[] split = line.split(" ");
        if (split.length != 2){
            throw new InitialLoadException(INVALID_LOAD);
        }
        try {
            pc.setWeight(BigDecimal.valueOf(Float.parseFloat(split[0])));
        } catch (NumberFormatException nfe){
            throw new InvalidUserInputException(WHOOPS_HERE_COMES_THE_CHEESE);
        }
        try {
            // try to check is postal code is valid 5 digit number
            Integer.parseInt(split[1]);
        } catch (NumberFormatException nfe){
            throw new InvalidUserInputException(WHOOPS_HERE_COMES_THE_CHEESE);
        }
        if (split[1].length() != 5){
            throw new InvalidUserInputException(WHOOPS_HERE_COMES_THE_CHEESE);
        }
        pc.setPostCode(split[1]);
        if (pc.getWeight().compareTo(BigDecimal.ZERO) <= 0){
            throw new InvalidUserInputException("+++Wahhhhhhh! Mine!+++");
        }
        return pc;
    }

    private void setPostCode(String postCode) {
        this.postCode = postCode;
    }
}
