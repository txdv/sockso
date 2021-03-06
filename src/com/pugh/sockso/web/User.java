/*
 * User.java
 * 
 * Created on Aug 4, 2007, 10:15:20 AM
 * 
 * A user of the web interface
 * 
 */

package com.pugh.sockso.web;

import com.pugh.sockso.Utils;
import com.pugh.sockso.db.Database;

import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.log4j.Logger;

public class User {

    private static final Logger log = Logger.getLogger( User.class );
    
    private final int sessionId;
    private final String name, pass, email, sessionCode;

    private int id;

    public User( final int id, final String name ) {

        this( id, name, "", "" );

    }
    
    public User( final String name, final String pass, final String email ) {

        this( -1, name, pass, email, -1, "" );

    }
    
    public User( final int id, final String name, final String email ) {

        this( id, name, "", email, -1, "" );

    }

    public User( final int id, final String name, final String pass, final String email ) {

        this( id, name, pass, email, -1, "" );

    }

    public User( final int id, final String name, final String pass, final String email, final int sessionId, final String sessionCode ) {

        this.id = id;
        this.name = name;
        this.pass = pass;
        this.email = email;
        this.sessionId = sessionId;
        this.sessionCode = sessionCode;

    }

    /**
     *  Saves a new user to the database
     *
     *  @param db
     *
     */

    public void save( final Database db ) throws SQLException {

        PreparedStatement st = null;
        ResultSet rs = null;

        try {

            String sql = " insert into users ( name, pass, email, date_created ) " +
                         " values ( ?, ?, ?, current_timestamp ) ";

            st = db.prepare( sql );
            st.setString( 1, name );
            st.setString( 2, Utils.md5(pass) );
            st.setString( 3, email );
            st.executeUpdate();
            
            Utils.close( st );

            sql = " select max(id) as new_id " +
                  " from users ";
            st = db.prepare( sql );
            rs = st.executeQuery();
            
            if ( !rs.next() ) {
                throw new SQLException( "No results returned from aggregate query" );
            }
            
            id = rs.getInt( "new_id" );
            
        }

        finally {
            Utils.close( st );
        }
        
    }

    public int getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public String getEmail() {
        return email;
    }
    
    public int getSessionId() {
        return sessionId;
    }
    
    public String getSessionCode() {
        return sessionCode;
    }
}
