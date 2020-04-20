package com.gillianbc.moviecatalogservice.resource;

import com.gillianbc.moviecatalogservice.model.CatalogItem;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {

  @RequestMapping("/{userId}")
  public List<CatalogItem> getCatalog(String userId){
    return Collections.singletonList(new CatalogItem("Fried Green Tomatoes", "One of my favourite films", 4));
  }
}
