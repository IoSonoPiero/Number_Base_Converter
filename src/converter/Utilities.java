package converter;

import java.math.BigDecimal;

class Utilities {
    static String toDecimalBase(String sourceNumber, BigDecimal sourceBase) {
        if (sourceBase.compareTo(BigDecimal.TEN) == 0) {
            return new BigDecimal(sourceNumber).toString();
        }

        BigDecimal decimalNumber = BigDecimal.ZERO;
        BigDecimal tempNumber;
        int power = 0;

        for (int i = sourceNumber.length() - 1; i >= 0; i--) {

            // if source base is > 10, then use letters
            if (sourceBase.compareTo(BigDecimal.TEN) > 0 && sourceNumber.charAt(i) >= 'a' && sourceNumber.charAt(i) <= 'z') {
                tempNumber = BigDecimal.valueOf(9 + sourceNumber.charAt(i) - 96);
            } else {
                tempNumber = new BigDecimal(String.valueOf(sourceNumber.charAt(i)));
            }

            decimalNumber = decimalNumber.add(tempNumber.multiply(sourceBase.pow(power)));
            power++;
        }

        return decimalNumber.toString();
    }

    static String toAnyBaseIntegerPart(BigDecimal number, BigDecimal targetBase) {
        if (targetBase.compareTo(BigDecimal.TEN) == 0) {
            return number.toString();
        }

        BigDecimal quotient = number;

        StringBuilder stringRemainder = new StringBuilder();

        if (quotient.compareTo(BigDecimal.ZERO) == 0) {
            return "0";
        }

        while (quotient.compareTo(BigDecimal.ZERO) != 0) {

            // returns an array consisting of two numbers: the result of integer division and the remainder
            BigDecimal[] remainder = quotient.divideAndRemainder(targetBase);

            int intRemainder = remainder[1].intValue();

            if (targetBase.compareTo(BigDecimal.TEN) > 0 && intRemainder > 9) {
                stringRemainder.append((char) (55 + intRemainder));
            } else {
                stringRemainder.append(remainder[1]);
            }
            quotient = quotient.divideToIntegralValue(targetBase);
        }
        return stringRemainder.reverse().toString();
    }

    static String toDecimalBaseFractionalPart(String number, BigDecimal targetBase) {

        if ("0".equals(number)) {
            return "00000";
        }

        // transform the number in base 10
        //String result = null;
        int iterator = -1;
        BigDecimal sum = BigDecimal.ZERO;

        for (int i = 0; i < number.length(); i++) {
            String result = toDecimalBase(String.valueOf(number.charAt(i)), targetBase);
            BigDecimal value = new BigDecimal(result);
            BigDecimal power = BigDecimal.valueOf(Math.pow(targetBase.doubleValue(), iterator));
            value = value.multiply(power);
            iterator--;
            sum = sum.add(value);
        }

        return sum.toString();
    }

    static String toAnyBaseFractionalPart(BigDecimal number, BigDecimal targetBase) {
        // transform the number to base TargetBase

        //    multiply the number by 2;
        //    get the fractional part for the next iteration;
        //    remember the integer remainder for the binary digit;
        //    repeat the steps until the fractional part is equal to 0;
        //    write the remainders in the non-reverse order.


        BigDecimal newNumber = number;

        int iterator = 0;
        StringBuilder stringRemainder = new StringBuilder();

        if (newNumber.compareTo(BigDecimal.ZERO) == 0) {
            return "00000";
        }

        while (newNumber.remainder(BigDecimal.ONE).compareTo(BigDecimal.ZERO) != 0 && iterator < 5) {
            newNumber = newNumber.multiply(targetBase);

            // Fraction part
            BigDecimal fractionalPart = newNumber.remainder(BigDecimal.ONE);

            // Calculate Integer Part
            BigDecimal natural = newNumber.subtract(fractionalPart);
            natural = natural.setScale(0);

            stringRemainder.append(toAnyBaseIntegerPart(natural, targetBase));

            newNumber = fractionalPart;
            iterator++;

        }

        return stringRemainder.toString();
    }
}
