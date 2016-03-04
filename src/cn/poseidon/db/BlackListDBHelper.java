package cn.poseidon.db;
/*SQLiteOpenHelper是SQLiteDatabse的一个帮助类，用来管理数据的创建和版本更新。
 * 一般的用法是定义一个类继承SQLiteOpenHelper，并实现两个回调方法，
 * OnCreate(SQLiteDatabase db)
 * onUpgrade(SQLiteDatabse, int oldVersion, int newVersion)来创建和更新数据库*/
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
		//第一次创建表时执行
		db.execSQL(CREATE_TBL);
		Log.i("BlackListActivity.this", "BlackListActivity onCreate called.");	
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub	
	}

}
