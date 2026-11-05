package com.dataaccess.finalactivityMMR2627;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class PassengerDAO {
    private static final String INSERT_SQL =
            "INSERT INTO passengers (passportno, firstname, lastname, address, phone) VALUES (?, ?, ?, ?, ?)";

    public static void insertPassengerTransaction(List<Passenger> passengers) throws Exception{
        Connection conn  = DatabaseManager.getConnection();

        boolean originalAutoCommit = conn.getAutoCommit();

        try {
            conn.setAutoCommit(false);

            try(PreparedStatement pstmt = conn.prepareStatement(INSERT_SQL)){
                for (Passenger p : passengers) {
                    pstmt.setString(1, p.getPasssportNo());
                    pstmt.setString(2, p.getFirstName());
                    pstmt.setString(3, p.getLastName());
                    pstmt.setString(4, p.getAddress());
                    pstmt.setString(5, p.getPhone());

                    pstmt.executeUpdate();
                }
            }

            conn.commit();
            System.out.println("Success: " + passengers.size() + " passengers inserted");
        }catch (SQLException e){

            conn.rollback();
            throw new Exception("Transaction insertion failed. Cause: " + e.getMessage());
        } finally{
            conn.setAutoCommit(originalAutoCommit);
        }
    }
}
