package me.donghun.vanilla.dao;

import me.donghun.vanilla.ConnectionFactory;
import me.donghun.vanilla.model.Comment;
import me.donghun.vanilla.model.Doc;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
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

    public List<Doc> getAllDoc() {
        List<Doc> docs = new ArrayList<>();
        try{
            conn = getConnection();
            String sql = "SELECT * FROM document order by ts desc";
            rs = stmt.executeQuery(sql);
            while(rs.next()) {
                Calendar nowGMTplus9 = Calendar.getInstance(TimeZone.getTimeZone("GMT+9"));
                docs.add(new Doc(rs.getLong("doc_id"),
                        rs.getString("title"),
                        rs.getString("content"),
                        rs.getTimestamp("ts", nowGMTplus9),
                        rs.getInt("hit"),
                        rs.getString("user_id")));
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

    public Doc getDocByDocId(Long docId) {
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
                    rs.getInt("hit"),
                    rs.getString("user_id"));
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

    // 내가 쓴 글 보기 기능
    public List<Doc> getDocsByUserId(String userId) {
        List<Doc> ret = new ArrayList<>();
        try{
            conn = getConnection();
            String sql = "SELECT * FROM document where user_id = '" + userId + "'"; // 아 sql은 이게 무넺네 String 들어가면 작은따옴표
            rs = stmt.executeQuery(sql); //SQL문을 전달하여 실행
            while(rs.next()) {
                Calendar nowGMTplus9 = Calendar.getInstance(TimeZone.getTimeZone("GMT+9"));
                ret.add(new Doc(rs.getLong("doc_id"),
                        rs.getString("title"),
                        rs.getString("content"),
                        rs.getTimestamp("ts", nowGMTplus9),
                        rs.getInt("hit"),
                        rs.getString("user_id")));
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
        return ret;
    }

    public void addDoc(Doc doc) {
        try{
            conn = getConnection();
            String sql = "insert into document (title, content, hit, user_id) values (?, ?, ?, ?)";
            ps = (PreparedStatement)conn.prepareStatement(sql);
            ps.setString(1, doc.getTitle());
            ps.setString(2, doc.getContent());
            ps.setInt(3, 0);
            ps.setString(4, doc.getUserId());
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
            // 이런식으로 코드짜면 content나 title 같은 string에 ' 들어갔을 때 골치아프다. preparestmt가 좋은듯
//            String sql = "update document set title='"+doc.getTitle()+"', "
//                    + "content='"+doc.getContent()+"', ts=CURRENT_TIMESTAMP"
//                    + " where doc_id = " + doc.getId();
//            System.out.println(sql);
            String sql = "UPDATE document SET title = ?, content = ?, ts = CURRENT_TIMESTAMP WHERE doc_id = ?";
            ps = (PreparedStatement) conn.prepareStatement(sql);
            ps.setString(1, doc.getTitle());
            ps.setString(2, doc.getContent());
            ps.setLong(3, doc.getId());
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

    public void addComment(Comment comment) {
        try{
            conn = getConnection();
            String sql = "insert into comment (content, user_id, doc_id) values (?, ?, ?)";
            ps = (PreparedStatement)conn.prepareStatement(sql);
            ps.setString(1, comment.getContent());
            ps.setString(2, comment.getUserId());
            ps.setLong(3, comment.getDocumentId());
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

    public List<Comment> getCommentsByDocId(Long docId) {
        List<Comment> ret = new ArrayList<>();
        try{
            conn = getConnection();
            String sql = "SELECT * FROM comment where doc_id = " + docId;
            rs = stmt.executeQuery(sql); //SQL문을 전달하여 실행
            while(rs.next()) {
                Calendar nowGMTplus9 = Calendar.getInstance(TimeZone.getTimeZone("GMT+9"));
                ret.add(new Comment(rs.getLong("comment_id"),
                        docId,
                        rs.getString("content"),
                        rs.getString("user_id"),
                        rs.getTimestamp("ts", nowGMTplus9)));
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
        return ret;
    }

    public void deleteComment(Long commentId) {
        try{
            conn = getConnection();
            String sql = "delete from comment where comment_id = " + commentId;
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
