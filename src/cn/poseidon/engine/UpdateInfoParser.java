package cn.poseidon.engine;

import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;

import android.util.Xml;

import cn.poseidon.domain.ContactInfo;

public class UpdateInfoParser
{
        
        public static ContactInfo getUpdateInfo(InputStream is) throws Exception
        {
                ContactInfo info = new ContactInfo();
                XmlPullParser xmlPullParser = Xml.newPullParser();
                xmlPullParser.setInput(is, "utf-8");
                int type = xmlPullParser.getEventType();
                while(type != XmlPullParser.END_DOCUMENT)
                {
                        switch(type)
                        {
                                case XmlPullParser.START_TAG :
                                        if(xmlPullParser.getName().equals("name"))
                                        {
                                                info.setName(xmlPullParser.nextText());
                                                System.out
														.println(info.getName());
                                        }
                                        else if(xmlPullParser.getName().equals("number"))
                                        {
                                                info.setPhone(xmlPullParser.nextText());
                                        }
                                        
                                default : 
                                        break;
                        }
                        type = xmlPullParser.next();
                }
                return info;
        }

}
