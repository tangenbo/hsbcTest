package server.com.hsbc.jettyFilter;

import server.com.hsbc.util.Log;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * All custom security related header modifications go here, we have the following now:
 * 1. ClickJacking protection: https://www.owasp.org/index.php/Clickjacking_Defense_Cheat_Sheet
 * 2. XSS protection: https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/X-XSS-Protection
 * 3. MIME type attack protection: https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/X-Content-Type-Options
 * 4. UTF-7 XSS attack protection: http://michaelthelin.se/security/2014/06/08/web-security-cross-site-scripting-attacks-using-utf-7.html
 * 5. StrictTransportSecurity: https://developer.mozilla.org/en-US/docs/Web/Security/HTTP_strict_transport_security
 * 6. ContentSecurityPolicy: https://developers.google.com/web/fundamentals/security/csp/
 * Note: HSTS takes into effect only if the website has a valid https certificate
 *
 * Created by athakur on 1/18/18.
 */
public class CustomSecurityHeadersFilter implements Filter {
    private final Log log = new Log(CustomSecurityHeadersFilter.class);
    private boolean isCSPHeaderEnabled = true;

    @Override
    public void init(FilterConfig filterConfig) {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        /*  The below two "getParameters" are required for Jersey to retain http parameters since onelogin saml client library requires it.
            We can retain the parameters by accessing servletRequest.getParameter(<name>) before Jersey filters/empties it.
            Reference: http://jersey.576304.n2.nabble.com/Jersey-empties-request-getParameterMap-with-POST-td4254182.html
         */
        servletRequest.getParameter("SAMLResponse");
        servletRequest.getParameter("SAMLRequest");

        HttpServletResponse response = (HttpServletResponse)servletResponse;

        response.setHeader("X-Frame-Options", "SAMEORIGIN");        // for clickjacking protection
        response.setHeader("X-XSS-Protection", "1; mode=block");    // for XSS protection
        response.setHeader("X-Content-Type-Options", "nosniff");    // for MIME type attack protection
        // max-age set to 18 weeks to comply with HSTS preload criteria https://www.troyhunt.com/understanding-http-strict-transport/
        response.setHeader("Strict-Transport-Security", "max-age=10886400; includeSubDomains; preload");
        if(isCSPHeaderEnabled) {
            // IMPORTANT!! all domains from which web client will load content (js, css, images etc) need to be whitelisted here. Browser will refuse to load non-whilelisted content. There will be browser console errors when any content is blocked
            response.setHeader("Content-Security-Policy", "default-src 'self'; connect-src 'self' wss: *.googleapis.com *.amazonaws.com *.silverpeaksystems.net *.silverpeak.cloud; script-src 'self' 'unsafe-inline' 'unsafe-eval' *.googleapis.com *.amazonaws.com *.silverpeaksystems.net *.silverpeak.cloud; style-src 'self' 'unsafe-inline' *.googleapis.com *.silverpeaksystems.net *.silverpeak.cloud; img-src 'self' data: *.gstatic.com *.googleapis.com *.silverpeaksystems.net *.silverpeak.cloud; font-src 'self' *.gstatic.com *.silverpeaksystems.net *.silverpeak.cloud");
        }

        response.setCharacterEncoding("utf-8"); // for UTF-7 XSS attack protection
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
