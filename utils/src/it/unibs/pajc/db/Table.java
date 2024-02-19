package it.unibs.pajc.db;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Marks a class as the model of a database table.
 * Use the attribute {@link #name() name} for specifying the name of the table.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Table {
    /**
     * @return The name of the table on the db
     */
    String name();
}
