/**
 *
 */
package com.advinow.mica.services.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import io.github.resilience4j.retry.annotation.Retry;

import com.advinow.mica.domain.IllnessTreatment;
import com.advinow.mica.domain.SymptomTemplate;
import com.advinow.mica.domain.SymptomTreatment;
import com.advinow.mica.domain.TreatmentSourceInfo;
import com.advinow.mica.domain.TreatmentTypeRef;
import com.advinow.mica.exception.DataInvalidException;
import com.advinow.mica.exception.DataNotFoundException;
import com.advinow.mica.mapper.TreatmentRxRequestMapper;
import com.advinow.mica.mapper.TreatmentRxResponseMapper;
import com.advinow.mica.model.AuditSourceModel;
import com.advinow.mica.model.AuditTreatmentTypeModel;
import com.advinow.mica.model.DrugDescModel;
import com.advinow.mica.model.NonDrugDescModel;
import com.advinow.mica.model.TreatmentAuditSourcesModel;
import com.advinow.mica.model.TreatmentGroupModel;
import com.advinow.mica.model.TreatmentMainDocModel;
import com.advinow.mica.model.TreatmentMainModel;
import com.advinow.mica.model.TreatmentModel;
import com.advinow.mica.model.TreatmentStatusModel;
import com.advinow.mica.repositories.DrugRxRepository;
import com.advinow.mica.repositories.IllnessTreatmentRxRespository;
import com.advinow.mica.repositories.NonDrugRepository;
import com.advinow.mica.repositories.SymptomTemplateRepository;
import com.advinow.mica.repositories.SymptomTreatmentRxRepository;
import com.advinow.mica.repositories.TreatmentSourceInfoRepository;
import com.advinow.mica.repositories.TreatmentTypeRefRepository;
import com.advinow.mica.services.RxTreatmentService;

/**
 * @author Govinda Reddy
 *
 */
@Service
@Retry(name = "neo4j")
public class RxTreatmentServiceImpl implements RxTreatmentService {

	@Autowired
	IllnessTreatmentRxRespository illnessTreatmentRxRespository;

	@Autowired
	SymptomTreatmentRxRepository  symptomTreatmentRxRepository;

	@Autowired
	TreatmentTypeRefRepository  treatmentTypeRepository;

	/*	@Autowired
	TreatmentRepository treatmentRepository;*/

	@Autowired
	NonDrugRepository nonDrugRepository;

	@Autowired
	DrugRxRepository drugRxRepository;

	/*	@Autowired
	TreatmentGroupRespository treatmentGroupRespository;*/

	@Autowired
	SymptomTemplateRepository symptomTemplateRepository;

	@Autowired
	TreatmentSourceInfoRepository  treatmentSourceInfoRepository;

	TreatmentRxRequestMapper  treatmentRxRequestMapper = new TreatmentRxRequestMapper();

	TreatmentRxResponseMapper treatmentRxResponseMapper = new TreatmentRxResponseMapper();

	@Override
	@Transactional
	public TreatmentStatusModel creatreatTrementsForIllness(TreatmentMainModel treatmentMainModel) {
		if(treatmentMainModel == null || treatmentMainModel.getIcd10Code() == null ){
			throw new DataInvalidException("ICD10Code does not exists in the request");
		}
		String icd10Code =  treatmentMainModel.getIcd10Code().toUpperCase();
		IllnessTreatment illnessTreatment =	treatmentRxRequestMapper.createDbIllnessTreatment(treatmentMainModel);
		IllnessTreatment dbIllnessTrtmt = illnessTreatmentRxRespository.findIllnessByCode(icd10Code) ;
		TreatmentStatusModel treatmentStatusModel = new TreatmentStatusModel();
		if(dbIllnessTrtmt != null  ) {
			deleteTreatmentIllnessByCode(icd10Code);
		}
		treatmentStatusModel.setStatus("Treatment created successfully");
		illnessTreatmentRxRespository.save(illnessTreatment);


		return treatmentStatusModel;
	}

