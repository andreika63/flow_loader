import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Created by kay on 19.12.2016.
 */
public class FlowLoader {
    final static String CHARSET = "UTF8";
    final static String SPLITER = " ";
    final static String FOLDER = "d:/stat";
    final static String DRIVER = "oracle.jdbc.OracleDriver";
    static Connection connection = null;
    static PreparedStatement ps = null;

    public static void main(String[] args) {
        long time = System.currentTimeMillis();
        final String CON_STRING = args[0];

        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        try {
            Locale.setDefault(Locale.US);
            connection = DriverManager.getConnection(CON_STRING);
            if (connection != null) System.out.println("Connected");
            //database is ready, so we can process import data
            import_data();
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(-1);
        } finally {
            try {
                if (connection != null) connection.close();
                if (ps != null) ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        System.out.println(System.currentTimeMillis() - time);
    }

    private static void import_data() throws SQLException {
        File fileDir = new File(FOLDER);
        List<File> files = Arrays.asList(fileDir.listFiles());
        connection.setAutoCommit(false);
        ps = connection.prepareStatement("insert into FLOWSTAT(FLOWTYPE,FLOWDATE,IPADDR,FLOWS,OCTETS,PACKETS) " +
                "values(?,?,?,?,?,?)");
        files.forEach(file -> {
                    FlowReader flowReader = new FlowReader(file.getPath(), CHARSET);
                    try {
                        process_file(flowReader);
                        System.out.println(file.getPath());
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
        );

    }

    private static void process_file(FlowReader flowReader) throws SQLException {
        ps.clearBatch();
        flowReader.findAll(SPLITER).forEach(s -> {
            for (int i = 0; i < s.length; i++) {
                try {
                    ps.setString(i + 1, s[i]);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            try {
                ps.addBatch();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        ps.executeBatch();
        connection.commit();
    }
}
