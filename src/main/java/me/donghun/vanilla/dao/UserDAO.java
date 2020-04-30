package me.donghun.vanilla.dao;

import me.donghun.vanilla.ConnectionFactory;
import me.donghun.vanilla.model.User;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@Component
public class UserDAO {

    Connection conn = null;
    Statement stmt = null;
    ResultSet rs = null;

    public UserDAO() {
    }

    private Connection getConnection() throws SQLException {
        Connection conn;
        conn = ConnectionFactory.getInstance().getConnection();
        stmt = conn.createStatement();
        return conn;
    }

    public void modify(User user) {
        try{
            String sql = "UPDATE _user SET password='"+user.getPw()+"', "
                    + "name='"+user.getName()+"' where id = '" + user.getId() + "'";
            conn = getConnection();
            stmt.executeUpdate(sql);
            stmt.close();
            conn.close();
        } catch(Exception e){
            e.printStackTrace();
        } finally {
            try{
                if(stmt!=null)
                    stmt.close();
            }catch(SQLException ex1){
            }
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException ex1){
            }
        }
    }

    public void withdrawal(String id) {
        try{
            String sql = "DELETE FROM _user where id = '" + id + "'";
            conn = getConnection();
            stmt.executeUpdate(sql);
            stmt.close();
            conn.close();
        } catch(Exception e){
            e.printStackTrace();
        } finally {
            try{
                if(stmt!=null)
                    stmt.close();
            }catch(SQLException ex1){
            }
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException ex1){
            }
        }
    }

    public boolean signUp(User user) {
        boolean ret = true;
        try{
            String sql = "SELECT * FROM _user where id = '" + user.getId() + "'";
            conn = getConnection();
            rs = stmt.executeQuery(sql);
            if(rs.next())
                ret = false;
            else {
                sql = "insert into _user (id, password, name) values ('"+user.getId()+"', '"+user.getPw()+"', '"+user.getName()+"')";
                stmt.executeUpdate(sql);
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch(Exception e){
            e.printStackTrace();
        } finally {
            try{
                if(stmt!=null)
                    stmt.close();
            }catch(SQLException ex1){
            }
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException ex1){
            }
        }
        return ret;
    }

    public User login(User user) {
        User ret = null;
        try{
            String sql = "SELECT * FROM _user where id = '" + user.getId() + "'";
            conn = getConnection();
            rs = stmt.executeQuery(sql);
            if(rs.next() && user.getPw().equals(rs.getString("password"))) {
                user.setName(rs.getString("name"));
                ret = user;
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch(Exception e){
            e.printStackTrace();
        } finally {
            try{
                if(stmt!=null)
                    stmt.close();
            }catch(SQLException ex1){
            }
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException ex1){
            }
        }
        return ret;
    }

}