	@Override
	@Transactional
	public TreatmentStatusModel creatreatTrementsForSymptom(
			TreatmentMainModel treatmentMainModel) {
		if(treatmentMainModel == null ||  treatmentMainModel.getSymptomID() == null ){
			throw new DataInvalidException("SymptomID does not exists in the request");
		}
		String	symptomID = treatmentMainModel.getSymptomID().toUpperCase();
		SymptomTreatment symptomTreatment =	treatmentRxRequestMapper.createDbSymptomsTreatment(treatmentMainModel);
		SymptomTemplate symptomTemplate = symptomTemplateRepository.findBySymptomCode(symptomID);
		if(symptomTemplate ==null ){
			throw new DataNotFoundException("No symptom data found found for  "+ symptomID);
		}
		SymptomTreatment dbSymptomTreatment = symptomTreatmentRxRepository.findTreatmentBySymptomID(symptomID);
		TreatmentStatusModel treatmentStatusModel = new TreatmentStatusModel();
		if(dbSymptomTreatment != null) {
			deleteSymptomBycode(symptomID);


		}
		symptomTreatmentRxRepository.save(symptomTreatment);
		treatmentStatusModel.setStatus("Treatment created successfully");

		return treatmentStatusModel;
	}

	@Override
	public List<TreatmentMainModel> getIllnessTreatments() {
		List<TreatmentMainModel> listTreatments = new ArrayList<TreatmentMainModel>();
		Iterable<IllnessTreatment> dbillMainTreatments = illnessTreatmentRxRespository.getIllnessTreatments();
		List<TreatmentTypeRef> TreatmentTypeRefList = treatmentTypeRepository.getAllTreatmentsWithDesc(); 

		if(dbillMainTreatments != null ) {
			Iterator<IllnessTreatment> itr = dbillMainTreatments.iterator();
			while(itr.hasNext()){
				IllnessTreatment dbIllnesTreatment = itr.next();
				TreatmentMainModel treatmentMainModel = treatmentRxResponseMapper.createIllnessTreatment(dbIllnesTreatment,TreatmentTypeRefList,true);
				listTreatments.add(treatmentMainModel);
			}
		}
		return listTreatments;
	}

	@Override
	public List<TreatmentMainModel> getSymptomsTreatments() {
		List<TreatmentMainModel> listTreatments = new ArrayList<TreatmentMainModel>();
		Iterable<SymptomTreatment> dbSymptomsMainTreatments = symptomTreatmentRxRepository.getSymptomTreatments();
		List<TreatmentTypeRef> TreatmentTypeRefList = treatmentTypeRepository.getAllTreatmentsWithDesc(); 

		if(dbSymptomsMainTreatments != null ) {
			Iterator<SymptomTreatment> itr = dbSymptomsMainTreatments.iterator();
			while(itr.hasNext()){
				SymptomTreatment dbSymptomTreatment = itr.next();
				TreatmentMainModel treatmentMainModel = treatmentRxResponseMapper.createSymptomTreatment(dbSymptomTreatment,TreatmentTypeRefList,true);
				listTreatments.add(treatmentMainModel);
			}

		}
		return listTreatments;
	}



	@Override
	public TreatmentMainModel getTreatmentBySymptomID(String symptomID) {
		List<SymptomTreatment> treatments = symptomTreatmentRxRepository.findTreatmentsBySymptomID(symptomID.toUpperCase());
		List<TreatmentTypeRef> TreatmentTypeRefList = treatmentTypeRepository.getAllTreatmentsWithDesc(); 
		//return	treatmentResponseMapper.createSymptomTreatment(dbSymptomTreatment,treatmentTypeRepository,true);


		SymptomTreatment treatmentData= null;

		if(treatments != null && !treatments.isEmpty()) {

			treatmentData =	treatments.stream().filter(s->s.getSource() != null && s.getSource().equalsIgnoreCase("MICA")).findAny().orElse(null);

			if(treatmentData==null) {
				treatmentData =	treatments.stream().filter(s->s.getSource() != null && s.getSource().equalsIgnoreCase("NLP")).findAny().orElse(null);

			}

		}



		return	treatmentRxResponseMapper.createSymptomTreatment(treatmentData,TreatmentTypeRefList,true);
	}

