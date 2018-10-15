package ncr.res.mobilepos.test;

import org.jbehave.core.steps.ParameterConverters;

import java.lang.reflect.Type;

public class EmptyStringConverter implements ParameterConverters.ParameterConverter {
    @Override
    public boolean accept(Type type) {
        if (type instanceof Class<?>) {
            return String.class.isAssignableFrom((Class<?>) type);
        }
        return false;
    }

    @Override
    public Object convertValue(String value, Type type) {
        if (value.equals("{}") ){
            return "";
        }
        return value;
    }
}
