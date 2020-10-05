package com.example.mybook.utils;

import com.example.mybook.bean.EBook;
import com.example.mybook.bean.Novel;

import java.util.List;

public abstract class Website {

    public abstract EBook getEBook(String httpUrl);

    public abstract List<Novel> search(String bookname);

}
