package net.radewon.skyblock;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main extends Thread {
    public Main() {
    }

    public static void main(String[] args) throws Exception {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(() -> {
            try {
                System.out.println("bjr je suis le fonctionnement2");
                BankAPI.refreshTransactions();
            } catch (IOException var1) {
                var1.printStackTrace();
                System.out.println(var1.getMessage());
                System.out.println(var1.getLocalizedMessage());
            }

        }, 0L, 13L, TimeUnit.SECONDS);
    }
}
