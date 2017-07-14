package com.jesushghar.uss.helper;

import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by mukul on 23-04-2016.
 */
public class XmlParser {

    public Document getDomElement(String xml) {
        Document doc = null;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();

            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(xml));
            doc = db.parse(is);

        } catch (ParserConfigurationException e) {
            Log.e("Error: ", e.getMessage());
            return null;
        } catch (SAXException e) {
            Log.e("Error: ", e.getMessage());
            return null;
        } catch (IOException e) {
            Log.e("Error: ", e.getMessage());
            return null;
        }
        return doc;
    }

    public String getValue(Element item, String string) {
        NodeList n = item.getElementsByTagName(string);
        return this.getElementValue(n.item(0));
    }

    public final String getElementValue(Node ele) {
        Node child;
        if (ele!= null) {
            if (ele.hasChildNodes()) {
                for (child = ele.getFirstChild(); child != null; child=child.getNextSibling()) {
                    return child.getNodeValue();
                }
            }
        }
        return "";
    }

}
