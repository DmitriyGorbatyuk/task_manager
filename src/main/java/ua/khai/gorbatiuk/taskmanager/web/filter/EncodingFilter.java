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
import java.io.UnsupportedEncodingException;
import java.util.Map;

public class EncodingFilter implements Filter {

    private static final Logger logger = Logger.getLogger(EncodingFilter.class);

    private String encoding;

    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {


        if (request.getCharacterEncoding() == null) {
            request.setCharacterEncoding(encoding);
        }

        chain.doFilter(new EncodedParamsRequest((HttpServletRequest) request, encoding), response);
    }

    public void init(FilterConfig fConfig) throws ServletException {
        logger.debug("Filter initialization starts");
        encoding = fConfig.getInitParameter("encoding");
        logger.trace("Encoding from web.xml --> " + encoding);
        logger.debug("Filter initialization finished");
    }

    public void destroy() {
        logger.debug("Filter destruction starts");
        // do nothing
        logger.debug("Filter destruction finished");
    }

    private class EncodedParamsRequest extends HttpServletRequestWrapper {

        private static final String BROWSER_ENCODING = "ISO-8859-1";
        private String charsetName;

        EncodedParamsRequest(HttpServletRequest request, String charsetName) {
            super(request);
            this.charsetName = charsetName;
        }

        @Override
        public String getParameter(String name) {
            try {
                String param = super.getParameter(name);
                if( param == null) {
                    return null;
                } else {
//                    charsetName = "UTF-8";
                    return new String(param.getBytes(BROWSER_ENCODING), charsetName);
                }
            } catch (UnsupportedEncodingException e) {
                return super.getParameter(name);
            }
        }
    }

}