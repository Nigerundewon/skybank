package net.radewon.skyblock;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class Transaction {
    private int amount;
    private long timestamp;
    private String name;
    private static List<Transaction> transactions = new ArrayList();

    public Transaction(int i, long t, String s) {
        this.amount = i;
        this.timestamp = t;
        this.name = s;
    }

    public String getName() {
        return this.name;
    }

    public int getAmount() {
        return this.amount;
    }

    public long getTime() {
        return this.timestamp;
    }

    public static List<Transaction> getTransactions() {
        return transactions;
    }

    public static int getAmountWithAction(int money, String a) {
        int i = 1;
        if (a.equals("WITHDRAW")) {
            i = -1;
        }

        return money * i;
    }

    public static boolean addTransaction(Transaction t) {
        return transactions.add(t);
    }

    public String toString() {
        String str = "";
        if (this.amount >= 0) {
            str = "+";
        }

        String var10000 = this.name;
        return "Name: " + var10000 + " | Transaction: " + str + this.amount + " coins on " + new Date(this.timestamp);
    }

    public static void printList() {
        Iterator var0 = transactions.iterator();

        while(var0.hasNext()) {
            Transaction t = (Transaction)var0.next();
            System.out.println(t.toString());
        }

    }
}