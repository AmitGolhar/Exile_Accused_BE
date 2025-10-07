package com.exileaccused.service;

 
 
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.exileaccused.entity.ExileData;
import com.exileaccused.entity.ExileRecord;
import com.exileaccused.repository.ExileDataRepository;
import com.exileaccused.repository.ExileRecordRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
@Service
public class ExcelService {

    @Autowired
    private ExileDataRepository exileDataRepository;
    @Autowired
    private ExileRecordRepository exileRecordRepository;
    
    @Autowired
    private final ObjectMapper mapper = new ObjectMapper();
 
    @Transactional
    public void processAndSaveExcel(MultipartFile file, String uploadedBy) {
        try {
            // 1️⃣ Create a parent upload entity
            ExileData exileData = new ExileData();
            exileData.setUploadedBy(uploadedBy);
            exileDataRepository.save(exileData);

            // 2️⃣ Read Excel file using Apache POI
            Workbook workbook = WorkbookFactory.create(file.getInputStream());
            Sheet sheet = workbook.getSheetAt(0);

            List<ExileRecord> records = new ArrayList<>();

            for (int i = 2; i <= sheet.getLastRowNum(); i++) { // skip header
                Row row = sheet.getRow(i);
                if (row == null) continue;

                ExileRecord record = new ExileRecord();
                record.setSrNo(getCellValue(row, 0));
                record.setPoliceStation(getCellValue(row, 1));
                record.setNameOfAccused(getCellValue(row, 2));
                record.setAddress(getCellValue(row, 3));
                record.setMcoaSection(getCellValue(row, 4));
                record.setPeriodOfExile(getCellValue(row, 5));
                record.setDateOfExileOrder(getCellValue(row, 6));
                record.setDateOfExecutionOfExileOrder(getCellValue(row, 7));
                record.setEndDateOfExilePeriod(getCellValue(row, 8));
                record.setMobileNumber(getCellValue(row, 9));

                record.setExileData(exileData); // link parent upload
                records.add(record);
            }

            exileRecordRepository.saveAll(records);
            workbook.close();

        } catch (Exception e) {
            throw new RuntimeException("Failed to upload Excel: " + e.getMessage());
        }
    }
    
    private String getCellValue(Row row, int cellIndex) {
        Cell cell = row.getCell(cellIndex);
        return cell != null ? cell.toString().trim() : "";
    }

    /**
     * Handles numbers and date serials correctly.
     */
    private String formatNumericValue(double value, Cell cell) {
        try {
            if (DateUtil.isCellDateFormatted(cell) || (value > 20000 && value < 60000)) {
                Date date = DateUtil.getJavaDate(value);
                return new SimpleDateFormat("dd/MM/yyyy").format(date);
            }
        } catch (Exception ignored) {
        }

        // Avoid scientific notation (for mobiles)
        BigDecimal bd = new BigDecimal(value);
        bd = bd.stripTrailingZeros();
        String str = bd.toPlainString();

        // If looks like a mobile number (>=10 digits, no decimal)
        if (str.matches("\\d{10,}")) {
            return str.substring(0, Math.min(str.length(), 10));
        }

        // If it's integer-like, keep it clean
        if (str.endsWith(".0")) str = str.replace(".0", "");

        return str;
    }
    
    /** ✅ Skip unwanted or blank rows */
    private boolean shouldSkipRow(String srNo, String policeStation, String name, String mobile) {
        // Skip fully empty rows
        if ((srNo + policeStation + name + mobile).trim().isEmpty()) return true;

        // Skip Marathi/English header rows
        String combined = (policeStation + name).toLowerCase();
        return combined.contains("पोलीस") ||
               combined.contains("अ.क्र") ||
               combined.contains("तडीपार") ||
               combined.contains("हद्दपार") ||
               combined.contains("मोबाईल नंबर") ||
               combined.contains("पत्ता") ||
               combined.contains("हद्दपार") ||
               combined.contains("मपोका कलम");
    }
}

