package com.aliens.hipster.aggregator.documents;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PCMDocument {

	String legalBrandName;
	String pcmArticleType;
	List<String> categories;
}
