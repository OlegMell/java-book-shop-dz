package org.home.helpers;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.home.entities.mongo.Author;
import org.home.entities.mongo.Book;
import org.home.entities.mongo.Genre;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExcelHelper {
    public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    static String SHEET = "Books";
    static String[] HEADER = {"Id", "Title", "Price", "Date", "Authors"};

    public static boolean hasExcelFormat(MultipartFile file) {
        return TYPE.equals(file.getContentType());
    }

    public static List<Book> mapExcel(InputStream is) {
        try {
            Workbook workbook = new XSSFWorkbook(is);

            Sheet sheet = workbook.getSheet(SHEET);
            Iterator<Row> rows = sheet.iterator();

            List<Book> books = new ArrayList<>();

            int rowNumber = 0;
            while (rows.hasNext()) {
                Row currentRow = rows.next();

                Iterator<Cell> cellsInRow = currentRow.iterator();

                Book book = new Book();

                int cellIdx = 0;
                while (cellsInRow.hasNext()) {
                    Cell currentCell = cellsInRow.next();

                    switch (cellIdx) {
                        case 0:
                            book.setPrice((float) currentCell.getNumericCellValue());
                            break;

                        case 1:
                            book.setTitle(currentCell.getStringCellValue());
                            break;

                        case 2:
                            String author = currentCell.getStringCellValue();
                            var arr = author.split(" ");
                            Author _author;
                            if (arr.length > 2) {
                                _author = new Author(author);

                            } else {
                                _author = new Author(arr[0], arr[1]);
                            }
                            book.getAuthors().add(_author);
                            break;

                        case 3:
                            book.setDate(LocalDate.parse(currentCell.getStringCellValue()));
                            break;

                        case 4:
                            book.setGenre(new Genre(currentCell.getStringCellValue()));
                            break;
                        default:
                            break;
                    }

                    cellIdx++;
                }

                books.add(book);
            }

            workbook.close();

            return books;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
        }
    }

    public static ByteArrayInputStream booksToExcel(List<Book> books) {

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream();) {
            Sheet sheet = workbook.createSheet(SHEET);

            // Header
            Row headerRow = sheet.createRow(0);

            for (int col = 0; col < HEADER.length; col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(HEADER[col]);
            }

            int rowIdx = 1;
            for (Book book : books) {
                Row row = sheet.createRow(rowIdx++);

                row.createCell(0).setCellValue(book.getId());
                row.createCell(1).setCellValue(book.getTitle());

                Float price = book.getPrice();
                if (price != null)
                    row.createCell(2).setCellValue(price);

                row.createCell(3).setCellValue(book.getDate().toString());
                List<Author> authors = book.getAuthors();

                StringBuilder _authorsStr = new StringBuilder();
                if (authors.size() > 1) {
                    authors.forEach(author -> _authorsStr.append(" ").append(author.getFirstName()).append(" ")
                            .append(author.getLastName()));
                } else {
                    _authorsStr.append(authors.get(0).getFirstName()).append(" ").append(authors.get(0).getLastName());
                }

                row.createCell(4).setCellValue(_authorsStr.toString());
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("fail to import data to Excel file: " + e.getMessage());
        }
    }
}
