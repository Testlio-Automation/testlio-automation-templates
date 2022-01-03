package com.testlio.lib.utility;

import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;
import java.io.StringReader;

import static java.lang.String.valueOf;
import static javax.xml.xpath.XPathConstants.NODESET;

@Slf4j
public class XPathUtil {

    public static XPath xPath = XPathFactory.newInstance().newXPath();
	public static DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    public static DocumentBuilder builder;

    private XPathUtil() {}

	static {
		try {
			builder = factory.newDocumentBuilder();
			factory.setNamespaceAware(true);
		} catch (ParserConfigurationException e) {
			log.warn("Could not create javax.xml.parsers.DocumentBuilder");
		}
	}

	public static String getAttributeNodeValueForElement(String pageSource, String expression, String attributeName,
			String targetValue, boolean shouldReplaceFileName) {
		try {
			pageSource = pageSource.toLowerCase();
			expression = valueOf(expression).trim().toLowerCase();
			attributeName = valueOf(attributeName).trim().toLowerCase();
			targetValue = valueOf(targetValue).toLowerCase();

			InputSource inputSource = new InputSource(new StringReader(pageSource));
			Document doc = builder.parse(inputSource);
			NodeList list = (NodeList) xPath.evaluate(expression, doc, NODESET);
			for (int i = 0; i < list.getLength(); i++) {
				Node node = list.item(i);
				Element element = (Element) node;
				Attr attributeNode = element.getAttributeNode(attributeName);
				String value = attributeNode.getValue();
				if (shouldReplaceFileName) {
					value = FileNameUtil.makeValidFileOrDirectoryName(value);
				}
				if (value.contains(targetValue)) {
					log.info("Found node needed [" + node.toString() + "]");
					return value;
				}
			}
		} catch (Exception e) {
			log.warn("Couldn't find " + expression + " element with " + attributeName
					+ " attributeNode due to Exception " + e.getClass().getName());
		}
		return "";
	}

	public static String getAttributeNodeValueForElement(String pageSource, String expression, String attributeName,
			String targetValue) {
		return getAttributeNodeValueForElement(pageSource, expression, attributeName, targetValue, false);
	}

}
