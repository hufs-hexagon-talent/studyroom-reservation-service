package hufs.computer.studyroom.common.util.excel.style;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.poi.ss.usermodel.BorderStyle;

@Getter
@AllArgsConstructor
public enum ExcelBorderStyle {
    NONE(BorderStyle.NONE),
    THIN(BorderStyle.THIN),
    MEDIUM(BorderStyle.MEDIUM),
    THICK(BorderStyle.THICK);

    private final BorderStyle borderStyle;

}
