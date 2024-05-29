package org.wildflowergardening.backend.core.wildflowergardening.application;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Component;
import org.wildflowergardening.backend.core.wildflowergardening.application.dto.SleepoverExcelDto;

@Component
@RequiredArgsConstructor
@Slf4j
class SleepoverExcelCreator {

  public void writeOnOutputStream(List<SleepoverExcelDto> dtoList, OutputStream outputStream)
      throws IOException {
    try (Workbook workbook = new SXSSFWorkbook()) {
      Sheet sheet = workbook.createSheet("외박신청내역");

      int rowNum = 0;

      // header
      Row headerRow = sheet.createRow(0);
      headerRow.createCell(0, CellType.STRING).setCellValue("성함");
      headerRow.createCell(1, CellType.STRING).setCellValue("외박시작일");
      headerRow.createCell(2, CellType.STRING).setCellValue("외박종료일");
      headerRow.createCell(3, CellType.STRING).setCellValue("외박신청일시");
      rowNum++;

      // data
      for (SleepoverExcelDto dto : dtoList) {
        Row row = sheet.createRow(rowNum);
        row.createCell(0, CellType.STRING).setCellValue(dto.getHomelessName());
        row.createCell(1, CellType.STRING).setCellValue(dto.getStartDate());
        row.createCell(2, CellType.STRING).setCellValue(dto.getEndDate());
        row.createCell(3, CellType.STRING).setCellValue(dto.getCreatedAt());
        rowNum++;
      }
      workbook.write(outputStream);
    }
  }
}
