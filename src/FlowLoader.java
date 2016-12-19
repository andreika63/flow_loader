import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * Created by kay on 19.12.2016.
 */
public class FlowLoader {
    final static String charset = "UTF8";
    final static String folder = "d:/stat";
    public static void main(String[] args) {
        File fileDir = new File(folder);
        List<File> files = Arrays.asList(fileDir.listFiles());
        files.forEach(file -> {
            FlowReader flowReader = new FlowReader(file.getPath(),charset);

            flowReader.findAll(" ").forEach(s -> System.out.println(Arrays.toString(s)));
        }
        );
    }
}
