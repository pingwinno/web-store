package com.study.store.web.template;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import lombok.SneakyThrows;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.util.Collections;

public class TemplateProvider {
    private final Configuration cfg = new Configuration(Configuration.VERSION_2_3_29);

    @SneakyThrows
    public TemplateProvider() {
        cfg.setClassForTemplateLoading(this.getClass(), "/templates");
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        cfg.setLogTemplateExceptions(false);
        cfg.setWrapUncheckedExceptions(true);
        cfg.setFallbackOnNullLoopVariable(false);
    }

    @SneakyThrows
    public byte[] writePage(Object params, String templateName) {
        var outputStream = new ByteArrayOutputStream();
        var writer = new OutputStreamWriter(outputStream);
        Template temp = cfg.getTemplate(templateName);
        temp.process(params, writer);
        return outputStream.toByteArray();
    }

    @SneakyThrows
    public byte[] writePage(String templateName) {
        return writePage(Collections.emptyMap(), templateName);
    }
}
