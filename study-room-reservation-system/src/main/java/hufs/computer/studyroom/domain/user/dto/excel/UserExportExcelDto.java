package hufs.computer.studyroom.domain.user.dto.excel;

import hufs.computer.studyroom.common.util.excel.annotation.ExcelColumn;
import hufs.computer.studyroom.common.util.excel.style.header.BlueHeaderStyle;
import hufs.computer.studyroom.domain.user.entity.ServiceRole;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "회원 정보 EXCEL 추출")
public record UserExportExcelDto(
        @ExcelColumn(headerName = "학과", headerStyle = BlueHeaderStyle.class)
        String departmentName,

        @ExcelColumn(headerName = "학번", headerStyle = BlueHeaderStyle.class)
        String serial,

        @ExcelColumn(headerName = "이름", headerStyle = BlueHeaderStyle.class)
        String name,

        @ExcelColumn(headerName = "이메일", headerStyle = BlueHeaderStyle.class)
        String email,

        @ExcelColumn(headerName = "상태", headerStyle = BlueHeaderStyle.class)
        ServiceRole status
) {
}