	@Override
	public TreatmentMainModel getTreatmentByICD10Code(String icd10Code) {
		List<IllnessTreatment> treatments = illnessTreatmentRxRespository.findTreatmentsByIcd10Code(icd10Code.toUpperCase());
		List<TreatmentTypeRef> TreatmentTypeRefList = treatmentTypeRepository.getAllTreatmentsWithDesc(); 
		//return treatmentResponseMapper.createIllnessTreatment(treatmentData, treatmentTypeRepository,true);

		IllnessTreatment treatmentData= null;

		if(treatments != null && !treatments.isEmpty()) {

			treatmentData =	treatments.stream().filter(s->s.getSource() != null && s.getSource().equalsIgnoreCase("MICA")).findAny().orElse(null);

			if(treatmentData==null) {
				treatmentData =	treatments.stream().filter(s->s.getSource() != null && s.getSource().equalsIgnoreCase("NLP")).findAny().orElse(null);

			}

		}



		return treatmentRxResponseMapper.createIllnessTreatment(treatmentData, TreatmentTypeRefList,true);
	}

	@Override
	public void deleteSymptomBycode(String symptomID) {
		if(symptomID == null ){
			throw new DataInvalidException("SymptomID does not exists in the request");
		}


		symptomTreatmentRxRepository.deleteIllnessTmtDrugPolicies(symptomID.toUpperCase());
		symptomTreatmentRxRepository.deleteIllnessTmtDrugDosage(symptomID.toUpperCase());
		symptomTreatmentRxRepository.deleteSymptomTmtDrugSources(symptomID.toUpperCase());
		symptomTreatmentRxRepository.deleteSymptomTmtDrugsNonDrugs(symptomID.toUpperCase());
		symptomTreatmentRxRepository.deleteIllnessTreamentsGroups(symptomID.toUpperCase());
		symptomTreatmentRxRepository.deleteIllnessTreatmentsTmt(symptomID.toUpperCase());
		symptomTreatmentRxRepository.deleteSymptomTreatment(symptomID.toUpperCase());


	}

	@Override
	public void deleteTreatmentIllnessByCode(String icd10Code) {
		if(icd10Code == null  ){
			throw new DataInvalidException("ICD10Code does not exists in the request");
		}


		illnessTreatmentRxRespository.deleteIllnessTmtDrugPolicies(icd10Code.toUpperCase());
		//	illnessTreatmentRxRespository.deleteIllnessTmtDrugRxnorms(icd10Code.toUpperCase());
		illnessTreatmentRxRespository.deleteIllnessTmtDrugDosage(icd10Code.toUpperCase());
		illnessTreatmentRxRespository.deleteIllnessTmtDrugSources(icd10Code.toUpperCase());

		illnessTreatmentRxRespository.deleteIllnessTmtdrugsNonDrugs(icd10Code.toUpperCase());
		illnessTreatmentRxRespository.deleteIllnessTreamentsGroups(icd10Code.toUpperCase());
		illnessTreatmentRxRespository.deleteIllnessTreatmentsTmt(icd10Code.toUpperCase());
		illnessTreatmentRxRespository.deleteIllnessTreatment(icd10Code.toUpperCase());


	}

	@Override
	public TreatmentMainModel getTreatmentByIcd10CodeWithNoDesc(String icd10Code) {
		//	List<TreatmentTypeRef> TreatmentTypeRefList = treatmentTypeRepository.getAllTreatmentsWithDesc();
		List<TreatmentTypeRef> TreatmentTypeRefList = (List<TreatmentTypeRef>) treatmentTypeRepository.findAll();
		List<IllnessTreatment> treatments = illnessTreatmentRxRespository.findTreatmentsByIcd10Code(icd10Code.toUpperCase());

		IllnessTreatment treatmentData= null;

		if(treatments != null && !treatments.isEmpty()) {

			treatmentData =	treatments.stream().filter(s->s.getSource() != null && s.getSource().equalsIgnoreCase("MICA")).findAny().orElse(null);

			if(treatmentData==null) {
				treatmentData =	treatments.stream().filter(s->s.getSource() != null && s.getSource().equalsIgnoreCase("NLP")).findAny().orElse(null);

			}

		}


		return treatmentRxResponseMapper.createIllnessTreatment(treatmentData, TreatmentTypeRefList,false);
	}

