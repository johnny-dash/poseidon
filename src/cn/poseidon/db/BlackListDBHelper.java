package cn.poseidon.db;
/*SQLiteOpenHelper��SQLiteDatabse��һ�������࣬�����������ݵĴ����Ͱ汾���¡�
 * һ����÷��Ƕ���һ����̳�SQLiteOpenHelper����ʵ�������ص�������
 * OnCreate(SQLiteDatabase db)
 * onUpgrade(SQLiteDatabse, int oldVersion, int newVersion)�������͸������ݿ�*/
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class BlackListDBHelper extends SQLiteOpenHelper {
	
	private static final String TAG = "BlackListDBHelper";
	private static final int VERSION = 1;
	private static final String DBNAME = "blacklist.db";
	private final static String CREATE_TBL = "create table blacklist(id integer primary key autoincrement, number varchar(20));";
	
	public BlackListDBHelper(Context context) {
		super(context, DBNAME, null, VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		//��һ�δ�����ʱִ��
		db.execSQL(CREATE_TBL);
		Log.i("BlackListActivity.this", "BlackListActivity onCreate called.");	
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub	
	}

}
