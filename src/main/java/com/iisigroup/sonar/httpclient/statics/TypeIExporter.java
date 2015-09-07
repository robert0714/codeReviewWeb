package com.iisigroup.sonar.httpclient.statics;

import java.io.File;
import java.io.IOException; 
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap; 
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Set;

import net.sf.jxls.exception.ParsePropertyException;
import net.sf.jxls.transformer.XLSTransformer;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.PrintSetup;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iisigroup.scan.folder.internal.UserFolder;
import com.iisigroup.sonar.httpclient.JsonClient01;
import com.iisigroup.sonar.httpclient.internal.Component;
import com.iisigroup.sonar.httpclient.internal.Issue;
import com.iisigroup.sonar.httpclient.internal.PageIssues;
import com.iisigroup.sonar.httpclient.internal.Rule;
import com.iisigroup.sonar.httpclient.statics.model.MonthlyProjSum;
import com.iisigroup.sonar.httpclient.statics.model.ProjectSum;
import com.iisigroup.sonar.httpclient.statics.model.SumPer2Weeks;
 
 
 
// TODO: Auto-generated Javadoc
/**
 * The Class TypeIExporter.
 */
public class TypeIExporter {
    /** The Constant logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(TypeIExporter.class);
     
    
    /**
     * Export by jxls library.
     *
     * @param data the data
     * @param file the file
     * @throws ParsePropertyException the parse property exception
     * @throws InvalidFormatException the invalid format exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static void exportJxlsV1(final List<ProjectSum>  data, final File file) throws ParsePropertyException, InvalidFormatException, IOException {
        Map<String, Object> beans = new HashMap<String, Object>();
       
        beans.put("data", data);
        XLSTransformer transformer = new XLSTransformer();
        final Workbook aHSSFWorkbook = transformer.transformXLS(TypeIExporter.class.getResource("RLFP_TEMPLATE_v2.xls").openStream(), beans);
        POIUtils.writeWorkbookOut(file, aHSSFWorkbook);
    }
    
    /**
     * Export by jxls library.
     *
     * @param data the data
     * @param file the file
     * @throws ParsePropertyException the parse property exception
     * @throws InvalidFormatException the invalid format exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static void exportJxlsV3(final List<ProjectSum>  data, final File file) throws ParsePropertyException, InvalidFormatException, IOException {
        final Workbook aHSSFWorkbook = exportWorkbookJxlsV3(data );
        POIUtils.writeWorkbookOut(file, aHSSFWorkbook);
    }
    
    /**
     * Export workbook jxls v3.
     *
     * @param data the data
     * @return the workbook
     * @throws ParsePropertyException the parse property exception
     * @throws InvalidFormatException the invalid format exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static Workbook exportWorkbookJxlsV3(final List<ProjectSum>  data ) throws ParsePropertyException, InvalidFormatException, IOException {
        Map<String, Object> beans = new HashMap<String, Object>();
       
        if(CollectionUtils.isNotEmpty(data)){
           
            beans.put("name",  data.get(0).getKey());
        }else {
            beans.put("name", "data"); 
        }
        
        beans.put("data", data);
        XLSTransformer transformer = new XLSTransformer();
        final Workbook aHSSFWorkbook = transformer.transformXLS(TypeIExporter.class.getResource("RLFP_TEMPLATE_v3.xls").openStream(), beans);
        return aHSSFWorkbook;
    }
    
    /**
     * Export by jxls library.
     * 無法使用 jxls template方法
     *
     * @param data the data
     * @param file the file
     * @throws ParsePropertyException the parse property exception
     * @throws InvalidFormatException the invalid format exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static void exportJxlsV4(final List<MonthlyProjSum> data, final File file) throws ParsePropertyException, InvalidFormatException, IOException {
        final Map<String, Object> beans = new HashMap<String, Object>();
       
        final List<String> months = new ArrayList<String>();
        
        if(CollectionUtils.isNotEmpty(data)){
           for(MonthlyProjSum unit : data){
               final Set<String> monthSet = unit.getData().keySet();
               for(String month : monthSet ){
                   if(!months.contains(month)){
                       months.add(month);
                   }
               }
           }
        }
        
        Collections.sort ( months ) ;
        
        beans.put("months", months);
        beans.put("data", data);
        XLSTransformer transformer = new XLSTransformer();
        final Workbook aHSSFWorkbook = transformer.transformXLS(TypeIExporter.class.getResource("RLFP_TEMPLATE_v4.xls").openStream(), beans);
        POIUtils.writeWorkbookOut(file, aHSSFWorkbook);
    }
	
	/**
	 * Export xls v04.
	 *
	 * @param data the data
	 * @param targetFile the target file
	 * @throws ParsePropertyException the parse property exception
	 * @throws InvalidFormatException the invalid format exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void exportXlsV04(Map<String, List<SumPer2Weeks>> data,
			File targetFile)throws ParsePropertyException, InvalidFormatException, IOException {
		
		if(MapUtils.isEmpty(data)){
			return;
		}
		if(MapUtils.isNotEmpty(data)){
			 final Workbook wb = new HSSFWorkbook();
			 exportXlsV04(data, wb);
			  POIUtils.writeWorkbookOut(targetFile, wb);
		}
	}
	public static void exportXlsV04(Map<String, List<SumPer2Weeks>> data,
			final Workbook wb  )throws ParsePropertyException, InvalidFormatException, IOException {
		
		if(MapUtils.isEmpty(data)){
			return;
		}
		if(MapUtils.isNotEmpty(data)){
			
			//產生表頭
			 final List<String> months = new ArrayList<String>();
			 final List<SumPer2Weeks> allData = new ArrayList<SumPer2Weeks>();
			 for ( List<SumPer2Weeks> unitList: data.values() ){
				 allData.addAll(unitList);
			 }
			for (SumPer2Weeks unit : allData) {
				final Set<String> monthSet = unit.getData().keySet();
				for (String month : monthSet) {
					if (!months.contains(month)) {
						months.add(month);
					}
				}
			}
			 Collections.sort ( months ) ;
//			 final Workbook wb = new HSSFWorkbook();
			 Collections.sort ( allData ,new SumPer2WeeksComparator()) ;
			 processSheet("all",wb,allData,months);
			 
			 final Set<Entry<String, List<SumPer2Weeks>>> entrySet =recombine( data).entrySet() ;
			 
			 for( Entry<String, List<SumPer2Weeks>> entryUnit : entrySet ){
				 final String depart = entryUnit.getKey();
				 final List<SumPer2Weeks> unitData = entryUnit.getValue();
				 processSheet(depart,wb,unitData,months);
			 } 
		}
	}
	/**
	 * Recombine.
	 *
	 * @param src the src
	 * @return the map
	 */
	static Map<String, List<SumPer2Weeks>> recombine(Map<String, List<SumPer2Weeks>>  src){
		final Map<String, List<SumPer2Weeks>> result = new HashMap<String, List<SumPer2Weeks>>();
		 final Set<Entry<String, List<SumPer2Weeks>>> entrySet = src.entrySet() ;
		 
		 for( Entry<String, List<SumPer2Weeks>> entryUnit : entrySet ){
			 final String depart = entryUnit.getKey();
			 final List<SumPer2Weeks> unitData = entryUnit.getValue();
			 final  String newDepart =  extract(depart);
			 List<SumPer2Weeks> data = result.get(newDepart);
			 if( data == null ){
				 data = new ArrayList<SumPer2Weeks>();
			 }
			 data.addAll(unitData);
			 
			 result.put(newDepart, data);
			 
		 }
		return result;
	}
	
