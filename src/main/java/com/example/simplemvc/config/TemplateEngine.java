package com.example.simplemvc.config;

import java.nio.file.*;
import java.util.*;
import java.util.regex.*;

public class TemplateEngine {
    private String templatesPath;

    public TemplateEngine(String templatesPath) {
        this.templatesPath = templatesPath;
    }

    public String render(String templateFile, Map<String, Object> variables) throws Exception {
        String content = Files.readString(Paths.get(templatesPath, templateFile));

        // Includes
        Pattern includePattern = Pattern.compile("@include\\(\"([^\"]+)\"\\)");
        Matcher m = includePattern.matcher(content);
        while (m.find()) {
            String includeFile = m.group(1);
            String includeContent = render(includeFile, variables);
            content = content.replace(m.group(0), includeContent);
        }

        // Variables simples
        Pattern varPattern = Pattern.compile("@var\\(([a-zA-Z0-9_]+)\\)");
        Matcher varMatcher = varPattern.matcher(content);
        while (varMatcher.find()) {
            String varName = varMatcher.group(1);
            Object value = variables.getOrDefault(varName, "");
            content = content.replace(varMatcher.group(0), value.toString());
        }

        // Loops @foreach(item in items) ... @endforeach
        Pattern foreachPattern = Pattern.compile("@foreach\\(([a-zA-Z0-9_]+) in ([a-zA-Z0-9_]+)\\)([\\s\\S]*?)@endforeach");
        Matcher foreachMatcher = foreachPattern.matcher(content);
        while (foreachMatcher.find()) {
            String itemName = foreachMatcher.group(1);
            String listName = foreachMatcher.group(2);
            String block = foreachMatcher.group(3);

            Object listObj = variables.get(listName);
            StringBuilder blockResult = new StringBuilder();
            if (listObj instanceof Iterable) {
                for (Object item : (Iterable<?>) listObj) {
                    Map<String, Object> loopVars = new HashMap<>(variables);
                    loopVars.put(itemName, item);
                    blockResult.append(renderString(block, loopVars));
                }
            }
            content = content.replace(foreachMatcher.group(0), blockResult.toString());
        }

        // Condiciones simples @if(varName) ... @endif
        Pattern ifPattern = Pattern.compile("@if\\(([a-zA-Z0-9_]+)\\)([\\s\\S]*?)@endif");
        Matcher ifMatcher = ifPattern.matcher(content);
        while (ifMatcher.find()) {
            String varName = ifMatcher.group(1);
            String block = ifMatcher.group(2);
            Object value = variables.get(varName);
            String blockResult = "";
            if (value instanceof Boolean && (Boolean) value) {
                blockResult = renderString(block, variables);
            }
            content = content.replace(ifMatcher.group(0), blockResult);
        }

        return content;
    }

    // Método auxiliar para renderizar bloques internos
    private String renderString(String content, Map<String, Object> variables) throws Exception {
        // Variables simples dentro del bloque
        Pattern varPattern = Pattern.compile("@var\\(([a-zA-Z0-9_]+)\\)");
        Matcher varMatcher = varPattern.matcher(content);
        while (varMatcher.find()) {
            String varName = varMatcher.group(1);
            Object value = variables.getOrDefault(varName, "");
            content = content.replace(varMatcher.group(0), value.toString());
        }
        return content;
    }
}
