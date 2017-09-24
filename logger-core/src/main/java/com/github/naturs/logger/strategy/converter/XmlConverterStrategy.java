package com.github.naturs.logger.strategy.converter;

import com.github.naturs.logger.internal.Utils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * convert xml to format string.
 */
public class XmlConverterStrategy implements ConverterStrategy {
    /**
     * It is used for xml pretty print
     */
    private static final int XML_INDENT = 4;
    private static final int DEFAULT_PRIORITY = 700;

    private final int indent;

    public XmlConverterStrategy() {
        this(XML_INDENT);
    }

    public XmlConverterStrategy(int indent) {
        this.indent = indent;
    }

    @Override
    public String convert(@Nullable String message, @NotNull Object object, int level) {
        if (object instanceof String) {
            String xml = (String) object;
            xml = xml.trim();
            if (!xml.startsWith("<") || !xml.endsWith(">")) {
                return null;
            }
            try {
                Source xmlInput = new StreamSource(new StringReader(xml));
                StreamResult xmlOutput = new StreamResult(new StringWriter());
                Transformer transformer = TransformerFactory.newInstance().newTransformer();
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", String.valueOf(indent));
                transformer.transform(xmlInput, xmlOutput);
                return Utils.concat(message, xmlOutput.getWriter().toString().replaceFirst(">", ">\n"), DEFAULT_DIVIDER);
            } catch (TransformerException e) {
                return null;
            }
        }
        return null;
    }

    @Override
    public int priority() {
        return DEFAULT_PRIORITY;
    }
}
