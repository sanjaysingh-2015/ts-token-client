package com.techsophy.tstokens.utils;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ApplicationMapping<T, V> {
    public T convert(V source, Class<T> destClass) {
        ModelMapper mapper = new ModelMapper();
        return mapper.map(source, destClass);
    }
}
