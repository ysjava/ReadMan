// MyAidl.aidl
package com.android.learn;

// Declare any non-default types here with import statements
import com.android.learn.ipc.Book;
import com.android.learn.IBookUpdateListener;
interface IBookManager {
    void addBook(in Book book);
    List<Book> getBookList();
    void registerListener(IBookUpdateListener listener);
    void unregisterListener(IBookUpdateListener listener);
}