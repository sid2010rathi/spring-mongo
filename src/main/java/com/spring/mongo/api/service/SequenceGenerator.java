package com.spring.mongo.api.service;
import java.util.Objects;

import javax.sound.midi.Sequence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.spring.mongo.api.model.DatabaseSequence;

@Service
public class SequenceGenerator {

	private MongoOperations mongoOperations;
	
	@Autowired
	public SequenceGenerator(MongoOperations mongoOperations) {
		this.mongoOperations = mongoOperations;
	}
	
	public int generateSequence(String seqName) {
		
		
		/*
		 * Query query = Query.query(Criteria.where("_id").is(seqName));
		 * 
		 * Update update = new Update(); update.inc("seq", 1);
		 * 
		 * FindAndModifyOptions options = new FindAndModifyOptions();
		 * options.returnNew(true);
		 */
		 
		DatabaseSequence sequence = mongoOperations.findAndModify(Query.query(Criteria.where("_id").is(seqName)),
	            new Update().inc("seq",1),
	            FindAndModifyOptions.options().returnNew(true).upsert(true),
	            DatabaseSequence.class);
		return !Objects.isNull(sequence) ? sequence.getSeq() : 1;
	}
}