	/**
	 * Extract.
	 *
	 * @param sample the sample
	 * @return the string
	 */
	static String extract(String sample) {
		final Matcher matcher = Pattern.compile(regularRex).matcher(sample);
		if (matcher.find()) {
			return matcher.group(matcher.groupCount()) ;
		}
		return null ;
	}
	
	/** The Constant regularRex. */
	private static final String regularRex ="[5|1]0\\((.*技術處)" ;
	
	/**
	 * Process sheet.
	 *
	 * @param sheetName the sheet name
	 * @param wb the wb
	 * @param data the data
	 * @param months the months
	 */
	protected static void processSheet( final String sheetName , 
			final Workbook wb ,
			final List<SumPer2Weeks> data , 
			final List<String> months){
		final Sheet sheet = wb.createSheet(sheetName);
         
        
        sheet.setColumnWidth(0, (int) (14 * 256));;
        
        CellStyle style01 = buildCellStyle(sheet, 12, CellStyle.ALIGN_CENTER,
                        true, false, null,"標楷體");
        CellStyle style04 = buildCellStyle(sheet, 14, CellStyle.ALIGN_CENTER,
                        true, false, null,"標楷體");
        
        XlsUtils.setSheetCellPosValue(sheet, 0, 0, "中文姓名", style04, 30);
        
        for(int i=0;i < months.size();++i){
                XlsUtils.setSheetCellPosValue(sheet, 1+i, 0, months.get(i)+"瑕疵密度"
                        , style01, 30);
        }                       
         
        for(int i = 0 ; i < data.size() ; ++i ){
          //填入姓名
            XlsUtils.setSheetCellPosValue(sheet,0 , i+1 ,  data.get(i).getEmpData().getChtName() , style01, 30);
            for (int j = 0 ; j < months.size(); ++j ){
                final String month = months.get(j);
                final SumPer2Weeks mps = data.get(i);
                final String value = mps.getData().get(month);
                
                XlsUtils.setSheetCellPosValue(sheet, j+1, i+1, StringUtils.isNoneBlank(value) ?value:"" , style01, 30);
                sheet.setColumnWidth(j+1, (int) (30 * 256));
            }
        }
        
        sheet.setMargin(HSSFSheet.TopMargin, 0.6);
        sheet.setMargin(HSSFSheet.BottomMargin, 0.5);
        sheet.setMargin(HSSFSheet.LeftMargin, 0.5);
        sheet.setMargin(HSSFSheet.RightMargin, 0.5);
        
        PrintSetup printSetup = sheet.getPrintSetup();
        printSetup.setLandscape(false);
        printSetup.setPaperSize(PrintSetup.A4_PAPERSIZE);
        printSetup.setScale((short) 92);
        sheet.setHorizontallyCenter(true);
	}
	
