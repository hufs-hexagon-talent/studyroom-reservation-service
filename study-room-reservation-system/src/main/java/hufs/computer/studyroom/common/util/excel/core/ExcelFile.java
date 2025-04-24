package hufs.computer.studyroom.common.util.excel.core;

import java.io.IOException;
import java.io.OutputStream;

/*
                     ExcelFile - 인터페이스
                               ↑
                 SXSSFExcelFile - 추상 클래스
                               ↑
            OneSheetExcelFile, MultiSheetExcelFile  - 구현 클래스
 */
public interface ExcelFile {
    void write(OutputStream stream) throws IOException;

//    void addRows(List<T> data);
}
