package com.example.ipc_contentprovider.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

public class BookProvider extends ContentProvider {
    private final static String TAG = "BookProvider";
    @Override
    public boolean onCreate() {
        Log.d(TAG, "onCreate: ");
        return false;
    }

    @Override
    public Cursor query( Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Log.d(TAG, "query: " + Thread.currentThread().getName());
        return null;
    }

    @Override
    public String getType( Uri uri) {
        return null;
    }

    @Override
    public Uri insert( Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete( Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update( Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
