package com.meesho.notificationservice.services;

import com.meesho.notificationservice.models.SMS.Message;
import com.meesho.notificationservice.models.ElasticSearch.SearchEntity;
import com.meesho.notificationservice.models.ElasticSearch.SearchMessagesByPhoneNumberAndTimeRequest;
import com.meesho.notificationservice.models.ElasticSearch.SearchMessagesByTextAndTimeRequest;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.meesho.notificationservice.constants.Constants.INDEX_NAME;
import static com.meesho.notificationservice.constants.Constants.LOGGER_NAME;

@Slf4j
@Service
public class SearchService {
    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    private static final Logger logger = LoggerFactory.getLogger(LOGGER_NAME);

    public List<SearchEntity> findByPhoneNumberAndTime(SearchMessagesByPhoneNumberAndTimeRequest
                                                               searchMessagesByPhoneNumberAndTimeRequest) {
        Criteria timeCriteria = new Criteria("lastUpdatedAt").greaterThanEqual(
                        searchMessagesByPhoneNumberAndTimeRequest.getStartTime())
                .lessThanEqual(searchMessagesByPhoneNumberAndTimeRequest.getEndTime());
        Criteria nameCriteria = new Criteria("phoneNumber").contains(searchMessagesByPhoneNumberAndTimeRequest
                .getPhoneNumber());
        nameCriteria = nameCriteria.and(timeCriteria);
        Query searchQuery = new CriteriaQuery(nameCriteria).setPageable(PageRequest.of(
                searchMessagesByPhoneNumberAndTimeRequest.getPage(), searchMessagesByPhoneNumberAndTimeRequest.getSize()));
        SearchHits<SearchEntity> searchEntitySearchHits = elasticsearchOperations.search(searchQuery, SearchEntity.class,
                IndexCoordinates.of(INDEX_NAME));
        List<SearchEntity> searchEntities= new ArrayList<SearchEntity>();
        searchEntitySearchHits.forEach(searchHit->{
            searchEntities.add(searchHit.getContent());
        });
        return searchEntities;
    }

    public List<SearchEntity> findByTextAndTime(SearchMessagesByTextAndTimeRequest searchMessagesByTextAndTimeRequest) {
        Criteria timeCriteria = new Criteria("lastUpdatedAt").greaterThanEqual(
                searchMessagesByTextAndTimeRequest.getStartTime())
                .lessThanEqual(searchMessagesByTextAndTimeRequest.getEndTime());
        Criteria nameCriteria = new Criteria("text").contains(searchMessagesByTextAndTimeRequest.getText());
        nameCriteria = nameCriteria.and(timeCriteria);
        Query searchQuery = new CriteriaQuery(nameCriteria).setPageable(PageRequest.of(
                searchMessagesByTextAndTimeRequest.getPage(), searchMessagesByTextAndTimeRequest.getSize()));
        SearchHits<SearchEntity> searchEntitySearchHits = elasticsearchOperations.search(searchQuery, SearchEntity.class,
                        IndexCoordinates.of(INDEX_NAME));
        List<SearchEntity> searchEntities= new ArrayList<SearchEntity>();
        searchEntitySearchHits.forEach(searchHit->{
            searchEntities.add(searchHit.getContent());
        });
        return searchEntities;
    }

    public String createSearchIndex(Message message) {
        SearchEntity searchEntity = SearchEntity.builder().text(message.getText()).createdAt(message.getCreatedOn())
                .lastUpdatedAt(message.getLastUpdatedAt()).phoneNumber(message.getPhoneNumber())
                .status(message.getStatus()).build();
        IndexQuery indexQuery = new IndexQueryBuilder().withId(searchEntity.getId()).withObject(searchEntity).build();
        String documentId = elasticsearchOperations.index(indexQuery, IndexCoordinates.of(INDEX_NAME));
        logger.info(String.format("message %s indexed to index table ",searchEntity));
        return documentId;
    }
}
