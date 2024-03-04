package com.consulti.api.controller;

import com.consulti.api.model.SessionRecord;
import com.consulti.api.model.User;
import com.consulti.api.service.SessionRecordService;
import com.consulti.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/attendance")
public class AttendanceRecordController {

    private final UserService userService;
    private final SessionRecordService sessionRecordService;

    @Autowired
    public AttendanceRecordController(
            UserService userService,
            SessionRecordService sessionRecordService
    ){
        this.sessionRecordService = sessionRecordService;
        this.userService=userService;
    }

    @GetMapping("history/{userName}")
    @ResponseStatus(HttpStatus.OK)
    public List<SessionRecord> historyByUserName(@PathVariable String userName) {
        return this.sessionRecordService.findHistoryByUserName(userName);
    }

    @PostMapping("/{userName}")
    @ResponseStatus(HttpStatus.CREATED)
    public SessionRecord registerAttendance(@PathVariable String userName) {
        User currentUser = this.userService.findUserByUserName(userName);
        return sessionRecordService.registerAttendance(currentUser);
    }

//    @GetMapping("history/all")
//    @ResponseStatus(HttpStatus.OK)
//    public  ResponseEntity<byte[]> globalHistory (HttpServletResponse response) throws IOException {
//
//        HttpHeaders headers= new HttpHeaders();
//        String headerValue = "attachment; filename=Report__";
//
//        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
//        String currentDateTime = dateFormatter.format(new Date());
//
//        response.setContentType("application/vnd.ms-excel");
//        String headerKey = HttpHeaders.CONTENT_DISPOSITION;
//        String fileName = headerValue + currentDateTime + ".xlsx";
//
//        headers.add(headerKey, fileName);
//
//        try {
//
//            ByteArrayInputStream excelResponse = this.excelService
//                    .generateExcel();
//
//            byte[] byteArray = excelResponse.readAllBytes();
//
//            return ResponseEntity
//                    .ok()
//                    .headers(headers)
//                    .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
//                    .body(byteArray);
//
//        }catch (IOException e){
//            System.out.println(e.getMessage());
//        }
//        return null;
//    }

//    @PostMapping("history/upload")
//    @ResponseBody
//    public String handleFileUpload(@RequestParam("file") MultipartFile file) throws IOException {
//        return this.excelService.loadFile(file);
//    }
}
