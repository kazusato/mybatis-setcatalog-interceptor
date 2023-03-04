package dev.kazusato.mybatismultidb.interceptor;

public class CatalogHolder {

    private static final ThreadLocal<String> value = new ThreadLocal<>();

    public static void setCatalog(String catalog) {
        value.set(catalog);
    }

    public static String getCatalog() {
        return value.get();
    }

}
