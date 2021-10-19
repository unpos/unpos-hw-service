package ru.unpos.hw.service.vfd;

public class DisplayData {

    private DisplayDriverException error;

    public DisplayData() {}

    public DisplayData(DisplayDriverException error) {
        this();
        this.setError(error);
    }

    public DisplayDriverException getError() {
        return error;
    }

    public void setError(DisplayDriverException error) {
        this.error = error;
    }
}
