package com.example.ethanwalker.xmlparse;

/**
 * Created by EthanWalker on 2017/6/10.
 */

import android.util.Log;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Created by EthanWalker on 2017/5/23.
 */

public class MyHandler extends DefaultHandler {
    String nodeName;
    StringBuilder id;
    StringBuilder name;
    StringBuilder version;

    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
        id=new StringBuilder();
        name = new StringBuilder();
        version = new StringBuilder();
    }

    @Override
    public void endDocument() throws SAXException {
        super.endDocument();
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);
        switch (nodeName){
            case "id":
                id.append(ch,start,length);
                break;
            case "name":
                name.append(ch,start,length);
                break;
            case "version":
                version.append(ch,start,length);
                break;
            default:
                break;
        }
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        nodeName = localName;
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);
        if("app".equals(localName)){
            Log.e("id=>",id.toString());
            Log.e("name=>",name.toString());
            Log.e("version=>",version.toString());
            id.setLength(0);
            name.setLength(0);
            version.setLength(0);
        }
    }
}
