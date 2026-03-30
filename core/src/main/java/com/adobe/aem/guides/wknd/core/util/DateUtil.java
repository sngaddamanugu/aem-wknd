package com.adobe.aem.guides.wknd.core.util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import static com.adobe.aem.guides.wknd.core.util.Constants.DATE_FORMAT;

public class DateUtil {

    private DateUtil() {
        // Private constructor to hide the implicit public one.
    }

    private static final Logger LOG = LoggerFactory.getLogger(DateUtil.class);

    public static String getDisplayDate(String inputDate, String outputFormat) {

        final SimpleDateFormat storedFormat = new SimpleDateFormat(DATE_FORMAT);
        storedFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            Date date = storedFormat.parse(inputDate);
            SimpleDateFormat displayFormat = new SimpleDateFormat(outputFormat);
            displayFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            return displayFormat.format(date);
        }
        catch (ParseException e) {
            LOG.error("Error parsing date: {}", inputDate, e);
            return inputDate;
        }
    }
}
