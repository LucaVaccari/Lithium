package it.unibs.pajc.db;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Marks a field as the model of a database column.
 * The attribute {@link #name() name} specifies the name of the column.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Column {
    /**
     * @return The name of the column on the db
     */
    String name();
}
