package converter;

import java.math.BigDecimal;
import java.util.Scanner;

import static converter.Utilities.*;

public class Main {
    static Scanner scanner = new Scanner(System.in);
    static int sourceBase;
    static int targetBase;

    public static void main(String[] args) { // 1101010010.01000
        boolean isRunning = true; //2 8
        boolean isSameBase;
        while (isRunning) {

            System.out.print("Enter two numbers in format: {source base} {target base} (To quit type /exit) ");
            String firstCommand = scanner.nextLine();

            Scanner intScanner = new Scanner(firstCommand);

            if ("/exit".equals(firstCommand)) {
                isRunning = false;
            } else {
                sourceBase = intScanner.nextInt();
                targetBase = intScanner.nextInt();
                isSameBase = true;

                while (isSameBase) {
                    System.out.format("Enter number in base %d to convert to base %d (To go back type /back) ", sourceBase, targetBase);
                    String secondCommand = scanner.nextLine();

                    if ("/back".equals(secondCommand)) {
                        isSameBase = false;
                    } else {
                        // if number has a fractional part, split it into 2 pieces.
                        String[] parts = secondCommand.split("\\.", 2);

                        // convert integer part to base 10
                        String firstNumber = toDecimalBase(parts[0], BigDecimal.valueOf(sourceBase));
                        BigDecimal integerPart = new BigDecimal(firstNumber);

                        String numberToDecimal = toAnyBaseIntegerPart(integerPart, BigDecimal.valueOf(targetBase));

                        // If there's a fractional part, then:
                        if (parts.length > 1) {

                            BigDecimal fractionalPart = new BigDecimal(toDecimalBaseFractionalPart(parts[1], BigDecimal.valueOf(sourceBase)));
                            String convertedFractional = toAnyBaseFractionalPart(fractionalPart, BigDecimal.valueOf(targetBase));
                            for (int i = convertedFractional.length(); i < 5; ++i) {
                                convertedFractional += "0";
                            }
                            numberToDecimal += "." + convertedFractional;
                        }
                        System.out.format("Conversion result: %s%n%n", numberToDecimal);
                    }
                }
            }
        }
    }

}