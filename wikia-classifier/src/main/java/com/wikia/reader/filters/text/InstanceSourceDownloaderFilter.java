package com.wikia.reader.filters.text;/**
 * Author: Artur Dwornik
 * Date: 07.04.13
 * Time: 20:49
 */

import com.wikia.api.service.Page;
import com.wikia.api.service.PageServiceFactory;
import com.wikia.reader.filters.FilterBase;
import com.wikia.reader.text.data.InstanceSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;

public class InstanceSourceDownloaderFilter extends FilterBase<InstanceSource, Page> {
    private static Logger logger = LoggerFactory.getLogger(InstanceSourceDownloaderFilter.class);

    public InstanceSourceDownloaderFilter() {
        super(InstanceSource.class, Page.class);
    }

    @Override
    protected Page doFilter(InstanceSource params) {
        try {
            return new PageServiceFactory().get(new URL(params.getWikiRoot().toString())).getPage(params.getTitle());
        } catch (IOException e) {
            logger.error(String.format("Cannot fetch instance %d %d"
                            ,params.getWikiRoot().toString()
                            , params.getTitle()), e);
        }
        return null;
    }
}