	@Override
	public TreatmentMainModel getTreatmentBySymptomIDWithNoDesc(String symptomID) {
		//		List<TreatmentTypeRef> TreatmentTypeRefList = treatmentTypeRepository.getAllTreatmentsWithDesc();
		List<TreatmentTypeRef> TreatmentTypeRefList = (List<TreatmentTypeRef>) treatmentTypeRepository.findAll();
		List<SymptomTreatment> treatments = symptomTreatmentRxRepository.findTreatmentsBySymptomID(symptomID.toUpperCase());


		SymptomTreatment treatmentData= null;

		if(treatments != null && !treatments.isEmpty()) {

			treatmentData =	treatments.stream().filter(s->s.getSource() != null && s.getSource().equalsIgnoreCase("MICA")).findAny().orElse(null);

			if(treatmentData==null) {
				treatmentData =	treatments.stream().filter(s->s.getSource() != null && s.getSource().equalsIgnoreCase("NLP")).findAny().orElse(null);

			}

		}




		return	treatmentRxResponseMapper.createSymptomTreatment(treatmentData,TreatmentTypeRefList,false);


	}

	@Override
	public TreatmentMainDocModel getTreatmentTypesByIcd10Code(String icd10Code) {
		//		List<TreatmentTypeRef> TreatmentTypeRefList = treatmentTypeRepository.getAllTreatmentsWithDesc();
		List<TreatmentTypeRef> TreatmentTypeRefList = (List<TreatmentTypeRef>) treatmentTypeRepository.findAll();
		List<IllnessTreatment> treatments = illnessTreatmentRxRespository.findTreatmentsByIcd10Code(icd10Code.toUpperCase());


		IllnessTreatment treatmentData= null;

		if(treatments != null && !treatments.isEmpty()) {

			treatmentData =	treatments.stream().filter(s->s.getSource() != null && s.getSource().equalsIgnoreCase("MICA")).findAny().orElse(null);

			if(treatmentData==null) {
				treatmentData =	treatments.stream().filter(s->s.getSource() != null && s.getSource().equalsIgnoreCase("NLP")).findAny().orElse(null);

			}

		}


		return treatmentRxResponseMapper.createIllnessDocTreatment(treatmentData, TreatmentTypeRefList);
	}

	@Override
	public TreatmentMainDocModel getTreatmentTypesBySymptomID(String symptomID) {
		//		List<TreatmentTypeRef> TreatmentTypeRefList = treatmentTypeRepository.getAllTreatmentsWithDesc();
		List<TreatmentTypeRef> TreatmentTypeRefList = (List<TreatmentTypeRef>) treatmentTypeRepository.findAll();		
		List<SymptomTreatment> treatments = symptomTreatmentRxRepository.findTreatmentsBySymptomID(symptomID.toUpperCase());



		SymptomTreatment treatmentData= null;

		if(treatments != null && !treatments.isEmpty()) {

			treatmentData =	treatments.stream().filter(s->s.getSource() != null && s.getSource().equalsIgnoreCase("MICA")).findAny().orElse(null);

			if(treatmentData==null) {
				treatmentData =	treatments.stream().filter(s->s.getSource() != null && s.getSource().equalsIgnoreCase("NLP")).findAny().orElse(null);

			}

		}



		return	treatmentRxResponseMapper.createSymptomDocTreatment(treatmentData,TreatmentTypeRefList);
	}

	@Override
	public List<TreatmentSourceInfo> getTreatmentSourcesForGivenIllness(String icd10Code) {
		return treatmentSourceInfoRepository.getTreatmentSourcesByIllness(icd10Code);
	}

