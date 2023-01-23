package com.example.chat;

import android.os.AsyncTask;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

public class ConnectionHelper {
    private static final String URL = "jdbc:postgresql://10.0.2.2:5432/alexandracrisan";
    private static final String USERNAME = "alexandracrisan";
    private static final String PASSWORD = "postgres";

        public static Future<Connection> getConnection() {
            Callable<Connection> connectionTask = new ConnectionTask();
            FutureTask<Connection> connectionFuture = new FutureTask<>(connectionTask);
            new Thread(connectionFuture).start();
            return connectionFuture;
        }

        private static class ConnectionTask implements Callable<Connection> {
            @Override
            public Connection call() throws Exception {
                Class.forName("org.postgresql.Driver");
                return DriverManager.getConnection(URL, USERNAME, PASSWORD);
            }
        }
    }

