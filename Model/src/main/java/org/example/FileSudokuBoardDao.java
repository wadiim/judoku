package org.example;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class FileSudokuBoardDao implements Dao<SudokuBoard> {
    FileInputStream fileInput;
    FileOutputStream fileOutput;
    ObjectInputStream objInput;
    ObjectOutputStream objOutput;

    FileSudokuBoardDao(String fileName) throws SudokuFileException {
        try {
            fileOutput = new FileOutputStream(fileName, true);
            fileInput = new FileInputStream(fileName);
            objOutput = new ObjectOutputStream(fileOutput);
            objInput = new ObjectInputStream(fileInput);
        } catch (IOException e) {
            throw new SudokuFileException(Lang.get("message.could_not_open_file"), e);
        }
    }

    @Override
    public SudokuBoard read() throws SudokuFileException {
        try {
            return (SudokuBoard) objInput.readObject();
        } catch (Exception e) {
            throw new SudokuFileException(Lang.get("message.could_not_read_from_file"), e);
        }
    }

    @Override
    public void write(SudokuBoard obj) throws SudokuFileException {
        try {
            objOutput.writeObject(obj);
        } catch (Exception e) {
            throw new SudokuFileException(Lang.get("message.could_not_write_to_file"), e);
        }
    }

    @Override
    public void close() throws Exception {
        try {
            objInput.close();
            objOutput.close();
            fileInput.close();
            fileOutput.close();
        } catch (Exception e) {
            throw new SudokuFileException(Lang.get("message.could_not_close_file"), e);
        }
    }
}