	@Override
	public String updateTreatmentSources(TreatmentAuditSourcesModel treatmentSourcesModel) {

		Collection<Map<String, Object>> drugsSelf = new ArrayList<Map<String,Object>>();
		Collection<Map<String, Object>> drugsTreatment = new ArrayList<Map<String,Object>>();
		Collection<Map<String, Object>> drugsAll = new ArrayList<Map<String,Object>>();
		Collection<Map<String, Object>> drugsAddSources = new ArrayList<Map<String,Object>>();

		Collection<Map<String, Object>> nonDrugsSelf = new ArrayList<Map<String,Object>>();
		Collection<Map<String, Object>> nonDrugsTreatment = new ArrayList<Map<String,Object>>();
		Collection<Map<String, Object>> nonDrugsAll = new ArrayList<Map<String,Object>>();
		Collection<Map<String, Object>> nonDrugsAddSources = new ArrayList<Map<String,Object>>();

		String icd10Code = null;
		String symptomID = null;


		if(treatmentSourcesModel != null) {
			List<AuditTreatmentTypeModel> auditTreatmentTypes = treatmentSourcesModel.getTreatments();
			icd10Code =	treatmentSourcesModel.getIcd10Code();
			symptomID =	treatmentSourcesModel.getSymptomID();

			if( auditTreatmentTypes != null) {
				if(auditTreatmentTypes != null &&  !auditTreatmentTypes.isEmpty()) {
					for (AuditTreatmentTypeModel auditTreatmentTypeModel : auditTreatmentTypes) {
						if(auditTreatmentTypeModel != null ) {
							Integer typeDescID = auditTreatmentTypeModel.getTypeDescID();
							String drugName = auditTreatmentTypeModel.getDrugName();
							if(typeDescID != null ) {
								updateNonDrugSources(icd10Code,symptomID,auditTreatmentTypeModel, nonDrugsSelf, nonDrugsTreatment, nonDrugsAll, nonDrugsAddSources);
							}
							if(drugName != null) {
								updateDrugSources( icd10Code,symptomID, auditTreatmentTypeModel,  drugsSelf, drugsTreatment, drugsAll,  drugsAddSources);
							}

						}
					}

				}
			}
		}

		if(! drugsSelf.isEmpty() ) 
		{

			if(icd10Code != null) {
				treatmentSourceInfoRepository.unMapSourcesFromDrugs(drugsSelf);
			}

			if(symptomID != null) {
				treatmentSourceInfoRepository.unMapSourcesFromSymptomDrugs(drugsSelf);
			}

		}

		if(! drugsTreatment.isEmpty() ) 
		{
			if(icd10Code != null) {
				treatmentSourceInfoRepository.unMapSourcesFromDrugTreatment(drugsTreatment);
				treatmentSourceInfoRepository.unMapSourcesFromNonDrugTreatment(drugsTreatment);
			}
			if(symptomID != null) {
				treatmentSourceInfoRepository.unMapSourcesFromSymptomDrugTreatment(drugsTreatment);
				treatmentSourceInfoRepository.unMapSourcesFromSymptomsNonDrugTreatment(drugsTreatment);
			}

		}

		if(! drugsAll.isEmpty() ) 
		{

			treatmentSourceInfoRepository.unMapSourcesFromAllDrugSymptomTreatment(drugsAll);
			treatmentSourceInfoRepository.unMapSourcesFromAllNonDrugSymptomsTreatment(drugsAll);
			treatmentSourceInfoRepository.unMapSourcesFromAllDrugTreatment(drugsAll);
			treatmentSourceInfoRepository.unMapSourcesFromAllNonDrugTreatment(drugsAll);
		}


		if(! drugsAddSources.isEmpty() ) 
		{
			if(icd10Code != null) {
				treatmentSourceInfoRepository.mapSourcesToDrugs(drugsAddSources);
			}
			if(symptomID != null) { 
				treatmentSourceInfoRepository.mapSourcesToDrugSymptoms(drugsAddSources);

			}
		}

		if(! nonDrugsSelf.isEmpty() ) 
		{
			if(icd10Code != null) {
				treatmentSourceInfoRepository.unMapSourcesFromNonDrugs(nonDrugsSelf);
			}
			if(symptomID != null) {
				treatmentSourceInfoRepository.unMapSourcesFromNonDrugSymptoms(nonDrugsSelf);

			}

		}

		if(! nonDrugsTreatment.isEmpty() ) 
		{

			if(icd10Code != null) {
				treatmentSourceInfoRepository.unMapSourcesFromDrugTreatment(nonDrugsTreatment);
				treatmentSourceInfoRepository.unMapSourcesFromNonDrugTreatment(nonDrugsTreatment);
			}
			if(symptomID != null) {
				treatmentSourceInfoRepository.unMapSourcesFromSymptomDrugTreatment(nonDrugsTreatment);
				treatmentSourceInfoRepository.unMapSourcesFromSymptomsNonDrugTreatment(nonDrugsTreatment);
			}
		}

		if(! nonDrugsAll.isEmpty() ) 
		{
			treatmentSourceInfoRepository.unMapSourcesFromAllNonDrugTreatment(nonDrugsAll);
			treatmentSourceInfoRepository.unMapSourcesFromAllDrugTreatment(nonDrugsAll);			
			treatmentSourceInfoRepository.unMapSourcesFromAllDrugSymptomTreatment(nonDrugsAll);
			treatmentSourceInfoRepository.unMapSourcesFromAllNonDrugSymptomsTreatment(nonDrugsAll);



		}
		if(! nonDrugsAddSources.isEmpty() ) 
		{
			if(icd10Code != null) {
				treatmentSourceInfoRepository.mapSourcesToNonDrugs(nonDrugsAddSources);
			}
			if(symptomID != null) { 
				treatmentSourceInfoRepository.mapSourcesToNonDrugsSymptoms(nonDrugsAddSources);

			}
		}


		return "Treatment sources updated";
	}



