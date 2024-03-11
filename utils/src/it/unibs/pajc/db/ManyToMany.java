package it.unibs.pajc.db;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Apply this to arrays of ids in other om classes to map one class to another with a many-to-many relationship
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ManyToMany {
    /**
     * @return The name of the other table
     */
    String otherTableName();

    /**
     * @return The name of the field in the many-to-many table referencing to the other object (not the class in
     * which this attribute is being used)
     */

    String otherTableColumnName();
}
