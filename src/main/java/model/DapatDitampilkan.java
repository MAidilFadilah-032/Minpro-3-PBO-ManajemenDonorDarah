package model;

public interface DapatDitampilkan {

    String formatData();

    default String formatRingkas() {
        return formatData();
    }
}