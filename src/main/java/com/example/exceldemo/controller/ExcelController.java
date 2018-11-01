package com.example.exceldemo.controller;

import com.example.exceldemo.entity.CapacityDO;
import com.example.exceldemo.entity.CapacityModelVO;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hollysys.common.util.DateTimeUtil;
import com.hollysys.haier.robot.bean.RestResponse;
import com.hollysys.poi.excel.Excel;
import com.hollysys.poi.excel.ExcelType;
import com.hollysys.poi.excel.sheet.Row;
import com.hollysys.poi.excel.sheet.cell.Cell;
import com.hollysys.poi.excel.sheet.cell.data.CellValue;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Carmelo
 * @date 2018/10/30 - 13:40
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api/v1/devices")
@EnableAutoConfiguration
public class ExcelController {
    /**
     * 下载计划与实际产量分析模板
     * 生成Excel需要以行为单位，进行逐行生成。
     *
     * @return 操作结果
     */
    @RequestMapping(value = "/report", method = RequestMethod.GET)
    @DateTimeFormat(pattern = "yyyy-MM")
    @ApiOperation(value = "下载计划与实际产量分析模板", notes = "下载计划与实际产量分析模板")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "productionId", value = "产线", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "date", value = "日期", paramType = "query", dataType = "yearMonth"),
    })
    public RestResponse exportTemplate(@RequestParam(value = "productionId", required = false) String productionID,
                                       @RequestParam(value = "date", required = false) YearMonth yearMonth,
                                       HttpServletResponse response) {
        try(ServletOutputStream outputStream = response.getOutputStream()) {
            String fileName = "product_capacity_" + DateTimeUtil.format(LocalDateTime.now());
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Pragma", "No-cache");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
            //数据来源
            List<String> list = Lists.newArrayList("湖人","火箭","马刺");

            //创建工作簿，sheet
            XSSFWorkbook wb = new XSSFWorkbook();
            XSSFSheet sheet = wb.createSheet("ABBBB");

            //设置单元格加锁格式
            XSSFCellStyle unLockStyle = wb.createCellStyle();
            unLockStyle.setLocked(false);
            XSSFCellStyle lockStyle = wb.createCellStyle();
            lockStyle.setLocked(true);

            //设置sheet单元格的宽度和高度
            sheet.setColumnWidth(0,20*256);
            sheet.setDefaultRowHeightInPoints(30);

            //按行创建单元格，并进行赋值
            XSSFRow row = sheet.createRow(0);
            row.createCell(0).setCellValue("设备编号");
            row.createCell(1).setCellValue("日产量");
            int dayvalue = DateTimeUtil.lastDayOfMonth(yearMonth);
            for (int i = 1;i <= dayvalue; i++) {
                XSSFCell cell = row.createCell(i + 1);
                cell.setCellValue(DateTimeUtil.format(yearMonth.atDay(i)));
                sheet.setColumnWidth(i,20*256);
            }
            for (int j = 0;j<list.size();j++) {
                row = sheet.createRow(j * 2 + 1);
                row.createCell(0).setCellValue(list.get(j));
                row.createCell(1).setCellValue("计划");
                //选择不加锁的区域
                for (int i = 0;i < dayvalue;i++) {
                    row.createCell(i+2).setCellStyle(unLockStyle);
                }

                row = sheet.createRow(j*2+2);
                row.createCell(0).setCellValue(list.get(j));
                row.createCell(1).setCellValue("实际");
                //选择不加锁的区域
                for (int i = 0;i < dayvalue;i++) {
                    row.createCell(i+2).setCellStyle(unLockStyle);
                }

                //合并单元格
                sheet.addMergedRegion(new CellRangeAddress(j*2+1,j*2+2,0,0));
            }

            //对sheet中的指定单元格加锁。调用sheet.protectSheet()默认sheet内所有单元格全部加锁
            //下一步则根据业务选择不加锁的区域，将对应的单元格的格式设置为unLockStyle
            sheet.protectSheet("12345");
            wb.write(outputStream);
        } catch (IOException e) {
            return new RestResponse()
                    .setSuccess(false)
                    .setMessage("import exception. (" + e.getMessage() + ")");
        }
        return new RestResponse()
                .setSuccess(true)
                .setMessage("模板下载成功");
    }

    /**
     * 模板数据批量导入
     *
     * @return 操作结果
     */
    @RequestMapping(value = "/imports", method = RequestMethod.POST,consumes = "multipart/form-data")
    @ApiOperation(value = "模板数据批量导入", notes = "模板数据批量导入")
    public RestResponse<List<CapacityModelVO>> importTemplate(@RequestParam(value = "productionID", required = false) String productionID,
                                                              HttpServletRequest request) {
        MultipartHttpServletRequest fileRequest = ((MultipartHttpServletRequest) request);
        MultipartFile file = fileRequest.getFile("file");
        try (InputStream inputStream = file.getInputStream()) {
            List<CapacityDO> capacities = Lists.newLinkedList();
            Map<CellValue,CapacityDO> resultMap = Maps.newHashMap();
            Map<String,Map<CellValue,CapacityDO>> finalMap = Maps.newHashMap();

            AtomicInteger atomicInteger = new AtomicInteger(0);
            Excel excel = Excel.of(inputStream, ExcelType.XLSX);
            //1 读取第一行，并将日期信息存储到Map集合中
            excel.sheet(0).ifPresent(sheet -> {
                Map<String, CellValue> dayMap = sheet.getRow(0).stream()
                        .filter(cell -> !Objects.equals(cell.address().columnName(), "A") && !Objects.equals(cell.address().columnName(), "B"))
                        .filter(cell -> cell.data().asString().isPresent())
                        .collect(Collectors.toMap(cell -> cell.address().columnName(), cell -> cell.data()));
            //2 读取产量值
            while (true) {
                //2.1 读取计划产量值
                Row rowPlan = sheet.row(atomicInteger.incrementAndGet());
                if (Objects.isNull(rowPlan)) {
                    break;
                }
                CapacityDO capacityDO = new CapacityDO();
                rowPlan.cellStream()
                        .filter(cell -> cell.data().asString().isPresent())
                        .filter(cell -> !Objects.equals(cell.address().columnName(), "A")
                                && !Objects.equals(cell.address().columnName(), "B"))
                        .forEach(cell -> {
                            rowPlan.cell("A")
                                    .ifPresent(cellValue1 ->
                                            cellValue1.data().asString().ifPresent(capacityDO::setDeviceID));
                            dayMap.get(cell.address().columnName()).asString().ifPresent(cellvalue -> {
                                String[] date = cellvalue.split("-");
                                capacityDO.setYear(Integer.parseInt(date[0]));
                                capacityDO.setMonth(Integer.parseInt(date[1]));
                                capacityDO.setDay(Integer.parseInt(date[2]));
                            });
                            cell.data().asLong().ifPresent(cellvalue -> {
                                rowPlan.cell("B").ifPresent(cell1 -> {
                                    cell1.data().asString().ifPresent(cell1value -> {
                                        if (Objects.equals(cell1value,"计划")) {
                                            capacityDO.setPlanCapacity(cellvalue);
                                        }
                                    });
                                });
                            });
                            resultMap.put(dayMap.get(cell.address().columnName()),capacityDO);
                            rowPlan.cell("A").ifPresent(cells -> cells.data().asString().ifPresent(cellsvalue -> finalMap.put(cellsvalue,resultMap)));
                            capacities.add(capacityDO);
                        });

                //2.2 读取实际产量值
                Row rowActual = sheet.row(atomicInteger.incrementAndGet());
                if (Objects.isNull(rowActual)) {
                    break;
                }
                rowActual.cellStream()
                        .filter(cell -> cell.data().asString().isPresent())
                        .filter(cell -> !Objects.equals(cell.address().columnName(), "A") && !Objects.equals(cell.address().columnName(), "B"))
                        .forEach(cell -> {
                            cell.data().asLong().ifPresent(cellvalue -> {
                                rowActual.cell("B").ifPresent(cell1 -> {
                                    cell1.data().asString().ifPresent(cell1value -> {
                                        if (Objects.equals(cell1value,"实际")) {
                                            rowActual.cell("A").ifPresent(value ->
                                                    value.data().asString().ifPresent(value1 -> {
                                                finalMap.get(value1).get(dayMap.get(cell.address().columnName())).setActualCapacity(cellvalue);
                                            }));
                                        }
                                    });
                                });
                            });
                        });
            }
            });
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return new RestResponse<List<CapacityModelVO>>()
                .setSuccess(true)
                .setMessage("模板导入成功")
                .setData(null);

    }

}
