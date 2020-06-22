package by.jwd.testsys.presentation;

import by.jwd.testsys.controller.parameter.SessionAttributeName;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

public class DateTimeTag extends TagSupport {

    private static final long serialVersionUID = -8517243223404530698L;
    private String date;

    @Override
    public int doStartTag() throws JspException {

        JspWriter out = pageContext.getOut();
        try {
            String locale = (String) pageContext.getSession().getAttribute(SessionAttributeName.LANGUAGE_ATTRIBUTE);
            Locale locale1=Locale.forLanguageTag(locale);
            LocalDateTime ldt = LocalDateTime.parse(date);
            ZonedDateTime zdt = ZonedDateTime.of(ldt, ZoneId.systemDefault());
            DateTimeFormatter pattern = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT).withLocale(locale1);
            out.write(zdt.format(pattern));
        } catch (IOException e) {
            throw new JspException(e);
        }
        return SKIP_BODY;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
