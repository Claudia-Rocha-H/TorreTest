package com.torre.techtest.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class for handling HTML entity decoding.
 * 
 * Torre.ai API responses sometimes contain HTML-encoded entities (like &#x27; for apostrophes)
 * that need to be decoded for proper display in the frontend.
 */
public class HtmlUtils {
    
    /** Pattern to match numeric HTML entities (&#nnnn; and &#xHHHH;) */
    private static final Pattern NUMERIC_ENTITY_PATTERN = Pattern.compile("&#(x?)([0-9a-fA-F]+);");
    
    /** Map of common named HTML entities to their character equivalents */
    private static final Map<String, String> NAMED_ENTITIES = new HashMap<>();
    
    static {
        // Common named entities
        NAMED_ENTITIES.put("&amp;", "&");
        NAMED_ENTITIES.put("&lt;", "<");
        NAMED_ENTITIES.put("&gt;", ">");
        NAMED_ENTITIES.put("&quot;", "\"");
        NAMED_ENTITIES.put("&apos;", "'");
        NAMED_ENTITIES.put("&#39;", "'");
        NAMED_ENTITIES.put("&nbsp;", " ");
    }
    
    /**
     * Decodes HTML entities in the given text.
     * 
     * Handles both named entities (&amp;, &lt;, etc.) and numeric entities
     * (&#39;, &#x27;, etc.) commonly found in Torre.ai API responses.
     * 
     * @param text The text containing HTML entities to decode
     * @return The decoded text with HTML entities converted to their character equivalents
     */
    public static String decodeHtmlEntities(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        
        String result = text;
        
        // First, handle named entities
        for (Map.Entry<String, String> entity : NAMED_ENTITIES.entrySet()) {
            result = result.replace(entity.getKey(), entity.getValue());
        }
        
        // Then handle numeric entities (&#39; and &#x27; format)
        Matcher matcher = NUMERIC_ENTITY_PATTERN.matcher(result);
        StringBuffer sb = new StringBuffer();
        
        while (matcher.find()) {
            String isHex = matcher.group(1); // "x" if hexadecimal, empty if decimal
            String number = matcher.group(2);
            
            try {
                int charCode;
                if ("x".equals(isHex)) {
                    // Hexadecimal entity (&#x27;)
                    charCode = Integer.parseInt(number, 16);
                } else {
                    // Decimal entity (&#39;)
                    charCode = Integer.parseInt(number, 10);
                }
                
                // Convert to character and replace
                String replacement = String.valueOf((char) charCode);
                matcher.appendReplacement(sb, Matcher.quoteReplacement(replacement));
            } catch (NumberFormatException e) {
                // If parsing fails, leave the entity as-is
                matcher.appendReplacement(sb, Matcher.quoteReplacement(matcher.group(0)));
            }
        }
        matcher.appendTail(sb);
        
        return sb.toString();
    }
    
    /**
     * Safely decodes HTML entities, handling null values gracefully.
     * 
     * @param text The text to decode (can be null)
     * @return The decoded text, or null if input was null
     */
    public static String safeDecodeHtmlEntities(String text) {
        return text == null ? null : decodeHtmlEntities(text);
    }
}
