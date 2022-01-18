package com.testlio.lib.listeners;

import lombok.extern.slf4j.Slf4j;
import org.testng.IExecutionListener;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import ru.yandex.qatools.properties.PropertyLoader;
import ru.yandex.qatools.properties.annotations.Property;
import com.testlio.lib.properties.AutomationProperties;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.Properties;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static javax.xml.transform.OutputKeys.ENCODING;
import static javax.xml.transform.OutputKeys.INDENT;
import static javax.xml.xpath.XPathConstants.NODESET;
import static javax.xml.xpath.XPathFactory.newInstance;
import static org.apache.commons.io.FileUtils.copyFileToDirectory;
import static com.testlio.lib.properties.Execution.properties;
import static com.testlio.lib.utility.functional.OptionalConsumer.of;

@Slf4j
public class TestExecutionStartFinishListener implements IExecutionListener {

    @Property(value = "allure.results.directory")
    private String allureReporterFolderPath;

    @Property(value = "environment.allure.configuration")
    private String environmentConfigurationFileTextPath;

    @Override
    public void onExecutionStart() {
        log.info("... Test Execution started ...");
        PropertyLoader.populate(this);
    }

    @Override
    public void onExecutionFinish() {
        log.info("... Test Execution finish ...");
        copyConfigurationsForReport();
        // deleteIgnoreNodesFromSurefireJUnitResults();
    }

    private void copyConfigurationsForReport() {
        log.info("Copying allure report env. file into artifacts folder");
        Path allureArtifactsFolderPath = Paths.get(allureReporterFolderPath);
        if (allureArtifactsFolderPath.toFile().exists()) {
            if (new File(environmentConfigurationFileTextPath).exists()) {
                FileOutputStream outputStream = null;
                try {
                    File environmentConfigurationFile = new File(environmentConfigurationFileTextPath);
                    String envsFileName = environmentConfigurationFile.getName();

                    copyFileToDirectory(new File(environmentConfigurationFileTextPath),
                            new File(allureReporterFolderPath));

                    outputStream = new FileOutputStream(allureReporterFolderPath + "/" + envsFileName,
                            true);

                    preparePropertyFile(properties()).store(outputStream, "");
                } catch (IOException e) {
                    log.error("Fail to copy env. file for allure report");
                } finally {
                    if (nonNull(outputStream)) {
                        try {
                            outputStream.close();
                        } catch (IOException e) {
                            log.error(e.getMessage());
                        }
                    }
                }
            } else {
                String errorMessage = "Environment configuration file " + environmentConfigurationFileTextPath
                        + " doesn't exist";
                throw new IllegalStateException(errorMessage);
            }
        }
    }

    private Properties preparePropertyFile(AutomationProperties automationEnvironmentProperties) {
        Properties properties = new Properties();

        properties.setProperty("Browser type", String.valueOf(automationEnvironmentProperties.getBrowserName()));

        properties.setProperty("Allure report results directory", allureReporterFolderPath);
        properties.setProperty("Allure report env. configuration property file", environmentConfigurationFileTextPath);
        properties.setProperty("Parallel thread counts",
                String.valueOf(automationEnvironmentProperties.getThreadCounts()));
        properties.setProperty("Execution type", String.valueOf(automationEnvironmentProperties.getExecutionType()));
        properties.setProperty("Provider", String.valueOf(automationEnvironmentProperties.getProvider()));
        properties.setProperty("Capture screenshots for each step",
                String.valueOf(automationEnvironmentProperties.isTakesScreenshotOnEachStep()));

        return properties;
    }

    @SuppressWarnings("unused")
    @Deprecated
    private void deleteIgnoreNodesFromSurefireJUnitResults() {
        File[] reportDirectories = new File("target/surefire-reports").listFiles(File::isDirectory);
        File surefireReportFolder = asList(reportDirectories)
                .stream()
                .filter(dir -> !dir.getName().equals("junitreports")).filter(dir -> !dir.getName().equals("old"))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("no surefire reporter folder found"));

        log.info(format("Surefire report folder name is : %s", surefireReportFolder.getName()));

        of(ofNullable(surefireReportFolder)).ifPresent(consumer1 -> {
            File[] reportFiles = surefireReportFolder.listFiles(File::isFile);
            Optional<File> surefireReportXmlFile = asList(reportFiles).stream()
                    .filter(dir -> dir.getName().contains(".xml")).findFirst();

            surefireReportXmlFile.ifPresent(consumer2 -> {
                log.info(format("Surefire report xml file name is : %s", surefireReportXmlFile.get().getName()));
                try {
                    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                    Document document = dbf.newDocumentBuilder().parse(surefireReportXmlFile.get());
                    XPath xPath = newInstance().newXPath();
                    String expression = "//testcase[descendant-or-self::ignored]";
                    NodeList nodes = (NodeList) xPath.compile(expression).evaluate(document, NODESET);

                    Element xmlDocument = document.getDocumentElement();

                    log.info("Deleting <ignore> elements from surefire report");
                    for (int i = 0; i < nodes.getLength(); i++) {
                        xmlDocument.removeChild(nodes.item(i));
                    }

                    Transformer tf = TransformerFactory.newInstance().newTransformer();
                    tf.setOutputProperty(ENCODING, "UTF-8");
                    tf.setOutputProperty(INDENT, "yes");
                    Writer out = new StringWriter();
                    tf.transform(new DOMSource(document), new StreamResult(out));

                    log.info("Building new xml file without ignore nodes");
                    surefireReportXmlFile.get().setWritable(true);
                    try (BufferedWriter writer = new BufferedWriter(
                            new FileWriter(surefireReportXmlFile.get(), false))) {
                        writer.write(out.toString());
                    }
                } catch (SAXException e) {
                    log.error(e.getMessage());
                } catch (IOException e) {
                    log.error(e.getMessage());
                } catch (ParserConfigurationException e) {
                    log.error(e.getMessage());
                } catch (XPathExpressionException e) {
                    log.error(e.getMessage());
                } catch (TransformerConfigurationException e) {
                    log.error(e.getMessage());
                } catch (TransformerFactoryConfigurationError e) {
                    log.error(e.getMessage());
                } catch (TransformerException e) {
                    log.error(e.getMessage());
                }
            });
        }).ifNotPresent(() -> log.error("Surefire Report Folder is not present"));
    }

}