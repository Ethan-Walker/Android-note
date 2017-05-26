package com.example.ethanwalker.mycontentprovider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.net.Uri;
import android.util.Log;

public class DBContentProvider extends ContentProvider {

    public static final int BOOK_DIR = 0;
    public static final int BOOK_ITEM = 1;
    public static final int CATEGORY_DIR = 2;
    public static final int CATEGORY_ITEM = 3;
    public static final String AUTHORITY = "com.example.ethanwalker.mycontentprovider.provider";
    private static UriMatcher uriMatcher;
    private MyDatabaseHelper dbHelper;
    private static final String TAG = "DBContentProvider";
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, "book", BOOK_DIR);
        uriMatcher.addURI(AUTHORITY, "book/#", BOOK_ITEM);
        uriMatcher.addURI(AUTHORITY, "category", CATEGORY_DIR);
        uriMatcher.addURI(AUTHORITY, "category/#", CATEGORY_ITEM);
    }

    public DBContentProvider() {
    }
    // 程序启动时，就会调用
    @Override
    public boolean onCreate() {
        // TODO: Implement this to initialize your content provider on startup.
        Log.e(TAG, "onCreate: ");

        dbHelper = new MyDatabaseHelper(getContext(), "BookStore.db", null, 1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("insert into Book values(?,?,?,?,?)",new Object[]{null,"author1",11.11,111,"book-name1"});
        db.execSQL("insert into Book values(?,?,?,?,?)",new Object[]{null,"author2",22.22,222,"book-name2"});
        return true;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        Log.e(TAG, "delete: " );

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int delRows = 0;
        switch (uriMatcher.match(uri)){
            case BOOK_DIR:
                delRows = db.delete("Book",selection,selectionArgs);
                break;
            case BOOK_ITEM:
                String bookId = uri.getPathSegments().get(1);
                delRows = db.delete("Book","id = ?",new String[]{bookId});
                break;
            case CATEGORY_DIR:
                delRows = db.delete("Category",selection,selectionArgs);
                break;
            case CATEGORY_ITEM:
                String categoryId = uri.getPathSegments().get(1);
                delRows = db.delete("Category","id = ?",new String[]{categoryId});
                break;
            default:
                break;
        }
        return delRows;
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        Log.e(TAG, "getType: " );

        String type = "";
        switch(uriMatcher.match(uri)){
            case BOOK_DIR:
                type = "vnd"+"android.cursor.dir/"+"vnd."+AUTHORITY+"."+"book";
                break;
            case BOOK_ITEM:
                type = "vnd"+"android.cursor.item/"+"vnd."+AUTHORITY+"."+"book";
                break;
            case CATEGORY_DIR:
                type = "vnd"+"android.cursor.dir/"+"vnd."+AUTHORITY+"."+"category";
                break;
            case CATEGORY_ITEM:
                type = "vnd"+"android.cursor.item/"+"vnd."+AUTHORITY+"."+"category";
                break;
            default:
                break;
        }
        return  type;
    }

    // 当调用ContentResolver 的insert方法时，就会回调该方法
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO: Implement this to handle requests to insert a new row.
        Log.e(TAG, "insert: ");

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Uri returnUri = null;
        switch (uriMatcher.match(uri)) {
            case BOOK_DIR:
            case BOOK_ITEM:
                long newBookId = db.insert("Book", null, values);
                returnUri = Uri.parse("content://"+AUTHORITY+"/book/"+newBookId);
                break;
            case CATEGORY_DIR:
            case CATEGORY_ITEM:
                long newCategoryId = db.insert("Category",null,values);
                returnUri = Uri.parse("content://"+AUTHORITY+"/category/"+newCategoryId);
                break;
            default:
                break;
        }
        return returnUri;
    }


    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        // TODO: Implement this to handle query requests from clients.
        Log.e(TAG, "query: ");

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;

        switch (uriMatcher.match(uri)) {
            case BOOK_DIR:
                cursor = db.query("Book", projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case BOOK_ITEM:
                //uri.getPathSegments() : List<String>
                // 将内容URI 权限Authority之后的部分以 /符号分割，分割后的结果放入到一个字符串列表中
                // 列表的第 0 个位置是路径，第 1 个位置是 id :  /book/2

                String bookId = uri.getPathSegments().get(1);
                cursor = db.query("Book", projection, "id = ?", new String[]{bookId}, null, null, sortOrder);
                break;
            case CATEGORY_DIR:
                cursor = db.query("Category", projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case CATEGORY_ITEM:
                String categoryId = uri.getPathSegments().get(1);
                cursor = db.query("Book", projection, "id = ?", new String[]{categoryId}, null, null, sortOrder);
                break;
            default:
                break;
        }
        return cursor;

    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        Log.e(TAG, "update: " );

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int updateRows = 0;
        switch(uriMatcher.match(uri)){
            case BOOK_DIR:
                updateRows = db.update("Book",values,selection,selectionArgs);
                break;
            case BOOK_ITEM:
                String bookId = uri.getPathSegments().get(1);
                updateRows = db.update("Book",values,"id = ?",new String[]{bookId});
                break;
            case CATEGORY_DIR:
                updateRows = db.update("Category",values,selection,selectionArgs);
                break;
            case CATEGORY_ITEM:
                String categoryId = uri.getPathSegments().get(1);
                updateRows = db.update("Category",values,"id = ?",new String[]{categoryId});
                break;
            default:
                break;
        }
        return updateRows;
    }
}
