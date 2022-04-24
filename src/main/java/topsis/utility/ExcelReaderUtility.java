package topsis.utility;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import topsis.models.Factory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExcelReaderUtility {

    private static final Logger logger = LoggerFactory.getLogger(ExcelReaderUtility.class);

    private static List<Factory> factoryList = new ArrayList<>();

    public static List<Factory> readExcelFile(File excelFile) throws IOException, InvalidFormatException {
        logger.info("Starting excel file read");

        FileInputStream fileInputStream = new FileInputStream(excelFile);
        Workbook workbook = new XSSFWorkbook(excelFile);

        Sheet datatypeSheet = workbook.getSheetAt(1);
        Iterator<Row> iterator = datatypeSheet.iterator();
        Factory.FactoryBuilder factoryBuilder = Factory.builder();


        int rowCount = 1;
        while (iterator.hasNext() && rowCount < 80) {
            //Skip header rows
            if(rowCount < 4){
                iterator.next();
                ++rowCount;
                continue;
            }

            //Iterate through cells in each row and build the factory list
            Row currentRow = iterator.next();
            Iterator<Cell> cellIterator = currentRow.iterator();
            int cellCount = 0;
            while (cellIterator.hasNext()  && cellCount <= 5) {
                if(cellCount == 0){
                    factoryBuilder.factoryName(cellIterator.next().getStringCellValue());
                    ++cellCount;
                }

                if(cellCount == 1){
                    factoryBuilder.acceptanceQualityLevel(cellIterator.next().getNumericCellValue());
                    ++cellCount;
                }

                if(cellCount == 2){
                    factoryBuilder.avgResponseTime(cellIterator.next().getNumericCellValue());
                    ++cellCount;
                }

                if(cellCount == 3){
                    factoryBuilder.sampleAcceptanceRate(cellIterator.next().getNumericCellValue());
                    ++cellCount;
                }

                if(cellCount == 4){
                    factoryBuilder.samplingTime(cellIterator.next().getNumericCellValue());
                    ++cellCount;
                }

                if(cellCount == 5){
                    factoryBuilder.certificationCount(cellIterator.next().getNumericCellValue());
                    ++cellCount;
                }
            }

            factoryList.add(factoryBuilder.build());
            ++rowCount;
        }

        fileInputStream.close();

        logger.info("Total rows read: " + factoryList.size());
        logger.info("Excel file read complete");

        return factoryList;
    }
}
