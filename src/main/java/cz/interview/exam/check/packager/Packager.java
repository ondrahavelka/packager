package cz.interview.exam.check.packager;

import cz.interview.exam.check.packager.exceptions.InitialLoadException;
import cz.interview.exam.check.packager.exceptions.InvalidUserInputException;
import cz.interview.exam.check.packager.exceptions.UnknownFileTypeException;
import cz.interview.exam.check.packager.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

@Slf4j
@Component
public class Packager implements CommandLineRunner {


    private List<FeesContent> fees = new ArrayList<>();

    private List<PackageContent> packages = new ArrayList<>();

    private SortedSet<String> registeredPostalCodes = new TreeSet<>();

    private static boolean noFees = true;

    private static final String ERROR = "error";

    /**
     * Runner method for spring boot app - integrates initial load and user input logic
     * @param args
     * @throws Exception
     */
    @Override
    public void run(String... args) throws Exception {
        // initial lode
        try {
            initialLoad(args);
            // read users input from console
            Scanner scanner = new Scanner(System.in);
            boolean endTheUniverse = false;
            while(scanner.hasNext()) {
                String usersLine = scanner.nextLine();
                switch (usersLine) {
                    case "quit" : {
                        endTheUniverse = true;
                        break;
                    }
                    case "packages" : printPackages(); break;
                    case "sum" : sumPackages(); break;
                    case "fees" : printFees(); break;
                    default: {
                        try {
                            PackageContent pc = PackageContent.build(usersLine);
                            this.packages.add(pc);
                            registeredPostalCodes.add(pc.getPostCode());
                        } catch (Exception e ){
                            log.error("Invalid user input exception " + e.getMessage());
                        }
                        break;
                    }
                }
                if (endTheUniverse) {
                    break;
                }

            }
        } catch (Exception e ){
            log.error("Invalid initial parameters");
        }

        log.info("Turning of the universe. Please fasten your seatbelts.");

    }

    /**
     * Scheduler to print packages output every minute, with 1 second delay (usual delay to initialize this app)
     */
    @Async
    @Scheduled(fixedRate = 1000 * 60, initialDelay = 1000)
    public void schedulerPrinter(){
        sumPackages();
    }

    /**
     * Prints fees if they are present.
     */
    private void printFees() {
        if (noFees) {
            log.info("I am doing this for free! Dobby is a free elf!");
            return;
        }
        log.info("Fees: +++");
        this.fees.forEach(fee -> log.info("Fee with weigh at least " + String.format("%.3f", fee.getWeight()).replace(",", ".") + " has price " + String.format("%.2f", fee.getPrice()).replace(",", ".")));
        log.info("Melon melon melon +++");
    }

    /**
     * Print every package in system with his weight
     */
    private void printPackages() {
        log.info("Packages: +++");
        this.packages.forEach(postPackage -> log.info("Package with weigh " + String.format("%.3f", postPackage.getWeight()).replace(",", ".") + " to post code " + postPackage.getPostCode().replace(",", ".")));
        log.info("Melon melon melon +++");
    }

    /**
     * Sorts packages to post office (by postal code) and sums its weight. If fees are present in a system, counts packages fees as well.
     */
    private void sumPackages() {
        log.info("+++ Printing sum of packages sorted by weight +++");
        TreeMap<BigDecimal, String> sortedWeight = new TreeMap<>();
        this.registeredPostalCodes.forEach(postalCode -> {
            BigDecimal sum = this.packages.stream().filter(postalPackage -> postalPackage.getPostCode().equals(postalCode)).map(Content::getWeight).reduce(BigDecimal.ZERO, BigDecimal::add);
            sortedWeight.put(sum, postalCode);
        });
            sortedWeight.descendingKeySet().forEach(weight -> {
                BigDecimal sumFees = this.packages.stream().filter(postalPackage -> postalPackage.getPostCode().equals(sortedWeight.get(weight))).map(this::countFee).reduce(BigDecimal.ZERO, BigDecimal::add);
                if (noFees){
                    log.info(sortedWeight.get(weight) + " " + String.format("%.3f", weight).replace(",", "."));
                } else {
                    log.info(sortedWeight.get(weight) + " " + String.format("%.3f", weight).replace(",", ".") + " " + String.format("%.2f", sumFees).replace(",", "."));
                }
            });
    }

