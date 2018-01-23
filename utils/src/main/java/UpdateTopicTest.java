import java.sql.*;
public class UpdateTopicTest {
    public static void main(String[] args) {
        String url = "jdbc:mysql://" + "192.168.67.99:3306" + "/bfd_mf_data?autoReconnect=true";
        String driverName = "com.mysql.jdbc.Driver";
        String user = "bfd_mf_v3_qa";
        String pwd = "bfd_mf_v3_qa@168";
        try {
            Integer sum = 0;
            String sql = "SELECT id,es_content FROM mf_subject WHERE label_id IN(\n" +
                    "SELECT id FROM mf_label_node WHERE token='A7CC9548CD74BD58' AND is_leaf=TRUE)";
            Class.forName(driverName);
            Connection conn = DriverManager.getConnection(url, user, pwd);
            Statement stmt = getStmt(conn);
            conn.createStatement();
            ResultSet ret = null;
            if (stmt != null) {
                ret = stmt.executeQuery(sql);
            }
            if (ret != null) {
                while (ret.next()) {
                    Long id = ret.getLong("id");
                    String esContent = ret.getString("es_content");
                    Statement stmt1 = getStmt(conn);
                    esContent = esContent.replaceAll("match", "match_phrase")
                            .replaceAll("\"type\" : \"phrase\",", "")
                            .replaceAll("\"minimum_should_match_phrase\"","\"minimum_should_match_phrase\"");
                    String update = "update mf_subject set es_content='" + esContent + "'" + " where id=" + id;
                    if (stmt1 != null) {
                        stmt1.executeUpdate(update);
                        stmt1.close();
                    }
                    sum++;
                    System.out.println("更新话题DSL成功，id:" + id);
                }
            }
            System.out.println("更新总数：" + sum);
            if (ret != null) {
                ret.close();
            }
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