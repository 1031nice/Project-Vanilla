package me.donghun.vanilla.dao;

import me.donghun.vanilla.ConnectionFactory;
import me.donghun.vanilla.model.Doc;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

@Component
public class DocDAO {
    Connection conn = null;
    Statement stmt = null;
    ResultSet rs = null;
    PreparedStatement ps = null;

    public DocDAO() {
    }

    private Connection getConnection() throws SQLException {
        Connection conn;
        conn = ConnectionFactory.getInstance().getConnection();
        stmt = conn.createStatement();
        return conn;
    }

    public List<Doc> getAll(List<Doc> docs) {
        try{
            conn = getConnection();
            String sql = "SELECT * FROM document order by ts desc";
            rs = stmt.executeQuery(sql); //SQL문을 전달하여 실행
            while(rs.next()) {
                Calendar nowGMTplus9 = Calendar.getInstance(TimeZone.getTimeZone("GMT+9"));
                docs.add(new Doc(rs.getLong("doc_id"),
                        rs.getString("title"),
                        rs.getString("content"),
                        rs.getTimestamp("ts", nowGMTplus9),
                        rs.getInt("hit")));
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch(Exception e){
            e.printStackTrace();
        } finally { //예외가 있든 없든 무조건 실행
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
        return docs;
    }

    public Doc get(int docId) {
        Doc ret = null;
        try{
            conn = getConnection();
            String sql = "SELECT * FROM document where doc_id = " + docId;
            rs = stmt.executeQuery(sql); //SQL문을 전달하여 실행
            rs.next();
            Calendar nowGMTplus9 = Calendar.getInstance(TimeZone.getTimeZone("GMT+9"));
            ret = new Doc(rs.getLong("doc_id"),
                    rs.getString("title"),
                    rs.getString("content"),
                    rs.getTimestamp("ts", nowGMTplus9),
                    rs.getInt("hit"));
            sql = "UPDATE document set hit = hit + 1 where doc_id = " + docId;
            stmt.executeUpdate(sql);
            rs.close();
            stmt.close();
            conn.close();
        } catch(Exception e){
            e.printStackTrace();
        } finally { //예외가 있든 없든 무조건 실행
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

    public void add(Doc doc) {
        try{
            conn = getConnection();
            String sql = "insert into document (title, content, hit) values (?, ?, ?)";
            ps = (PreparedStatement)conn.prepareStatement(sql);
            ps.setString(1, doc.getTitle());
            ps.setString(2, doc.getContent());
            ps.setInt(3, 0);
            ps.executeUpdate();
            ps.close();
            stmt.close();
            conn.close();
        } catch(Exception e){
            e.printStackTrace();
        } finally { //예외가 있든 없든 무조건 실행
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

    public void update(Doc doc) {
        try{
            conn = getConnection();
            String sql = "update document set title='"+doc.getTitle()+"', "
                    + "content='"+doc.getContent()+"', ts=CURRENT_TIMESTAMP"
                    + " where doc_id = " + doc.getId();
            System.out.println(sql);
            stmt.executeUpdate(sql); //SQL문을 전달하여 실행
            stmt.close();
            conn.close();
        } catch(Exception e){
            e.printStackTrace();
        } finally { //예외가 있든 없든 무조건 실행
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

    public void delete(int docId) {
        try{
            conn = getConnection();
            String sql = "delete from document where doc_id = " + docId;
            stmt.executeUpdate(sql);
            stmt.close();
            conn.close();
        } catch(Exception e){
            e.printStackTrace();
        } finally { //예외가 있든 없든 무조건 실행
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
}
