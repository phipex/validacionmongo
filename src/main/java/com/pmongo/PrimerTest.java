package com.pmongo;

import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.ServerAddress;

import com.mongodb.client.MongoDatabase;
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

public class PrimerTest {

	public static void main(String[] arg){
		MongoClient mongoClient = new MongoClient();
		
		MongoDatabase database = mongoClient.getDatabase("mydb");
		
		MongoCollection<Document> collection = database.getCollection("test"); 
		
		Document doc = new Document("name", "MongoDB")
                .append("type", "database")
                .append("count", 1)
                .append("versions", Arrays.asList("v3.2", "v3.0", "v2.6"))
                .append("info", new Document("x", 203).append("y", 102));
		collection.insertOne(doc);
		
		List<Document> documents = new ArrayList<Document>();
		for (int i = 0; i < 100; i++) {
		    documents.add(new Document("i", i));
		}
		
		collection.insertMany(documents);
		
		System.out.println(collection.count());
		
		Document myDoc = collection.find().first();
		System.out.println(myDoc.toJson());
		
		MongoCursor<Document> cursor = collection.find().iterator();
		try {
		    while (cursor.hasNext()) {
		        System.out.println(cursor.next().toJson());
		    }
		} finally {
		    cursor.close();
		}
		
		for (Document cur : collection.find()) {
		    System.out.println(cur.toJson());
		}
		
		myDoc = collection.find(eq("i", 71)).first();
		System.out.println(myDoc.toJson());
		
		Block<Document> printBlock = new Block<Document>() {
		     public void apply(final Document document) {
		         System.out.println(document.toJson());
		     }
		};

		collection.find(gt("i", 50)).forEach(printBlock);
		
		collection.updateOne(eq("i", 10), new Document("$set", new Document("i", 110)));
		
		UpdateResult updateResult = collection.updateMany(lt("i", 100), inc("i", 100));
		System.out.println(updateResult.getModifiedCount());

		UpdateResult updateResult2 = collection.updateMany(lt("i", 100), new Document("$inc", new Document("i", 100))); System.out.println(updateResult.getModifiedCount());
		System.out.println(updateResult2.getModifiedCount());
		
		collection.deleteOne(eq("i", 110));
		
		DeleteResult deleteResult = collection.deleteMany(gte("i", 100));
		System.out.println(deleteResult.getDeletedCount());
		
		collection.createIndex(new Document("i", 1));
		
	}
	
}
