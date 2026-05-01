package com.torre.techtest.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.Test;

class HtmlUtilsTest {

    @Test
    void decodeAll() {
        String value = HtmlUtils.decodeHtmlEntities("Ana &amp; Ruiz &#39;Senior&#39; &#x27;Java&#x27;");

        assertEquals("Ana & Ruiz 'Senior' 'Java'", value);
    }

    @Test
    void nullInput() {
        assertNull(HtmlUtils.safeDecodeHtmlEntities(null));
    }

    @Test
    void emptyString() {
        String value = HtmlUtils.decodeHtmlEntities("");
        assertEquals("", value);
    }

    @Test
    void noEntities() {
        String value = HtmlUtils.decodeHtmlEntities("Plain text without entities");
        assertEquals("Plain text without entities", value);
    }

    @Test
    void onlySpaces() {
        String value = HtmlUtils.decodeHtmlEntities("   ");
        assertEquals("   ", value);
    }

    @Test
    void consecutiveEntities() {
        String value = HtmlUtils.decodeHtmlEntities("&amp;&amp;&lt;&gt;");
        assertEquals("&&<>", value);
    }

    @Test
    void singleEntity() {
        String value = HtmlUtils.decodeHtmlEntities("&lt;");
        assertEquals("<", value);
    }

    @Test
    void mixedContent() {
        String value = HtmlUtils.decodeHtmlEntities("Start &amp; middle &lt;tag&gt; end");
        assertEquals("Start & middle <tag> end", value);
    }

    @Test
    void unicodeHex() {
        String value = HtmlUtils.decodeHtmlEntities("&#x003C;script&#x003E;");
        assertEquals("<script>", value);
    }
}
