package server;

import java.io.Serializable;

public class PrinterTask implements Serializable {
    private static final long serialVersionUID = 1L;
    private int index;
    private String fileName;
    private String printer;

    public PrinterTask() {
    }


    public PrinterTask(int index, String fileName, String printer) {
        this.index = index;
        this.fileName = fileName;
        this.printer = printer;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getPrinter() {
        return printer;
    }

    public void setPrinter(String printer) {
        this.printer = printer;
    }
}
