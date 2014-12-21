package Sample;


import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.formula.eval.NotImplementedException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import utils.Factory;
import utils.User;
import utils.UserFile;
import utils.Utils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.sql.SQLException;
import java.util.Iterator;

/**
 * Created by Амфетамин on 27.10.14.
 */
@WebServlet("/showxml.jsp")

public class ShowXml extends HttpServlet {
    private static String FileNotFound = "File not found";
    private static String SAVE_DIR = "ConvertedFiles";
    private String outPutFile;
    private String currentUserID;


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        currentUserID = Utils.checkCache(request);
        String xlsFile = (String) request.getAttribute(Utils.Uploaded_File);
        if (!(currentUserID == null || currentUserID.isEmpty())) {
            request.setCharacterEncoding("Cp1251");
            response.setContentType("text/xml;charset=Windows-1251");

            FileInputStream file = null;
            if (xlsFile == null || xlsFile.isEmpty()) {
                response.sendRedirect("/upload.jsp");
                return;
                /*request.setAttribute(Utils.Upload_Result, FileNotFound);
                request.getRequestDispatcher("/upload.jsp").forward(request, response);
                return;*/
            }
            try {
                PrintWriter out = response.getWriter();
                File localExcelFile = new File(xlsFile);
                file = new FileInputStream(localExcelFile);
                String appPath = request.getServletContext().getRealPath("");
                // constructs path of the directory to save uploaded file
                String savePath = appPath + File.separator + SAVE_DIR;
                // creates the save directory if it does not exists
                outPutFile = savePath + File.separator + localExcelFile.getName();
                File fileSaveDir = new File(savePath);
                if (!fileSaveDir.exists()) {
                    fileSaveDir.mkdir();
                }
                String extension = getExtension(xlsFile);
                if (extension.equals("xls")) {
                    createXML(file, out, true);
                } else if (extension.equals("xlsx")) {
                    createXML(file, out, false);

                } else {
                    request.setAttribute(Utils.Upload_Result, "UNKNOWN FORMAT");
                    request.getRequestDispatcher("/upload.jsp").forward(request, response);
                    return;
                }

                User current = Factory.getInstance().getUserDAO().getUser(Long.decode(currentUserID));
                Factory.getInstance().getFileDAO().addFile(new UserFile(localExcelFile.getName(), xlsFile, outPutFile, current));


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (TransformerException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                if (file != null) {
                    file.close();
                }
            }
        } else {
            response.sendRedirect("/index.jsp");
        }

    }

    private String getExtension(String filePath) {
        String extension = null;
        int i = filePath.lastIndexOf('.');
        if (i > 0) {
            extension = filePath.substring(i + 1);
        }
        return extension;

    }

    private void createXML(FileInputStream excelFile, PrintWriter out, boolean old) throws IOException, ParserConfigurationException, TransformerException {
        //Get the workbook instance for XLS file
        Workbook wb = null;
        if (old) {
            wb = new HSSFWorkbook(excelFile);
        } else {
            wb = new XSSFWorkbook(excelFile);
        }

        //Get first sheet from the workbook
        Sheet sheet = wb.getSheetAt(0);
        FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();
        DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();

        DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();

        Document document = documentBuilder.newDocument();
        // root element

        Element root = document.createElement("company");
        document.appendChild(root);


        //Iterate through each rows from first sheet
        for (Row row : sheet) {
            Element rowElement = document.createElement("row" + row.getRowNum());
            root.appendChild(rowElement);

            //For each row, iterate through each columns
            Iterator<Cell> cellIterator = row.cellIterator();
            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                handleCell(cell.getCellType(), cell, evaluator, rowElement, document, false);
            }
        }
        // write the content into xml file
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(document);
        StreamResult result = new StreamResult(new File(outPutFile));
        StreamResult result2 = new StreamResult(out);

        transformer.transform(source, result);//save on disc
        transformer.reset();
        transformer.transform(source, result2);//show output
    }


    private void handleCell(int type, Cell cell, FormulaEvaluator evaluator, Element rowElement, Document document, boolean formula) {

        DataFormatter formatter = new DataFormatter();
        try {
            String value = "";
            switch (type) {
                case Cell.CELL_TYPE_STRING:

                    value = formatter.formatCellValue(cell);
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                    if (DateUtil.isCellDateFormatted(cell)) {
                        value = formatter.formatCellValue(cell);
                    } else if (formula) {
                        value = Double.toString(cell.getNumericCellValue());
                    } else {
                        value = formatter.formatCellValue(cell);
                    }

                    break;
                case Cell.CELL_TYPE_BOOLEAN:
                    value = Boolean.toString(cell.getBooleanCellValue());
                    break;
                case Cell.CELL_TYPE_FORMULA:
                    evaluator.evaluate(cell);
                    handleCell(cell.getCachedFormulaResultType(), cell, evaluator, rowElement, document, true);
                    break;
            }

            Element element = document.createElement("Column" + cell.getColumnIndex());
            element.appendChild(document.createTextNode(value));
            rowElement.appendChild(element);

        } catch (NotImplementedException e) {//can't evaluate formula
            if (formula) {
                Element element = document.createElement("Column" + cell.getColumnIndex());
                element.appendChild(document.createTextNode(formatter.formatCellValue(cell)));
                rowElement.appendChild(element);
            }
        }
    }

    private void printXML(File xmlFile, PrintWriter out) throws ParserConfigurationException, IOException, SAXException, TransformerException {

        if (xmlFile.getAbsoluteFile().exists()) {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = null;
            try {
                transformer = transformerFactory.newTransformer();
            } catch (TransformerConfigurationException e) {
                e.printStackTrace();
            }
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document document = db.parse(xmlFile);
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(out);
            transformer.transform(source, result);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        currentUserID = Utils.checkCache(request);
        String xmlID = (String) request.getParameter(Utils.FILE_TO_SHOW);
        if (!(currentUserID == null || currentUserID.isEmpty())) {
            request.setCharacterEncoding("UTF-8");
            response.setContentType("text/xml;charset=UTF-8");
            if (!(xmlID == null || xmlID.isEmpty())) {
                long id = Long.decode(xmlID);
                if (id > 0) {
                    String xmlFile = Factory.getInstance().getFileDAO().getXML(id);
                    if (xmlFile != null && !xmlFile.isEmpty()) {

                        File file = new File(xmlFile);
                        try {
                            printXML(file, response.getWriter());
                        } catch (ParserConfigurationException e) {
                            e.printStackTrace();
                        } catch (SAXException e) {
                            e.printStackTrace();
                        } catch (TransformerConfigurationException e) {
                            e.printStackTrace();
                        } catch (TransformerException e) {
                            e.printStackTrace();
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }

                    }
                }
            }
        } else {
            response.sendRedirect("/index.jsp");
        }

    }

}
