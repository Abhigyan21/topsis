package topsis.utility;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import topsis.models.Factory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExcelReaderUtility {

    private static List<Factory> factoryList = new ArrayList<>();

    public static List<Factory> readExcelFile(File excelFile) throws IOException, InvalidFormatException {
        FileInputStream fileInputStream = new FileInputStream(excelFile);
        Workbook workbook = new XSSFWorkbook(excelFile);

        Sheet datatypeSheet = workbook.getSheetAt(1);
        Iterator<Row> iterator = datatypeSheet.iterator();
        Factory.FactoryBuilder factoryBuilder = Factory.builder();


        int rowCount = 1;
        while (iterator.hasNext()) {
            //Skip header rows
            if(rowCount < 2){
                ++rowCount;
                continue;
            }

            //Iterate through cells in each row and build the factory list
            Row currentRow = iterator.next();
            Iterator<Cell> cellIterator = currentRow.iterator();
            int cellCount = 1;
            while (cellIterator.hasNext()) {
                Cell currentCell = cellIterator.next();
                if(cellCount == 1){
                    factoryBuilder.factoryName(currentCell.getStringCellValue());
                    ++cellCount;
                }

                if(cellCount == 2){
                    factoryBuilder.acceptanceQualityLevel(currentCell.getNumericCellValue());
                    ++cellCount;
                }

                if(cellCount == 3){
                    factoryBuilder.avgResponseTime(currentCell.getNumericCellValue());
                    ++cellCount;
                }

                if(cellCount == 4){
                    factoryBuilder.sampleAcceptanceRate(currentCell.getNumericCellValue());
                    ++cellCount;
                }

                if(cellCount == 5){
                    factoryBuilder.samplingTime(currentCell.getNumericCellValue());
                    ++cellCount;
                }

                if(cellCount == 6){
                    factoryBuilder.certificationCount(currentCell.getNumericCellValue());
                }
            }

            factoryList.add(factoryBuilder.build());
        }

        fileInputStream.close();

        return factoryList;
    }
}