    /**
     * Export by jxls library.
     * 無法使用 jxls template方法
     *
     * @param data the data
     * @param file the file
     * @throws ParsePropertyException the parse property exception
     * @throws InvalidFormatException the invalid format exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static void exportXlsV02(final List<SumPer2Weeks> data, final File file) throws ParsePropertyException, InvalidFormatException, IOException { 
       
        final List<String> months = new ArrayList<String>();
        
        if(CollectionUtils.isNotEmpty(data)){
           for(SumPer2Weeks unit : data){
               final Set<String> monthSet = unit.getData().keySet();
               for(String month : monthSet ){
                   if(!months.contains(month)){
                       months.add(month);
                   }
               }
           }
        }
        
        Collections.sort ( months ) ; 
        
        final Workbook wb = new HSSFWorkbook();
         
        processSheet("all",wb,data,months);
        
        
        POIUtils.writeWorkbookOut(file, wb);
    }
    
    /**
     * Export common xls v03.
     *
     * @param srcTmp the src tmp
     * @param data the data
     * @param file the file
     * @throws ParsePropertyException the parse property exception
     * @throws InvalidFormatException the invalid format exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
    protected static void exportCommonXlsV03(final List<PageIssues> srcTmp ,final UserFolder data , final File file)throws ParsePropertyException, InvalidFormatException, IOException{
    	 final Workbook wb00 = new HSSFWorkbook();
    	final Workbook wb   = generatCommonXlsV03(srcTmp, data ,wb00) ;
    	POIUtils.writeWorkbookOut(file, wb);
    }
   
   /**
    * Generat common xls v03.
    *
    * @param srcTmp the src tmp
    * @param data the data
    * @param wb the wb
    * @return the workbook
    * @throws ParsePropertyException the parse property exception
    * @throws InvalidFormatException the invalid format exception
    * @throws IOException Signals that an I/O exception has occurred.
    */
   public static Workbook   generatCommonXlsV03(final List<PageIssues> srcTmp ,final UserFolder data , final Workbook wb )throws ParsePropertyException, InvalidFormatException, IOException{
	   final List<Issue> issueList = new ArrayList<Issue>(); 
   	
   	final Map<String,Rule> ruleIndex = new HashMap<String,Rule>();
   	final Map<String,Component> componentIndex = new HashMap<String,Component>();
   	
   	for(PageIssues pageIssue : srcTmp){
   		final Rule[] rules = pageIssue.getRules();
   		final Component[] components = pageIssue.getComponents();
   		for(Rule unit :rules){
   			ruleIndex.put(unit.getKey(), unit);
   		}
   		for(Component unit :components ){
   			if(! "false".equals(unit.getEnabled())){
   				componentIndex.put(unit.getId(), unit);
   			}
   		}
   		for(Issue issue : pageIssue.getIssues()){
   			if(StringUtils.isBlank(issue.getCloseDate())){
   				issueList.add(issue);
   			}
   		} 
   	}
   	Collections.sort(issueList, new IssueComparator());
   	
//       final Workbook wb = new HSSFWorkbook();
       final Sheet sheet = wb.createSheet("CR查核結果表");
        
       
       sheet.setColumnWidth(0, (int) (80 * 256));
       sheet.setColumnWidth(1, (int) (10 * 256));
       sheet.setColumnWidth(2, (int) (40 * 256));
       sheet.setColumnWidth(3, (int) (40 * 256));
       sheet.setColumnWidth(4, (int) (40 * 256));
       sheet.setColumnWidth(5, (int) (10 * 256));
       sheet.setColumnWidth(6, (int) (10 * 256));
       sheet.setColumnWidth(7, (int) (40 * 256));;;
       
       CellStyle style01 = buildCellStyle(sheet, 11, CellStyle.ALIGN_LEFT,
                       true, false, null,"微軟正黑體");
       CellStyle style02 = buildCellStyle(sheet, 11, CellStyle.ALIGN_LEFT,
               true, true, null,"微軟正黑體");
       CellStyle style03 = buildCellStyle(sheet, 10, CellStyle.ALIGN_LEFT,
               true, true, null,"微軟正黑體");
       CellStyle style04 = buildCellStyle(sheet, 12, CellStyle.ALIGN_CENTER,
                       true, false, null,"微軟正黑體");
       CellStyle style05 = buildCellStyle(sheet, 11, CellStyle.ALIGN_LEFT,
               true, true, null,"微軟正黑體");
       CellStyle style06 = buildCellStyle(sheet, 11, CellStyle.ALIGN_LEFT,
               true, true, null,"微軟正黑體");
       CellStyle style07 = buildCellStyle(sheet, 11, CellStyle.ALIGN_LEFT,
               true, true, null,"微軟正黑體");
       style01.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND );
       style01.setFillForegroundColor(new HSSFColor.YELLOW().getIndex());
       style02.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND );
       style02.setFillForegroundColor(new HSSFColor.LIGHT_GREEN().getIndex());
       style06.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND );
       style06.setFillForegroundColor(new HSSFColor.LIGHT_CORNFLOWER_BLUE().getIndex());
       style07.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND );
       style07.setFillForegroundColor(new HSSFColor.LIGHT_TURQUOISE().getIndex());
       
       
       XlsUtils. setSheetCellBlank(sheet, 8 , 17 +issueList.size() ,style05);
       
       XlsUtils.setSheetCellPosValue(sheet, 0, 0, "查核同仁姓名 :", style06, 30);
       
       if(CollectionUtils.isNotEmpty(issueList)){
       	 XlsUtils.setSheetCellPosValue(sheet, 1, 0, "查核期間 :"+issueList.get(0).getUpdateDate(), style05, 30); 
       }else{
       	 XlsUtils.setSheetCellPosValue(sheet, 1, 0, "查核期間 :", style05, 30); 
       }   
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 1, 7));
       
		
       
		final List<String> codName =new ArrayList<String>();
		
		for (Component unit : componentIndex.values()){
			
			if(StringUtils.isNotBlank ( unit.getSubProjectId() )){
				codName.add(unit.getPath());
			}
		}
		int height =  25*codName.size() ;
		
		XlsUtils.setSheetCellPosValue(sheet, 0, 1, "程式碼Owner :"+data.getInfo().getEmpdata().getChtName(), style05,height);
		sheet.addMergedRegion(new CellRangeAddress(1, 1, 1, 7));
		
       XlsUtils.setSheetCellPosValue(sheet, 1, 1, "本次查核之所有程式名稱 :\n\r"+StringUtils.join(codName ,"\n\r"), style05,height);
       
       
       
       XlsUtils.setSheetCellPosValue(sheet, 0, 2, "", style02, 30);
       XlsUtils.setSheetCellPosValue(sheet, 1, 2, "查核同仁填寫欄", style06, 30);
       sheet.addMergedRegion(new CellRangeAddress(2, 2, 1, 3));
       XlsUtils.setSheetCellPosValue(sheet, 4, 2, "程式碼Owner填寫欄", style07, 30);
       XlsUtils.setSheetCellPosValue(sheet, 5, 2, "查核同仁填寫欄", style02, 30);
       XlsUtils.setSheetCellPosValue(sheet, 6, 2, "組長填寫欄", style02, 30);
       sheet.addMergedRegion(new CellRangeAddress(2, 2, 6, 7));
       
       XlsUtils.setSheetCellPosValue(sheet, 0, 3, "檢核項目", style04, 30);
       XlsUtils.setSheetCellPosValue(sheet, 1, 3, "發生次數", style04, 30);
       XlsUtils.setSheetCellPosValue(sheet, 2, 3, "所在程式之名稱與行數", style04, 30);
       XlsUtils.setSheetCellPosValue(sheet, 3, 3, "問題說明", style04, 30);
       XlsUtils.setSheetCellPosValue(sheet, 4, 3, "改善方式說明\r\n/未能改善原因", style04, 30);
       XlsUtils.setSheetCellPosValue(sheet, 5, 3, "查核同仁複查簽核日期", style06, 30);
       XlsUtils.setSheetCellPosValue(sheet, 6, 3, "組長簽核日期", style04, 30);
       XlsUtils.setSheetCellPosValue(sheet, 7, 3, "備註", style04, 30);
       
     
       //檢核項目欄位
       XlsUtils.setSheetCellPosValue(sheet, 0, 4, "Coding Convention", style02, 30);
       XlsUtils.setSheetCellPosValue(sheet, 0, 5, "", style01, 10);
       XlsUtils.setSheetCellPosValue(sheet, 0, 6, "Error Handling", style02, 30);
       XlsUtils.setSheetCellPosValue(sheet, 0, 7, " Error messages 應完整且易於了解", style03, 30);
       XlsUtils.setSheetCellPosValue(sheet, 0, 8, "", style01, 10);
       XlsUtils.setSheetCellPosValue(sheet, 0, 9, "Thread Safety", style02, 30);
       XlsUtils.setSheetCellPosValue(sheet, 0, 10, "不要自行產生執行緒，應使用批次元件(如Quartz、Batch)", style02, 30);
       XlsUtils.setSheetCellPosValue(sheet, 0, 11, "", style01, 10);
       XlsUtils.setSheetCellPosValue(sheet, 0, 12, "Performance", style02, 30);
       XlsUtils.setSheetCellPosValue(sheet, 0, 13, "Avoid large objects in memory, or using String to hold large documents which should be handled with better tools. For example, don't read a large XML document into a String, or DOM.", style03, 30);
       XlsUtils.setSheetCellPosValue(sheet, 0, 14, "Do not leave debugging code in production code.", style03, 30);
       XlsUtils.setSheetCellPosValue(sheet, 0, 15, "Sonar分析結果加入項目", style02, 30);
       
       
       for(int i = 0 ; i < issueList.size() ; ++i ){
       	
       	final Issue issue  =   issueList.get(i);
       	
       	String ruleId =issue.getRule();
       	Rule rule = ruleIndex.get(ruleId);
       	
       	XlsUtils.setSheetCellPosValue(sheet, 0, 16+i, rule.getName() , style03, 30);
       	
       	String codeLine = null ;
       	
       	try {
			codeLine = String.format("%s(%s)",componentIndex.get(issue.getComponentId() ).getPath() , issue.getLine());
		} catch (Exception e) {
			LOGGER.error(e.getMessage() ,e );
			//勝輝回報這邊發生 null-pointer發生exception後....servlet吐出錯誤
			
			LOGGER.error("project key: {} ,emp:{} happened to problem!!" ,
					data.getInfo().getProjectKey() ,
					data.getInfo().getEmpdata().getChtName());
			
		}
       	
       	
       	XlsUtils.setSheetCellPosValue(sheet, 2, 16+i, codeLine , style03, 30);
       	
       	XlsUtils.setSheetCellPosValue(sheet, 3, 16+i, issue.getMessage() , style03, 30);
       }
       XlsUtils.setSheetCellPosValue(sheet, 0, 16+issueList.size(), "Reviewer自行加入項目", style02, 30);
       
       
       sheet.setMargin(HSSFSheet.TopMargin, 0.6);
       sheet.setMargin(HSSFSheet.BottomMargin, 0.5);
       sheet.setMargin(HSSFSheet.LeftMargin, 0.5);
       sheet.setMargin(HSSFSheet.RightMargin, 0.5);
       
       
       
       sheet.createFreezePane(8, 4);
       
       PrintSetup printSetup = sheet.getPrintSetup();
       printSetup.setLandscape(false);
       printSetup.setPaperSize(PrintSetup.A4_PAPERSIZE);
       printSetup.setScale((short) 92);
       sheet.setHorizontallyCenter(true);
       return wb ; 
   }
    
    /**
     * Export by jxls library.
     * 無法使用 jxls template方法
     *
     * @param data the data
     * @param file the file
     * @throws ParsePropertyException the parse property exception
     * @throws InvalidFormatException the invalid format exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static void exportXlsV03(final UserFolder data, final File file) throws ParsePropertyException, InvalidFormatException, IOException { 
          	
    	final List<PageIssues> srcTmp  = getSrcV03(data );
    	
    	exportCommonXlsV03(srcTmp, data, file);
    	
    } 

	/**
	 * Gets the src v03.
	 *
	 * @param data the data
	 * @return the src v03
	 * @throws ParsePropertyException the parse property exception
	 * @throws InvalidFormatException the invalid format exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static List<PageIssues> getSrcV03(final UserFolder data 
			 ) throws ParsePropertyException,
			InvalidFormatException, IOException {
		final JsonClient01 client = new JsonClient01();

		/***
		 * 2015.03.02 意鍰反應已經作過的分析找不到所以將當天的限制換掉
		 * 
		 * **/
		final List<PageIssues> srcTmp = client.searchIssuessByProjectKey(data
				.getInfo().getProjectKey());
		return srcTmp;
	}
    
    /**
     * Export by jxls library.
     * 無法使用 jxls template方法
     *
     * @param data the data
     * @param file the file
     * @throws ParsePropertyException the parse property exception
     * @throws InvalidFormatException the invalid format exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static void exportXlsV01(final List<MonthlyProjSum> data, final File file) throws ParsePropertyException, InvalidFormatException, IOException { 
       
        final List<String> months = new ArrayList<String>();
        
        if(CollectionUtils.isNotEmpty(data)){
           for(MonthlyProjSum unit : data){
               final Set<String> monthSet = unit.getData().keySet();
               for(String month : monthSet ){
                   if(!months.contains(month)){
                       months.add(month);
                   }
               }
           }
        }
        
        Collections.sort ( months ) ; 
        
        final Workbook wb = new HSSFWorkbook();
        final Sheet sheet = wb.createSheet("all");
         
        
        sheet.setColumnWidth(0, (int) (14 * 256));;
        
        CellStyle style01 = buildCellStyle(sheet, 12, CellStyle.ALIGN_CENTER,
                        true, false, null,"標楷體");
        CellStyle style04 = buildCellStyle(sheet, 14, CellStyle.ALIGN_CENTER,
                        true, false, null,"標楷體");
        
        XlsUtils.setSheetCellPosValue(sheet, 0, 0, "中文姓名", style04, 30);
        
        for(int i=0;i < months.size();++i){
                XlsUtils.setSheetCellPosValue(sheet, 1+i, 0, months.get(i)+"瑕疵密度"
                        , style01, 30);
        }                       
         
        for(int i = 0 ; i < data.size() ; ++i ){
          //填入姓名
            XlsUtils.setSheetCellPosValue(sheet,0 , i+1 ,  data.get(i).getEmpData().getChtName() , style01, 30);
            for (int j = 0 ; j < months.size(); ++j ){
                final String month = months.get(j);
                final MonthlyProjSum mps = data.get(i);
                final String value = mps.getData().get(month);
                
                XlsUtils.setSheetCellPosValue(sheet, j+1, i+1, StringUtils.isNoneBlank(value) ?value:"" , style01, 30);
                sheet.setColumnWidth(j+1, (int) (30 * 256));
            }
        }
        
        sheet.setMargin(HSSFSheet.TopMargin, 0.6);
        sheet.setMargin(HSSFSheet.BottomMargin, 0.5);
        sheet.setMargin(HSSFSheet.LeftMargin, 0.5);
        sheet.setMargin(HSSFSheet.RightMargin, 0.5);
        
        PrintSetup printSetup = sheet.getPrintSetup();
        printSetup.setLandscape(false);
        printSetup.setPaperSize(PrintSetup.A4_PAPERSIZE);
        printSetup.setScale((short) 92);
        sheet.setHorizontallyCenter(true);
        POIUtils.writeWorkbookOut(file, wb);
    }
    
    /**
     * 調整字型/畫線.
     *
     * @param sheet the sheet
     * @param fontSize the font size
     * @param columnStyle            {@see CellStyle.ALIGN_CENTER}
     * @param line            畫線(預設值:黑色)
     * @param bold            粗體字
     * @param color            底色
     * @param fontName the font name
     * @return the cell style
     */
    private static CellStyle buildCellStyle(Sheet sheet, Integer fontSize,
                    short columnStyle, boolean line, boolean bold, Integer color,String fontName) {
            HSSFWorkbook wb = (HSSFWorkbook) sheet.getWorkbook();
            HSSFCellStyle cellStyle = (HSSFCellStyle) wb.createCellStyle();
            cellStyle.setAlignment(columnStyle); // 置中對齊
            cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 水平對齊
            cellStyle.setWrapText(true);

            Font titleFont = wb.createFont();
            if (bold) {
                    titleFont.setBoldweight(Font.BOLDWEIGHT_BOLD); // 粗體
            }
            titleFont.setFontName(fontName);
            if (fontSize != null) {
                    short fontHeightInPoints = fontSize.shortValue();
                    titleFont.setFontHeightInPoints(fontHeightInPoints);
            }
            cellStyle.setFont(titleFont);
            if (color != null) {
                    cellStyle.setFillForegroundColor((short) (int) color);
                    cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
            }
            // cellStyle.setDataFormat(sheet.getWorkbook().createDataFormat().getFormat("#,##0"));
            // // 千分位數學符號(ex. 1,000)

            
            if (line) {
                    cellStyle.setBorderBottom(CellStyle.BORDER_THIN);
                    cellStyle.setBorderLeft(CellStyle.BORDER_THIN);
                    cellStyle.setBorderRight(CellStyle.BORDER_THIN);
                    cellStyle.setBorderTop(CellStyle.BORDER_THIN);
            }

            return cellStyle;
    }
   


	/**
	 * The Class IssueComparator.
	 */
	private static class IssueComparator implements Comparator<Issue>{

		/**
		 * Compare.
		 *
		 * @param object1 the object1
		 * @param object2 the object2
		 * @return the int
		 */
		public int compare(Issue object1, Issue object2) {			
			  return new CompareToBuilder()
              .append(object1.getRule(), object2.getRule())
              .append(object1.getComponentId(), object2.getComponentId())
              .append(object1.getLine(), object2.getLine())
              .toComparison();
		}
    }
	
	/**
	 * The Class SumPer2WeeksComparator.
	 */
	private static class SumPer2WeeksComparator implements Comparator<SumPer2Weeks>{

		/**
		 * Compare.
		 *
		 * @param o1 the o1
		 * @param o2 the o2
		 * @return the int
		 */
		@Override
		public int compare(SumPer2Weeks o1, SumPer2Weeks o2) {
			return new CompareToBuilder().append(o1.getEmpData().getEmpId(),
					o2.getEmpData().getEmpId()).toComparison();
		}
    }
}
