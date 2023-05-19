package ua.foxminded.asharov.cardatamanager.initial;

public enum HeadColomn {

    REPORT, position, objectId, Make, Year, Model, Category;

    public static final String[] HEADER_SET_IN = { "objectId", "Make", "Year", "Model", "Category" };
    public static final String[] HEADER_SET_OUT = { "REPORT", "position", "objectId", "Make", "Year", "Model", "Category" };

}
