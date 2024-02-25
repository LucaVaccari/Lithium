package it.unibs.pajc.db;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Marks a field as the model of a database table id (use this if the table has an id column).
 * An ID is considered as such if it is an integer, a primary key, and it has AUTO_INCREMENT.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Id {
}
