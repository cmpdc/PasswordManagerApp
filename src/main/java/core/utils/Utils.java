package core.utils;

import javafx.scene.Node;

import java.io.File;
import java.net.URL;
import java.security.SecureRandom;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Random;

public class Utils {
    public static String generateRandomStrings(int length){
        final Random SECURE_RANDOM = new SecureRandom();
        final String PASSWORD_KEYS = Constants.UPPERCASES + Constants.DIGITS + Constants.LOWERCASES;
        StringBuilder SaltedValue = new StringBuilder(length);


        for (int i = 0; i < length; i++){
            SaltedValue.append(PASSWORD_KEYS.charAt(SECURE_RANDOM.nextInt(PASSWORD_KEYS.length())));
        }

        return new String(SaltedValue);
    }

    public static boolean isChildOf(Node nodeInQuestion, Node parentInQuestion) {
        Node cur = nodeInQuestion.getParent();

        while (cur != null) {
            if (cur == parentInQuestion) return true;
            cur = cur.getParent();
        }

        return false;
    }

    public File getResourceFile(final String fileName){
        System.out.println(this.getClass().getClassLoader());

        URL url = this.getClass().getClassLoader().getResource(fileName);

        if(url == null){
            throw new IllegalArgumentException(fileName + " is not found!");
        }

        return new File(url.getFile());
    }

    public static String stringMultiplier(int length, String s)
    {
        String text = "";
        for(int i = 0; i < length; i++){
            text += s;
        }

        return text;
    }

    public static String getTimeAgo(OffsetDateTime dateArg) {
        Instant instantNow = Instant.now();
        Instant instantThen = dateArg.toInstant();
        Duration comparedDuration = Duration.between(instantThen, instantNow);

        LocalDate localDate = dateArg.toLocalDate();
        LocalDate localDateToday = LocalDate.now(ZoneId.systemDefault());
        Period comparedPeriod = Period.between(localDate, localDateToday);

        ZonedDateTime zdt1 = ZonedDateTime.ofInstant(instantThen, ZoneId.systemDefault());
        ZonedDateTime zdt2 = ZonedDateTime.ofInstant(instantNow, ZoneId.systemDefault());
        Calendar calendarFetched = GregorianCalendar.from(zdt1);
        Calendar calendarNow = GregorianCalendar.from(zdt2);

        Instant d1i = Instant.ofEpochMilli(calendarFetched.getTimeInMillis());
        Instant d2i = Instant.ofEpochMilli(calendarNow.getTimeInMillis());

        LocalDateTime startDateWeek = LocalDateTime.ofInstant(d1i, ZoneId.systemDefault());
        LocalDateTime endDateWeek = LocalDateTime.ofInstant(d2i, ZoneId.systemDefault());

        long weeksCalc = ChronoUnit.WEEKS.between(startDateWeek, endDateWeek);


        String s = "";
        if(comparedPeriod.getYears() != 0) {
            s += comparedPeriod.getYears() + " year" + ((comparedPeriod.getYears() == 1) ? " " : "s ");
        }

        if(comparedPeriod.getMonths() == 0){
            if(weeksCalc != 0){
                s += weeksCalc + " weeks ";
            }
        }
        else {
            s += comparedPeriod.getMonths() + " month" + ((comparedPeriod.getMonths() == 1) ? " " : "s ");
        }

        if(comparedPeriod.getDays() != 0){
            s += comparedPeriod.getDays() + " day" + ((comparedPeriod.getDays() == 1) ? " " : "s ");
        }
        else{
            if(comparedDuration.toHoursPart() != 0){
                s += comparedDuration.toHoursPart() + " hour" + ((comparedDuration.toHoursPart() == 1) ? " " : "s ");
            }
        }

        if(comparedDuration.toMinutesPart() != 0){
            s += comparedDuration.toMinutesPart() + " minute" + ((comparedDuration.toMinutesPart() == 1) ? " " : "s ");
        }

        else{
            s += comparedDuration.toSecondsPart() + " seconds ";
        }

        return s + "ago";
    }

    public static String getFormattedDateTime(OffsetDateTime dateArg){
        return DateTimeFormatter.ofPattern("LLLL dd, yyyy (HH:mm:ss) O").format(dateArg);
    }
}
