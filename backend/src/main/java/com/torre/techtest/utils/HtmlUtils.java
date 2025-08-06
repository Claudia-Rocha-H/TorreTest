package com.torre.techtest.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility for HTML entity decoding in Torre.ai API responses
 */
public class HtmlUtils {
    
    private static final Pattern NUMERIC_ENTITY_PATTERN = Pattern.compile("&#(x?)([0-9a-fA-F]+);");
    private static final Map<String, String> NAMED_ENTITIES = new HashMap<>();
    
    static {
        NAMED_ENTITIES.put("&amp;", "&");
        NAMED_ENTITIES.put("&lt;", "<");
        NAMED_ENTITIES.put("&gt;", ">");
        NAMED_ENTITIES.put("&quot;", "\"");
        NAMED_ENTITIES.put("&apos;", "'");
        NAMED_ENTITIES.put("&#39;", "'");
        NAMED_ENTITIES.put("&nbsp;", " ");
    }

    /**
     * Decodes HTML entities in text
     */
    public static String decodeHtmlEntities(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        
        String result = text;
        
        for (Map.Entry<String, String> entity : NAMED_ENTITIES.entrySet()) {
            result = result.replace(entity.getKey(), entity.getValue());
        }
        
        Matcher matcher = NUMERIC_ENTITY_PATTERN.matcher(result);
        StringBuffer sb = new StringBuffer();
        
        while (matcher.find()) {
            String isHex = matcher.group(1);
            String number = matcher.group(2);
            
            try {
                int charCode;
                if ("x".equals(isHex)) {
                    charCode = Integer.parseInt(number, 16);
                } else {
                    charCode = Integer.parseInt(number, 10);
                }
                
                String replacement = String.valueOf((char) charCode);
                matcher.appendReplacement(sb, Matcher.quoteReplacement(replacement));
            } catch (NumberFormatException e) {
                matcher.appendReplacement(sb, Matcher.quoteReplacement(matcher.group(0)));
            }
        }
        matcher.appendTail(sb);
        
        return sb.toString();
    }
    
    /**
     * Safely decodes HTML entities with null handling
     */
    public static String safeDecodeHtmlEntities(String text) {
        return text == null ? null : decodeHtmlEntities(text);
    }
}
