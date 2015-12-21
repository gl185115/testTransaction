package ncr.res.mobilepos.resourcefilter;

import java.util.Collections;
import java.util.List;

import javax.ws.rs.core.HttpHeaders;

import com.sun.jersey.api.model.AbstractMethod;
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;
import com.sun.jersey.spi.container.ContainerResponse;
import com.sun.jersey.spi.container.ContainerResponseFilter;
import com.sun.jersey.spi.container.ResourceFilter;
import com.sun.jersey.spi.container.ResourceFilterFactory;

/**
 * {@link CacheFilterFactory} is a Resource Filter class for Cache-Control.
 * @author cc185102
 *
 */
public class CacheFilterFactory implements ResourceFilterFactory {
    /** The Default No-Cache Filter. */
    private static final List<ResourceFilter> NO_CACHE_FILTER
        = Collections.<ResourceFilter>singletonList(
                new CacheResponseFilter("no-cache"));

    @Override
    public final List<ResourceFilter> create(final AbstractMethod am) {
        CacheControl cch = am.getAnnotation(CacheControl.class);
        if (cch == null) {
            return NO_CACHE_FILTER;
        } else {
            return Collections.<ResourceFilter>singletonList(
                    new CacheResponseFilter(cch.value()));
        }
    }

    /**
     * {@link CacheResponseFilter} class is the Cache Response filter.
     * @author NCRP
     *
     */
    private static class CacheResponseFilter implements ResourceFilter,
    ContainerResponseFilter {
        /** The header value. */
        private final String headerValue;

        /**
         * The default constructor.
         * @param headerValueToSet   The header value.
         */
        CacheResponseFilter(final String headerValueToSet) {
            this.headerValue = headerValueToSet;
        }

        @Override
        public ContainerRequestFilter getRequestFilter() {
            return null;
        }

        @Override
        public ContainerResponseFilter getResponseFilter() {
            return this;
        }

        @Override
        public final ContainerResponse filter(
                final ContainerRequest request,
                final ContainerResponse response) {
            // attache Cache Control header to
            //each response based on the annotation value
            response.getHttpHeaders().putSingle(
                    HttpHeaders.CACHE_CONTROL, headerValue);
            return response;
        }
    }
}
