package cn.poseidon;

import java.util.ArrayList;
import java.util.List;

import com.example.poseidon.R;

import cn.poseidon.dao.BlackListDAO;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class BlackListActivity extends Activity {


	private BlackListDAO dao;
	private ImageView addview;
	private ImageView contactview;
	private EditText et;
	private ListView lv;
	private List<String> numbers;
	private blacknoAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//���ر�����
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//����״̬��	
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.blacklist);

		dao = new BlackListDAO(BlackListActivity.this);
		addview = (ImageView) findViewById(R.id.bt_add_bl_num);
		contactview = (ImageView) findViewById(R.id.imb_bl_contacts);	
		et = (EditText)findViewById(R.id.ed_blacklist);		
		lv = (ListView)findViewById(R.id.lv_blacklist);		

		//��listviewע�������Ĳ˵�		
		registerForContextMenu(lv);
		
		lv.setOnItemClickListener(itemlistener);

		//�����buttonע������¼�
		addview.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.i("BlackListActivity.this", "button�����¼�����.");
				String number = et.getText().toString().trim();
				if(number.length()>0) {
					String match = "\\d{5,}";//����5λ����
					if (number.matches(match)) {
						if(dao != null) {
							dao.addNo(number);
							numbers.clear();
							numbers = null;
							numbers = dao.getallNo();							
							Log.i("BlackListActivity.this", "listview��������֮ǰ.");
							//֪ͨlistview��������
							adapter.notifyDataSetChanged();	
							Log.i("BlackListActivity.this", "listview����֮ǰ.");
						}
					}
					else {
						Toast.makeText(BlackListActivity.this, "�����ʽ�Ƿ�", 0).show();	
					}
				}
				else {
					Toast.makeText(BlackListActivity.this, "����5λ����", 0).show();					
				}
			}
		});

		//��ѡ����ϵ��imagebuttonע������¼�
		contactview.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intentcontact = new Intent(BlackListActivity.this,SelectContactActivity.class);
				Toast.makeText(BlackListActivity.this, "����ϵ����ѡ��", 0).show();
				startActivityForResult(intentcontact,0);
				Log.i("BlackListActivity", "imagebutton�����¼�����");				
			}			
		});		

		numbers = dao.getallNo();
		adapter = new blacknoAdapter();
		lv.setAdapter(adapter);
	}

	OnItemClickListener itemlistener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			Toast.makeText(BlackListActivity.this,"onclick",1).show();
		}		
	};
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == 0){
			if(data != null){
				Bundle bundle = data.getExtras();
				String number = bundle.getString("number");
				//��ʾ��13667687342��
				dao.addNo(number);	
				numbers.clear();
				numbers = null;
				numbers = dao.getallNo();
				//֪ͨlistview��������
				adapter.notifyDataSetChanged();	
				Log.i("BlackListActivity.this", "imagebutton�����¼�����֮��.");
			}
		}
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		super.onCreateContextMenu(menu, v, menuInfo);
		//�Ѳ˵�xml��Դת��Ϊ���󣬲���ӵ�menu������
		MenuInflater inflater = getMenuInflater();
		Log.i("onCreateContextMenu", "��blacklistmenu����");	
		inflater.inflate(R.menu.blacklistmenu, menu);
	}
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		//��õ�ǰ��ѡ�е�item����Ϣ		
		AdapterContextMenuInfo info = (AdapterContextMenuInfo)item.getMenuInfo();
		System.out.println("1");
		Log.i("onContextItemSelected", "onContextItemSelected���֮��.");		
		if(dao == null)  {
			Toast.makeText(this, "���ݿ����", 0).show();			
		}		
		else {
			AlertDialog.Builder builder;
			System.out.println("2");
			switch (item.getItemId()) {
			//�޸ĺ���
			case R.id.bl_menu_up:
				final String oldNo = numbers.get(info.position);
				builder = new Builder(BlackListActivity.this);
				final EditText et_no = new EditText(BlackListActivity.this);
				builder.setView(et_no);
				//�޸İ�ť
				builder.setPositiveButton("�޸�", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int which){
						String newNo = et_no.getText().toString().trim();
						String match = "\\d{5,}";
						if (newNo.matches(match)) {
							dao.updateNo(oldNo,newNo);
							numbers.clear();
							numbers = null;
							numbers = dao.getallNo();
							adapter.notifyDataSetChanged();
						}
						else {
							Toast.makeText(BlackListActivity.this,"����5λ����",0).show();
						}						
					}	
				});
				//ȡ����ť		
				builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int which){					
					}	
				});
				break;

				//ɾ������	
			case R.id.bl_menu_del:
				System.out.println("3");
				dao.delNo(numbers.get(info.position));
				numbers.clear();
				numbers = null;
				numbers = dao.getallNo();
				adapter.notifyDataSetChanged();
				break;

				//����ɾ��	
			case R.id.bl_menu_batch:
				builder = new Builder(BlackListActivity.this);
				AlertDialog dialog ;
				builder.setTitle("����ɾ������");
				builder.setIcon(R.drawable.black_delete);
				int size = numbers.size();
				//ArrayList ת��Ϊ����   
				//arrNos:����ÿ������    arrChked������ÿ�������Ƿ�ѡ�У�Ĭ��ֵΪ0
				final String[] arrNos = (String[]) numbers.toArray(new  String[size]);	
				final boolean[] arrChked = new boolean[size];
				for (int i = 0; i < arrChked.length; i++) {
					arrChked[i] = false;
				}
				//��ѡ��
				builder.setMultiChoiceItems(arrNos, arrChked, new OnMultiChoiceClickListener() {
					public void onClick(DialogInterface dialog, int which, boolean isChecked) {
						arrChked[which] = isChecked;
					}
				});
				//ȷ��
				builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						//chedNos:ѡ�еĺ���
						ArrayList<String> chedNos = new ArrayList<String>();
						for (int i = 0; i < arrChked.length; i++) {
							if (arrChked[i]) {
								chedNos.add(arrNos[i]);
							}
						}
						if (chedNos.size() > 0) {
							dao.delNos(chedNos);
							numbers.clear();
							numbers = null;
							numbers = dao.getallNo();
							adapter.notifyDataSetChanged();
						}
						else {
							Toast.makeText(BlackListActivity.this, "δѡ���κ�ѡ��", 0).show();
						}

					}
				});
				builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

					}
				});
				builder.create().show();
				break;

				//��պ�����	
			case R.id.bl_menu_wp:
				dao.removeAll();
				numbers.clear();
				numbers.add("������Ϊ��");
				adapter.notifyDataSetChanged();
				break;

			default:
				break;				
			}
		}
		return super.onContextItemSelected(item);
	}

	public class blacknoAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return numbers.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return numbers.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View view = View.inflate(BlackListActivity.this, R.layout.blacklist_item, null);
			TextView tv = (TextView) view.findViewById(R.id.tv_bl_item);
			tv.setText(numbers.get(position));
			return view;
		}

	}
}
