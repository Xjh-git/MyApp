package com.example.mybook.utils;

import com.example.mybook.bean.Chapter;
import com.example.mybook.bean.EBook;
import com.example.mybook.bean.Novel;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BiQuGe extends Website {
    String biQuGeUrl = "http://www.xbiquge.la";

    public Chapter getChapter(String httpUrl) {
        //        String httpUrl =
        //                "http://www.xbiquge.la/10/10489/4535761.html";
        Document doc = null;
        try {
            doc = Jsoup.connect(httpUrl).get();
        } catch (IOException e) {
            System.out.println("connect error");
            e.printStackTrace();
        }

        Element tit = doc.select("div.bookname h1").get(0);
        String title = tit.text();

        Element con = doc.select("#content").get(0);
        String content = Jsoup.clean(con.toString(), "", Whitelist.none(), new Document.OutputSettings().prettyPrint(false)).
                replaceAll("&nbsp;", " ");

        Chapter chapter = new Chapter(title, content);
        return chapter;
    }

    public EBook getEBook(String httpUrl) {
        //        String httpUrl =
        //                "http://www.xbiquge.la/10/10489";
        Document doc = null;
        try {
            doc = Jsoup.connect(httpUrl).get();
        } catch (IOException e) {
            System.out.println("connect error");
            e.printStackTrace();
        }
        Element name = doc.select("#info h1").get(0);
        Element wirter = doc.select("#info p").get(0);
        String bookName = name.text();
        String bookWriter = wirter.text().replaceAll("作 者：", "");
        //        System.out.println(bookName + " , " + bookWriter);

        Elements chapters = doc.select("div#list a");
        List<Chapter> chapterList = new ArrayList<>();

        for (int i = 0; i < chapters.size(); i++) {
            Element chapter = chapters.get(i);
            String link = chapter.attr("href");
            //            String chapterName = chapter.text();
            Chapter chapter1 = new BiQuGe().getChapter(biQuGeUrl + link);
            chapterList.add(chapter1);
            System.out.println("正在下载：" + i + "/" + chapters.size());
        }
        EBook eBook = new EBook(bookName, bookWriter, chapterList);
        return eBook;
        //
    }

    public List<Novel> search(String name) {
        String httpUrl =
                "http://www.xbiquge.la/xiaoshuodaquan";

        Document doc = null;
        try {
            doc = Jsoup.connect(httpUrl).timeout(5000).get();
        } catch (IOException e) {
            System.out.println("connect error");
            e.printStackTrace();
        }
        Elements novels = doc.select("div#main div.novellist a");
        Element novel;
        List<Novel> novelList = new ArrayList<>();
        for (int i = 0; i < novels.size(); i++) {
            novel = novels.get(i);
            String novelName = novel.text();
            String novelLink = novel.attr("href");
            if (novelName.contains(name)) {
                novelList.add(new Novel(novelName, "?", novelLink));
            }
        }
        return novelList;
    }


}

