package com.blog.utils;

import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;

import java.util.regex.Pattern;

/**
 * HTML内容清理工具类
 * 用于清理富文本内容，防止XSS攻击
 *
 * @author xuesong.lei
 * @since 2025-09-16
 */
public final class HtmlSanitizer {

    // 匹配危险标签及其内容（如 <script>...</script>）
    private static final Pattern DANGEROUS_TAG_PATTERN = Pattern.compile(
            "<\\s*(script|iframe|object|embed|form|input|textarea|button|select|meta|link|base|applet|svg|math)"
                    + "(\\s[^>]*)?>.*?</\\s*\\1\\s*>",
            Pattern.CASE_INSENSITIVE | Pattern.DOTALL
    );

    // 匹配残留的危险开标签或自闭合标签
    private static final Pattern DANGEROUS_OPEN_TAG_PATTERN = Pattern.compile(
            "<\\s*/?(script|iframe|object|embed|form|input|textarea|button|select|meta|link|base|applet|svg|math)(\\s[^>]*)?>",
            Pattern.CASE_INSENSITIVE
    );

    // 匹配事件处理属性（如 onerror="...", onclick="..."），支持有引号和无引号两种写法
    private static final Pattern EVENT_ATTR_PATTERN = Pattern.compile(
            "(<[^>]*?)\\s+on\\w+\\s*=\\s*(?:([\"'])[^\"']*\\2|[^\\s>]+)",
            Pattern.CASE_INSENSITIVE
    );

    // 匹配 javascript: 协议（有引号和无引号）
    private static final Pattern JS_PROTOCOL_PATTERN = Pattern.compile(
            "(<[^>]*?(?:href|src|action)\\s*=\\s*)(?:[\"']\\s*javascript\\s*:[^\"']*[\"']|javascript\\s*:[^\\s>]*)",
            Pattern.CASE_INSENSITIVE
    );

    private HtmlSanitizer() {
        // 私有构造函数，防止实例化
    }

    /**
     * 清理Markdown内容中的危险HTML标签
     * 仅移除 script、iframe、object、embed 等危险标签，保留Markdown原文格式（换行、特殊字符等）
     *
     * @param markdown 原始Markdown内容
     * @return 清理后的Markdown内容，如果输入为null或空则原样返回
     */
    public static String sanitizeMarkdown(String markdown) {
        if (markdown == null || markdown.isEmpty()) {
            return markdown;
        }
        // 移除危险的HTML标签（包括标签内的内容）
        String result = DANGEROUS_TAG_PATTERN.matcher(markdown).replaceAll("");
        // 移除残留的危险自闭合/开标签
        result = DANGEROUS_OPEN_TAG_PATTERN.matcher(result).replaceAll("");
        // 移除事件属性（如 onerror, onclick 等）
        result = EVENT_ATTR_PATTERN.matcher(result).replaceAll("$1");
        // 移除 javascript: 协议
        result = JS_PROTOCOL_PATTERN.matcher(result).replaceAll("$1");
        return result;
    }

    /**
     * 清理为纯文本（移除所有HTML标签）
     * 适用于需要提取纯文本内容的场景，如搜索索引、摘要生成等
     *
     * @param html 原始HTML内容
     * @return 纯文本内容，如果输入为null或空则原样返回
     */
    public static String toPlainText(String html) {
        if (html == null || html.isEmpty()) {
            return html;
        }
        return Jsoup.clean(html, Safelist.none());
    }
}
