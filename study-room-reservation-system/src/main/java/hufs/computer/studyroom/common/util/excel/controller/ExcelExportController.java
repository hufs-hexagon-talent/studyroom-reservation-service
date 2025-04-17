package hufs.computer.studyroom.common.util.excel.controller;

import hufs.computer.studyroom.domain.reservation.dto.response.ReservationInfoResponse;
import hufs.computer.studyroom.domain.reservation.dto.response.ReservationInfoResponses;
import hufs.computer.studyroom.domain.reservation.service.ReservationQueryService;
import hufs.computer.studyroom.domain.user.dto.response.UserInfoResponse;
import hufs.computer.studyroom.domain.user.dto.response.UserInfoResponses;
import hufs.computer.studyroom.domain.user.service.UserQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.DefaultIndexedColorMap;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ExcelExportController {
    private final UserQueryService userQueryService;
    private final ReservationQueryService reservationQueryService;

    /*
     *.전체 데이터 메모리에 올림  -> 대용량 처리에 런타임시 (out of mem err)
     * Workbook regacyWorkbook = new XSSFWorkbook();
     */

    /*
     * XSSFWorkbook의 확장 버전, 대용량 Excel 쓰기에 최적화.
     * 일정 수 row 만 메모리에 저장하고, 나머지는 디스크에 저장함
     * 단, 쓰기 전용임
     * Workbook workbook1 = new SXSSFWorkbook();
     */
    private void applyCellStyle(CellStyle cellStyle, java.awt.Color awtColor) {
        byte[] rgb = new byte[]{
                (byte) awtColor.getRed(),
                (byte) awtColor.getGreen(),
                (byte) awtColor.getBlue()
        };

        XSSFCellStyle xssfCellStyle = (XSSFCellStyle) cellStyle;
        xssfCellStyle.setFillForegroundColor(new XSSFColor(rgb, new DefaultIndexedColorMap()));
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderBottom(BorderStyle.THIN);
    }

    @GetMapping("reservations/test")
    @ApiResponse(
            responseCode = "200",
            description = "Excel 파일 (XLSX)",
            content = @Content(mediaType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    )
    @Operation(summary = "✅ [관리자] 예약 엑셀 추출",
            description = "모든 reservation export API",
            security = {@SecurityRequirement(name = "JWT")})
    public void exportReservationInfo(HttpServletResponse response) throws IOException {
//   엑셀 파일 생성
        Workbook workbook = new SXSSFWorkbook();
        Sheet sheet = workbook.createSheet();

        CellStyle greyCellStyle = workbook.createCellStyle();
        applyCellStyle(greyCellStyle, new java.awt.Color(231, 234, 236));

        CellStyle blueCellStyle = workbook.createCellStyle();
        applyCellStyle(blueCellStyle, new java.awt.Color(223, 235, 246));

        CellStyle bodyCellStyle = workbook.createCellStyle();
        applyCellStyle(bodyCellStyle, new java.awt.Color(255, 255, 255));

        // write 할 정보 get
        ReservationInfoResponses allReservations = reservationQueryService.getAllReservations();

        // header setting
        // 헤더 설정
        int rowIndex = 0;
        Row headerRow = sheet.createRow(rowIndex++);

        Cell headerCell0 = headerRow.createCell(0);
        headerCell0.setCellValue("예약 시설");
        headerCell0.setCellStyle(blueCellStyle);

        Cell headerCell1 = headerRow.createCell(1);
        headerCell1.setCellValue("예약자명");
        headerCell1.setCellStyle(greyCellStyle);

        Cell headerCell2 = headerRow.createCell(2);
        headerCell2.setCellValue("학번");
        headerCell2.setCellStyle(greyCellStyle);

        Cell headerCell3 = headerRow.createCell(3);
        headerCell3.setCellValue("예약 시간 시간");
        headerCell3.setCellStyle(greyCellStyle);

        Cell headerCell4 = headerRow.createCell(4);
        headerCell4.setCellValue("예약 종료 시간");
        headerCell4.setCellStyle(greyCellStyle);

        Cell headerCell5 = headerRow.createCell(5);
        headerCell5.setCellValue("예약 생성 시간");
        headerCell5.setCellStyle(greyCellStyle);

        Cell headerCell6 = headerRow.createCell(6);
        headerCell6.setCellValue("예약 상태");
        headerCell6.setCellStyle(greyCellStyle);

        // 바디 설정
        for (ReservationInfoResponse reservation : allReservations.reservationInfoResponses()){
            Row bodyRow = sheet.createRow(rowIndex++);

            Cell bodyCell0 = bodyRow.createCell(0);
            bodyCell0.setCellValue(reservation.roomName() + reservation.partitionNumber());
            bodyCell0.setCellStyle(bodyCellStyle);

            Cell bodyCell1 = bodyRow.createCell(1);
            bodyCell1.setCellValue(reservation.name());
            bodyCell1.setCellStyle(bodyCellStyle);

            Cell bodyCell2 = bodyRow.createCell(2);
            bodyCell2.setCellValue(userQueryService.findUserById(reservation.userId()).serial());
            bodyCell2.setCellStyle(bodyCellStyle);

            Cell bodyCell3 = bodyRow.createCell(3);
            bodyCell3.setCellValue(reservation.reservationStartTime().toString());
            bodyCell3.setCellStyle(bodyCellStyle);

            Cell bodyCell4 = bodyRow.createCell(4);
            bodyCell4.setCellValue(reservation.reservationEndTime().toString());
            bodyCell4.setCellStyle(bodyCellStyle);

            Cell bodyCell5 = bodyRow.createCell(5);
            bodyCell5.setCellValue(reservation.createAt().toString());
            bodyCell5.setCellStyle(bodyCellStyle);

            Cell bodyCell6 = bodyRow.createCell(6);
            bodyCell6.setCellValue(reservation.reservationState().toString());
            bodyCell6.setCellStyle(bodyCellStyle);
        }
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=users.xlsx");
        workbook.write(response.getOutputStream());
        workbook.close();


    }

    @GetMapping("users/test")
    @ApiResponse(
            responseCode = "200",
            description = "Excel 파일 (XLSX)",
            content = @Content(mediaType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    )
    @Operation(summary = "✅ [관리자] 엑셀 추출",
            description = "모든 user export API",
            security = {@SecurityRequirement(name = "JWT")})
    public void exportUserInfo(HttpServletResponse response) throws IOException {

        //   엑셀 파일 생성
        Workbook workbook = new SXSSFWorkbook();
        Sheet sheet = workbook.createSheet();

        CellStyle greyCellStyle = workbook.createCellStyle();
        applyCellStyle(greyCellStyle, new java.awt.Color(231, 234, 236));

        CellStyle blueCellStyle = workbook.createCellStyle();
        applyCellStyle(blueCellStyle, new java.awt.Color(223, 235, 246));

        CellStyle bodyCellStyle = workbook.createCellStyle();
        applyCellStyle(bodyCellStyle, new java.awt.Color(255, 255, 255));


        // write 할 정보 get
        UserInfoResponses allUsers = userQueryService.findAllUsers();

        // header setting
        // 헤더 설정
        int rowIndex = 0;
        Row headerRow = sheet.createRow(rowIndex++);

        Cell headerCell0 = headerRow.createCell(0);
        headerCell0.setCellValue("상태");
        headerCell0.setCellStyle(blueCellStyle);

        Cell headerCell1 = headerRow.createCell(1);
        headerCell1.setCellValue("이름");
        headerCell1.setCellStyle(greyCellStyle);

        Cell headerCell2 = headerRow.createCell(2);
        headerCell2.setCellValue("학번");
        headerCell2.setCellStyle(greyCellStyle);

        Cell headerCell3 = headerRow.createCell(3);
        headerCell3.setCellValue("이메일");
        headerCell3.setCellStyle(greyCellStyle);

        Cell headerCell4 = headerRow.createCell(4);
        headerCell4.setCellValue("학과");
        headerCell4.setCellStyle(greyCellStyle);

        // 바디 설정
        for (UserInfoResponse user : allUsers.users()){
            Row bodyRow = sheet.createRow(rowIndex++);


            Cell bodyCell0 = bodyRow.createCell(0);
            bodyCell0.setCellValue(user.serviceRole().toString());
            bodyCell0.setCellStyle(bodyCellStyle);

            Cell bodyCell1 = bodyRow.createCell(1);
            bodyCell1.setCellValue(user.name());
            bodyCell1.setCellStyle(bodyCellStyle);

            Cell bodyCell2 = bodyRow.createCell(2);
            bodyCell2.setCellValue(user.serial());
            bodyCell2.setCellStyle(bodyCellStyle);

            Cell bodyCell3 = bodyRow.createCell(3);
            bodyCell3.setCellValue(user.email());
            bodyCell3.setCellStyle(bodyCellStyle);

            Cell bodyCell4 = bodyRow.createCell(4);
            bodyCell4.setCellValue(user.departmentName());
            bodyCell4.setCellStyle(bodyCellStyle);
        }

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=users.xlsx");
        workbook.write(response.getOutputStream());
        workbook.close();

    }



}
