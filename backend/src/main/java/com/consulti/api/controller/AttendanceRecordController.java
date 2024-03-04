package com.consulti.api.controller;

import com.consulti.api.model.AttendanceRecord;
import com.consulti.api.model.User;
import com.consulti.api.service.AttendanceRecordService;
import com.consulti.api.service.ExcelService;
import com.consulti.api.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/attendance")
public class AttendanceRecordController {

    private final UserService userService;
    private final AttendanceRecordService attendanceRecordService;

    private final ExcelService excelService;

    @Autowired
    public AttendanceRecordController(
            UserService userService,
            AttendanceRecordService attendanceRecordService,
            ExcelService excelService,
            ExcelService excelService1)
    {
        this.attendanceRecordService = attendanceRecordService;
        this.userService=userService;
        this.excelService = excelService1;
    }

    @GetMapping("history/{userName}")
    @ResponseStatus(HttpStatus.OK)
    public List<AttendanceRecord> historyByUserName(@PathVariable String userName) {
        return this.attendanceRecordService.findHistoryByUserName(userName);
    }

    @GetMapping("history/all")
    @ResponseStatus(HttpStatus.OK)
    public  ResponseEntity<byte[]> globalHistory (HttpServletResponse response) throws IOException {

        HttpHeaders headers= new HttpHeaders();
        String headerValue = "attachment; filename=Report__";

        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        response.setContentType("application/vnd.ms-excel");
        String headerKey = HttpHeaders.CONTENT_DISPOSITION;
        String fileName = headerValue + currentDateTime + ".xlsx";

        headers.add(headerKey, fileName);

        try {

            ByteArrayInputStream excelResponse = this.excelService
                    .generateExcel();

            byte[] byteArray = excelResponse.readAllBytes();

            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                    .body(byteArray);

        }catch (IOException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    @PostMapping("/{userName}")
    @ResponseStatus(HttpStatus.CREATED)
    public AttendanceRecord registerAttendance(@PathVariable String userName) {
        User currentUser = this.userService.findUserByUserName(userName);
        return attendanceRecordService.registerAttendance(currentUser);
    }

    @PostMapping("history/upload")
    @ResponseBody
    public String handleFileUpload(@RequestParam("file") MultipartFile file) throws IOException {
        return this.excelService.loadFile(file);
    }
}
