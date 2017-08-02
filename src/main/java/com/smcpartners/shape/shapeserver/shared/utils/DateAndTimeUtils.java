package com.smcpartners.shape.shapeserver.shared.utils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;

/**
 * Responsible: Wraps java.time date functionality. Used in many places in the application<br/>
 * <p>
 * <p>
 * Created by johndestefano on 9/12/15.
 * <p>
 * Changes:<br/>
 * Created by johndestefano on 9/21/15.
 */
public class DateAndTimeUtils {

    /**
     * Date formatter for MMMM dd, yyyy
     */
    private static final DateTimeFormatter df1 = DateTimeFormatter.ofPattern("MMMM dd, yyyy");

    /**
     * Date formatter for MM-dd-yyyy
     */
    private static final DateTimeFormatter df2 = DateTimeFormatter.ofPattern("MM-dd-yyyy");

    /**
     * Date formatter for MM-dd-yyyy
     */
    private static final DateTimeFormatter df3 = DateTimeFormatter.ofPattern("MM-dd-yyyy hh:mm:ss");

    /**
     * Get the Monday of the week that contains the given date
     *
     * @param dt
     * @return
     * @throws Exception
     */
    public static Date getMondayOfWeekWithDate(Date dt) throws Exception {
        return getDayofWeekForContainedDate(dt, DayOfWeek.MONDAY);
    }

    /**
     * Get the Sunday of the week that contaons the given date
     *
     * @param dt
     * @return
     * @throws Exception
     */
    public static Date getSundayofWeekWithDate(Date dt) throws Exception {
        return getDayofWeekForContainedDate(dt, DayOfWeek.SUNDAY);
    }

    /**
     * Find the day of week within the week that the dt is part of.
     *
     * @param dt
     * @param dow
     * @return
     * @throws Exception
     */
    public static Date getDayofWeekForContainedDate(Date dt, DayOfWeek dow) throws Exception {
        LocalDate dtLocalDate = dateToSystemLocalDate(dt);
        LocalDateTime dtLocalDateTime = dtLocalDate.atStartOfDay();
        dtLocalDate = dtLocalDateTime.toLocalDate();
        Date retDate;
        if (isDayOfWeek(dtLocalDate, dow)) {
            retDate = localDateToSystemAdjustedStartOfDayDate(dtLocalDate);
        } else {
            int dtVal = dtLocalDateTime.getDayOfWeek().getValue();
            int dowVal = dow.getValue();

            LocalDate tempLocalDate;
            if (dowVal > dtVal) {
                tempLocalDate = dtLocalDate.with(TemporalAdjusters.next(dow));
            } else {
                tempLocalDate = dtLocalDate.with(TemporalAdjusters.previous(dow));
            }
            retDate = localDateToSystemAdjustedStartOfDayDate(tempLocalDate);
        }
        return retDate;
    }

    /**
     * Find the next day of the week from the given LocalDate
     *
     * @param d
     * @param dow
     * @return
     */

    public static LocalDate calcNextDayOfWeek(LocalDate d, DayOfWeek dow) throws Exception {
        return d.with(TemporalAdjusters.next(dow));
    }

    /**
     * Like it says
     *
     * @param ldt
     * @param dow
     * @return
     * @throws Exception
     */
    public static LocalDate calcNextDayOfWeekFromLDT(LocalDateTime ldt, DayOfWeek dow) throws Exception {
        LocalDate d = ldt.toLocalDate();
        return calcNextDayOfWeek(d, dow);
    }

    /**
     * Pass a date in. It is set to the system local date format,
     * then taken to Midnight and turned back into a date.
     *
     * @param dt
     * @return
     * @throws Exception
     */
    public static Date adjustToMidnightForSystem(Date dt) throws Exception {
        LocalDate ld = dateToSystemLocalDate(dt);
        LocalDateTime ldt = ld.atStartOfDay();
        return getSystemDefaultDate(ldt);
    }

