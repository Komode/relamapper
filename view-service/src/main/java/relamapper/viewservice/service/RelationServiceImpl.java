package relamapper.viewservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import relamapper.viewservice.model.Message;
import relamapper.viewservice.model.RelationContainer;
import relamapper.viewservice.model.RelationResponse;

@Service
public class RelationServiceImpl implements RelationService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${relamapper.gw.api.url}")
    private String API_URL;

    @Override
    public boolean relationEstablished(String uuid, String uuid2) {
        logger.debug("check if relation is established between {} and {}", uuid, uuid2);

        RestTemplate template = new RestTemplate();

        String URL = API_URL + "/relations/check/" + uuid + "/" + uuid2;

        Message result = template.getForObject(URL, Message.class);

        if(result.getStatus().equals("error"))
            logger.warn(result.getResult());
        return (result.getStatus().equals("success") && result.getResult().equals("established"));
    }

    @Override
    public RelationResponse getByID(int id) {
        logger.debug("get relation by ID : {}", id);

        RestTemplate template = new RestTemplate();

        String URL = API_URL + "/relations/get/id/" + id;

        return template.getForObject(URL, RelationResponse.class);
    }

    @Override
    public RelationContainer getRelation(String uuid1, String uuid2) {
        logger.debug("get relation between {} and {}", uuid1, uuid2);

        RestTemplate template = new RestTemplate();

        String URL = API_URL + "/relations/get/" + uuid1 + "/" + uuid2;

        RelationResponse result = template.getForObject(URL, RelationResponse.class);

        RelationContainer container = new RelationContainer(uuid1);
        if(result != null) {
            container.addRelation(result.getRelationOf(uuid1));
        }
        return container;
    }

    @Override
    public RelationContainer getRelations(String uuid) {
        logger.debug("get relations of {}", uuid);

        RestTemplate template = new RestTemplate();

        String URL = API_URL + "/relations/get/" + uuid;
        ResponseEntity<RelationResponse[]> response = template.getForEntity(URL, RelationResponse[].class);
        RelationResponse[] relations = response.getBody();

        RelationContainer container = new RelationContainer(uuid);

        if(relations != null && relations.length > 0) {
            for(RelationResponse relation : relations) {
                logger.debug("REL ID : {}", relation.getId());
                container.addRelation(relation.getRelationOf(uuid));
            }
        }
        return container;
    }

    @Override
    public Message add(String uuid1, String uuid2, String type) {
        logger.debug("add relation of type : {} between {} and {}", type, uuid1, uuid2);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String,String> map = new LinkedMultiValueMap<>();
        map.add("uuid1", uuid1);
        map.add("uuid2", uuid2);
        map.add("type", type);

        HttpEntity<MultiValueMap<String,String>> request = new HttpEntity<>(map, headers);

        RestTemplate template = new RestTemplate();

        String URL = API_URL + "/relations/add";
        return template.postForObject(URL, request, Message.class);
    }

    @Override
    public Message edit(String uuid, int id, String type) {
        logger.debug("edit relation type of : {} by {}", id, uuid);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String,String> map = new LinkedMultiValueMap<>();
        map.add("uuid", uuid);
        map.add("id", String.valueOf(id));
        map.add("type", type);

        HttpEntity<MultiValueMap<String,String>> request = new HttpEntity<>(map, headers);

        RestTemplate template = new RestTemplate();

        String URL = API_URL + "/relations/edit";
        return template.postForObject(URL, request, Message.class);
    }

    @Override
    public Message remove(String uuid, int id) {
        logger.debug("remove relation with ID : {} by {}", id, uuid);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String,String> map = new LinkedMultiValueMap<>();
        map.add("uuid", uuid);
        map.add("id", String.valueOf(id));

        HttpEntity<MultiValueMap<String,String>> request = new HttpEntity<>(map, headers);

        RestTemplate template = new RestTemplate();

        String URL = API_URL + "/relations/delete";
        return template.postForObject(URL, request, Message.class);
    }
}
