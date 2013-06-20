package com.example.examscheduler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ExamDatabase {

	public static final String KEY_ROW_ID = "_id";
	public static final String KEY_COURSE_NAME = "Course_name";
	public static final String KEY_DATE = "Date";
	public static final String KEY_DAY = "Day";
	public static final String KEY_START_TIME = "Start_time";
	public static final String KEY_END_TIME= "End_time";
	
	private static final String DATABASE_TABLE = "Exam";
	private static final String DATABASE_NAME = "Exam_Schedule";
	private static final int DATABASE_VERSION = 1;
	
	private dbHelper ourhelper;
	private Context ourcontext;
	private SQLiteDatabase exam_database;
	
private static class dbHelper extends SQLiteOpenHelper {

		

		public dbHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			db.execSQL("CREATE TABLE " + DATABASE_TABLE + " (" + KEY_ROW_ID
					+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
					+ KEY_COURSE_NAME + " TEXT, "
					+ KEY_DATE + " TEXT, "
					+ KEY_DAY + " TEXT, "
					+ KEY_START_TIME + " TEXT, "
					+ KEY_END_TIME + " TEXT );");
			

		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
			onCreate(db);
		}

	}

	public ExamDatabase(Context c) {
		ourcontext = c;
	}

	public ExamDatabase open() throws SQLException {
		ourhelper = new dbHelper(ourcontext);
		exam_database = ourhelper.getWritableDatabase();
		return this;
	}

	public void close() {
		ourhelper.close();
	}
// creates an entry in database
	public long createEntry ( String cname, String date, String day, String stime, String etime	)throws Exception {
		// TODO Auto-generated method stub
		ContentValues cv = new ContentValues();
		cv.put(KEY_COURSE_NAME , cname);
		cv.put(KEY_DATE , date);
		cv.put(KEY_DAY , day);
		cv.put(KEY_START_TIME , stime);
		cv.put(KEY_END_TIME , etime);
	
		return exam_database.insertOrThrow(DATABASE_TABLE, null, cv);
	}
	
	
//returns all data in table
	public String getdata() {
		// TODO Auto-generated method stub
		String[] columns ={KEY_ROW_ID,KEY_COURSE_NAME,KEY_DATE,KEY_DAY,KEY_START_TIME,KEY_END_TIME}; 
		Cursor c = exam_database.query(DATABASE_TABLE, columns, null, null, null, null, null);
		
		
		String result = "";
		
		for(c.moveToFirst();!(c.isAfterLast());c.moveToNext())
		{
			for(int i=0;i<columns.length;i++)
			{
			result = result + c.getString(i)+ "\t";
					
			}
			result = result + "\n";
		}
		return result;
	}

	public String GetCourseDetailById(long l) {
		// TODO Auto-generated method stub
		String[] columns ={KEY_ROW_ID,KEY_COURSE_NAME,KEY_DATE,KEY_DAY,KEY_START_TIME,KEY_END_TIME}; 
		Cursor c = exam_database.query(DATABASE_TABLE, columns, KEY_ROW_ID + "=" + l, null, null, null, null);
		if(c!=null && c.moveToFirst())
		{
			
			String name="";
			for(int i=1;i<columns.length;i++)
			{
				name = name + c.getString(i) + "-"  ;
			}
			return name;
		}
		return null;
	}
	public String GetCourseNameById(long l) {
		// TODO Auto-generated method stub
		String[] columns ={KEY_ROW_ID,KEY_COURSE_NAME,KEY_DATE,KEY_DAY,KEY_START_TIME,KEY_END_TIME}; 
		Cursor c = exam_database.query(DATABASE_TABLE, columns, KEY_ROW_ID + "=" + l, null, null, null, null);
		if(c!=null)
		{
			c.moveToFirst();
			String name = c.getString(1);
			return name;
		}
		return null;
	}
	public String GetAllCourses(){
		String[] columns ={KEY_ROW_ID,KEY_COURSE_NAME,KEY_DATE,KEY_DAY,KEY_START_TIME,KEY_END_TIME}; 
		Cursor c = exam_database.query(DATABASE_TABLE, columns, null, null, null, null, null);
		
		int iName = c.getColumnIndex(KEY_COURSE_NAME);
		String result = "";
		if (c.getCount()>0)
		{
			
			for(c.moveToFirst();!(c.isAfterLast());c.moveToNext())
			{
				result = result + c.getString(iName)+ "-" ;
			
			}
			return result; 
		}
		return null;
		
	}

	/*public void UpdateCourse(long l, String cname, String insname, String welink, String offhradd,String lect_d,
			String lect_St,
			String lect_Et,String tut_d,String tut_St,String tut_Et,String lab_d,String lab_St,
			String lab_Et,String lec_v,String lab_v,String tut_v,String hdata,String emailid 	) {
		// TODO Auto-generated method stub
		ContentValues cv = new ContentValues();
		cv.put(KEY_COURSE_NAME , cname);
		cv.put(KEY_INSTRUCTOR , insname);
		cv.put(KEY_WEBLINK , welink);
		cv.put(KEY_LAB_DAY , lab_d);
		cv.put(KEY_OFFICE_HR_ADD , offhradd);
		cv.put(KEY_LAB_STIME , lab_St);
		cv.put(KEY_LAB_ETIME , lab_Et);
		cv.put(KEY_LECT_DAY , lect_d);
		cv.put(KEY_LECT_STIME , lect_St);
		cv.put(KEY_LECT_ETIME , lect_Et);
		cv.put(KEY_TUT_DAY , tut_d);
		cv.put(KEY_TUT_STIME , tut_St);
		cv.put(KEY_TUT_ETIME , tut_Et);
		cv.put(KEY_LEC_VENUE , lec_v);
		cv.put(KEY_LAB_VENUE , lab_v);
		cv.put(KEY_TUT_VENUE , tut_v);
		cv.put(KEY_WEB_DETAIL , hdata);
		cv.put(KEY_EMAIL , emailid);
		
		course_database.update(DATABASE_TABLE, cv,KEY_ROW_ID +"="+ l, null);
		return;
		
	}*/
	
	public void KeyRowIdUpdate(){
		ContentValues cv = new ContentValues();
		String[] columns ={KEY_ROW_ID,KEY_COURSE_NAME,KEY_DATE,KEY_DAY,KEY_START_TIME,KEY_END_TIME}; 
		
		Cursor c = exam_database.query(DATABASE_TABLE, columns,null, null, null, null, null);
		int m=1;
		for(c.moveToFirst();!(c.isAfterLast());c.moveToNext())
		{
			cv.put(KEY_ROW_ID, m);
			exam_database.update(DATABASE_TABLE, cv, KEY_ROW_ID +"="+ c.getString(0), null);
			m++;
		}
	}

	public void deleteEntry(int m) {
		// TODO Auto-generated method stub
		
		exam_database.delete(DATABASE_TABLE, KEY_ROW_ID + "="+m, null);
		KeyRowIdUpdate();		
	}

	public int getCount() {
		// TODO Auto-generated method stub
		String[] columns ={KEY_ROW_ID,KEY_COURSE_NAME,KEY_DATE,KEY_DAY,KEY_START_TIME,KEY_END_TIME}; 
		Cursor c = exam_database.query(DATABASE_TABLE, columns, null, null, null, null, null);
		int i = c.getCount();
		
		return i;
	}
	
}
