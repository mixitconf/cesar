package org.mixit.cesar.site.web;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Filter which detects that the request is coming from a social network or web crawler user agent (facebook, twitter,
 * Google, etc.) and, if it's the case, for certain URLs, forwards to an "old-school" Spring controller which generates
 * a page at server-side specific for the bots
 *
 * @author JB Nizet
 */
@Component
public class BotFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (mustFowardToBotUrl(request)) {
            request.getRequestDispatcher("/bot" + request.getRequestURI()).forward(request, response);
        }
        else {
            filterChain.doFilter(request, response);
        }
    }

    private boolean mustFowardToBotUrl(HttpServletRequest request) {
        return request.getMethod().equals("GET")
                && hasBotUserAgent(request)
                && hasShareableUri(request);
    }

    private boolean hasShareableUri(HttpServletRequest request) {
        return request.getRequestURI().startsWith("/home") || request.getRequestURI().startsWith("/");
    }

    private boolean hasBotUserAgent(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        if (userAgent == null) {
            return false;
        }
        return userAgent.startsWith("facebookexternalhit")
                || userAgent.contains("http://www.google.com/webmasters/tools/richsnippets")
                || userAgent.contains("+https://developers.google.com/+/web/snippet/")
                || userAgent.startsWith("Twitterbot")
                || userAgent.contains("Googlebot")
                || userAgent.contains("bingbot")
                || userAgent.contains("Yahoo! Slurp")
                || userAgent.contains("DuckDuckBot");
    }

}