	private void updateNonDrugSources(String icd10Code,String symptomID,AuditTreatmentTypeModel auditTreatmentTypeModel, Collection<Map<String, Object>> nonDrugsSelf,Collection<Map<String, Object>> nonDrugsTreatment,Collection<Map<String, Object>> nonDrugsAll, Collection<Map<String, Object>> nonDrugsAddSources) {
		List<AuditSourceModel> sourceList = auditTreatmentTypeModel.getSourceInfo();		
		if(sourceList != null &&  !sourceList.isEmpty()) {
			for (AuditSourceModel auditSourceModel : sourceList) {
				if( auditSourceModel != null ) {
					Integer sourceId = auditSourceModel.getSourceID() ;
					String action = auditSourceModel.getAction();
					Integer typeDescID = auditTreatmentTypeModel.getTypeDescID();									
					if(sourceId !=null && action != null) {
						Map<String, Object> sourceInfo = new Hashtable<String, Object>();

						if(icd10Code != null) {
							sourceInfo.put("icd10Code", icd10Code);
						}

						if(symptomID != null) { 
							sourceInfo.put("symptomID", symptomID);
						}

						sourceInfo.put("typeDescID",typeDescID) ;
						sourceInfo.put("sourceID", auditSourceModel.getSourceID());	
						if(auditSourceModel.getAction().equalsIgnoreCase("Drug/NonDrug") ){
							sourceInfo.put("action", "Unmap");
							nonDrugsSelf.add(sourceInfo);
						} 
						if(auditSourceModel.getAction().equalsIgnoreCase("Treatment")){
							sourceInfo.put("action", "Unmap");
							nonDrugsTreatment.add(sourceInfo);
						}
						if(auditSourceModel.getAction().equalsIgnoreCase("All")){
							sourceInfo.put("action", "Unmap/Deleted");
							nonDrugsAll.add(sourceInfo);
						}

						if(auditSourceModel.getAction().equalsIgnoreCase("Add")){
							sourceInfo.put("action", "Added");
							nonDrugsAddSources.add(sourceInfo);

						}

					}

				}
			}

		}

	}


