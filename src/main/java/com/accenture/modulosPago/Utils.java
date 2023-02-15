package com.accenture.modulosPago;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;

public class Utils {
    public static Boolean verifyNumber(String number) {
        try {
            Double.parseDouble(number);
            return true;
        } catch (NumberFormatException e) {
            e.getMessage();
            return false;
        }
    }

    public static Boolean verifyTwoDecimal(Double number) {
        int counter = 0;
        try {
            int index = number.toString().indexOf(".");
            for (int i = index; i < number.toString().length(); i++) {
                counter++;
            }
            if (counter > 3) {
                return true;
            } else {
                return false;
            }
        } catch (NumberFormatException e) {
            e.getMessage();
            return false;
        }
    }

    public static boolean checkFormatDate(LocalDate dateTime) {
        boolean isCorrect = false;

        try {

            SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy");
            formatDate.setLenient(false);
            String day = Integer.toString(dateTime.getDayOfMonth());
            String month = Integer.toString(dateTime.getMonthValue());
            String year = Integer.toString(dateTime.getYear());

            formatDate.parse(day + "/" + month + "/" + year);
            isCorrect = true;
        } catch (ParseException e) {
            isCorrect = false;
        }
        return isCorrect;
    }

}
