package com.tsoft.util.sax;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class XMLParser extends DefaultHandler {
    private ElementStack elementStack = new ElementStack();
    private ElementListener listener;

    public XMLParser(ElementListener listener) {
        this.listener = listener;
    }

    public void parse(String fileName) throws ParserConfigurationException, SAXException, IOException {
        File file = new File(fileName);
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setNamespaceAware(true);
        SAXParser parser = factory.newSAXParser();
        parser.parse(file, this);
    }

    @Override
    public void startElement(String namespaceURI, String lname, String qname, Attributes attrs) throws SAXException {
        elementStack.push(lname);
        listener.afterPushElement(lname, elementStack);
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        elementStack.pop();
        listener.afterPopElement(localName, elementStack);
    }

    @Override
    public void characters(char ch[], int start, int length) throws SAXException {
        String value = String.copyValueOf(ch, start, length);
        listener.processValue(value, elementStack);
    }
}
