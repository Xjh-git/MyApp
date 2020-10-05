package com.example.mybook.bean;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class EBook {
    String bookName, writer;
    List<Chapter> chapters;


    public boolean download() {
        String filePath = "/storage/emulated/0/" + bookName + ".txt";
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            FileWriter fileWriter = new FileWriter(filePath);
            for (int i = 0; i < chapters.size(); i++) {
                fileWriter.write(chapters.get(i).toString() + "\r\n");
                //                System.out.println("已完成：" + i + "/" + eBook.getChapters().size());
            }
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //        System.out.println("已完成！");
        return true;
    }

    public EBook(String bookName, String writer, List<Chapter> chapters) {
        this.bookName = bookName;
        this.writer = writer;
        this.chapters = chapters;
    }

    public int getChapterCount() {
        return chapters.size();
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public void setChapters(List<Chapter> chapters) {
        this.chapters = chapters;
    }

    public String getBookName() {
        return bookName;
    }

    public String getWriter() {
        return writer;
    }

    public List<Chapter> getChapters() {
        return chapters;
    }

}
