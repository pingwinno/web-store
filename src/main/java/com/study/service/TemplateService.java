package com.study.service;

import com.study.model.Product;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import lombok.SneakyThrows;

import java.io.File;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TemplateService {
    private final Configuration cfg = new Configuration(Configuration.VERSION_2_3_29);

    @SneakyThrows
    public TemplateService() {
        cfg.setClassForTemplateLoading(this.getClass(), "/templates");
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        cfg.setLogTemplateExceptions(false);
        cfg.setWrapUncheckedExceptions(true);
        cfg.setFallbackOnNullLoopVariable(false);
    }

    @SneakyThrows
    public void writeListPage(List<Product> products, Writer writer) {
        Map<String, List<Product>> root = new HashMap<>();
        root.put("products", products);
        Template temp = cfg.getTemplate("list.ftlh");
        temp.process(root, writer);
    }

    @SneakyThrows
    public void writeEditPage(Product product, Writer writer) {
        Map<String, Product> root = new HashMap<>();
        root.put("product", product);
        Template temp = cfg.getTemplate("edit.ftlh");
        temp.process(root, writer);
    }
}
