package com.adobe.aem.guides.wknd.core.models;

import com.adobe.aem.guides.wknd.core.models.impl.TripInfoAccordionModelImpl;

import java.util.List;

public interface TripInfoAccordion {

    String getContentType();

    List<TripInfoAccordionModelImpl.FaqItem> getFaqList();

    List<TripInfoAccordionModelImpl.Section> getSectionsList();

    String getDestinationType();
}