package com.wy.dao;

import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

/** 
 * 描述：数据库操作类
 *
 * 作者: Liu wei
 * 
 * 邮箱：i@liuwei.co
 * 
 * 创建时间: 2013-5-30
 */
public abstract class DatabaseHelper extends OrmLiteSqliteOpenHelper{

	protected final String LOG_TAG = this.getClass().getSimpleName();
	
	protected Class<?> dataClass[] = null;

	/**
	 * 初始化需要创建的表
	 * 例如在子类中重载代码如下：
	 * super.dataClass = new Class<?>[]{ TABLE1.class, TABLE2.class };
	 */
	public abstract void initDataClass();
			
	public DatabaseHelper(Context context, String databaseName,CursorFactory factory, int databaseVersion) {
		super(context, databaseName, factory, databaseVersion);
		initDataClass();
	}

	/**
     * This is called when the database is first created. Usually you should call createTable statements here to create
     * the tables that will store your data.
     */
    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
            try {
                    Log.i(LOG_TAG, "onCreate");
                    if(dataClass!=null && dataClass.length>0){
                    	for(Class<?> clazz : dataClass){
                    		 TableUtils.createTable(connectionSource, clazz);
                    	}
                    }
                   
            } catch (SQLException e) {
                    Log.e(LOG_TAG, "Can't create database", e);
                    throw new RuntimeException(e);
            }
    }

    /**
     * This is called when your application is upgraded and it has a higher version number. This allows you to adjust
     * the various data to match the new version number.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
            try {
                    Log.i(LOG_TAG, "onUpgrade");
                    if(dataClass!=null && dataClass.length>0){
                    	for(Class<?> clazz : dataClass){
                    		TableUtils.dropTable(connectionSource, clazz, true);
                    	}
                    }
                    // after we drop the old databases, we create the new ones
                    onCreate(db, connectionSource);
            } catch (SQLException e) {
                    Log.e(LOG_TAG, "Can't drop databases", e);
                    throw new RuntimeException(e);
            }
    }
    
	public void clearData(){
		Log.i(LOG_TAG, "clearData");
        if(dataClass!=null && dataClass.length>0){
        	for(Class<?> clazz : dataClass){
        		try {
					TableUtils.clearTable(getConnectionSource(), clazz);
				} catch (SQLException e) {
					e.printStackTrace();
					continue;
				}
        	}
        }
	}
}
