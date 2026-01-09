package org.farm2.service.inter;

import org.farm2.tools.web.domain.WebDoc;

public interface WebResourceServiceInter {
    public WebDoc downloadWebPage(String url, String processKey);
}
