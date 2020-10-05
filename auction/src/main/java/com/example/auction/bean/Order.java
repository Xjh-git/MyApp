package com.example.auction.bean;

import java.util.Date;

public class Order {
    int id;
    Date date;
    String buyerID, sellerID;
    Commodity commodity;
    double price;
}
