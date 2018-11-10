package topic;


import java.sql.*;

/**
 * 修改话题关键词DSL
 *
 * @author shengbin
 */
public class TopicUtils {

    public static void main1(String[] args) {
        String url = "jdbc:mysql://" + args[0] + "/bfd_mf_data?autoReconnect=true";
        String driverName = "com.mysql.jdbc.Driver";
        String user = args[1];
        String pwd = args[2];
        try {
            Integer sum = 0;
            String sql = "select id,es_content from mf_subject";
            Class.forName(driverName);
            Connection conn = DriverManager.getConnection(url, user, pwd);
            Statement stmt = getStmt(conn);
            conn.createStatement();
            ResultSet ret = null;
            if (stmt != null) {
                ret = stmt.executeQuery(sql);
            }
            while (ret.next()) {
                Long id = ret.getLong("id");
                String esContent = ret.getString("es_content");
                Statement stmt1 = getStmt(conn);
                esContent = esContent.replaceAll("match", "match_phrase").replaceAll("\"type\" : \"phrase\",", "");
                String update = "update mf_subject set es_content='" + esContent + "'" + " where id=" + id;
                if (stmt1 != null) {
                    stmt1.executeUpdate(update);
                    stmt1.close();
                }
                sum++;
                System.out.println("更新话题DSL成功，id:" + id);
            }
            System.out.println("更新总数：" + sum);
            ret.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Statement getStmt(Connection conn) {
        try {
            return conn.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
