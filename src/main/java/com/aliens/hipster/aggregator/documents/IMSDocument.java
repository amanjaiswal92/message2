package com.aliens.hipster.aggregator.documents;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.collect.Lists;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class IMSDocument {

	String skuId;
	String ean;
	String sapSkuId;
	String styleId;
	String taxClass;
	Integer quantity;
	Double mrp;
	Double grossSP;
	String ageGroup;
	String size;
	String pdpSize;

	@Singular
	List<String> promotions= Lists.newArrayList();

	@Singular
	List<FCInventory> fCInventorys =Lists.newArrayList();
}
