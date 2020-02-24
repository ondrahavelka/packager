package cz.interview.exam.check.packager;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.rule.OutputCapture;
import org.springframework.boot.test.system.OutputCaptureRule;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.hamcrest.Matchers.not;


public class PackagerTest {

    @Test
    void run() throws Exception {
        Packager packager = new Packager();
        String input = "sum" + System.lineSeparator() + "quit";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        packager.run("src/test/resources/packages.txt");
    }

}
