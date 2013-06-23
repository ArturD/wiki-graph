package com.wikia.reader.commands;/**
 * Author: Artur Dwornik
 * Date: 23.04.13
 * Time: 14:25
 */

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.wikia.api.model.PageInfo;
import com.wikia.api.service.PageServiceFactory;
import com.wikia.reader.text.classifiers.CompositeClassifier;
import com.wikia.reader.text.classifiers.model.ClassificationResult;
import com.wikia.reader.text.data.InstanceSource;

import java.net.URL;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

@Parameters(commandDescription = "Fetch documents into files.")
public class CrawlAndClassifyCommand implements Command {
    private static java.util.logging.Logger logger = java.util.logging.Logger.getLogger(CrawlAndClassifyCommand.class.toString());

    @Parameter(required = true)
    private List<String> urls;

    @Override
    public String getName() {
        return "CrawlAndClassify";
    }

    @Override
    public void execute(AppParams params) {
        try {
            CompositeClassifier classifierManager = new CompositeClassifier();
            for(String url: urls) {
                Iterator<PageInfo> iterator = new PageServiceFactory().get(new URL(url)).getPages().iterator();
                while( iterator.hasNext() ) {
                    PageInfo textChunk = iterator.next();
                    ClassificationResult classification = classifierManager.classify(
                            new InstanceSource(new URL(url), textChunk.getTitle(), new HashSet())
                    );
                    System.out.print(
                            String.format("\"%s\", \"%s\"\n", textChunk.getTitle(), classification.getSingleClass())
                    );
                }
            }
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Unexpected exception.", ex);
        }
    }
}