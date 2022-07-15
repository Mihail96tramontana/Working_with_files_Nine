package guru.qa;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class FileParseTest {

    @Test
    void pdfTest() throws Exception {
        ZipFile zipFiles = new ZipFile(zipClassPath);
        ZipEntry zipEntry = zipFiles.getEntry("");
        InputStream inputStream = zipFiles.getInputStream(zipEntry);
        PDF pdf = new PDF(inputStream);
        assertThat(pdf.text).contains("");
    }

    @Test
    void xlsxTest() throws Exception {
        ZipFile zipFiles = new ZipFile(zipClassPath);
        ZipEntry zipEntry = zipFiles.getEntry("");
        InputStream inputStream = zipFiles.getInputStream(zipEntry);
        XLS xls = new XLS(inputStream);
        assertThat(xls.excel
                .getSheetAt()
                .getRow()
                .getCell()
                .getStringCellValue()).contains("");
    }

    @Test
    void parseCsvTest() throws Exception {
        ZipFile zipFiles = new ZipFile(zipClassPath);
        ZipEntry zipEntry = zipFiles.getEntry("");
        try (InputStream inputStream = zipFiles.getInputStream(zipEntry);
             CSVReader csv = new CSVReader(new InputStreamReader(inputStream))) {
            List<String[]> content = csv.readAll();
            assertThat(content.get()).contains("");
        }
    }