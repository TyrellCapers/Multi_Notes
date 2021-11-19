package persistence;

import core.Task;
import java.io.File;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class XMLPersistence {
    public static void save(Task[] tasks, String xmlFilePath){
        try {
            //Create new XML document
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dbBuilder = dbFactory.newDocumentBuilder();
            Document document = dbBuilder.newDocument();

            //Develop xml content
            Element root = document.createElement("Tasks");
            document.appendChild(root);

            for(Task task : tasks){
                Element taskElement = document.createElement("Task");
                taskElement.setAttribute("name", task.getName());
                taskElement.setAttribute("status", task.getStatus());

                Element notesElement = document.createElement("Notes");
                notesElement.setTextContent(task.getNotes());
                taskElement.appendChild(notesElement);

                root.appendChild(taskElement);
            }

            //Save document to location
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(new File(xmlFilePath));

            // If you use
            // StreamResult result = new StreamResult(System.out);
            // the output will be pushed to the standard output ...
            // You can use that for debugging

            transformer.transform(domSource, streamResult);
        }
        catch(Exception e){
            System.out.println("Exception caught when saving xml");
            System.out.println(e.getMessage());
            e.printStackTrace();
        }


    }

    public static Task[] load(String xmlFilePath){
        try {
            //Load XML into DOM Parser
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dbBuilder = dbFactory.newDocumentBuilder();
            Document loadedDocument = dbBuilder.parse(xmlFilePath);

            //Load task array
            ArrayList<Task> taskArray = new ArrayList<>();
            NodeList taskElementList = loadedDocument.getElementsByTagName("Task");
            for(int i = 0; i < taskElementList.getLength(); i++){
                Element item = (Element) taskElementList.item(i);

                Element notesElement = (Element) item.getElementsByTagName("Notes").item(0);

                Task task = new Task();
                task.setName(item.getAttribute("name"));
                task.setStatus(item.getAttribute("status"));
                if(notesElement != null){
                    task.setNotes(notesElement.getTextContent());
                }

                taskArray.add(task);
            }
            return taskArray.toArray(new Task[0]);
        }
        catch(Exception e){
            System.out.println("Exception caught when trying to load xml");
            e.printStackTrace();
            return null;
        }
    }
}
