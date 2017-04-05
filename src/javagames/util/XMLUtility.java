package javagames.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * This class provides methods for reading xml documents.
 *
 * @author Timothy Wright
 *
 */
public class XMLUtility {

  public static Document parseDocument(final InputStream inputStream)
      throws ParserConfigurationException, SAXException, IOException {
    final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    final DocumentBuilder builder = factory.newDocumentBuilder();
    final Document document = builder.parse(new InputSource(inputStream));
    return document;
  }

  public static Document parseDocument(final Reader reader)
      throws ParserConfigurationException, SAXException, IOException {
    final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    final DocumentBuilder builder = factory.newDocumentBuilder();
    final Document document = builder.parse(new InputSource(reader));
    return document;
  }

  public static List<Element> getAllElements(final Element element,
      final String tagName) {
    final ArrayList<Element> elements = new ArrayList<>();
    final NodeList nodes = element.getElementsByTagName(tagName);
    for (int i = 0; i < nodes.getLength(); i++) {
      elements.add((Element) nodes.item(i));
    }
    return elements;
  }

  public static List<Element> getElements(final Element element,
      final String tagName) {
    final ArrayList<Element> elements = new ArrayList<>();
    final NodeList children = element.getChildNodes();
    for (int i = 0; i < children.getLength(); i++) {
      final Node node = children.item(i);
      if (node.getNodeType() == Node.ELEMENT_NODE) {
        final String nodeName = node.getNodeName();
        if ((nodeName != null) && nodeName.equals(tagName)) {
          elements.add((Element) node);
        }
      }
    }
    return elements;
  }

}