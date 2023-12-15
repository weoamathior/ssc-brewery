package guru.sfg.brewery.security;

import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;

public class RestParamAuthFilter extends AbstractRestAuthFilter{

    public RestParamAuthFilter(String defaultFilterProcessesUrl) {
        super(defaultFilterProcessesUrl);
    }

    public RestParamAuthFilter(RequestMatcher requiresAuthenticationRequestMatcher) {
        super(requiresAuthenticationRequestMatcher);
    }

    @Override
    String getPassword(HttpServletRequest request) {
        return request.getParameter("Api-Secret");
    }

    @Override
    String getUsername(HttpServletRequest request) {
        return request.getParameter("Api-Key");
    }

}
