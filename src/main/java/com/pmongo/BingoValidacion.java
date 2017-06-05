package com.pmongo;

import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.ServerAddress;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;

import org.bson.Document;

import com.mongodb.client.MongoCursor;
import static com.mongodb.client.model.Filters.*;
import com.mongodb.client.result.DeleteResult;
import static com.mongodb.client.model.Updates.*;
import com.mongodb.client.result.UpdateResult;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BingoValidacion {

	public static void main(String[] args){
		
		MongoClient mongoClient = new MongoClient();
		
		MongoDatabase database = mongoClient.getDatabase("bingo");
		
		MongoCollection<Document> collection = database.getCollection("cartones");
		
		System.out.println("cantidad de cartones: "+collection.count());
		
		Block<Document> printBlock = new Block<Document>() {
	        public void apply(final Document document) {
	            System.out.println(document.toJson());
	        }
	    };
	    
	    AggregateIterable<Document> aggregate = collection.aggregate(
	    	      Arrays.asList(
	    	              Aggregates.match(Filters.eq("numero", 1)),
	    	              Aggregates.group("$posicion", Accumulators.addToSet("tablas", "$tabla"))
	    	              
	    	      )
	    	);
	    
	    Document first = aggregate.first();
		if(first != null){
			System.out.println("Encontro: "+first.toJson());
	    }else{
	    	System.out.println("no encontro ninguno");
	    }
	    
	    
	    
	    
	}
	
}
