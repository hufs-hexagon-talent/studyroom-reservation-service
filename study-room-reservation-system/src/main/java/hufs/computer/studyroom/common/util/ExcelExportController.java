package hufs.computer.studyroom.common.util;

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

        // header set
        int rowIndex = 0;
        Row headerRow = sheet.createRow(rowIndex++);

        Cell headerRowCell0 = headerRow.createCell(0);
        headerRowCell0.setCellValue("상태");
        headerRowCell0.setCellStyle(blueCellStyle);

        Cell headerRowCell1 = headerRow.createCell(1);
        headerRowCell1.setCellValue("이름");
        headerRowCell1.setCellStyle(greyCellStyle);

        Cell headerRowCell2 = headerRow.createCell(2);
        headerRowCell2.setCellValue("학번");
        headerRowCell2.setCellStyle(greyCellStyle);

        Cell headerRowCell3 = headerRow.createCell(3);
        headerRowCell3.setCellValue("이메일");
        headerRowCell3.setCellStyle(greyCellStyle);

        Cell headerRowCell4 = headerRow.createCell(4);
        headerRowCell4.setCellValue("학과");
        headerRowCell4.setCellStyle(greyCellStyle);

        for (UserInfoResponse user : allUsers.users()){
            Row bodyRow = sheet.createRow(rowIndex++);


            Cell bodyRowCell0 = bodyRow.createCell(0);
            bodyRowCell0.setCellValue(user.serviceRole().toString());
            bodyRowCell0.setCellStyle(bodyCellStyle);

            Cell bodyRowCell1 = bodyRow.createCell(1);
            bodyRowCell1.setCellValue(user.name());
            bodyRowCell1.setCellStyle(bodyCellStyle);

            Cell bodyRowCell2 = bodyRow.createCell(2);
            bodyRowCell2.setCellValue(user.serial());
            bodyRowCell2.setCellStyle(bodyCellStyle);

            Cell bodyRowCell3 = bodyRow.createCell(3);
            bodyRowCell3.setCellValue(user.email());
            bodyRowCell3.setCellStyle(bodyCellStyle);

            Cell bodyRowCell4 = bodyRow.createCell(4);
            bodyRowCell4.setCellValue(user.departmentName());
            bodyRowCell4.setCellStyle(bodyCellStyle);
        }

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=users.xlsx");
        workbook.write(response.getOutputStream());
        workbook.close();

    }



}
