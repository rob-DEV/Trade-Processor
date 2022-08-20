package com.robdev.tradeprocessor.util;

import java.util.function.Supplier;

public class CommonUtil {

    public static <T> T resultOrExcept(Supplier<T> risky) {
        try {
            return risky.get();
        } catch (Exception ex) {
//            throw new RuntimeException(ex);
            return null;
        }
    }
}
