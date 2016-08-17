package ncr.res.mobilepos.resourcefilter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * {@link CacheControl} is the interface
 * for resource filter annotation to cache control of WSS.
 *
 * @author CC185102
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface CacheControl {
    /** Get the value used as flag for Caching. Example value is "no-cache". */
    String value();
}
