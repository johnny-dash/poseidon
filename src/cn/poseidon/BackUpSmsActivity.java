package cn.poseidon;

import java.io.File;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.poseidon.R;

import cn.poseidon.engine.SmsService;
import cn.poseidon.service.BackupSmsService;

public class BackUpSmsActivity extends Activity implements OnClickListener
{
        private static final int ERROR = 0;
        private static final int SUCCESS = 1;
        

        private ImageButton bt_backupButton;
        private ImageButton bt_restoreButton;

        private ProgressDialog pd;
        
        private SharedPreferences sp;
        
        @SuppressLint("HandlerLeak")
        private Handler handler = new Handler()
        {
                public void handleMessage(Message msg) 
                {
                        switch(msg.what)
                        {
                                case ERROR : 
                                        Toast.makeText(BackUpSmsActivity.this, "�������ݿ�ʧ�ܣ��������磡", Toast.LENGTH_SHORT).show();
                                        break;
                                        
                                case SUCCESS : 
                                        Toast.makeText(BackUpSmsActivity.this, "���ݿ����سɹ���", Toast.LENGTH_SHORT).show();
                                        break;
                                        
                                default : 
                                        break;
                        }
                }
        };
        
        @Override
        protected void onCreate(Bundle savedInstanceState)
        {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.backupsms_main);
                
                sp = getSharedPreferences("config", Context.MODE_PRIVATE);
                
                
                bt_backupButton = (ImageButton) findViewById(R.id.bt_backup);
                bt_backupButton.setOnClickListener(this);
                
                bt_restoreButton = (ImageButton) findViewById(R.id.bt_restore);
                bt_restoreButton.setOnClickListener(this);
                

               

        }

        @Override
        public void onClick(View v)
        {
                switch(v.getId())
                {
                                                       
                        case R.id.bt_backup : 
                                Intent backupIntent = new Intent(this, BackupSmsService.class);
                                startService(backupIntent);
                                break;
                                
                        case R.id.bt_restore : 
                                restore();
                                break;
                                
                        default : 
                                break;
                }
        }
        
        
        private boolean isDBExist()
        {
                if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
                {
                        File file = new File(Environment.getExternalStorageDirectory() + "/security/db/data.db");
                        if(file.exists())
                        {
                                return true;
                        }
                }
                return false;
        }
        
        
        private void query()
        {
                if(isDBExist())
                {
                        //Intent intent = new Intent(this, QueryNumberActivity.class);
                        //startActivity(intent);
                }
                else
                {
                        //��ʾ�û��������ݿ�
                        pd = new ProgressDialog(this);
                        pd.setMessage("�����������ݿ�...");
                        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                        pd.setCancelable(false);
                        pd.show();
                        new Thread()
                        {
                                public void run() 
                                {
                                        //String path = getResources().getString(R.string.serverdb);
                                        File dir = new File(Environment.getExternalStorageDirectory(), "/security/db");
                                        if(!dir.exists())
                                        {
                                                dir.mkdirs();
                                        }
                                        String dbPath = Environment.getExternalStorageDirectory() + "/security/db/data.db";
                                        try
                                        {
                                                //����࣬������������apk��ʱ���Ѿ�д�õ���������ֱ���ù����þͿ�����
                                                //DownloadTask.getFile(path, dbPath, pd);
                                                pd.dismiss();
                                        }
                                        catch (Exception e)
                                        {
                                                e.printStackTrace();
                                                pd.dismiss();
                                                Message message = new Message();
                                                message.what = ERROR;
                                                handler.sendMessage(message);
                                        }
                                };
                        }.start();
                }
        }
        


        
        private void restore()
        {
                final ProgressDialog pd = new ProgressDialog(this);
                pd.setTitle("��ԭ����");
                pd.setMessage("���ڻ�ԭ����...");
                pd.setCancelable(false);
                pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                pd.show();
                final SmsService smsService = new SmsService(this);
                new Thread()
                {
                        public void run() 
                        {
                                try
                                {
                                        smsService.restore(Environment.getExternalStorageDirectory() + "/security/backup/smsbackup.xml", pd);
                                        pd.dismiss();
                                        Looper.prepare();//����һ��Looper
                                        Toast.makeText(getApplicationContext(), "��ԭ�ɹ�", Toast.LENGTH_SHORT).show();
                                        Looper.loop();//��ѭһ��Looper
                                }
                                catch (Exception e)
                                {
                                        e.printStackTrace();
                                        Looper.prepare();//����һ��Looper
                                        Toast.makeText(getApplicationContext(), "��ԭʧ��", Toast.LENGTH_SHORT).show();
                                        Looper.loop();//��ѭһ��Looper
                                }
                        }
                }.start();
        }

}