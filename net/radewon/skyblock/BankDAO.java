package net.radewon.skyblock;

import java.sql.ResultSet;
import java.sql.Statement;

public class BankDAO {
    public BankDAO() {
    }

    public boolean save(Transaction t) {
        try {
            DB.open();
            Statement smt = DB.getConnection().createStatement();
            ResultSet test = smt.executeQuery("SELECT COUNT(*) as c FROM transac WHERE time=" + t.getTime() + ";");
            test.next();
            if (test.getInt(1) != 0) {
                test.close();
                DB.close();
                return false;
            } else {
                if (t.getName().contains("Bank Interest")) {
                    ResultSet rs2 = smt.executeQuery("SELECT COUNT(*) as c FROM bank;");
                    rs2.next();
                    int i = rs2.getInt(1);
                    ResultSet rs = smt.executeQuery("SELECT * FROM bank;");

                    while (rs.next()) {
                        String var10000 = rs.getString(1);
                        String query = "INSERT INTO transac(player,amount,time,note) VALUES('" + var10000 + "',"
                                + (double) t.getAmount() * 1.0 / ((double) i * 1.0) + "," + t.getTime()
                                + ",'BANK INTEREST');";
                        smt.addBatch(query);
                        System.out.println(query);
                    }

                    smt.executeBatch();
                    rs.close();
                    rs2.close();
                } else {
                    String var10001 = t.getName();
                    smt.addBatch("INSERT INTO transac(player,amount,time) VALUES('" + var10001 + "'," + t.getAmount()
                            + "," + t.getTime() + ");");
                }

                smt.executeBatch();
                return true;
            }
        } catch (Exception var8) {
            System.out.println(var8.getMessage());
            return false;
        }
    }

    public ResultSet getBank() {
        String str = "";

        try {
            DB.open();
            Statement smt = DB.getConnection().createStatement();
            return smt.executeQuery("SELECT * FROM bank;");
        } catch (Exception var3) {
            System.out.println(var3.getMessage());
            return null;
        }
    }
}