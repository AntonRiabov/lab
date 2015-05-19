package com.dss.tpcp;

import sun.misc.Version;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by paladii on 15.05.2015.
 */
public class Postgre {
    public static final String INSERT_HOTEL_BOOKING = "BEGIN;" +"INSERT INTO \"HotelBooking\"(\n" +
            "            \"BookingID\", \"ClientName\", \"HotelName\", \"Aarrival\", \"Departue\")\n" +
            "    VALUES (?, ?, ?, ?, ?);\n" + "PREPARE TRANSACTION '%s';";

    public static final String INSERT_FLY_BOOKING = "BEGIN;" +
            " INSERT INTO \"FlyBooking\"(" +
            "             \"BookingID\",\"ClientName\", \"FlyNumber\", \"From\", \"To\", \"Date\")" +
            "    VALUES ( ?, ?, ?, ?, ?, ?);" +
            "PREPARE TRANSACTION '%s';";
    public static final String COMMIT = "COMMIT PREPARED '%s';";
    public static final String ROLLBACK = "ROLLBACK PREPARED '%s';";

    public static void main(String[] args) {
        Connection conDB1 = null;
        Connection conDB2 = null;
        Statement stDB1 = null;
        Statement stDB2 = null;
        PreparedStatement preparedStatementDB1 = null;
        PreparedStatement preparedStatementDB2 = null;
        String transactionNameDB1 = "FlyBooking";
        String transactionNameDB2 = "HotelBooking";


        String urlDB1 = "jdbc:postgresql://localhost/DB1";
        String urlDB2 = "jdbc:postgresql://localhost/DB2";
        String user = "postgres";
        String password = "admin";

        try {
            conDB1 = DriverManager.getConnection(urlDB1, user, password);
            prepareFlyBooking(conDB1,preparedStatementDB1,  transactionNameDB1);
        } catch (SQLException ex) {
            try {
                if(null != conDB1){
                    rollback(conDB1, transactionNameDB1);
                }
            } catch (SQLException e) {
            }
        }

        try {
            conDB2 = DriverManager.getConnection(urlDB2, user, password);
            prepareHotelBooking(conDB2, preparedStatementDB2, transactionNameDB2);
        } catch (Exception ex) {
            try {
                if(null != conDB2){
                    rollback(conDB2, transactionNameDB2);
                }
            } catch (SQLException e) {
            }
        }

        try {
            stDB1 = conDB1.createStatement();
            stDB1.execute(String.format(COMMIT, transactionNameDB1));
            stDB2 = conDB2.createStatement();
            stDB2.execute(String.format(COMMIT, transactionNameDB2));
        } catch (SQLException e) {
//            e.printStackTrace();
        }finally {
            try {

                if (stDB1 != null) {
                    stDB1.close();
                }
                if (conDB1 != null) {
                    conDB1.close();
                }

            } catch (SQLException ex) {
                Logger lgr = Logger.getLogger(Version.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }

    }

    private static boolean rollback(Connection conDB1, String transactionNameDB1) throws SQLException {
        return conDB1.createStatement().execute(String.format(ROLLBACK, transactionNameDB1));
    }

    private static void prepareHotelBooking(Connection conDB2, PreparedStatement preparedStatementDB2, String transactionNameDB2) throws SQLException {
        preparedStatementDB2 = conDB2.prepareStatement(String.format(INSERT_HOTEL_BOOKING, transactionNameDB2));

        preparedStatementDB2.setInt(1, 1111111111);
        preparedStatementDB2.setString(2, "ClientName");
        preparedStatementDB2.setString(3, "HotelName");
        preparedStatementDB2.setDate(4, Date.valueOf("1993-4-6"));
        preparedStatementDB2.setDate(5, Date.valueOf("1993-4-6"));

        preparedStatementDB2.execute();

    }

    private static void prepareFlyBooking(Connection conDB1, PreparedStatement preparedStatementDB1, String transactionName) throws SQLException {
        preparedStatementDB1 = conDB1.prepareStatement(String.format(INSERT_FLY_BOOKING, transactionName));

        preparedStatementDB1.setInt(1, 222222222);
        preparedStatementDB1.setString(2, "Name");
        preparedStatementDB1.setString(3, "number");
        preparedStatementDB1.setString(4, "from");
        preparedStatementDB1.setString(5, "to");
        preparedStatementDB1.setDate(6, Date.valueOf("1993-4-6"));

        preparedStatementDB1.execute();
    }
}


