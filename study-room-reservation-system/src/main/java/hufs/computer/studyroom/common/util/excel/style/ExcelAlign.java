package hufs.computer.studyroom.common.util.excel.style;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;

@Getter
@AllArgsConstructor
public enum ExcelAlign {
    LEFT(HorizontalAlignment.LEFT, null),
    CENTER(HorizontalAlignment.CENTER, VerticalAlignment.CENTER),
    RIGHT(HorizontalAlignment.RIGHT, null),
    TOP(null, VerticalAlignment.TOP),
    MIDDLE(null, VerticalAlignment.CENTER),
    BOTTOM(null, VerticalAlignment.BOTTOM);

    private final HorizontalAlignment horizontalAlign;
    private final VerticalAlignment verticalAlign;

}
