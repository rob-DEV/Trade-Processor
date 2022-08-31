package com.robdev.tradeprocessor.util;

import java.util.function.Supplier;

public class CommonUtils {

    public static <T> T resultOrExcept(Supplier<T> risky) {
        try {
            return risky.get();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
