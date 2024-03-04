package com.consulti.api.service;

import com.consulti.api.model.AttendanceRecord;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ExcelService {
    Workbook workbook;

    Sheet sheet;

    private final AttendanceRecordService attendanceRecordService;

    private ExcelService(AttendanceRecordService attendanceRecordService) {
        this.attendanceRecordService = attendanceRecordService;
    }

    public ByteArrayInputStream generateExcel()
            throws IOException
    {
        workbook=new XSSFWorkbook();
        sheet= workbook.createSheet("Global history");

        List<AttendanceRecord> globalHistory = attendanceRecordService.findAllHistory();
        List<String> headersList= List.of(
                "Identity ID",
                "Employee Name",
                "Check in",
                "Check out",
                "Date"
        );

        CellStyle styleHeader = workbook.createCellStyle();
        CellStyle styleBody = workbook.createCellStyle();

        CellStyle styleCellHeader=setStyleCell(styleHeader, true);
        CellStyle styleCellBody=setStyleCell(styleBody, false);

        setHeader(headersList, styleCellHeader);
        writeData(globalHistory, styleCellBody);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
    }

    private void setHeader(List<String> headers, CellStyle styleHeader) {
        Row row = sheet.createRow(0);
        for (String headerString : headers) {
            int index = headers.indexOf(headerString);
            createCell(row, index, headerString, styleHeader);
        }
    }

    private CellStyle setStyleCell(CellStyle style, Boolean bold){
        Font font = workbook.createFont();
        font.setBold(bold);
        style.setFont(font);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }

    private void createCell(Row row, int columnCount, Object valueOfCell, CellStyle style){
        sheet.autoSizeColumn(columnCount);
        Cell cell= row.createCell(columnCount);

        if(valueOfCell instanceof Integer){
            cell.setCellValue((Integer) valueOfCell);
        }

        if(valueOfCell instanceof String){
            cell.setCellValue((String) valueOfCell);
        }
    }

    private void writeData(List<AttendanceRecord> data, CellStyle styleBody){
        int rowCount=1;
        for ( AttendanceRecord register : data){
            Row row=sheet.createRow(rowCount++);
            int columnCount=0;

            LocalTime timeIn = LocalTime.of(
                    register.getCheckIn().getHour(),
                    register.getCheckIn().getMinute(),
                    register.getCheckIn().getSecond()
            );

            LocalTime timeOut = LocalTime.of(
                    register.getCheckOut().getHour(),
                    register.getCheckOut().getMinute(),
                    register.getCheckOut().getSecond()
            );

//            String employeeName= register.getUser().getFirstName()+" "+register.getUser().getLastName();
            LocalDate dateRegister = register.getCheckIn().toLocalDate();

            createCell(row, columnCount++, register.getUser().getId(),styleBody);
//            createCell(row, columnCount++, employeeName, styleBody);
            createCell(row, columnCount++, timeIn, styleBody);
            createCell(row, columnCount++, timeOut, styleBody);
            createCell(row, columnCount++, dateRegister, styleBody);

        }

    }


    public String loadFile (MultipartFile file) throws IOException {
        if(file.isEmpty()){
            return "File is empty";
        }

        InputStream inputStream = file.getInputStream();
        Workbook workbook = new XSSFWorkbook(inputStream);
        Sheet sheet = workbook.getSheetAt(0);

        for (int rowIndex = 0; rowIndex < sheet.getPhysicalNumberOfRows(); rowIndex++) {
            Row row = sheet.getRow(rowIndex);
            if (row == null) {
                continue;
            }

            if (rowIndex == 0) {
                continue;
            }

            Cell identityCardCell = row.getCell(0);
            Cell checkInCell = row.getCell(2);
            Cell checkOutCell = row.getCell(3);
            Cell dateCell = row.getCell(4);

            if (identityCardCell == null || checkInCell == null || checkOutCell == null || dateCell == null) {
                System.out.println("Warning: Missing cells in row " + rowIndex);
                continue;
            }

            String identityCard = identityCardCell.getStringCellValue();

            if (identityCard.isEmpty()) {
                System.out.println("Warning: Empty identity card in row " + rowIndex);
                continue;
            }

            String checkInStr = checkInCell.getStringCellValue();
            String checkOutStr = checkOutCell.getStringCellValue();
            String dateStr = dateCell.getStringCellValue();

            if (checkInStr.isEmpty() || checkOutStr.isEmpty() || dateStr.isEmpty()) {
                System.out.println("Warning: Empty time or date values in row " + rowIndex);
                continue;
            }

            LocalTime checkIn = LocalTime.parse(checkInStr);
            LocalTime checkOut = LocalTime.parse(checkOutStr);

            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("M/d/yyyy");
            LocalDate date = LocalDate.parse(dateStr, inputFormatter);

            LocalDateTime checkInDateTime = date.atTime(checkIn);
            LocalDateTime checkOutDateTime = date.atTime(checkOut);

            AttendanceRecord newRegister = this.attendanceRecordService.registerAttendanceImported(
                    identityCard,
                    checkInDateTime,
                    checkOutDateTime,
                    date
            );
            this.attendanceRecordService.saveRegister(newRegister);
        }

        workbook.close();
        return ("File uploaded successfully");
    }

}
