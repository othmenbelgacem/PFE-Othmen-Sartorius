package com.sartorius.tma.utils;

import com.google.common.base.Strings;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * Dates Utils
 *
 * @author mdh
 */
public class DatesUtils {

  /**
   * Return arrear Date between Dates arrays
   *
   * @param dates
   * @return arrear Date
   */
  public static Date max(Date... dates) {
    return Arrays.stream(dates).filter(Objects::nonNull).max(Date::compareTo).orElse(null);
  }

  /**
   * Return earliest Date between Dates arrays
   *
   * @param dates
   * @return earliest Date
   */
  public static Date min(Date... dates) {
    return Arrays.stream(dates).filter(Objects::nonNull).min(Date::compareTo).orElse(null);
  }

  /**
   * Return DAY_OF_WEEK index from date
   *
   * @param date
   * @return DAY_OF_WEEK index: start from 0 ( 0 === SUNDAY)
   */
  public static Integer getDayOfWeekFromDate(Date date) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    return calendar.get(Calendar.DAY_OF_WEEK) - 1;
  }

  /**
   * calculate duration in minutes between 2 dates
   *
   * @param startDate
   * @param endDate
   * @return Duration in minutes
   */
  public static int getMinDurationsBetween(Date startDate, Date endDate) {
    long diff = endDate.getTime() - startDate.getTime();
    return (int) TimeUnit.MILLISECONDS.toMinutes(diff);
  }
  public static int getHourDurationsBetween(Date startDate, Date endDate) {
    long diff = endDate.getTime() - startDate.getTime();
    return (int) TimeUnit.MILLISECONDS.toHours(diff);
  }



  /**
   * compare 2 dates times
   *
   * @param startDate
   * @param endDate
   * @return true/false
   */
  public static boolean compareDatesTimes(Date startDate, Date endDate) {
    Calendar calendar1 = Calendar.getInstance();
    calendar1.setTime(startDate);
    Calendar calendar2 = Calendar.getInstance();
    calendar2.setTime(endDate);
    return calendar2.get(Calendar.HOUR_OF_DAY) == calendar1.get(Calendar.HOUR_OF_DAY)
        && calendar2.get(Calendar.MINUTE) == calendar1.get(Calendar.MINUTE);
  }

  /**
   * Adjust time to half hour based (XX:00 or XX:30)
   *
   * @param date
   * @param isStart
   * @return adjusted date
   */
  public static Date adjustDate(Date date, boolean isStart) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);

    int unroundedHours = calendar.get(Calendar.HOUR_OF_DAY);
    int unroundedMinutes = calendar.get(Calendar.MINUTE);
    if (!isStart && unroundedHours == 0 && unroundedMinutes == 0) {
      calendar.add(Calendar.DAY_OF_MONTH, 1);
    } else {
      int mod = unroundedMinutes % 30;
      if (mod != 0) {
        calendar.add(Calendar.MINUTE, isStart ? (30 - mod) : -mod);
      }
    }
    calendar.set(Calendar.SECOND, 0);
    calendar.set(Calendar.MILLISECOND, 0);
    return calendar.getTime();
  }

  /**
   * convert Date to specific timezone LocalDateTime
   *
   * @param date
   * @param toTimeZone
   * @return LocalDateTime
   */
  public static LocalDateTime convertDateTimeZone(Date date, String toTimeZone) {
    ZoneId fromTimeZoneId = ZoneId.of(Constants.DEFAULT_TIMEZONE);
    ZoneId toTimeZoneId = ZoneId.of(toTimeZone);

    ZonedDateTime fromZonedDateTime = ZonedDateTime.ofInstant(date.toInstant(), fromTimeZoneId);
    ZonedDateTime toZonedDateTime = fromZonedDateTime.withZoneSameInstant(toTimeZoneId);

    return toZonedDateTime.toLocalDateTime();
  }

  public static Date setDateTime(Date date1,Date date2){
    date1.setHours(date2.getHours());
    date1.setMinutes(date2.getMinutes());
    return date1;
  }

  /**
   * Calls {@link #asLocalDateTime(Date, ZoneId)} with the system default time zone.
   */
  public static LocalDateTime asLocalDateTime(java.util.Date date) {
    return asLocalDateTime(date, ZoneId.systemDefault());
  }

  /**
   * Creates {@link LocalDateTime} from {@code java.util.Date} or it's subclasses. Null-safe.
   */
  public static LocalDateTime asLocalDateTime(java.util.Date date, ZoneId zone) {
    if (date == null)
      return null;

    if (date instanceof java.sql.Timestamp)
      return ((java.sql.Timestamp) date).toLocalDateTime();
    else
      return Instant.ofEpochMilli(date.getTime()).atZone(zone).toLocalDateTime();
  }

  /**
   * convert Date to LocalDateTime in default system timezone
   *
   * @param date
   * @return LocalDateTime
   */
  public static LocalDateTime convertDateToLocalDate(Date date) {
    return Instant.ofEpochMilli(date.getTime())
        .atZone(ZoneId.of(Constants.DEFAULT_TIMEZONE))
        .toLocalDateTime();
  }

  /**
   * convert Date to LocalDateTime in default system timezone
   *
   * @param date
   * @return LocalDateTime
   */
  public static ZonedDateTime convertToZonedDateTime(Date date, String toTimeZone) {
    ZoneId fromTimeZoneId = ZoneId.of(Constants.DEFAULT_TIMEZONE);
    ZoneId toTimeZoneId = ZoneId.of(toTimeZone);

    ZonedDateTime fromZonedDateTime = ZonedDateTime.ofInstant(date.toInstant(), fromTimeZoneId);
    return fromZonedDateTime.withZoneSameInstant(toTimeZoneId);
  }

  /**
   * get timezone Abbreviation for given timezone
   *
   * @param timezone
   * @return timezone Abbreviation
   */
  public static String getTimeZoneAbbreviation(String timezone) {
    if (Strings.isNullOrEmpty(timezone)) {
      timezone = Constants.DEFAULT_TIMEZONE;
    }
    return TimeZone.getTimeZone(timezone).getDisplayName(false, TimeZone.SHORT);
  }

  /**
   * check if is passed date in default system timezone
   *
   * @param date
   * @return true/false
   */
  public static Boolean isPassedDate(Date date) {
    LocalDateTime localDate = convertDateToLocalDate(date);
    LocalDateTime now = LocalDateTime.now(ZoneId.of(Constants.DEFAULT_TIMEZONE));
    return localDate.isAfter(now);
  }
  public static Date getMeYesterday(){
    return new Date(System.currentTimeMillis()-24*60*60*1000);
  }
  public static Integer convertHourToMinutes(Integer hours) {
    return hours*60;
  }
  private DatesUtils() {}
  public static Date calculateEndDate(Date startDate,int monthsNumber) {
    LocalDate localStartDate = startDate.toInstant()
        .atZone(ZoneId.systemDefault())
        .toLocalDate()
        .plusMonths(monthsNumber);

    Instant instant = localStartDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
    Date endDate = Date.from(instant);

    return endDate;
  }
}
