package hufs.computer.studyroom.domain.reservation.dto.excel;

import hufs.computer.studyroom.common.util.excel.annotation.ExcelColumn;
import hufs.computer.studyroom.common.util.excel.style.header.BlueHeaderStyle;
import hufs.computer.studyroom.common.util.excel.style.header.GrayHeaderStyle;
import hufs.computer.studyroom.domain.reservation.entity.Reservation;
import hufs.computer.studyroom.domain.reservation.entity.Reservation.ReservationState;
import hufs.computer.studyroom.domain.user.entity.ServiceRole;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.Instant;

@Builder
@Schema(description = "회원 정보 EXCEL 추출")
public record ReservationExportExcelDto(

        @ExcelColumn(headerName = "학번", headerStyle = GrayHeaderStyle.class)
        String serial,

        @ExcelColumn(headerName = "이름", headerStyle = GrayHeaderStyle.class)
        String name,

        @ExcelColumn(headerName = "예약 장소", headerStyle = GrayHeaderStyle.class)
        String place, // 예약 장소 = roomName + partitionNumber

        @ExcelColumn(headerName = "예약 시작 시간", headerStyle = GrayHeaderStyle.class)
        String reservationStartTimeKst,

        @ExcelColumn(headerName = "예약 종료 시간", headerStyle = GrayHeaderStyle.class)
        String reservationEndTimeKst,

        @ExcelColumn(headerName = "상태", headerStyle = GrayHeaderStyle.class)
        ReservationState state
) {
}