    /**
     * Check the LocalDate given to see if it is the DayOfWeek given
     *
     * @param d
     * @param dow
     */
    public static boolean isDayOfWeek(LocalDate d, DayOfWeek dow) throws Exception {
        if (d.getDayOfWeek().compareTo(dow) == 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Get local system Date from LocalDateTime
     *
     * @param ldt
     * @return
     * @throws Exception
     */
    public static Date getSystemDefaultDate(LocalDateTime ldt) throws Exception {
        return Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * Takes a java.util.Date and turns it into a LocalDate based on the
     * systems default timezone.
     *
     * @param d
     * @return
     * @throws Exception
     */
    public static LocalDate dateToSystemLocalDate(Date d) throws Exception {
        return d.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    /**
     * Adjusts a LocalDate to Midnight of the say day (start of day).
     *
     * @param d
     * @return
     * @throws Exception
     */
    public static Date localDateToSystemAdjustedStartOfDayDate(LocalDate d) throws Exception {
        LocalDateTime ldt = d.atStartOfDay();
        return getSystemDefaultDate(ldt);
    }

    /**
     * Turns a LocalDateTime into a LocalDate
     *
     * @param ldt
     * @return
     * @throws Exception
     */
    public static LocalDate localDateTimeToLocalDate(LocalDateTime ldt) throws Exception {
        return ldt.toLocalDate();
    }

    /**
     * Takes the LocalDate, subtracts the day indicated from it, and
     * adjusts it to Midnight (start of day).
     *
     * @param ld
     * @param days
     * @return
     * @throws Exception
     */
    public static LocalDateTime minusDaysAdjustedToStartOfDay(LocalDate ld, int days) throws Exception {
        return ld.minusDays(days).atStartOfDay();
    }

    /**
     * Add the number of days to the date.
     *
     * @param d
     * @param days
     * @return
     * @throws Exception
     */
    public static LocalDate addDaysToDate(Date d, int days) throws Exception {
        LocalDate ld = dateToSystemLocalDate(d);
        return ld.plusDays(days);
    }

    /**
     * Returns Date plus days
     *
     * @param dt
     * @param days
     * @return
     * @throws Exception
     */
    public static Date plusDays(Date dt, int days) throws Exception {
        LocalDate ld = addDaysToDate(dt, days);
        return localDateToSystemAdjustedStartOfDayDate(ld);
    }

    /**
     * Add weeks to date
     *
     * @param dt
     * @param weeks
     * @return
     * @throws Exception
     */
    public static Date plusWeeks(Date dt, int weeks) throws Exception {
        LocalDate ld = dateToSystemLocalDate(dt);
        LocalDate addedWks = ld.plusWeeks(weeks);
        return localDateToSystemAdjustedStartOfDayDate(addedWks);
    }

    /**
     * Return the day of week string for the given date
     *
     * @param dt
     * @return
     * @throws Exception
     */
    public static String getDayNameForDate(Date dt) throws Exception {
        LocalDate ld = dateToSystemLocalDate(dt);
        return ld.getDayOfWeek().name();
    }

    /**
     * Uses format df1 above to format the supplied date.
     *
     * @param dt
     * @return
     * @throws Exception
     */
    public static String getLongDateString(Date dt) throws Exception {
        LocalDate ld = dateToSystemLocalDate(dt);
        return df1.format(ld);
    }

    /**
     * Expects date string as MM-dd-yyyy
     *
     * @param dtStr
     * @return
     * @throws Exception
     */
    public static Date getDateFromFormat(String dtStr) throws Exception {
        LocalDate ld = LocalDate.parse(dtStr, df2);
        return localDateToSystemAdjustedStartOfDayDate(ld);
    }

    /**
     * Converst a date to date time string - MM-dd-yyyy hh:mm:ss
     *
     * @param date
     * @return
     * @throws Exception
     */
    public static String convertDateToDTString(Date date) throws Exception {
        LocalDateTime ld = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        return df3.format(ld);
    }

    /**
     * Returns the number of full weeks between 2 dates
     *
     * @param d1
     * @param d2
     * @return
     * @throws Exception
     */
    public static long getFullWeeksBetween(Date d1, Date d2) throws Exception {
        LocalDateTime ldt1 = dateToSystemLocalDate(d1).atStartOfDay();
        Instant d1i = Instant.ofEpochMilli(d1.getTime());
        Instant d2i = Instant.ofEpochMilli(d2.getTime());
        LocalDateTime startDate = LocalDateTime.ofInstant(d1i, ZoneId.systemDefault());
        LocalDateTime endDate = LocalDateTime.ofInstant(d2i, ZoneId.systemDefault());
        return ChronoUnit.WEEKS.between(startDate, endDate);
    }
}
