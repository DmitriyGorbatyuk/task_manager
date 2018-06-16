package ua.khai.gorbatiuk.taskmanager.web.filter;

import org.apache.log4j.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;

public class LocaleSelectFilter implements Filter {

    private static final Logger logger = Logger.getLogger(LocaleSelectFilter.class);

    private static final String DEFAULT_LOCALE_PARAM = "defaultLocale";
    private static final String AVAILABLE_LOCALES_PARAM = "availableLocales";
    private static final String LANG = "lang";

    private List<String> availableLocaleNames;
    private String defaultLocaleName;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        defaultLocaleName = filterConfig.getInitParameter(DEFAULT_LOCALE_PARAM);
        checkForNullAndThrowException(defaultLocaleName, "There is no default locale in the filter init param");
        availableLocaleNames = Arrays.asList(filterConfig.getInitParameter(AVAILABLE_LOCALES_PARAM).split(","));
        checkForNullAndThrowException(availableLocaleNames, "There is no available locales in the filter init param");
    }

    private void checkForNullAndThrowException(Object object, String msg) {
        if (object == null) {
            throw new UnsupportedOperationException(msg);
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        ServletRequest requestWrapper = selectLocale(httpRequest, (HttpServletResponse) response);
        chain.doFilter(requestWrapper, response);
    }

    private ServletRequest selectLocale(HttpServletRequest request, HttpServletResponse response) {
        Locale selectedLocale = getLocale(request);

        setLocale(request, selectedLocale);
        return new SingleLocaleHttpServletRequest(request, selectedLocale);
    }

    private Locale getLocale(HttpServletRequest request) {
        Locale selectedLocale = getLocaleFromRequestParameter(request);

        if (selectedLocale == null) {
            selectedLocale = getLocaleFromSession(request);

            if (selectedLocale == null) {
                selectedLocale = getLocaleFromRequestHeader(request);

                if (selectedLocale == null) {
                    selectedLocale = new Locale(defaultLocaleName);
                }
            }
        }
        return selectedLocale;
    }

    private Locale getLocaleFromRequestParameter(HttpServletRequest request) {
        String paramLocale = request.getParameter(LANG);
        if (paramLocale != null && availableLocaleNames.contains(paramLocale)) {
            return new Locale(paramLocale);
        }
        return null;
    }

    private Locale getLocaleFromSession(HttpServletRequest request) {
        Locale locale = (Locale) request.getSession().getAttribute(LANG);
        if (locale != null && availableLocaleNames.contains(locale.getLanguage())) {
            return locale;
        }
        return null;
    }

    private Locale getLocaleFromRequestHeader(HttpServletRequest request) {
        Enumeration<Locale> localeEnumeration = request.getLocales();
        while (localeEnumeration != null && localeEnumeration.hasMoreElements()) {
            String localeLanguage = localeEnumeration.nextElement().getLanguage();
            if (availableLocaleNames.contains(localeLanguage)) {
                return new Locale(localeLanguage);
            }
        }
        return null;
    }

    private void setLocale(HttpServletRequest request, Locale selectedLocale) {
        request.getSession().setAttribute(LANG, selectedLocale);
    }

    @Override
    public void destroy() {
        //do nothing
    }

    private  class SingleLocaleHttpServletRequest extends HttpServletRequestWrapper {

        private Locale locale;

        SingleLocaleHttpServletRequest(HttpServletRequest request, Locale locale) {
            super(request);
            this.locale = locale;
        }

        @Override
        public Locale getLocale() {
            return locale;
        }

        @Override
        public Enumeration<Locale> getLocales() {
            return Collections.enumeration(Arrays.asList(locale));
        }
    }

}
