package hufs.computer.studyroom.common.util.excel.core;

import java.io.IOException;
import java.io.OutputStream;

/*
 * 엑셀 생성/쓰기 기능의 최상위 인터페이스
 * - write(OutputStream): 워크북 내용을 출력 스트림에 작성
 *
 *
                     ExcelFile - 인터페이스
                               ↑
                 SXSSFExcelFile - 추상 클래스
                               ↑
            OneSheetExcelFile, MultiSheetExcelFile  - 구현 클래스
 */
public interface ExcelFile {
    void write(OutputStream stream) throws IOException;
}
