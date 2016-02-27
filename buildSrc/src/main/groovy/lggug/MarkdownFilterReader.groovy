package lggug

import org.pegdown.PegDownProcessor

public class MarkdownFilterReader extends FilterReader {
    MarkdownFilterReader(Reader reader) {
        super(parse(reader))
    }

    private static Reader parse(Reader reader) {
        def html = new PegDownProcessor().markdownToHtml(reader.text)
        new StringReader(html)
    }
}