	private void updateDrugSources(String icd10Code, String symptomID,  AuditTreatmentTypeModel auditTreatmentTypeModel, Collection<Map<String, Object>> drugsSelf,Collection<Map<String, Object>> drugsTreatment,Collection<Map<String, Object>> drugsAll, Collection<Map<String, Object>> drugsAddSources) {
		List<AuditSourceModel> sourceList = auditTreatmentTypeModel.getSourceInfo();		
		if(sourceList != null &&  !sourceList.isEmpty()) {
			for (AuditSourceModel auditSourceModel : sourceList) {
				if( auditSourceModel != null ) {
					Integer sourceId = auditSourceModel.getSourceID() ;
					String action = auditSourceModel.getAction();
					String drugName = auditTreatmentTypeModel.getDrugName();
					if(sourceId !=null && action != null) {						
						Map<String, Object> sourceInfo = new Hashtable<String, Object>();

						if(icd10Code != null) { 
							sourceInfo.put("icd10Code", icd10Code);
						}

						if(symptomID != null) { 
							sourceInfo.put("symptomID", symptomID);
						}

						sourceInfo.put("drug",drugName) ;
						sourceInfo.put("sourceID", auditSourceModel.getSourceID());	
						if(auditSourceModel.getAction().equalsIgnoreCase("Drug/NonDrug") ){
							sourceInfo.put("action", "Unmap");
							drugsSelf.add(sourceInfo);
						} 
						if(auditSourceModel.getAction().equalsIgnoreCase("Treatment")){
							sourceInfo.put("action", "Unmap");
							drugsTreatment.add(sourceInfo);
						}
						if(auditSourceModel.getAction().equalsIgnoreCase("All")){
							sourceInfo.put("action", "Unmap/Deleted");
							drugsAll.add(sourceInfo);
						}

						if(auditSourceModel.getAction().equalsIgnoreCase("Add")){
							sourceInfo.put("action", "Added");
							drugsAddSources.add(sourceInfo);

						}

					}

				}
			}

		}


	}

	@Override
	public List<TreatmentSourceInfo> getTreatmentSourcesForGivenSymptom(
			String symptomID) {
		return treatmentSourceInfoRepository.getTreatmentSourcesBySymptom(symptomID);
	}

	@Override
	public TreatmentMainModel getAllIllnessTreatments(String icd10Code) {
		//	List<TreatmentTypeRef> TreatmentTypeRefList = treatmentTypeRepository.getAllTreatmentsWithDesc();
		List<TreatmentTypeRef> TreatmentTypeRefList = (List<TreatmentTypeRef>) treatmentTypeRepository.findAll();
		List<IllnessTreatment> treatments = illnessTreatmentRxRespository.findTreatmentsByIcd10Code(icd10Code.toUpperCase());
		TreatmentMainModel mainModel1 = null;
	
		if(treatments != null && !treatments.isEmpty() &&  treatments.size() > 0 ) {
			IllnessTreatment treatmentFirst = treatments.get(0);
			mainModel1 = treatmentRxResponseMapper.createIllnessTreatment(treatmentFirst, TreatmentTypeRefList,false);
			if(treatments.size()==2  && treatments.get(1) != null) {
				IllnessTreatment treatmentSecond = treatments.get(1);
				TreatmentMainModel mainModel2 = treatmentRxResponseMapper.createIllnessTreatment(treatmentSecond, TreatmentTypeRefList,false);
				  mergeIllness(mainModel1,mainModel2);
				 
			}
		}

		return mainModel1;
	}

	private void  mergeIllness(TreatmentMainModel mainModel1,TreatmentMainModel mainModel2) {

		mainModel1.setSource(mainModel1.getSource() + " & " + mainModel2.getSource());

		List<TreatmentModel> list2Treatments = mainModel2.getTreatments();

		List<TreatmentModel> listOne = mainModel1.getTreatments();
		
		
		for (TreatmentModel treatmentModelTwo : list2Treatments) {
			
			Integer treatmentIDTwo = treatmentModelTwo.getTypeID();
			TreatmentModel treatmentone = listOne.stream().filter(s->s.getTypeID().intValue()==treatmentIDTwo.intValue()).findAny().orElse(null);
			
			if(treatmentone != null) {
				mergeTreatments(treatmentone,treatmentModelTwo,treatmentIDTwo);
			

			} else {
				listOne.add(treatmentModelTwo);
	
			}





		}




	}

