package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JdbcSudokuBoardDao implements Dao<SudokuBoard> {
    private static final Logger logger = LoggerFactory.getLogger(JdbcSudokuBoardDao.class);
    private static final String protocol = "jdbc:derby:SudokuDB";
    private final Connection conn;
    private final String name;

    JdbcSudokuBoardDao(String name) throws SudokuJdbcException {
        this.name = name;
        Statement stmt;

        try {
            conn = DriverManager.getConnection(protocol + ";create=true");
            conn.setAutoCommit(false);
            stmt = conn.createStatement();
        } catch (SQLException e) {
            throw new SudokuJdbcException(Lang.get("message.could_not_access_db"), e);
        }

        try {
            stmt.execute("""
                    CREATE TABLE SudokuBoard (
                        id INTEGER PRIMARY KEY
                            GENERATED ALWAYS AS IDENTITY(Start with 1, Increment by 1),
                        name CHAR(64) UNIQUE
                    )""");
            conn.commit();
        } catch (SQLException e) {
            try {
                conn.rollback();
                logger.info(Lang.get("message.rolling_back"));
            } catch (SQLException ie) {
                throw new SudokuJdbcException(Lang.get("message.could_not_rollback"), ie);
            }

            if (tableAlreadyExist(e)) {
                logger.info(Lang.get("message.table_already_exists") + ": SudokuBoard");
            } else {
                throw new SudokuJdbcException(Lang.get("message.could_not_create_table")
                        + ": SudokuBoard", e);
            }
        }

        try {
            stmt.execute("""
                    CREATE TABLE Field (
                        sudokuBoardId INTEGER REFERENCES SudokuBoard,
                        x NUMERIC,
                        y NUMERIC,
                        value NUMERIC
                    )
                    """);
            conn.commit();
        } catch (SQLException e) {
            try {
                conn.rollback();
                logger.info(Lang.get("message.rolling_back"));
            } catch (SQLException ie) {
                throw new SudokuJdbcException(Lang.get("message.could_not_rollback"), ie);
            }

            if (tableAlreadyExist(e)) {
                logger.info(Lang.get("message.table_already_exists") + ": Field");
            } else {
                throw new SudokuJdbcException(Lang.get("message.could_not_create_table")
                        + ": Field", e);
            }
        }
    }

    @Override
    public SudokuBoard read() throws SudokuJdbcException {
        SudokuSolver ss = new BacktrackingSudokuSolver();
        SudokuBoard board = new SudokuBoard(ss);
        try {
            String query = "SELECT * FROM Field WHERE sudokuBoardId = " + getId();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            conn.commit();
            while (rs.next()) {
                board.set(rs.getInt(2), rs.getInt(3), rs.getInt(4));
            }
            rs.close();
        } catch (SQLException e) {
            try {
                conn.rollback();
                logger.info(Lang.get("message.rolling_back"));
                throw new SudokuJdbcException(Lang.get("message.could_not_read_from_db"), e);
            } catch (SQLException ie) {
                throw new SudokuJdbcException(Lang.get("message.could_not_rollback"), ie);
            }
        }
        return board;
    }

    @Override
    public void write(SudokuBoard obj) throws SudokuJdbcException {
        try {
            Statement stmt = conn.createStatement();
            stmt.execute("INSERT INTO SudokuBoard(name) VALUES ('" + name + "')");
            int id = getId();
            for (int row = 0; row < SudokuBoard.BOARD_SIZE; ++row) {
                for (int col = 0; col < SudokuBoard.BOARD_SIZE; ++col) {
                    stmt.execute("INSERT INTO Field VALUES ("
                            + id + "," + row + "," + col + "," + obj.get(row, col) + ")");
                }
            }
            conn.commit();
        } catch (SQLException e) {
            try {
                conn.rollback();
                logger.info(Lang.get("message.rolling_back"));
                throw new SudokuJdbcException(Lang.get("message.could_not_write_to_db"), e);
            } catch (SQLException ie) {
                throw new SudokuJdbcException(Lang.get("message.could_not_rollback"), ie);
            }
        }
    }

    private int getId() throws SudokuJdbcException {
        int id;
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT id FROM SudokuBoard WHERE name = '"
                    + name + "'");
            conn.commit();
            if (!rs.next()) {
                throw new SudokuJdbcException(Lang.get("message.could_not_fetch_board_from_db"));
            }
            id = rs.getInt(1);
            rs.close();
        } catch (SQLException | SudokuJdbcException e) {
            try {
                conn.rollback();
                logger.info(Lang.get("message.rolling_back"));
                throw new SudokuJdbcException("message.could_not_read_from_db", e);
            } catch (SQLException ie) {
                throw new SudokuJdbcException(Lang.get("message.could_not_rollback"), ie);
            }
        }
        return id;
    }

    private boolean tableAlreadyExist(SQLException e) {
        return e.getSQLState().equals("X0Y32");
    }

    public List<String> getAllNames() throws SudokuJdbcException {
        try {
            List<String> names = new ArrayList<>();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT name FROM SudokuBoard");
            conn.commit();
            while (rs.next()) {
                names.add(rs.getString(1).trim());
            }
            rs.close();
            return names;
        } catch (SQLException e) {
            try {
                conn.rollback();
                logger.info(Lang.get("message.rolling_back"));
                throw new SudokuJdbcException(
                        Lang.get("message.could_not_get_list_of_board_names"), e);
            } catch (SQLException ie) {
                throw new SudokuJdbcException(Lang.get("message.could_not_rollback"), ie);
            }
        }
    }

    @Override
    public void close() throws SudokuJdbcException {
        try {
            conn.setAutoCommit(true);
            conn.close();
        } catch (Exception e) {
            throw new SudokuJdbcException(Lang.get("message.could_not_disconnect_from_db"), e);
        }
    }
}
