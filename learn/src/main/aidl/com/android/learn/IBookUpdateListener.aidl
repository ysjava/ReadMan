// IBookUpdateListener.aidl
package com.android.learn;

// Declare any non-default types here with import statements
import com.android.learn.ipc.Book;
interface IBookUpdateListener {
    void onBookUpdate(in Book book);
}