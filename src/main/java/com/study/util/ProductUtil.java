package com.study.util;

import com.study.model.Product;
import org.apache.commons.text.StringEscapeUtils;

public class ProductUtil {
    public static void escapeProduct(Product product) {
        product.setName(StringEscapeUtils.escapeHtml4(product.getName()));
    }
}
