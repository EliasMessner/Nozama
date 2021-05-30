package com.unileipzig.shop;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;

import java.io.*;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class ReviewHandler {

    String inputPath;
    Connection conn;
    PrintWriter printWriter;

    ReviewHandler(String inputPath, Connection conn, String errorPath) throws IOException {
        this.inputPath = inputPath;
        this.conn = conn;
        this.printWriter = new PrintWriter(new FileWriter(errorPath));
    }

    public void handle() {
        try {
            List<String[]> listOfLines = this.readFromCsV();

            this.persistUserAndReview(listOfLines);
        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }
    }

    private List<String[]> readFromCsV() throws IOException, CsvException {
        CSVReader csvReader = new CSVReaderBuilder(new FileReader(inputPath)).withSkipLines(1).build();
        return csvReader.readAll();
    }

    private void persistUserAndReview(List<String[]> listOfLines) {
        try {
            PreparedStatement pStmtCustomer = conn.prepareStatement("INSERT INTO customer (username) VALUES (?)");

            PreparedStatement pStmtReview = conn.prepareStatement("INSERT INTO review (customer, product, date, " +
                    "stars, summary, details) VALUES (?, ?, ?, ?, ?, ?)");

            conn.setAutoCommit(false);
            for (String[] line : listOfLines) {
                try {
                    conn.setAutoCommit(false);

                    pStmtCustomer.setString(1, line[4]);
                    pStmtCustomer.executeUpdate();

                    setReviewQueryParameters(pStmtReview, line);
                    pStmtReview.executeUpdate();

                    conn.commit();
                } catch (SQLException e) {
                    try {
                        conn.rollback();
                    } catch (SQLException rollbackException) {
                        rollbackException.printStackTrace();
                    }
                    printWriter.println(e.getMessage());
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException autoCommitException) {
                autoCommitException.printStackTrace();
            }
        }
    }

    private void setReviewQueryParameters(PreparedStatement pStmtReview, String[] line) throws SQLException {
        pStmtReview.setString(1, line[4]);
        pStmtReview.setString(2, line[0]);
        pStmtReview.setDate(3, Date.valueOf(line[3]));
        pStmtReview.setInt(4, Integer.parseInt(line[1]));
        pStmtReview.setString(5, line[5]);
        pStmtReview.setString(6, line[6]);
    }
}
