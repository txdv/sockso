
package com.pugh.sockso.tests;

import com.pugh.sockso.Main;
import com.pugh.sockso.db.Database;

import java.sql.SQLException;
import java.sql.ResultSet;

import junit.framework.TestCase;

public class SocksoTestCase extends TestCase {

    static { Main.initTestLogger(); }

    /**
     *  Asserts that the size of a table is as expected
     * 
     *  @param db
     *  @param table
     *  @param size
     * 
     *  @throws java.sql.SQLException
     * 
     */

    public void assertTableSize( final Database db, final String table, final int size ) throws SQLException {

        ResultSet rs = null;

        try {

            rs = db.query( " select count(*) as total from " +table );

            assertTrue( rs.next() );
            assertEquals( size, rs.getInt("total") );

        }

        finally {
            try { rs.close(); }
            catch ( SQLException e ) {}
        }

    }

    /**
     *  Asserts a row exists in the table with the specified value
     * 
     *  @param db
     *  @param table
     *  @param column
     *  @param value
     * 
     */

    public void assertRowExists( final Database db, final String table, final String column, final String value ) throws SQLException {

        assertRowCount( db, table, column, value, 1 );

    }

    /**
     *  Asserts that a row doesn't exist with the specified value
     *
     *  @param db
     *  @param table
     *  @param column
     *  @param value
     *
     *  @throws java.sql.SQLException
     *
     */

    public void assertRowDoesntExist( final Database db, final String table, final String column, final String value ) throws SQLException {

        assertRowCount( db, table, column, value, 0 );

    }

    /**
     *  Asserts that a specified number of rows exist for a specific value
     * 
     *  @param db
     *  @param table
     *  @param column
     *  @param value
     *  @param rowCount
     * 
     *  @throws java.sql.SQLException
     * 
     */

    private void assertRowCount( final Database db, final String table, final String column, final String value, final int rowCount ) throws SQLException {

        final int total = getRowCount( db, table, column, value );

        if ( total < rowCount ) {
            fail( "Not enough rows found for " +table+ "." +column+ " = '" +value+ "' " );
        }

        if ( total > rowCount ) {
            fail( "Too many rows found for " +table+ "." +column+ " = '" +value+ "' " );
        }

    }

    /**
     *  Returns the number of rows that match a specific value
     * 
     *  @param db
     *  @param table
     *  @param column
     *  @param value
     * 
     *  @return
     * 
     *  @throws java.sql.SQLException
     * 
     */

    private int getRowCount( final Database db, final String table, final String column, final String value ) throws SQLException {

        ResultSet rs = null;
        
        try {

            final String sql = " select count(*) as total " +
                               " from " +table+ " " +
                               " where " +column+ " = '" +value+ "' ";

            rs = db.query( sql );
            
            if ( !rs.next() ) {
                fail( "Query failed: " + sql );
            }

            return rs.getInt( "total" );

        }
        
        finally {
            try { rs.close(); }
            catch ( final SQLException e ) {}
        }
        
    }

}
