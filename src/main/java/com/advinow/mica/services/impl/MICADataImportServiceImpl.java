/**
 * 
 */
package com.advinow.mica.services.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.neo4j.ogm.session.Session;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import io.github.resilience4j.retry.annotation.Retry;

import com.advinow.mica.services.MICADataImportService;



/**
 * @author Govinda Reddy
 *
 */

@Service
@Retry(name = "neo4j")
public class MICADataImportServiceImpl implements MICADataImportService {
	

	private Session session;
	
	public MICADataImportServiceImpl(Session session) {
		this.session = session;
	}

	@Transactional
	public void loadData() {
		StringBuilder strBuilder = new StringBuilder();
		BufferedReader reader = new BufferedReader(new InputStreamReader(Thread.currentThread().getContextClassLoader().getResourceAsStream("advinow.cql")));
		String line;
		try {
			while ((line = reader.readLine()) != null) {
				strBuilder.append(line);
				strBuilder.append(" ");
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	//	String cqlFile = strBuilder.toString();
	 //  session.query(cqlFile, Collections.EMPTY_MAP);
			
	}
	

	
	@Transactional
	public void clearDatabase() {
		// TODO Auto-generated method stub
		session.purgeDatabase();
		
	}

}