	private void mergeTreatments(TreatmentModel treatmentone,
			TreatmentModel treatmentModelTwo, Integer treatmentIDTwo) {

		List<TreatmentGroupModel> listSecondGroups = treatmentModelTwo.getGroups();


		List<TreatmentGroupModel> listOneGroups = treatmentone.getGroups();
		
			for (TreatmentGroupModel treatmentGroupModelSecond : listSecondGroups) {

			String groupCodeSecond = treatmentGroupModelSecond.getGroupCode();
			
			if(groupCodeSecond != null) {

				TreatmentGroupModel groupOne = listOneGroups.stream().filter(s->s.getGroupCode().equalsIgnoreCase(groupCodeSecond)).findAny().orElse(null);
				if(groupOne != null) {
					if(treatmentIDTwo.intValue() == 12 || treatmentIDTwo.intValue() == 0 ) {
						
						mergeDrugs(groupOne,treatmentGroupModelSecond);

					} else {
						mergeNonDrugs(groupOne,treatmentGroupModelSecond);
					}


				} else {
					listOneGroups.add(treatmentGroupModelSecond);
	
				}


			}


		}




	}

	private void mergeNonDrugs(TreatmentGroupModel groupOne,TreatmentGroupModel treatmentGroupModelSecond) {

		List<NonDrugDescModel> nonDrugsOne = groupOne.getNonDrugs();	

		List<NonDrugDescModel> nonDrugsTwo = treatmentGroupModelSecond.getNonDrugs();

		for (NonDrugDescModel nonDrugDescModelSecond : nonDrugsTwo) {

			NonDrugDescModel nonDrugOne = nonDrugsOne.stream().filter(s->s.getTypeDescID().intValue()==nonDrugDescModelSecond.getTypeDescID().intValue()).findAny().orElse(null);

			if(nonDrugOne==null) {
				nonDrugsOne.add(nonDrugDescModelSecond);
			}


		}




	}

	private void mergeDrugs(TreatmentGroupModel groupOne,TreatmentGroupModel treatmentGroupModelSecond) {

		List<DrugDescModel> drugsOne = groupOne.getDrugs();	

		List<DrugDescModel> drugsTwo = treatmentGroupModelSecond.getDrugs();

		for (DrugDescModel drugDescSecond : drugsTwo) {
			// check for drug name
			DrugDescModel drugOne = drugsOne.stream().filter(s->s.getIngredientRxcui().intValue() == drugDescSecond.getIngredientRxcui().intValue()).findAny().orElse(null);
			if(drugOne==null) {
				drugOne = drugsOne.stream().filter(s->s.getDrugName() != null && s.getDrugName().equalsIgnoreCase(drugDescSecond.getDrugName())).findAny().orElse(null);
				if(drugOne==null) {
					drugsOne.add(drugDescSecond);
				}
			}
		}




	}

	@Override
	public List<TreatmentMainModel> getAllSymptomTreatments(String symptomID) {
		//		List<TreatmentTypeRef> TreatmentTypeRefList = treatmentTypeRepository.getAllTreatmentsWithDesc();
		List<TreatmentTypeRef> TreatmentTypeRefList = (List<TreatmentTypeRef>) treatmentTypeRepository.findAll();
		List<SymptomTreatment> treatments = symptomTreatmentRxRepository.findTreatmentsBySymptomID(symptomID.toUpperCase());
		List<TreatmentMainModel> finealTreatments = new ArrayList<TreatmentMainModel>();
		for (SymptomTreatment symptomTreatment : treatments) {
			finealTreatments.add(treatmentRxResponseMapper.createSymptomTreatment(symptomTreatment,TreatmentTypeRefList,false));

		}


		return finealTreatments;

	}



}
