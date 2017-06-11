package com.example.ethanwalker.xmlparse;

/**
 * Created by EthanWalker on 2017/6/10.
 */

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.example.ethanwalker.myapp.R;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class XmlParseTest extends AppCompatActivity {
    Button sendRequest;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.xml_parse);
        sendRequest = (Button) findViewById(R.id.send_request);
        textView = (TextView) findViewById(R.id.display_content);

        sendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendReq();
            }
        });
    }

    public void sendReq() {
        new Thread() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url("http:/10.25.140.251:88/get_data.xml")     // 注意：前面一定要加协议
                        .build();
                try {
                    Response response = client.newCall(request).execute();
                    String data = response.body().string();
//                    xmlPullParse(data);
//                  saxParse(data);
                    dom4jParse(data);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }.start();
    }

    /*将字符串转换成reader流对象： new StringReader(str)*/
    private void dom4jParse(String data) {
        String id = "";
        String name = "";
        String version = "";
        SAXReader saxReader = new SAXReader();
        try {
            Document document = saxReader.read(new StringReader(data));
            Element root = document.getRootElement();
            List<Element> nodes = root.elements();
            for (Iterator it = nodes.iterator(); it.hasNext(); ) {
                Element ele = (Element) it.next();
                if (ele.getName().equals("app")) {
                    List<Element> innerNodes = ele.elements();
                    for (Iterator itInner = innerNodes.iterator(); itInner.hasNext(); ) {
                        Element eleInner = (Element) itInner.next();
                        switch (eleInner.getName()) {
                            case "id":
                                id = eleInner.getText();
                                break;
                            case "name":
                                name = eleInner.getText();
                                break;
                            case "version":
                                version = eleInner.getText();
                                break;
                            default:
                                break;
                        }
                    }
                    Log.e("id/name/version： ", id + "/" + name + "/" + version);
                }
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }

    }

    private void saxParse(String data) {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        XMLReader reader;

        try {
            reader = factory.newSAXParser().getXMLReader();
            MyHandler myHandler = new MyHandler();
            reader.setContentHandler(myHandler);
            reader.parse(new InputSource(new StringReader(data)));
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void xmlPullParse(String data) {
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(data));
            int eventType = parser.getEventType();
            String id = "";
            String name = "";
            String version = "";

            while (eventType != XmlPullParser.END_DOCUMENT) {
                String nodeName = parser.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if (nodeName.equals("id")) {
                            id = parser.nextText();
                        } else if (nodeName.equals("name")) {
                            name = parser.nextText();
                        } else if (nodeName.equals("version")) {
                            version = parser.nextText();
                        } else {
                            break;
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if ("app".equals(nodeName)) {
                            Log.e("id=>", id);
                            Log.e("name=>", name);
                            Log.e("version=>", version);
                        }
                        break;
                    default:
                        break;
                }
                eventType = parser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

