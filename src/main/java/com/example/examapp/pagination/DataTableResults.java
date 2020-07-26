package com.example.examapp.pagination;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

/**
 * The Class DataTableResults.
 *
 * @param <T> the generic type
 */

@Getter
@Setter
public class DataTableResults<T> {
	
	private String draw;
	private String recordsFiltered;
	private String recordsTotal;
	@SerializedName("data")
	List<T> listOfDataObjects;
		
	public String getJson() {
		return new Gson().toJson(this);
	}	
}
