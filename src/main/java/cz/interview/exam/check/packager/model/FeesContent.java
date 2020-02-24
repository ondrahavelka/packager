package cz.interview.exam.check.packager.model;

import cz.interview.exam.check.packager.exceptions.InitialLoadException;
import cz.interview.exam.check.packager.exceptions.InvalidUserInputException;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@EqualsAndHashCode(callSuper = true)
public class FeesContent extends Content implements Comparable<FeesContent> {

    private BigDecimal price;

    private FeesContent() {
    }

    public static FeesContent build(String line) throws InitialLoadException, InvalidUserInputException {
        if (line == null) {
            throw new InitialLoadException("Error At Address: 14, Treacle Mine Road, Ankh-Morpork");
        }
        FeesContent fc = new FeesContent();
        line = line.replace(",", ".");
        String[] split = line.split(" ");
        if (split.length != 2){
            throw new InitialLoadException("Error At Address: 14, Treacle Mine Road, Ankh-Morpork");
        }
        try {
            fc.setWeight(BigDecimal.valueOf(Float.parseFloat(split[0])));
        } catch (NumberFormatException nfe){
            throw new InvalidUserInputException("+++Whoops! Here comes the cheese! +++");
        }
        try {
            fc.setPrice(BigDecimal.valueOf(Float.parseFloat(split[1])));
        } catch (NumberFormatException nfe){
            throw new InvalidUserInputException("+++Whoops! Here comes the cheese! +++");
        }
        if (fc.getPrice().compareTo(BigDecimal.ZERO) <= 0 || fc.getWeight().compareTo(BigDecimal.ZERO) <= 0){
            throw new InvalidUserInputException("+++Wahhhhhhh! Mine!+++");
        }
        return fc;
    }

    @Override
    public int compareTo(FeesContent feesContent) {
        return this.getPrice().compareTo(feesContent.getPrice());
    }

    private void setPrice(BigDecimal price) {
        this.price = price;
    }
}