    /**
     * Counts fee for single package by its weight.
     * For example if package weight 8 kilograms, it is more than 5 but less than 10 kilograms, therefore price is for 5.
     * @param postalPackage Object representing single package
     * @return Its price
     */
    private BigDecimal countFee(PackageContent postalPackage) {

        AtomicReference<BigDecimal> initial = new AtomicReference<>(BigDecimal.ZERO);
        this.fees.forEach(fee -> {
            if (fee.getWeight().compareTo(postalPackage.getWeight())  <= 0) {
                initial.set(fee.getPrice());
            }
        });
        return initial.get();
    }

    /**
     * Loads initial content from file given in args, at least one arg is required or exception is thrown
     * @param args
     * @throws InitialLoadException
     */
    private void initialLoad(String[] args) throws InitialLoadException {
        List<PackageFile> packageFiles = parseArgumentsAndFindFiles(args);
        packageFiles.forEach(this::getContent);
    }

    /**
     * Process given arguments and prepare files to load
     * @param args
     * @return
     * @throws InitialLoadException
     */
    private static List<PackageFile> parseArgumentsAndFindFiles(String[] args) throws InitialLoadException {
        List<PackageFile> filesToReturn = new ArrayList<>(2);
        String initialPackageLoad;
        String initialFeesLoad = null;
        if (args != null && args.length >= 1){
            initialPackageLoad = args[0];
            if (args.length == 2){
                initialFeesLoad = args[1];
            }
        } else {
            throw new InitialLoadException("+++ Divide By Cucumber Error. Please Reinstall Universe And Reboot. +++");
        }
        if (initialPackageLoad != null){
            PackageFile packageFile = new PackageFile(initialPackageLoad, FileType.PACKAGE);
            filesToReturn.add(packageFile);
        }
        if (initialFeesLoad != null){
            PackageFile feesFile = new PackageFile(initialFeesLoad, FileType.FEES);
            filesToReturn.add(feesFile);
            noFees = false;
        }
        return filesToReturn;
    }

    /**
     * Load file to some content, sort it to specific in memory variables.
     * @param file
     */
    private void getContent(PackageFile file) {
        try(Stream<String> s = Files.lines(file.getPath())) {
            s.forEach(line -> {
                switch (file.getFileType()){
                    case PACKAGE: {
                        PackageContent packageContent = null;
                        try {
                            packageContent = PackageContent.build(line);
                        } catch (InitialLoadException | InvalidUserInputException e) {
                            log.error("Intitial load exception");
                            log.error(ERROR, e);
                        }
                        packages.add(packageContent);
                        registeredPostalCodes.add(Objects.requireNonNull(packageContent).getPostCode());
                        break;
                    }
                    case FEES: {
                        FeesContent feesContent = null;
                        try {
                            feesContent = FeesContent.build(line);
                        } catch (InitialLoadException | InvalidUserInputException e) {
                            log.error("Intitial load exception");
                            log.error(ERROR, e);
                        }
                        fees.add(feesContent);
                        break;
                    }
                    default: {
                        try {
                            throw new UnknownFileTypeException("I am sorry. It is hard to convey five-dimensional ideas in a language evolved to scream defiance at the monkeys in the next tree");
                        } catch (UnknownFileTypeException e) {
                            log.error(ERROR, e);
                        }
                    }
                }});
        } catch (IOException e) {
            log.error(ERROR, e);
        }
       Collections.sort(fees);
    }
}