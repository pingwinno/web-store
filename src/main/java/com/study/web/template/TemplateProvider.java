package com.study.web.template;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import lombok.SneakyThrows;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.util.Map;

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
    public byte[] writePage(Map map, String templateName) {
        var outputStream = new ByteArrayOutputStream();
        var writer = new OutputStreamWriter(outputStream);
        Template temp = cfg.getTemplate(templateName);
        temp.process(map, writer);
        return outputStream.toByteArray();
    }

    @SneakyThrows
    public byte[] writePage(String templateName) {
        var outputStream = new ByteArrayOutputStream();
        var writer = new OutputStreamWriter(outputStream);
        Template temp = cfg.getTemplate(templateName);
        temp.process(Map.of(), writer);
        return outputStream.toByteArray();
    }
}
