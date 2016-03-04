package cn.poseidon.service;


import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import org.xmlpull.v1.XmlSerializer;

import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.os.Looper;
import android.util.Xml;
import android.widget.Toast;

import cn.poseidon.domain.SmsInfo;
import cn.poseidon.engine.SmsService;

public class BackupSmsService extends Service
{
        SmsService smsService;

        @Override
        public IBinder onBind(Intent intent)
        {
                return null;
        }
        
        @Override
        public void onCreate()
        {
                super.onCreate();
                smsService = new SmsService(this);
                
                new Thread()
                {
                        public void run() 
                        {
                                List<SmsInfo> infos = smsService.getSmsInfo();
                                File dir = new File(Environment.getExternalStorageDirectory(), "/security/backup");
                                if(!dir.exists())
                                {
                                        dir.mkdirs();
                                }
                                File file = new File(Environment.getExternalStorageDirectory() + "/security/backup/smsbackup.xml");
                                
                                //����һ��xml�����л���
                                XmlSerializer xmlSerializer = Xml.newSerializer();
                                try
                                {
                                        FileOutputStream fos = new FileOutputStream(file);
                                        //����дxml�ı���
                                        xmlSerializer.setOutput(fos, "utf-8");
                                        xmlSerializer.startDocument("utf-8", true);
                                        xmlSerializer.startTag(null, "smss");
                                        for(SmsInfo info : infos)
                                        {
                                                xmlSerializer.startTag(null, "sms");
                                                
                                                xmlSerializer.startTag(null, "id");
                                                xmlSerializer.text(info.getId());
                                                xmlSerializer.endTag(null, "id");
                                                
                                                xmlSerializer.startTag(null, "address");
                                                xmlSerializer.text(info.getAddress());
                                                xmlSerializer.endTag(null, "address");
                                                
                                                xmlSerializer.startTag(null, "date");
                                                xmlSerializer.text(info.getDate());
                                                xmlSerializer.endTag(null, "date");
                                                
                                                xmlSerializer.startTag(null, "type");
                                                xmlSerializer.text(info.getType() + "");
                                                xmlSerializer.endTag(null, "type");
                                                
                                                xmlSerializer.startTag(null, "body");
                                                xmlSerializer.text(info.getBody());
                                                xmlSerializer.endTag(null, "body");
                                                
                                                xmlSerializer.endTag(null, "sms");
                                        }
                                        xmlSerializer.endTag(null, "smss");
                                        xmlSerializer.endDocument();
                                        
                                        fos.flush();
                                        fos.close();
                                        
                                        //�����߳������ǲ��ܵ���һ��Toast�ģ���Ϊ���߳�����û��Looper��
                                        //������ͨ�����漸������Ϳ��������߳����浯��Toast��
                                        Looper.prepare();//����һ��Looper
                                        Toast.makeText(getApplicationContext(), "���ݳɹ�", Toast.LENGTH_SHORT).show();
                                        Looper.loop();//��ѭһ��Looper
                                }
                                catch (Exception e)
                                {
                                        Looper.prepare();//����һ��Looper
                                        Toast.makeText(getApplicationContext(), "����ʧ��", Toast.LENGTH_SHORT).show();
                                        Looper.loop();//��ѭһ��Looper
                                        e.printStackTrace();
                                }
                        }
                }.start();
        }

}
