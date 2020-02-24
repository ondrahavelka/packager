package cz.interview.exam.check.packager.model;


import lombok.Data;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class Content {

    private BigDecimal weight;

    Content() {
    }

    void setWeight(BigDecimal weight) {
        this.weight = weight;
    }
}
