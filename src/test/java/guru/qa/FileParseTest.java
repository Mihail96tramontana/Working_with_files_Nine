package guru.qa;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.opencsv.CSVReader;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static org.assertj.core.api.Assertions.assertThat;

public class FileParseTest {

    ClassLoader classLoader = FileParseTest.class.getClassLoader();

    String zipClassPath = "src/test/resources/Files.zip";
    String jsonClassPath = "src/test/resources/student.json";

    @Test
    void pdfTest() throws Exception {
        ZipFile zipFiles = new ZipFile(zipClassPath);
        ZipEntry zipEntry = zipFiles.getEntry("Дар.Владимир Набоков.pdf");
        InputStream inputStream = zipFiles.getInputStream(zipEntry);
        PDF pdf = new PDF(inputStream);
        assertThat(pdf.text).contains("Дар");
    }

    @Test
    void xlsxTest() throws Exception {
        ZipFile zipFiles = new ZipFile(zipClassPath);
        ZipEntry zipEntry = zipFiles.getEntry("Таблица.xlsx");
        InputStream inputStream = zipFiles.getInputStream(zipEntry);
        XLS xls = new XLS(inputStream);
        assertThat(xls.excel
                .getSheetAt(0)
                .getRow(3)
                .getCell(2)
                .getStringCellValue()).contains("строка3");
    }

    @Test
    void csvTest() throws Exception {
        //с помощью класс лоудера ищем зип
        ZipFile zipFile = new ZipFile(Objects.requireNonNull(classLoader.getResource("Files.zip")).getFile());
        //достаем нужный файл в зипе
        ZipEntry entry = zipFile.getEntry("example.csv");
        List<String[]> list;
        InputStream inputStream = zipFile.getInputStream(entry);
        //читаем csv
        CSVReader csvReader = new CSVReader(new InputStreamReader(inputStream));
        list = csvReader.readAll();
        assertThat(list).contains(
                new String[] {"teacher","lesson","date"},
                new String[] {"Tuchs","junit","03.06"},
                new String[] {"Eroshenko","allure","07.06"}
        );
        }

    @Test
    void parseJsonTest() throws Exception{
        ObjectMapper mapper = new ObjectMapper();
        guru.qa.domain.Staff staff = mapper.readValue(Paths.get(jsonClassPath).toFile(), guru.qa.domain.Staff.class);
        assertThat(staff.name).isEqualTo("Михаил");
        assertThat(staff.isGoodStudent).isEqualTo("true");
        assertThat(staff.age).isEqualTo("25");
    }
}