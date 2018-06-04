package ua.khai.gorbatiuk.taskmanager.util.constant;

public final class Table {
    public final class Task{
        public static final String TABLE_NAME = "tasks";
        public static final String ID =TABLE_NAME + ".id_task";
        public static final String ID_ROOT =TABLE_NAME + ".id_root";
        public static final String NAME =TABLE_NAME + ".name";
        public static final String DATE =TABLE_NAME + ".date";
        public static final String TIME =TABLE_NAME + ".time";
        public static final String COMPLEXITY =TABLE_NAME + ".complexity";
        public static final String TEXT =TABLE_NAME + ".text";
        public static final String CHECKED =TABLE_NAME + ".checked";
        public static final String CATEGORY =TABLE_NAME + ".fk_category";
        public static final String USER =TABLE_NAME + ".fk_user";
    }
    public final class Category{
        public static final String TABLE_NAME = "categories";
        public static final String ID =TABLE_NAME + ".id_category";
        public static final String ID_ROOT =TABLE_NAME + ".id_root";
        public static final String NAME =TABLE_NAME + ".name";
        public static final String COLOR =TABLE_NAME + ".color";
    }
}
