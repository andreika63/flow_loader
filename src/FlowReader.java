import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by kay on 17.09.2016.
 */
public class FlowReader {
    private String fileName;
    private String charSet;
    private final Pattern PATTERN = Pattern.compile("(\\d{4}-\\d{2}-\\d{2})(\\w+)(\\.txt)");

    public String getFlowDate() {
        return flowDate;
    }

    public String getFlowType() {
        return flowType;
    }

    private String flowDate;
    private String flowType;

    public FlowReader(String fileName, String charSet) {
        this.fileName = fileName;
        this.charSet = charSet;
        Matcher matcher = PATTERN.matcher(fileName);
        while (matcher.find()) {
            flowDate = matcher.group(1);
            flowType = matcher.group(2).toUpperCase();
        }

    }

    public void print_bytes() {
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(new File(fileName)))){
            int bv;
            while ((bv = bis.read()) > -1) {
                System.out.print(bv + " ");
            }
            System.out.println("");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void print_chars() {
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(new File(fileName)))) {
            int bv;
            while ((bv = bis.read()) > -1) {
                System.out.print((char)bv);
            }
            System.out.println("");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void println(){
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(fileName)),charSet))){
            String line;
            while((line = br.readLine()) != null) {
                System.out.println(flowDate +"\t"+flowType +"\t"+ line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public List<String[]> findAll(String spliter){
        List<String[]> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(fileName)),charSet))){
            String line;
            while((line = br.readLine()) != null) {
                line = flowType+spliter+flowDate+spliter+line.replaceAll(" +",spliter);
                lines.add(line.split(spliter));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }
}
